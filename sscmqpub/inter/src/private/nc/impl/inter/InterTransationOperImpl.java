package nc.impl.inter;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.itf.inter.IInterTransationOper;
import nc.jdbc.framework.SQLParameter;
import nc.vo.sscmq.InterTransationVO;

public class InterTransationOperImpl implements IInterTransationOper {
	
	@Override
	public void insertTransation_RequiresNew(InterTransationVO tranvo) throws DAOException {
		insertTransation(tranvo);
	}

	@Override
	public void insertTransation(InterTransationVO tranvo) throws DAOException {
		new BaseDAO().insertVOWithPK(tranvo);
	}

	@Override
	public void updateTranSucess(String tranId) throws DAOException {
		String upSql="update mq_intertransation set transtatus='SUCCESS' where tranid=?";
		SQLParameter parm = new SQLParameter();
		parm.addParam(tranId);
		new BaseDAO().executeUpdate(upSql, parm);
	}

	@Override
	public InterTransationVO queryInterTransVOById(String tranId) throws DAOException {
		BaseDAO dao=new BaseDAO();
		InterTransationVO tranVO=(InterTransationVO) dao.retrieveByPK(InterTransationVO.class, tranId);
		return tranVO;
	}

	@Override
	public void queryAndInsertTran_RequiresNew(String tranId,
			InterTransationVO tranvo) throws DAOException {
		InterTransationVO tranVO= queryInterTransVOById(tranId);
		if(tranVO==null)
			insertTransation(tranvo);
	}

}
