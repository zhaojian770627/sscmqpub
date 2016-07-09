package nc.sscmq.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import nc.funcnode.ui.AbstractFunclet;
import nc.ui.uif2.NCAction;

public class MQManagerUI extends AbstractFunclet {

	MQTaskPanel taskPanel= null; 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2263470095824351459L;

	public MQManagerUI() {
		
	}

	

	@Override
	public void init() {
		setMenuActions(sscActions);
		initUI();
	}
	
	private void initUI()
	{
		setLayout(new BorderLayout());
		taskPanel=new MQTaskPanel();
		add(taskPanel, BorderLayout.CENTER);
	}

	// ---��ť��
	NCAction driveSSCFinished = new NCAction() {
		{
			setCode("driveSSCFinish");
			setBtnName("����SSC�������");
		}

		@Override
		public void doAction(ActionEvent e) throws Exception {
		}
	};
	
	NCAction rejectSSCFinished = new NCAction() {
		{
			setCode("rejectSSCFinished");
			setBtnName("����SSC�������");
		}

		@Override
		public void doAction(ActionEvent e) throws Exception {
		}
	};
	
	
	private NCAction[] sscActions = new NCAction[] { driveSSCFinished, rejectSSCFinished};
}