package main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Dialogs {
	private static HashMap<Integer, JDialog> cachedInputDiags = new HashMap<Integer, JDialog>();
	private static HashMap<Integer, JTextArea> cachedInputAreas = new HashMap<Integer, JTextArea>();
	
	public static int componentsSpacing = 5;
	public static Dimension minimumDialogSize = new Dimension(400, 400);
	
	public static interface InputAction {
		/**
		 * This method is called when the "Submit", "Cancel" or window close buttons are pressed
		 * @param text - The text that is in the JTextArea at time of method call
		 * @param submitted - Whether the "Submit" button was clicked
		 * @return Return true to hide the controlling dialog as normal, or false to make the dialog not hide
		 */
		public abstract boolean handleInput(String text, boolean submitted);
	}
	
	public static void showInputAreaDialog(Component parent, String title, InputAction action) {
		showInputAreaDialog(parent, title, action, -1, false);
	}

	/**
	 * Shows a dialog with a JTextArea for typing into, and a cancel and submit button at the bottom
	 * @param title - The title of the JDialog
	 * @param action - The InputAction that's handleInput method will be called when the "Submit", "Cancel" or window close buttons are pressed
	 * @param id - The id of this dialog - to reuse dialogs, call this method with the same id as the one you want to reuse
	 * @param clearText - If reusing the dialog, set to true to clear the reused dialog's text
	 */
	public static void showInputAreaDialog(Component parent, String title, InputAction action, int id, boolean clearText) {
		JDialog dialog = new JDialog();
		boolean fromMap = false;
		boolean saveDialog = false;
		if(id >= 0) {
			saveDialog = true;
			if(cachedInputDiags.containsKey(id)) {
				dialog = cachedInputDiags.get(id);
				fromMap = true;
			}
		}
		
		if(fromMap) {
			dialog.setVisible(true);
			if(clearText) {
				JTextArea area = cachedInputAreas.get(id);
				if(area != null) {
					area.setText("");
				}
			}
		} else {
			dialog.setTitle(title);
			dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			dialog.setMinimumSize(minimumDialogSize);
			dialog.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.insets = new Insets(componentsSpacing, componentsSpacing, componentsSpacing, componentsSpacing);
			// TODO: Fix insets between components
			
			JTextArea area = new JTextArea();
			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 2;
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1;
			c.weighty = 1;
			dialog.add(new JScrollPane(area), c); // Wrap the text area in a scroll pane, to allow scrolling
			
			JButton cancel = new JButton("Cancel");
			c.gridy = 1;
			c.gridwidth = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weighty = 0;
			dialog.add(cancel, c);
			
			JButton submit = new JButton("Submit");
			c.gridx = 1;
			dialog.add(submit, c);
			
			// So I can reference dialog inside anonymous classes
			final JDialog finalDialog = dialog;
			
			// Here be Dragons
			// Actually here be event handlers. Less exciting, I know
			cancel.addActionListener((actionEvent) -> {
				boolean hide = action.handleInput(area.getText(), false);
				if(hide) {
					finalDialog.setVisible(false);
				}
			});
			
			submit.addActionListener((actionEvent) -> {
				boolean hide = action.handleInput(area.getText(), true);
				if(hide) {
					finalDialog.setVisible(false);
				}
			});
			
			dialog.addWindowListener(new WindowAdapter() {
				@Override public void windowClosing(WindowEvent e) {
					boolean hide = action.handleInput(area.getText(), false);
					if(hide) {
						finalDialog.setVisible(false);
					}
				}
			});
			
			dialog.pack();
			dialog.setLocationRelativeTo(parent);
			dialog.setVisible(true);
			
			// Save the dialog and text area to be reused
			if(saveDialog) {
				cachedInputDiags.put(id, dialog);
				cachedInputAreas.put(id, area);
			}
		}
	}
}