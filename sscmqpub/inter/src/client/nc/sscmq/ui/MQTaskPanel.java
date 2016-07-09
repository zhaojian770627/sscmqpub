package nc.sscmq.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import nc.ui.pub.beans.UIPanel;


public class MQTaskPanel extends UIPanel {
	private static final long serialVersionUID = 1L;

	public MQTaskPanel() {
		initUI();
	}

	private void initUI() {
		setLayout(new BorderLayout());
		JLabel lblMemo=new JLabel("此界面主要用于提供SSC独立部署模式下的业务补偿操作");
		lblMemo.setText("此界面主要用于提供SSC独立部署模式下的业务补偿操作");
		add(lblMemo, BorderLayout.CENTER);
	}
}
