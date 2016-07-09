package nc.impl.inter;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.itf.inter.IInterTransationQuery;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.vo.sscmq.InterTransationVO;

public class InterTransationQueryImpl implements IInterTransationQuery {

	@Override
	public String isInterTranFinished(String tranId) throws DAOException {
		BaseDAO dao = new BaseDAO();
		InterTransationVO tranVO=(InterTransationVO) dao.retrieveByPK(InterTransationVO.class, tranId);
		if(tranVO==null)
			return "N";
		return "Y";
	}

	@Override
	public String[] isInterTransFinished(String[] tranIds) throws DAOException {
		// 能查到代表成功，因为服务方是在一个事务中进行插入的
		String ids = "";
		int index = 0;
		for (String tranId : tranIds) {
			if (index == 0)
				ids = "'" + tranId + "'";
			else
				ids = ids + "," + "'" + tranId + "'";

			index++;
		}
		String condition = " tranid in (" + ids + "')";

		String selSql = "select tranid from mq_intertransation where "
				+ condition;
		BaseDAO dao = new BaseDAO();
		List<String> lstIds=(List<String>) dao.executeQuery(selSql, new ColumnListProcessor(1));
		return lstIds.toArray(new String[0]);
	}

}
