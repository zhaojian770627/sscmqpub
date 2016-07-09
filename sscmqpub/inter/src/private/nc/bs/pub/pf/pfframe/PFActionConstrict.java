package nc.bs.pub.pf.pfframe;

import java.util.ArrayList;
import java.util.Iterator;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.logging.Logger;
import nc.bs.pub.pf.PfUtilDMO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.pub.pf.bservice.IPFActionConstrict;
import nc.jdbc.framework.exception.DbException;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pf.pub.util.ArrayUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.pf.IPFConfigInfo;
import nc.vo.pub.pf.PfUtilActionConstrictVO;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.pfflow05.ActionconstrictVO;
import nc.vo.uap.pf.PFBusinessException;

/**
 * 流程平台的单据动作约束类
 * <p>此处判断单据在进行前需要进行的校验动作 需各产品组提供审核的单据的VO，
 * 条件检验不成功 返回提示信息。
 * 说明：如果某条件在操作人有关及与组有关及与组人无关中都配置，则系统所有判断都执行。
 * 也可以考虑只执行一次
 *   
 * @author fangj 2005-1-27
 * @modifier leijun 2007-4-26 增加动作后约束 检查
 */
public class PFActionConstrict implements IPFActionConstrict {

	/**
	 * 某单据动作在当前业务流程下的约束检查
	 */
	private PfUtilActionConstrictVO[] aryActionConstricts = null;

	public PFActionConstrict() {
		super();
	}

	/**
	 * 查询单据动作 前、后的约束检查
	 * @param paraVo 
	 * @param isBefore 是否动作前约束
	 * @return
	 * @throws BusinessException
	 */
	private PfUtilActionConstrictVO[] queryActionConstrictVOs(PfParameterVO paraVo)
			throws BusinessException {
		if (aryActionConstricts == null) {
			try {
				PfUtilDMO dmo = new PfUtilDMO();
				aryActionConstricts = dmo.queryActionConstrict(paraVo.m_billType, paraVo.m_businessType,
						paraVo.m_actionName, paraVo.m_operator, paraVo.m_pkGroup);
				if(ArrayUtil.isNull(aryActionConstricts)) {
					//查不到，再按照交易类型中配的条件来查 add by zhangrui 2012-04-18
					aryActionConstricts = dmo.queryActionConstrict(paraVo.m_billType, null,
						paraVo.m_actionName, paraVo.m_operator, paraVo.m_pkGroup);
				}
				
			} catch (DbException e) {
				Logger.error(e.getMessage(), e);
				throw new PFBusinessException(e.getMessage());
			}
		}
		return aryActionConstricts;
	}

	public void actionConstrictBefore(PfParameterVO paraVo)
			throws BusinessException {
		Logger.debug("*******执行动作前约束actionConstrictBefore开始*********");
		//查询该动作所有的前约束检查
		ArrayList<PfUtilActionConstrictVO> alBeforeConstrictVOs = splitConstrictVOs(paraVo, true);

		for (Iterator<PfUtilActionConstrictVO> iter = alBeforeConstrictVOs.iterator(); iter.hasNext();) {
			PfUtilActionConstrictVO ac = iter.next();
			executeActionConstrict(paraVo, ac);
		}
		Logger.debug("*******执行动作前约束actionConstrictBefore结束,共有约束" + alBeforeConstrictVOs.size()
				+ "个*********");
	}

	/**
	 * 执行实际的动作约束检查
	 * 
	 * @param paraVo
	 * @param m_methodReturnHas
	 * @param ac
	 * @throws BusinessException
	 */
	private void executeActionConstrict(PfParameterVO paraVo, PfUtilActionConstrictVO ac) throws BusinessException {
		StringBuffer errorMessage = new StringBuffer();

		//lj+
		StringBuffer originalHintBuffer = new StringBuffer();
		String hintMsg = ac.getErrHintMsg();
		String originalHint = hintMsg == null ? "" : hintMsg;
		originalHintBuffer.append(originalHint);

		boolean boolTmp = PfUtilTools.parseSyntax(ac.getFunClassName(), ac.getMethod(), ac
				.getParameter(), ac.getFunNote(), ac.getFunReturnType(), ac.getYsf(), ac.getValue(), ac
				.getClassName2(), ac.getMethod2(), ac.getParameter2(), ac.getFunNote2(), paraVo,
				errorMessage, originalHintBuffer);
		if (!boolTmp) {
			String errString = NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PFActionConstrict-0000")/*单据动作不满足约束条件*/;
			//modified by 雷军 2004-03-08 动作约束不满足时的提示信息
			if (originalHintBuffer.length() > 0) {
				errString = originalHintBuffer.substring(originalHint.length());
			} else {
				if (ac.getFunNote() != null) {
					errString = errString + ":" + ac.getFunNote();
					if (ac.getYsf() != null) {
						if (ac.getClassName2() != null)
							errString = errString + ac.getYsf() + ac.getFunNote2();
						else
							errString = errString + ac.getYsf() + (ac.getValue() == null ? "" : ac.getValue());
					}
				}
				//XXX:leijun@2007-9-27 从当前线程获取函数执行时定制的信息
				Object outObj = Logger.getMappedThreadState(IPFConfigInfo.FUNCTION_OUT);
				//Logger.setMappedThreadState(IPFConfigInfo.FUNCTION_OUT, null);
				if (outObj != null)
					errString += "\n" + outObj;
			}
			Logger.error(errString);
			throw new PFBusinessException(errString);
		}
	}

