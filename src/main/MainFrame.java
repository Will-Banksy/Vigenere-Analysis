package main;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;

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
		
		// TODO: Add components
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
