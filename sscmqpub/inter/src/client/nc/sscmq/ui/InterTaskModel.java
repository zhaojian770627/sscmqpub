package nc.sscmq.ui;

import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import nc.vo.pub.ValueObject;
import nc.vo.sscmq.InterTaskVO;

public class InterTaskModel extends AbstractTableModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6011845924622808766L;
	
	private Object[] m_objTableFieldsHeader = { "±Í æ" };
	private Vector<Object> m_vecVOs;
	
	public InterTaskModel() {
		m_vecVOs = new Vector<Object>();
	}
	
	public InterTaskModel(List<InterTaskVO> tasks){
		m_vecVOs = new Vector<Object>();
		m_vecVOs.addAll(tasks);
	}
	
	public Object getVO(int idx) {
		if ((idx >= 0) && (idx < m_vecVOs.size())) {
			return m_vecVOs.elementAt(idx);
		}
		return null;
	}

	public void clearTable() {
		this.m_vecVOs.removeAllElements();
	}

	public void removeVO(int index) {
		if ((index < 0) || (index >= m_vecVOs.size())) {
			return;
		}
		m_vecVOs.removeElementAt(index);

	}

	public boolean addVO(Object o) {
		if (o == null) {
			return false;
		}
		m_vecVOs.addElement(o);
		return true;
	}

	public void addVO(ValueObject[] vos) {
		if ((vos == null) || (vos.length == 0))
			return;

		for (int i = 0; i < vos.length; ++i) {
			m_vecVOs.addElement(vos[i]);
		}

	}

	@Override
	public int getRowCount() {
		return m_vecVOs.size();
	}

	@Override
	public int getColumnCount() {
		return m_objTableFieldsHeader.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		InterTaskVO selectedObj = (InterTaskVO) m_vecVOs
				.elementAt(row);

		switch (col) {
		case 0:
			String taskId = selectedObj.getTaskid();
			return taskId;
		default:
			return null;
		}
	}
	
	@Override
	public String getColumnName(int col) {
		return ((String) this.m_objTableFieldsHeader[col]);
	}

}
