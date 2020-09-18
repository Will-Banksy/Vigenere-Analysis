package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableColumn;

import main.AnalysisUtils.RepeatedSequence;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 2231850685721535100L;
	
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
		JPanel mainContentCont = new JPanel();
		mainContentCont.setLayout(new BorderLayout());
		mainContentCont.add(mainContent, BorderLayout.NORTH);
		JScrollPane mainScrollArea = new JScrollPane(mainContentCont);
		mainScrollArea.setAlignmentY(TOP_ALIGNMENT);
		c.insets = new Insets(0, 5, 5, 5);
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		add(mainScrollArea, c);
		
		setText.addActionListener((actionEvent) -> {
			Dialogs.showInputAreaDialog(this, "Enter Ciphertext", (text, submitted) -> {
				if(submitted) {
					text = text.replaceAll("\\s+",""); // Remove all whitespaces
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
		c.insets = new Insets(5, 5, 5, 5);
		
		JLabel ciphertextAreaLabel = new JLabel("Cipher Text");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		mainContent.add(ciphertextAreaLabel, c);
		
		JTextArea ciphertextArea = new JTextArea(ciphertext);
		ciphertextArea.setEditable(false);
		JScrollPane ciphertextAreaSPane = new JScrollPane(ciphertextArea);
		ciphertextAreaSPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
		ciphertextAreaSPane.setMinimumSize(new Dimension(20, 100));
		c.gridy = 1;
		c.insets = new Insets(0, 5, 10, 5);
		mainContent.add(ciphertextAreaSPane, c);
		
		JLabel kasiskiLabel = new JLabel("Kasiski Examination");
		c.gridy = 2;
		c.insets = new Insets(0, 5, 5, 5);
		mainContent.add(kasiskiLabel, c);

		JTable table = visualiseKasiskiData(ciphertext);
		JPanel tableContainer = new JPanel();
		tableContainer.setLayout(new BorderLayout());
		tableContainer.add(table.getTableHeader(), BorderLayout.NORTH);
		tableContainer.add(table, BorderLayout.CENTER); // Need to wrap it in a JScrollPane or it doesn't show column headers
		c.gridy = 3;
		mainContent.add(tableContainer, c);
		
		mainContent.revalidate();
		getContentPane().repaint();
		pack();
		setLocationRelativeTo(null);
	}

	// We need a JTable to visualise the data from the Kasiski Test
	public JTable visualiseKasiskiData(String ciphertext) {
		ArrayList<RepeatedSequence> list = AnalysisUtils.KasiskiTest(ciphertext);
		
		int numCols = 36;
		
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
