package com.stefanaleksic;

import java.awt.BorderLayout;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ScreenShotMain {

	static String outputPath = "";;
	static boolean canPaste = false;
	static boolean canPath = true;
	static JButton paste = new JButton("Save the screenshot");
	static JButton fileLocation = new JButton(
			"Where would you like to save this?");
	static File output = null;
	static String dateString;

	public static void main(String args[]) {

		final BufferedImage s = null;
		final JFrame frame = new JFrame("SCREENSHOTTER");
		final Clipboard clipboard = frame.getToolkit().getSystemClipboard();
		paste.setEnabled(false);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame,
					"ERROR WITH SELECTING LOOK AND FEEL.");
		}

		fileLocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(frame);

				output = fc.getSelectedFile();
				fileLocation.setEnabled(false);
				paste.setEnabled(true);
			}
		});
		paste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm:ss");
				Date date = new Date();

				Transferable clipData = clipboard.getContents(clipboard);
				dateString = dateFormat.format(date);
				
				//Windows Doesn't allow : colons or / slashes in file names
				dateString = dateString.replace(':', '-');
				System.out.println(dateString);
				dateString = dateString.replace('/', '-');
				//
				
				try {

					if (clipData.isDataFlavorSupported(DataFlavor.imageFlavor)) {
						BufferedImage s = (BufferedImage) (clipData
								.getTransferData(DataFlavor.imageFlavor));
						System.out.println(output.toString());
						try {
							output.createNewFile();
							ImageIO.write(s, "png", output);
						} catch (IOException e) {
							JOptionPane
									.showConfirmDialog(frame,
											"The path doesn't exist. \n\nPlease try again with a real path.");
						}
						JOptionPane.showMessageDialog(frame, "Done!");
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(frame, "ERROR: Content in clipboard not an image!");
				}
			}
		});

		JPanel p = new JPanel();
		JPanel d = new JPanel();
		p.add(fileLocation);

		d.add(paste);
		frame.add(p, BorderLayout.SOUTH);
		frame.add(d, BorderLayout.NORTH);
		frame.setSize(300, 200);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}