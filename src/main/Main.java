package main;

import javax.swing.JOptionPane;

public class Main {
	Frame frame;
	
	public static void main(String[] args) {
		new Main().init();
	}
	
	public void init() {
		frame = new Frame();
		
		Dialogs.showInputAreaDialog(frame, "Please Provide Input", (text, submitted) -> {
			JOptionPane.showMessageDialog(frame, "Submitted: " + submitted);
			Dialogs.showInputAreaDialog(frame, "Please Provide Your SOul", (text_, submitted_) -> {
				JOptionPane.showMessageDialog(frame, "Submitted: " + submitted_);
				return true;
			}, 1, false);
			return false;
		}, 1, false);
	}
}