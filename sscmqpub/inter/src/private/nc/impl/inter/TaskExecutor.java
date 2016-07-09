package nc.impl.inter;

import nc.bs.logging.Logger;
import nc.itf.inter.ITaskProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.sscmq.InterTaskVO;

public class TaskExecutor implements Runnable {
	InterTaskVO taskvo;

	public TaskExecutor(InterTaskVO taskvo) {
		this.taskvo = taskvo;
	}

	@Override
	public void run() {
		String tasktype = taskvo.getTasktype();
		try {
			ITaskProcessor processor = getProcessor(tasktype);
			if(processor==null)
				throw new BusinessException(tasktype+" 没有找到对应的处理器");
			processor.setTask(taskvo);
			processor.doAction();
		} catch (BusinessException e) {
			Logger.error(e);
		}
	}

	private ITaskProcessor getProcessor(String tasktype) {
		ITaskProcessor process = null;
		if("NCTASK".equals(tasktype))
			return new NCWFTaskProcessor();
		else if("SSCTASK".equals(tasktype))
			return new SSCWFTaskProcessor();
		return null;
	}

}
