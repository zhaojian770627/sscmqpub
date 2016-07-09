package nc.impl.inter;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.execute.Executor;
import nc.itf.inter.IInterTaskManager;
import nc.vo.sscmq.InterTaskVO;

public class TaskDispatcher extends Executor {
	IInterTaskManager taskManager=NCLocator.getInstance().lookup(IInterTaskManager.class);
	private static final int NTHREADS = 100;
	private static final java.util.concurrent.Executor executor=Executors.newFixedThreadPool(NTHREADS);
	
	@Override
	public void run() {
		while(true)
		{
			// 获取一批任务
			InterTaskVO[] tasks=null;//taskManager.getSynTask();
			if(tasks==null||tasks.length==0)
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			
			
			for(InterTaskVO vo:tasks)
			{
				TaskExecutor taskExecutor=new TaskExecutor(vo);
				executor.execute(taskExecutor);
				
				Thread runThread = new Thread(taskExecutor);

				runThread.start();
				try {
					runThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		}
	}
	
}
