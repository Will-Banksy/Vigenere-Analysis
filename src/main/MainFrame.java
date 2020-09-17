package main;

import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 2231850685721535100L;

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
		
		JScrollPane mainScrollArea = new JScrollPane(); // This scroll pane is going to have to be accessible
		c.insets = new Insets(0, 5, 5, 5);
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		add(mainScrollArea, c);
		
		setText.addActionListener((actionEvent) -> {
			Dialogs.showInputAreaDialog(this, "Enter Ciphertext", (text, submitted) -> {
				return true;
			});
		});
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
