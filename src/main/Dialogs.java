package main;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Dialogs {
	private Dialogs() {
		// An AssertionError is an error in the assertion a programmer made - in this case the assertion was that this class couldn't be instantiated. If this error is thrown, then there has been a programming error
		throw new AssertionError("This class should not be instantiable"); // This is an ERROR, not an EXCEPTION - ERRORS cannot be handled, they always cause the program to exit abnormally
	}
	
	private static HashMap<Integer, IComponentCollection> cachedInputComps = new HashMap<Integer, IComponentCollection>();
	private static HashMap<Integer, OComponentCollection> cachedOutputComps = new HashMap<Integer, OComponentCollection>();
	
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
	
	private static class IComponentCollection {
		JDialog diag;
		JTextArea area;
		JButton cancel;
		JButton submit;
		
		public IComponentCollection(JDialog diag, JTextArea area, JButton cancel, JButton submit) {
			this.diag = diag;
			this.area = area;
			this.cancel = cancel;
			this.submit = submit;
		}
	}
	
	private static class OComponentCollection {
		JDialog diag;
		JTextArea area;
		
		public OComponentCollection(JDialog diag, JTextArea area) {
			this.diag = diag;
			this.area = area;
		}
	}
	
	private static void removeAllActionListeners(AbstractButton comp) {
		for(ActionListener l : comp.getActionListeners()) {
			comp.removeActionListener(l);
		}
	}
	private static void removeAllWindowListeners(Window window) {
		for(WindowListener l : window.getWindowListeners()) {
			window.removeWindowListener(l);
		}
	}
	
	/**
	 * Shows a dialog with a JTextArea for typing into, and a cancel and submit button at the bottom. Note that using this method means the created dialog is not available for reuse
	 * @param parent - The Frame this dialog belongs to
	 * @param title - The title of the JDialog
	 * @param action - The InputAction that's handleInput method will be called when the "Submit", "Cancel" or window close buttons are pressed
	 * @see #showInputAreaDialog(Frame, String, InputAction, int, boolean)
	 */
	public static void showInputAreaDialog(Frame parent, String title, InputAction action) {
		showInputAreaDialog(parent, title, action, -1, false);
	}

	/**
	 * Shows a dialog with a JTextArea for typing into, and a cancel and submit button at the bottom
	 * @param parent - The Frame this dialog belongs to
	 * @param title - The title of the JDialog
	 * @param action - The InputAction that's handleInput method will be called when the "Submit", "Cancel" or window close buttons are pressed
	 * @param id - The id of this dialog - to reuse dialogs, call this method with the same id as the one you want to reuse (id >= 0, otherwise the dialog won't be available for reuse)
	 * @param clearText - If reusing the dialog, set to true to clear the reused dialog's text
	 * @see #showInputAreaDialog(Frame, String, InputAction)
	 */
	public static void showInputAreaDialog(Frame parent, String title, InputAction action, int id, boolean clearText) {
		JDialog dialog = new JDialog(parent);
		boolean fromMap = false;
		boolean saveDialog = false;
		if(id >= 0) {
			saveDialog = true;
			if(cachedInputComps.containsKey(id)) {
				dialog = cachedInputComps.get(id).diag;
				fromMap = true;
			}
		}
		
		if(fromMap) {
			if(clearText) {
				JTextArea area = cachedInputComps.get(id).area;
				if(area != null) {
					area.setText("");
				}
			}
			// We need to reset the event listeners - or the action we give is ignored
			IComponentCollection comps = cachedInputComps.get(id);
			
			removeAllActionListeners(comps.cancel);
			comps.cancel.addActionListener((actionEvent) -> {
				boolean hide = action.handleInput(comps.area.getText(), false);
				if(hide) {
					comps.diag.setVisible(false);
				}
			});

			removeAllActionListeners(comps.submit);
			comps.submit.addActionListener((actionEvent) -> {
				boolean hide = action.handleInput(comps.area.getText(), true);
				if(hide) {
					comps.diag.setVisible(false);
				}
			});
			
			removeAllWindowListeners(dialog);
			dialog.addWindowListener(new WindowAdapter() {
				@Override public void windowClosing(WindowEvent e) {
					boolean hide = action.handleInput(comps.area.getText(), false);
					if(hide) {
						comps.diag.setVisible(false);
					}
				}
			});
			
			dialog.setTitle(title);
			dialog.setVisible(true);
		} else {
			dialog.setTitle(title);
			dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			dialog.setMinimumSize(minimumDialogSize);
			dialog.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.insets = new Insets(componentsSpacing, componentsSpacing, componentsSpacing, componentsSpacing);
			
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
			c.insets = new Insets(0, componentsSpacing, componentsSpacing, componentsSpacing);
			dialog.add(cancel, c);
			
			JButton submit = new JButton("Submit");
			c.gridx = 1;
			c.insets = new Insets(0, 0, componentsSpacing, componentsSpacing);
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
				cachedInputComps.put(id, new IComponentCollection(dialog, area, cancel, submit));
			}
		}
	}
	
	/**
	 * Shows a dialog with a JTextArea for displaying text. Note that using this method means the created dialog is not available for reuse
	 * @param parent - the parent of the JDialog
	 * @param title - the title of the JDialog
	 * @param text - the text shown by the JTextArea
	 * @param editable - Whether the JTextArea will be editable. Note that if true, edits won't be saved or returned in any way
	 * @see #showOutputAreaDialog(Frame, String, String, boolean, int)
	 */
	public static void showOutputAreaDialog(Frame parent, String title, String text, boolean editable) { // This is why I really love optional parameters - don't have to create overloads (implicit overloading?)
		showOutputAreaDialog(parent, title, text, editable, -1);
	}
	
	/**
	 * Shows a dialog with a JTextArea for displaying text
	 * @param parent - the parent of the JDialog
	 * @param title - the title of the JDialog
	 * @param text - the text shown by the JTextArea
	 * @param editable - Whether the JTextArea will be editable. Note that if true, edits won't be saved or returned in any way
	 * @param id - The id of this dialog - to reuse dialogs, call this method with the same id as the one you want to reuse (id >= 0, otherwise the dialog won't be available for reuse)
	 * @see #showOutputAreaDialog(Frame, String, String, boolean)
	 */
	public static void showOutputAreaDialog(Frame parent, String title, String text, boolean editable, int id) {
		JDialog dialog = new JDialog(parent);
		boolean fromMap = false;
		boolean saveDialog = false;
		if(id >= 0) {
			saveDialog = true;
			if(cachedOutputComps.containsKey(id)) {
				dialog = cachedOutputComps.get(id).diag;
				fromMap = true;
			}
		}
		
		if(fromMap) {
			JTextArea area = cachedOutputComps.get(id).area;
			area.setText(text);
			area.setEditable(editable);
			
			dialog.setTitle(title);
			dialog.setVisible(true);
		} else {
			dialog.setTitle(title);
			dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			dialog.setMinimumSize(minimumDialogSize);
			dialog.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.insets = new Insets(componentsSpacing, componentsSpacing, componentsSpacing, componentsSpacing);
			
			JTextArea area = new JTextArea(text);
			area.setEditable(editable);
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1;
			c.weighty = 1;
			dialog.add(new JScrollPane(area), c);
			
			dialog.pack();
			dialog.setLocationRelativeTo(parent);
			dialog.setVisible(true);
			
			if(saveDialog) {
				cachedOutputComps.put(id, new OComponentCollection(dialog, area));
			}
		}
	}
}