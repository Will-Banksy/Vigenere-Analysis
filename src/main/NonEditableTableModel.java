package main;

import javax.swing.table.AbstractTableModel;

public class NonEditableTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 8926476692586848995L;
	
	private String[] columnNames;
	private Object[][] data;

	public NonEditableTableModel(Object[][] data, String[] columnNames) {
		this.columnNames = columnNames;
		this.data = data;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/*
	 * Don't need to implement this method unless your table's
	 * editable.
	 */
	public boolean isCellEditable(int row, int col) {
		//Note that the data/cell address is constant,
		//no matter where the cell appears onscreen.
		return false;
	}

	/*
	 * Don't need to implement this method unless your table's
	 * data can change.
	 */
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
}