package nc.sscmq.ui;

import java.awt.BorderLayout;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;

public class InteTaskPanel extends UIPanel {
	private static final long serialVersionUID = 1L;
	private UITablePane tablePane;
	private InterTaskModel interTaskModel = null;

	public InteTaskPanel() {
		initUI();
	}

	private void initUI() {
		setLayout(new BorderLayout());

		tablePane = new UITablePane();
		add(tablePane, BorderLayout.CENTER);
		this.interTaskModel = new InterTaskModel();

		UITable table = tablePane.getTable();
		table.setModel(interTaskModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tablePane.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i = 0; i < tablePane.getTable().getColumnModel()
				.getColumnCount(); i++) {
			tablePane.getTable().getColumnModel().getColumn(i)
					.setPreferredWidth(150);
		}
	}


	public UITablePane getTablePane() {
		return this.tablePane;
	}
}
