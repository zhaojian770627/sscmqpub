package nc.impl.inter.background;

import java.util.concurrent.TimeUnit;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.impl.inter.TaskExecutor;
import nc.itf.inter.IInterTaskManager;
import nc.vo.pub.BusinessException;
import nc.vo.sscmq.InterTaskVO;

/**
 * 此后台插件专门用于测试
 * 
 * zhaojianc
 */
public class InterTaskExecutorBackgroundPlugin implements IBackgroundWorkPlugin {
	IInterTaskManager taskManager=NCLocator.getInstance().lookup(IInterTaskManager.class);
	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {

		while(true)
		{
			// 获取一批任务
			InterTaskVO[] tasks=taskManager.getSynTask();
			if(tasks==null|| tasks.length==0)
				return  returnEmtpyObj();
			if(tasks==null||tasks.length==0)
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			
			
			for (InterTaskVO vo : tasks) {
				try {
					TaskExecutor taskExecutor = new TaskExecutor(vo);
					Thread runThread = new Thread(taskExecutor);
					runThread.start();

					runThread.join();
				} catch (InterruptedException e) {
					Logger.error(e);
					throw new BusinessException(e);
				}
			}
		}
	}
	
	
	private PreAlertObject returnEmtpyObj() {
		PreAlertObject obj = new PreAlertObject();
		obj.setReturnType(PreAlertReturnType.RETURNNOTHING);

		return obj;
	}
}
