package main;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import main.AnalysisUtils.RepeatedSequence;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 2231850685721535100L;
	
	private class NonEditableTableModel extends AbstractTableModel {
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
	
	public JPanel mainContent;

	public MainFrame() throws HeadlessException {
		super();
		init();
	}

	public MainFrame(GraphicsConfiguration gc) {
		super(gc);
		init();
	}
	
	public void init() {
		setTitle("Vigenere Analysis");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(Dialogs.minimumDialogSize);
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		JButton setText = new JButton("Set Ciphertext");
		c.insets = new Insets(5, 5, 5, 5);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		add(setText, c);
		
		mainContent = new JPanel();
		mainContent.setLayout(new GridBagLayout());
		JScrollPane mainScrollArea = new JScrollPane(mainContent);
		c.insets = new Insets(0, 5, 5, 5);
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		add(mainScrollArea, c);
		
		setText.addActionListener((actionEvent) -> {
			Dialogs.showInputAreaDialog(this, "Enter Ciphertext", (text, submitted) -> {
				if(submitted) {
					setupMainContent(text);
				}
				return true;
			}, 0, false); // ID 0
		});
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void setupMainContent(String ciphertext) { // This function is called once we've recieved the ciphertext
		mainContent.removeAll();
		
		GridBagConstraints c = new GridBagConstraints();
		
		JTable table = visualiseKasiskiData(ciphertext);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		JPanel tableContainer = new JPanel();
		tableContainer.setLayout(new BorderLayout());
		tableContainer.add(table.getTableHeader(), BorderLayout.NORTH);
		tableContainer.add(table, BorderLayout.CENTER); // Need to wrap it in a JScrollPane or it doesn't show column headers
		mainContent.add(tableContainer, c);
		
		mainContent.revalidate();
		pack();
		setLocationRelativeTo(null);
	}

	// We need a JTable to visualise the data from the Kasiski Test
	public JTable visualiseKasiskiData(String ciphertext) {
		ArrayList<RepeatedSequence> list = AnalysisUtils.KasiskiTest(ciphertext);
		
		int numCols = 20;
		
		String[] columnNames = Utils.concat(new String[]{ "Sequence" }, Utils.count(2, numCols + 1));
		
		int rows = list.size();
		int cols = numCols;
		Object[][] data = new Object[rows][cols];
		// Need to provide the table with empty data that is overwritten
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				data[i][j] = "";
			}
		}
		
		for(int i = 0; i < list.size(); i++) {
			data[i][0] = list.get(i).getSequence();
			Integer[] spacings = list.get(i).getSpacings();
			for(int j = 0; j < spacings.length; j++) {
				int index = spacings[j] - 1;
				if(index < cols && index > 0) {
					data[i][index] = "X";
				}
			}
		}
		
		JTable table = new JTable(new NonEditableTableModel(data, columnNames));
		TableColumn column = null;
		for (int i = 0; i < cols; i++) {
		    column = table.getColumnModel().getColumn(i);
		    if (i > 0) {
		        column.setPreferredWidth(25); //third column is bigger
		    } else {
		        column.setPreferredWidth(100);
		    }
		}
		table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		return table;
	}
}
