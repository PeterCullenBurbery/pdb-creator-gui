//package com.peter_burbery.pdb_creator;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class DeletePDBGUI {
//	public static void main(String[] args) {
//		// Create the main frame
//		JFrame frame = new JFrame("PDB Entry");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setSize(400, 300);
//		frame.setLayout(new BorderLayout());
//
//		// Create a panel for input with button
//		JPanel inputPanel = new JPanel();
//		inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Align input and button in a row
//
//		JTextField textField = new JTextField(20);
//		JButton createPDBButton = new JButton("Delete PDB");
//
//		inputPanel.add(textField);
//		inputPanel.add(createPDBButton);
//
//		// Create a text area for displaying the output
//		JTextArea displayArea = new JTextArea(10, 30);
//		displayArea.setEditable(false);
//		JScrollPane scrollPane = new JScrollPane(displayArea);
//
//		// Add components to the frame
//		frame.add(inputPanel, BorderLayout.NORTH);
//		frame.add(scrollPane, BorderLayout.CENTER);
//
//		// Define the action that appends the input to the display area
//		ActionListener createPDBAction = new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				String pdbName = textField.getText();
//				displayArea.append("You entered: " + pdbName + "\n");
//			}
//		};
//
//		// Attach the action to both the button and the text field
//		createPDBButton.addActionListener(createPDBAction);
//		textField.addActionListener(createPDBAction); // This triggers on pressing Enter
//
//		// Make the frame visible
//		frame.setVisible(true);
//	}
//}
package com.peter_burbery.pdb_creator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class DeletePDBGUI {
    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("PDB Entry");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // Create a panel for input with button
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Align input and button in a row

        JTextField textField = new JTextField(20);
        JButton deletePDBButton = new JButton("Delete PDB");

        inputPanel.add(textField);
        inputPanel.add(deletePDBButton);

        // Create a text area for displaying the output
        JTextArea displayArea = new JTextArea(10, 30);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Add components to the frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Define the action that deletes the PDB
        deletePDBButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pdbName = textField.getText().trim();
                if (pdbName.isEmpty()) {
                    displayArea.append("Error: Please enter a valid PDB name.\n");
                    return;
                }

                try (Connection conn = PDB.getConnection("orcl.localdomain", true)) {
                    if (PDB.checkIfPDBExists(conn, pdbName)) {
                        PDB.dropPDB(conn, pdbName);
                        displayArea.append("Deleted PDB: " + pdbName + "\n");
                    } else {
                        displayArea.append("PDB " + pdbName + " does not exist.\n");
                    }
                } catch (SQLException ex) {
                    displayArea.append("An error occurred while deleting the PDB: " + ex.getMessage() + "\n");
                    ex.printStackTrace();
                }

                // Clear the text field after submission
                textField.setText("");
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }
}
