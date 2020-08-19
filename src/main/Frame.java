package main;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;

public class Frame extends JFrame {
	private static final long serialVersionUID = 2231850685721535100L;

	public Frame() throws HeadlessException {
		super();
	}

	public Frame(GraphicsConfiguration gc) {
		super(gc);
	}

	public Frame(String title) throws HeadlessException {
		super(title);
	}

	public Frame(String title, GraphicsConfiguration gc) {
		super(title, gc);
	}
}
