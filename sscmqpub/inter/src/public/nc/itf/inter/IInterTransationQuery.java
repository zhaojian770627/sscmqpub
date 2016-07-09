package nc.itf.inter;

import nc.bs.dao.DAOException;

public interface IInterTransationQuery {
	/**
	 * ������Ų�ѯ�����Ƿ����,Զ��
	 * 
	 * @param tranId
	 * @return
	 * @throws DAOException 
	 */
	String isInterTranFinished(String tranId) throws DAOException;
	
	/**
	 * �������������ѯ�����Ƿ����
	 * 
	 * @param tranIds
	 * @return
	 * @throws DAOException
	 */
	String[] isInterTransFinished(String[] tranIds) throws DAOException;
}
