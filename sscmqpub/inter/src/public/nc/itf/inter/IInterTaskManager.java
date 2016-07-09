package nc.itf.inter;

import nc.bs.dao.DAOException;
import nc.vo.sscmq.InterTaskVO;

public interface IInterTaskManager {
	
	/*
	 * �ȴ�����
	 */
	public static final String TASK_READY = "READY"; 

	/*
	 * ���ڴ���
	 */
	public static final String TASK_PROCESS = "PROCESS"; 
	
	/*
	 * �����ɹ�
	 */
	public static final String TASK_SUCCESS = "SUCCESS"; 
	
	/*
	 * ����ʧ��
	 */
	public static final String TASK_FAILED = "FAILED"; 
	
	/*
	 * �������ͣ�����
	 */
	public static final String OPERATE_DO="DO";
	
	
	/*
	 * ��������
	 */
	public static final String OPERATE_UNDO="UNDO";
	
	/**
	 * �����첽����
	 * 
	 * @param task
	 * @throws DAOException 
	 */
	void addSynTask(InterTaskVO task) throws DAOException;

	/**
	 * �õ�һ������
	 * 
	 * @return
	 * @throws DAOException 
	 */
	InterTaskVO[] getSynTask() throws DAOException;
	
	/**
	 * ��������õ�һ������
	 * 
	 * @return
	 * @throws DAOException
	 */
	InterTaskVO[] getSynTask_RequiresNew() throws DAOException;

	/**
	 * ��¼���������Ϣ
	 * 
	 * @param primaryKey
	 * @param message
	 * @throws DAOException 
	 */
	void LogError_RequiresNew(InterTaskVO task, String message) throws DAOException;

	/**
	 * ��¼�����ɹ���Ϣ
	 * 
	 * @param primaryKey
	 * @throws DAOException 
	 */
	void LogSuccess(String primaryKey) throws DAOException;
}