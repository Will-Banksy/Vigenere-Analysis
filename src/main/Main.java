package main;

import javax.swing.JOptionPane;

public class Main {
	Frame frame;
	
	public static void main(String[] args) {
		new Main().init();
	}
	
	public void init() {
		frame = new Frame();
		
		Dialogs.showInputAreaDialog(frame, "Hello, Input Text or die", (text, submitted) -> {
			JOptionPane.showMessageDialog(frame, "Submitted: " + submitted);
			return true;
		});
	}
}