package main;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;

public class Frame extends JFrame {
	private static final long serialVersionUID = 2231850685721535100L;

	public Frame() throws HeadlessException {
		super();
		init();
	}

	public Frame(GraphicsConfiguration gc) {
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
