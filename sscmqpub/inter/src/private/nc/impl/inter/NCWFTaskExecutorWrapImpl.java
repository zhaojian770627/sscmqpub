package nc.impl.inter;

import java.net.URL;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.inter.IInterTaskManager;
import nc.itf.inter.INCWFTaskExecutorWrap;
import nc.pubitf.para.SysInitQuery;
import nc.vo.pub.BusinessException;
import nc.vo.sscmq.InterTaskVO;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.xfire.client.Client;

public class NCWFTaskExecutorWrapImpl implements INCWFTaskExecutorWrap {

	@Override
	public Object execute_RequiresNew(InterTaskVO task) {
		IInterTaskManager taskManager = NCLocator.getInstance().lookup(
				IInterTaskManager.class);
		try {
			// 得到操作码
			// 得到操作类型
			String operCode=task.getOpercode();	// 驱动、驳回、取消
			String operType=task.getOpertype();	// 正常操作、回滚
			
			String groupid=InvocationInfoProxy.getInstance().getGroupId();
			try {
				if ("ADDSSCTASK".equals(operCode)) {
					if (IInterTaskManager.OPERATE_DO.equals(operType)) {
						String[] params=parseAddSSCTaskWorkflowParam(task.getTaskdatac());
						String billtype=params[0].toString();
						String transtype=params[1].toString();
						String billId=params[2].toString();
						// 驱动NC流程
						addSSCTask(billtype, transtype, billId, groupid,
								task.getTaskid());
					} 
				}
			} catch (Throwable e) {
				Logger.error(e);
				throw new Exception(e);
			}
			taskManager.LogSuccess(task.getPrimaryKey());
		} catch (Exception e) {
			Logger.error("TaskExecutorWrapImpl执行出现错误:", e);
			// 新启事务记错误日志
			try {
				taskManager.LogError_RequiresNew(task, e.getMessage());
			} catch (DAOException e1) {
				Logger.error(e);
			}
		}
		return null;
	}
	
	private String[] parseAddSSCTaskWorkflowParam(String taskdatac) throws JSONException {
		JSONObject eo = new JSONObject(taskdatac);
		String billtype=eo.getString("billtype");
		String transtype=eo.getString("transtype");
		String billId=eo.getString("billid");
		return new String[]{billtype,transtype,billId};
	}



	/**
	 * 驱动NC流程
	 * 
	 * @param billtype
	 * @param trantype
	 * @param billid
	 * @param groupid
	 * @param taskid
	 * @throws Throwable
	 */
	private void addSSCTask(String billtype, String trantype,
			String billid, String groupid,String taskid) throws Throwable {
		String xmlInfo = generateXml(groupid, billtype, trantype, billid,taskid);
		Logger.error("pushToSSCByWS:" + xmlInfo);
		String result = callWS(xmlInfo);
		if (!"success".equals(result))
			throw new BusinessException("向共享服务传递任务失败,单据号:" + billid);
		
	}

	/**
	 * 生成xml文件
	 * 
	 * @param pk_group
	 * @param billtype
	 * @param transtype
	 * @param billId
	 * @return
	 */
	private String generateXml(String pk_group, String billtype,
			String transtype, String billId,String tranid) {
		StringBuilder xmlBuilder = new StringBuilder();
		xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
				.append("<params>").append("<operate>addToSSCTask</operate>")
				.append("<pk_group>").append(pk_group).append("</pk_group>")
				.append("<billtype>").append(billtype).append("</billtype>")
				.append("<transtype>").append(transtype).append("</transtype>")
				.append("<billid>").append(billId).append("</billid>")
				.append("<tranid>").append(tranid).append("</tranid>")
				.append("</params>");
		return xmlBuilder.toString();
	}
	
	private String callWS(String xmlInfo) throws Exception {
		Client client = null;
		// 地址先暂时写死
		String wsip = SysInitQuery.getParaString("GLOBLE00000000000000",
				"SSC013");
		String wsdl = "http://" + wsip
				+ "/uapws/service/nc.itf.ssc.remote.ISSCPubWS?wsdl";
		Object[] result;

		client = new Client(new URL(wsdl));
		result = client.invoke("process", new Object[] { "addToSSCTask",
				xmlInfo });
		if (result != null)
			return result[0].toString();

		return null;
	}
}
