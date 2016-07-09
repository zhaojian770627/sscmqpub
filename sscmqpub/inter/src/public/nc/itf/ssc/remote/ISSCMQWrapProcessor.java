package nc.itf.ssc.remote;

import nc.vo.pub.BusinessException;

/**
 * ����������ڴ�������Ľӿ�
 * 
 * @author zhaojianc
 * 
 */
public interface ISSCMQWrapProcessor {

	/**
	 * �������ѹ����
	 * 
	 * ����Լ��������ӿڣ�����ӿڷ���������ӿڷ����������ͣ�����ӿڷ�������,����ID,VO
	 */
	public static final String Oper_addToSSCTask = "ADDTOSSCTASK"; // �Ƶ�

	/**
	 * �˲�����������NC�Ĺ�������
	 * 
	 * ����Լ�����������͡��������͡�����PK������ID��
	 */
	public static final String Oper_driveNCWorkflow = "DRIVENCWORKFLOW";

	/**
	 * �˲������ڲ���NC�Ĺ�������
	 * 
	 * ����Լ�����������͡��������͡�����PK������ID�����ػ���Id
	 */
	public static final String Oper_rejectNCWorkflow = "REJECTNCWORKFLOW";
	
	/**
	 * �˲������ڻ���NC�Ĺ�������
	 * 
	 * ����Լ�����������͡��������͡�����PK������ID�����ػ���Id
	 */
	public static final String Oper_rollbackNCWorkflow = "ROLLBACKNCWORKFLOW";
	
	/**
	 * ���ڴ�����Ϣ�Ľӿڰ�װ
	 * @param operName
	 * @param param
	 * @return
	 * @throws BusinessException 
	 */
	public Object process(String operName, Object[] param)
			throws BusinessException;
}
