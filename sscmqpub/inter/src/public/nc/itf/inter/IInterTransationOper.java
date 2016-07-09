package nc.itf.inter;

import nc.bs.dao.DAOException;
import nc.vo.sscmq.InterTransationVO;

public interface IInterTransationOper {

	public static final String SVR = "SVR"; // 服务方
	public static final String REQ = "REQ"; // 请求方
	
	// 
	public static final String SUCCESS = "SUCCESS"; // 成功
	public static final String WAIT = "WAIT"; // 成功
	
	/**
	 * 查询是否执行过此事务
	 * @param tranId
	 * 
	 * @return
	 * @throws DAOException 
	 */
	InterTransationVO queryInterTransVOById(String tranId) throws DAOException;
	/**
	 * 调用方使用，在远程调用之前，需要查询此记录，如果没有此记录
	 * 则需要插入此记录，如果没有此记录则需要插入此记录
	 * 
	 * @param tranvo
	 * @throws DAOException
	 */
	void insertTransation_RequiresNew(InterTransationVO tranvo) throws DAOException;
	
	/**
	 * 先进行查询，如果没有，然后执行插入
	 * 
	 * @param tranId
	 * @param tranvo
	 * @throws DAOException 
	 */
	void queryAndInsertTran_RequiresNew(String tranId,InterTransationVO tranvo) throws DAOException;
	
	/**
	 * 插入
	 * 
	 * @param tranvo
	 * @throws DAOException
	 */
	void insertTransation(InterTransationVO tranvo) throws DAOException;
	
	/**
	 * 
	 * @param tranId
	 * @throws DAOException
	 */
	void updateTranSucess(String tranId) throws DAOException;
}
