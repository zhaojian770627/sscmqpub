package nc.impl.inter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.itf.inter.IInterTaskManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.vo.sscmq.InterTaskVO;

public class InterTaskManagerImpl implements IInterTaskManager {

	//
	
	
	@Override
	public void addSynTask(InterTaskVO taskVO) throws DAOException {
		new BaseDAO().insertVOWithPK(taskVO);
	}

	static int Get_TASKS= 20;
	
	@Override
	public InterTaskVO[] getSynTask() throws DAOException {
		// ����
		
		// ȡReady���񣬲���״̬��Ϊ������
		BaseDAO dao = new BaseDAO();
		String selSql = "select taskid,tasktype,opertype,opercode,taskstatus,taskdatac,allowundo from mq_intertaskvo where taskstatus='READY' and rownum<="+Get_TASKS;
		ResultSetProcessor processor = new ResultSetProcessor() {

			@Override
			public Object handleResultSet(ResultSet rs) throws SQLException {
				List<InterTaskVO> lstTask = new ArrayList<InterTaskVO>();
				while (rs.next()) {
					InterTaskVO tvo = new InterTaskVO();
					tvo.setTaskid(rs.getString(1));
					tvo.setTasktype(rs.getString(2));
					tvo.setOpertype(rs.getString(3));
					tvo.setOpercode(rs.getString(4));
					tvo.setTaskstatus(rs.getString(5));
					tvo.setTaskdatac(rs.getString(6));
					tvo.setAllowundo(rs.getString(7));
					lstTask.add(tvo);
				}
				InterTaskVO[] aryTaskVOS = lstTask.toArray(new InterTaskVO[0]);
				return aryTaskVOS;
			}
		};
		
		InterTaskVO[] aryTaskVOS = (InterTaskVO[]) dao.executeQuery(selSql, processor);
		
		// ��������Ϊ״̬Ϊ������
		String ids = "";
		int index = 0;
		for (InterTaskVO taskVO : aryTaskVOS) {
			if (index == 0)
				ids = "'" + taskVO.getTaskid() + "'";
			else
				ids = ids + "," + "'" + taskVO.getTaskid() + "'";

			index++;
		}
		String condition = " taskid in (" + ids + "')";
		String upSql="update mq_intertaskvo set taskstatus='"+IInterTaskManager.TASK_PROCESS+"' where "+ condition;
		dao.executeUpdate(upSql);
		return aryTaskVOS;
	}
	
	@Override
	public InterTaskVO[] getSynTask_RequiresNew() throws DAOException {
		return getSynTask();
	}

	@Override
	public void LogError_RequiresNew(InterTaskVO task, String message) throws DAOException {
		String upSql="update mq_intertaskvo set taskstatus=?,taskresult=? where taskid=?";
		BaseDAO dao=new BaseDAO();
		SQLParameter params = new SQLParameter();
		params.addParam(IInterTaskManager.TASK_FAILED);
		params.addParam(message);
		params.addParam(task.getPrimaryKey());
		dao.executeUpdate(upSql);
		
		// ������Իع�����Դ��������һ���ع�����
		if("Y".equals(task.getAllowundo()))
		{
			task.setTaskid(UUID.randomUUID().toString());
			task.setOpertype(IInterTaskManager.OPERATE_UNDO);
			
			// �ع����������ٴλع�
			task.setAllowundo("N");
			task.setSourcetaskid(task.getPrimaryKey());
			task.setSourceresult(task.getTaskresult());
			task.setStatus(2);
			task.setTaskresult("");
			dao.insertVOWithPK(task);
		}
	}

	@Override
	public void LogSuccess(String primaryKey) throws DAOException {
		
		String upSql="update mq_intertaskvo set taskstatus=? where taskid=?";
		SQLParameter params = new SQLParameter();
		params.addParam(IInterTaskManager.TASK_SUCCESS);
		params.addParam(primaryKey);
		new BaseDAO().executeUpdate(upSql);
	}
}