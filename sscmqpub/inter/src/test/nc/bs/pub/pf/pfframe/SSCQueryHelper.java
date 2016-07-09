package nc.bs.pub.pf.pfframe;

import java.net.MalformedURLException;
import java.net.URL;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.logging.Logger;
import nc.pubitf.para.SysInitQuery;
import nc.vo.pub.BusinessException;

import org.codehaus.xfire.client.Client;

/**
 * 发送 WebService 用户查询某单据是否处于共享服务中
 * 
 * @author zhaojianc
 * 
 */
public class SSCQueryHelper {
	private String callWS(String xmlInfo) throws BusinessException {
		Client client = null;
		// 地址先暂时写死
		String wsip = SysInitQuery.getParaString("GLOBLE00000000000000",
				"SSC013");
		String wsdl = "http://" + wsip
				+ "/uapws/service/nc.itf.ssc.remote.ISSCPubWS?wsdl"; // TaskUtil.getInstance().getSysWebSvrIp();
		Object[] result;
		try {
			client = new Client(new URL(wsdl));
			result = client.invoke("process", new Object[] { "isBillInSSC",
					xmlInfo });
			if (result != null)
				return result[0].toString();

		} catch (MalformedURLException e) {
			Logger.error("向共享服务传递审核任务，调用WebService接口出现错误", e);
			throw new BusinessException(e.getMessage());
		} catch (Exception e) {
			Logger.error("向共享服务传递审核任务，调用WebService接口出现错误", e);
			throw new BusinessException(e.getMessage());
		}
		return null;
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
	private String generateXml(String pk_group,String billId) {
		StringBuilder xmlBuilder = new StringBuilder();
		xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
				.append("<params>").append("<operate>isBillInSSC</operate>")
				.append("<pk_group>").append(pk_group).append("</pk_group>")
				.append("<billid>").append(billId).append("</billid>")
				.append("</params>");
		return xmlBuilder.toString();
	}

	/**
	 * 查询billId是否在SSC系统中
	 * 
	 * @param gc
	 */
	public String queryBillInSSCByWS(String billId) throws BusinessException {
		String pk_group=InvocationInfoProxy.getInstance().getGroupId();
		String xmlInfo = generateXml(pk_group, billId);
		Logger.error("queryBillInSSCByWS:" + xmlInfo);
		String result = callWS(xmlInfo);
		return result;
	}
	
}
