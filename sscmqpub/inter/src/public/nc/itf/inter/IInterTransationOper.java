package nc.itf.inter;

import nc.bs.dao.DAOException;
import nc.vo.sscmq.InterTransationVO;

public interface IInterTransationOper {

	public static final String SVR = "SVR"; // ����
	public static final String REQ = "REQ"; // ����
	
	// 
	public static final String SUCCESS = "SUCCESS"; // �ɹ�
	public static final String WAIT = "WAIT"; // �ɹ�
	
	/**
	 * ��ѯ�Ƿ�ִ�й�������
	 * @param tranId
	 * 
	 * @return
	 * @throws DAOException 
	 */
	InterTransationVO queryInterTransVOById(String tranId) throws DAOException;
	/**
	 * ���÷�ʹ�ã���Զ�̵���֮ǰ����Ҫ��ѯ�˼�¼�����û�д˼�¼
	 * ����Ҫ����˼�¼�����û�д˼�¼����Ҫ����˼�¼
	 * 
	 * @param tranvo
	 * @throws DAOException
	 */
	void insertTransation_RequiresNew(InterTransationVO tranvo) throws DAOException;
	
	/**
	 * �Ƚ��в�ѯ�����û�У�Ȼ��ִ�в���
	 * 
	 * @param tranId
	 * @param tranvo
	 * @throws DAOException 
	 */
	void queryAndInsertTran_RequiresNew(String tranId,InterTransationVO tranvo) throws DAOException;
	
	/**
	 * ����
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
