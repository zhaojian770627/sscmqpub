package nc.itf.inter;

import java.util.List;

import nc.bs.dao.DAOException;
import nc.vo.pub.BusinessException;
import nc.vo.sscmq.InterTaskVO;

public interface IInterTaskManager {
	
	/*
	 * 等待处理
	 */
	public static final String TASK_READY = "READY"; 

	/*
	 * 正在处理
	 */
	public static final String TASK_PROCESS = "PROCESS"; 
	
	/*
	 * 处理成功
	 */
	public static final String TASK_SUCCESS = "SUCCESS"; 
	
	/*
	 * 处理失败
	 */
	public static final String TASK_FAILED = "FAILED"; 
	
	/*
	 * 操作类型，正向
	 */
	public static final String OPERATE_DO="DO";
	
	
	/*
	 * 操作类型
	 */
	public static final String OPERATE_UNDO="UNDO";
	
	/**
	 * 添加异步任务
	 * 
	 * @param task
	 * @throws DAOException 
	 */
	void addSynTask(InterTaskVO task) throws DAOException;

	/**
	 * 得到一批任务
	 * 
	 * @return
	 * @throws DAOException 
	 */
	InterTaskVO[] getSynTask() throws DAOException;
	
	/**
	 * 新启事务得到一批任务
	 * 
	 * @return
	 * @throws DAOException
	 * @throws BusinessException 
	 */
	InterTaskVO[] getSynTask_RequiresNew() throws BusinessException;

	/**
	 * 记录错误出错信息
	 * 
	 * @param primaryKey
	 * @param message
	 * @throws DAOException 
	 */
	void LogError_RequiresNew(InterTaskVO task, String message) throws DAOException;

	/**
	 * 记录操作成功信息
	 * 
	 * @param primaryKey
	 * @throws DAOException 
	 */
	void LogSuccess(String primaryKey) throws DAOException;

	/**
	 * 得到指定状态的任务
	 * 
	 * @param taskProcess
	 * @return
	 * @throws DAOException 
	 */
	List<InterTaskVO> getInterTaskByState(String taskProcess) throws DAOException;
	
	/**
	 * 更新任务为待处理
	 * 
	 * @param taskid
	 * @throws DAOException
	 */
	void updateTaskToReady(String taskid) throws DAOException;
}
