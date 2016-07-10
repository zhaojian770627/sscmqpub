package nc.impl.inter;

import nc.bs.framework.common.NCLocator;
import nc.itf.inter.ISSCWFTaskExecutorWrap;
import nc.itf.inter.ITaskProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.sscmq.InterTaskVO;

public class SSCWFTaskProcessor implements ITaskProcessor {
	InterTaskVO taskvo;

	@Override
	public Object doAction() throws BusinessException {
		if (taskvo == null)
			throw new BusinessException("任务数据不能为空！");

		ISSCWFTaskExecutorWrap svr = NCLocator.getInstance().lookup(
				ISSCWFTaskExecutorWrap.class);
		Object ret = svr.execute_RequiresNew(taskvo);
		return ret;
	}

	@Override
	public void setTask(InterTaskVO taskvo) {
		this.taskvo=taskvo;
	}

	@Override
	public Object undoAction() throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

}
