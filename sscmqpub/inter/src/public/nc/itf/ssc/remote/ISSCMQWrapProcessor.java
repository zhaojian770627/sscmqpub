package nc.itf.ssc.remote;

import nc.vo.pub.BusinessException;

/**
 * 共享服务用于处理任务的接口
 * 
 * @author zhaojianc
 * 
 */
public interface ISSCMQWrapProcessor {

	/**
	 * 向共享服务压任务
	 * 
	 * 参数约定：保存接口，保存接口方法，保存接口方法参数类型，保存接口方法参数,集团ID,VO
	 */
	public static final String Oper_addToSSCTask = "ADDTOSSCTASK"; // 制单

	/**
	 * 此操作用于驱动NC的工作流程
	 * 
	 * 参数约定：单据类型、交易类型、单据PK、集团ID，
	 */
	public static final String Oper_driveNCWorkflow = "DRIVENCWORKFLOW";

	/**
	 * 此操作用于驳回NC的工作流程
	 * 
	 * 参数约定：单据类型、交易类型、单据PK、集团ID，驳回环节Id
	 */
	public static final String Oper_rejectNCWorkflow = "REJECTNCWORKFLOW";
	
	/**
	 * 此操作用于回退NC的工作流程
	 * 
	 * 参数约定：单据类型、交易类型、单据PK、集团ID，驳回环节Id
	 */
	public static final String Oper_rollbackNCWorkflow = "ROLLBACKNCWORKFLOW";
	
	/**
	 * 用于处理消息的接口包装
	 * @param operName
	 * @param param
	 * @return
	 * @throws BusinessException 
	 */
	public Object process(String operName, Object[] param)
			throws BusinessException;
}