	/**
	 * 分离动作约束
	 * @param paraVo
	 * @param isBefore 是否动作前约束
	 * @return
	 * @throws BusinessException
	 */
	private ArrayList<PfUtilActionConstrictVO> splitConstrictVOs(PfParameterVO paraVo,
			boolean isBefore) throws BusinessException {

		ArrayList<PfUtilActionConstrictVO> alRet = new ArrayList<PfUtilActionConstrictVO>();
		PfUtilActionConstrictVO[] allActionConstricts = queryActionConstrictVOs(paraVo);
		for (int i = 0; i < allActionConstricts.length; i++) {
			if (isBefore) {
				//动作前约束
				if (allActionConstricts[i].getIsBefore().equals(ActionconstrictVO.TYPE_BEFORE))
					alRet.add(allActionConstricts[i]);
			} else {
				//动作后约束
				if (allActionConstricts[i].getIsBefore().equals(ActionconstrictVO.TYPE_AFTER))
					alRet.add(allActionConstricts[i]);
			}
		}
		return alRet;
	}

	public void actionConstrictAfter(PfParameterVO paraVo)
			throws BusinessException {
		Logger.debug("*******执行动作后约束 actionConstrictAfter 开始*********");
		//查询该动作所有的前约束检查
		ArrayList<PfUtilActionConstrictVO> alAfterConstrictVOs = splitConstrictVOs(paraVo, false);
		for (Iterator<PfUtilActionConstrictVO> iter = alAfterConstrictVOs.iterator(); iter.hasNext();) {
			PfUtilActionConstrictVO ac = iter.next();
			executeActionConstrict(paraVo, ac);
		}
		
		// zhaojian add 用于判断单据是否处于共享服务中心
		if ((paraVo.m_actionName.equals(IPFActionName.APPROVE) || paraVo.m_actionName
				.equals(IPFActionName.SIGNAL))
				&& paraVo.m_workFlow != null
				&& paraVo.m_workFlow.getApproveresult() != null
				&& paraVo.m_workFlow.getApproveresult().equals("R")) {
			String noteflag=InvocationInfoProxy.getInstance().getProperty("doRejectNCWorkflow");
			InvocationInfoProxy.getInstance().setProperty("doRejectNCWorkflow", null);
			if(noteflag == null)
			{
				String result = new SSCQueryHelper().queryBillInSSCByWS(paraVo.m_billId);
				if ("Y".equals(result))
					throw new BusinessException(
							"单据已经处于共享服务中心，请对单据进行审批进入共享服务中心，不能进行驳回。");
					
			}
	
		}
		Logger.debug("*******执行动作后约束 actionConstrictAfter 结束,共有约束" + alAfterConstrictVOs.size()
				+ "个*********");
	}

	/**
	 * 执行驱动前约束
	 * @param pkMessageDrive
	 * @param destBillType
	 * @param beDrivedActionName
	 * @param paraVo
	 * @param hashMethodReturn
	 * @return
	 * @throws BusinessException
	 */
	public boolean actionConstrictBeforeDrive(String pkMessageDrive, String destBillType,
			String beDrivedActionName, PfParameterVO paraVo)
			throws BusinessException {
		Logger.debug(">>执行驱动前约束");
		Logger.debug("** pkMessageDrive=" + pkMessageDrive);
		Logger.debug("** destBillType=" + destBillType);
		Logger.debug("** beDrivedActionName=" + beDrivedActionName);
		try { 
			PfUtilDMO pfDmo = new PfUtilDMO();
			aryActionConstricts = pfDmo.queryActionConstrict(destBillType, pkMessageDrive,
					beDrivedActionName, paraVo.m_operator, paraVo.m_pkGroup);

			for (int i = 0; i < aryActionConstricts.length; i++) {
				PfUtilActionConstrictVO ac = aryActionConstricts[i];

				StringBuffer errorMessage = new StringBuffer();
				StringBuffer originalHintBuffer = new StringBuffer();
				String hintMsg = ac.getErrHintMsg();
				String originalHint = hintMsg == null ? "" : hintMsg;
				originalHintBuffer.append(originalHint);
				boolean boolTmp = PfUtilTools.parseSyntax(ac.getFunClassName(), ac.getMethod(), ac
						.getParameter(), ac.getFunNote(), ac.getFunReturnType(), ac.getYsf(), ac.getValue(), ac
						.getClassName2(), ac.getMethod2(), ac.getParameter2(), ac.getFunNote2(), paraVo,
						errorMessage, originalHintBuffer);
				if (!boolTmp)
					return false; //XXX:只要某个驱动前约束不满足，则不执行该驱动
			}
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		}

		return true;
	}
}