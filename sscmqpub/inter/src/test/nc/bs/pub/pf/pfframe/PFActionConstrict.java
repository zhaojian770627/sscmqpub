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
 * ����ƽ̨�ĵ��ݶ���Լ����
 * <p>�˴��жϵ����ڽ���ǰ��Ҫ���е�У�鶯�� �����Ʒ���ṩ��˵ĵ��ݵ�VO��
 * �������鲻�ɹ� ������ʾ��Ϣ��
 * ˵�������ĳ�����ڲ������йؼ������йؼ��������޹��ж����ã���ϵͳ�����ж϶�ִ�С�
 * Ҳ���Կ���ִֻ��һ��
 *   
 * @author fangj 2005-1-27
 * @modifier leijun 2007-4-26 ���Ӷ�����Լ�� ���
 */
public class PFActionConstrict implements IPFActionConstrict {

	/**
	 * ĳ���ݶ����ڵ�ǰҵ�������µ�Լ�����
	 */
	private PfUtilActionConstrictVO[] aryActionConstricts = null;

	public PFActionConstrict() {
		super();
	}

	/**
	 * ��ѯ���ݶ��� ǰ�����Լ�����
	 * @param paraVo 
	 * @param isBefore �Ƿ���ǰԼ��
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
					//�鲻�����ٰ��ս�������������������� add by zhangrui 2012-04-18
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
		Logger.debug("*******ִ�ж���ǰԼ��actionConstrictBefore��ʼ*********");
		//��ѯ�ö������е�ǰԼ�����
		ArrayList<PfUtilActionConstrictVO> alBeforeConstrictVOs = splitConstrictVOs(paraVo, true);

		for (Iterator<PfUtilActionConstrictVO> iter = alBeforeConstrictVOs.iterator(); iter.hasNext();) {
			PfUtilActionConstrictVO ac = iter.next();
			executeActionConstrict(paraVo, ac);
		}
		Logger.debug("*******ִ�ж���ǰԼ��actionConstrictBefore����,����Լ��" + alBeforeConstrictVOs.size()
				+ "��*********");
	}

	/**
	 * ִ��ʵ�ʵĶ���Լ�����
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
			String errString = NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PFActionConstrict-0000")/*���ݶ���������Լ������*/;
			//modified by �׾� 2004-03-08 ����Լ��������ʱ����ʾ��Ϣ
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
				//XXX:leijun@2007-9-27 �ӵ�ǰ�̻߳�ȡ����ִ��ʱ���Ƶ���Ϣ
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
	 * ���붯��Լ��
	 * @param paraVo
	 * @param isBefore �Ƿ���ǰԼ��
	 * @return
	 * @throws BusinessException
	 */
	private ArrayList<PfUtilActionConstrictVO> splitConstrictVOs(PfParameterVO paraVo,
			boolean isBefore) throws BusinessException {

		ArrayList<PfUtilActionConstrictVO> alRet = new ArrayList<PfUtilActionConstrictVO>();
		PfUtilActionConstrictVO[] allActionConstricts = queryActionConstrictVOs(paraVo);
		for (int i = 0; i < allActionConstricts.length; i++) {
			if (isBefore) {
				//����ǰԼ��
				if (allActionConstricts[i].getIsBefore().equals(ActionconstrictVO.TYPE_BEFORE))
					alRet.add(allActionConstricts[i]);
			} else {
				//������Լ��
				if (allActionConstricts[i].getIsBefore().equals(ActionconstrictVO.TYPE_AFTER))
					alRet.add(allActionConstricts[i]);
			}
		}
		return alRet;
	}

	public void actionConstrictAfter(PfParameterVO paraVo)
			throws BusinessException {
		Logger.debug("*******ִ�ж�����Լ�� actionConstrictAfter ��ʼ*********");
		//��ѯ�ö������е�ǰԼ�����
		ArrayList<PfUtilActionConstrictVO> alAfterConstrictVOs = splitConstrictVOs(paraVo, false);
		for (Iterator<PfUtilActionConstrictVO> iter = alAfterConstrictVOs.iterator(); iter.hasNext();) {
			PfUtilActionConstrictVO ac = iter.next();
			executeActionConstrict(paraVo, ac);
		}
		
		// zhaojian add �����жϵ����Ƿ��ڹ����������
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
							"�����Ѿ����ڹ���������ģ���Ե��ݽ����������빲��������ģ����ܽ��в��ء�");
					
			}
	
		}
		Logger.debug("*******ִ�ж�����Լ�� actionConstrictAfter ����,����Լ��" + alAfterConstrictVOs.size()
				+ "��*********");
	}

	/**
	 * ִ������ǰԼ��
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
		Logger.debug(">>ִ������ǰԼ��");
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
					return false; //XXX:ֻҪĳ������ǰԼ�������㣬��ִ�и�����
			}
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		}

		return true;
	}
}