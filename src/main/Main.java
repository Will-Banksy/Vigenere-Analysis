package main;

import javax.swing.JFrame;

public class Main {
	Frame frame;
	
	public static void main(String[] args) {
		new Main().init();
	}
	
	public void init() {
		frame = new Frame("Vigenere Analysis");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// TODO: Add components
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}