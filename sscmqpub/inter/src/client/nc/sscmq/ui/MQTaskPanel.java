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
		JLabel lblMemo=new JLabel("�˽�����Ҫ�����ṩSSC��������ģʽ�µ�ҵ�񲹳�����");
		lblMemo.setText("�˽�����Ҫ�����ṩSSC��������ģʽ�µ�ҵ�񲹳�����");
		add(lblMemo, BorderLayout.CENTER);
	}
}
