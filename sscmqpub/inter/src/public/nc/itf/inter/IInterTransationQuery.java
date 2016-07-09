package nc.itf.inter;

import nc.bs.dao.DAOException;

public interface IInterTransationQuery {
	/**
	 * 按服务号查询服务是否完成,远程
	 * 
	 * @param tranId
	 * @return
	 * @throws DAOException 
	 */
	String isInterTranFinished(String tranId) throws DAOException;
	
	/**
	 * 按服务号批量查询服务是否完成
	 * 
	 * @param tranIds
	 * @return
	 * @throws DAOException
	 */
	String[] isInterTransFinished(String[] tranIds) throws DAOException;
}
