//package pDB_creator;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class PDBEntryGUI {
//    public static void main(String[] args) {
//        // Create the main frame
//        JFrame frame = new JFrame("PDB Entry");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 300);
//        frame.setLayout(new BorderLayout());
//
//        // Create a panel for input with button
//        JPanel inputPanel = new JPanel();
//        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Align input and button in a row
//
//        JTextField textField = new JTextField(20);
//        JButton createPDBButton = new JButton("Create PDB");
//
//        inputPanel.add(textField);
//        inputPanel.add(createPDBButton);
//
//        // Create a text area for displaying the output
//        JTextArea displayArea = new JTextArea(10, 30);
//        displayArea.setEditable(false);
//        JScrollPane scrollPane = new JScrollPane(displayArea);
//
//        // Add components to the frame
//        frame.add(inputPanel, BorderLayout.NORTH);
//        frame.add(scrollPane, BorderLayout.CENTER);
//
//        // Define the action that appends the input to the display area
//        ActionListener createPDBAction = new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String pdbName = textField.getText();
//                displayArea.append("You entered: " + pdbName + "\n");
//            }
//        };
//
//        // Attach the action to both the button and the text field
//        createPDBButton.addActionListener(createPDBAction);
//        textField.addActionListener(createPDBAction); // This triggers on pressing Enter
//
//        // Make the frame visible
//        frame.setVisible(true);
//    }
//}
package com.peter_burbery.pdb_creator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class CreatePDBGUI {

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("PDB Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Create a panel for input with button
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JTextField textField = new JTextField(20);
        JButton createPDBButton = new JButton("Create/Check PDB");

        inputPanel.add(textField);
        inputPanel.add(createPDBButton);

        // Create a text area for displaying the output
        JTextArea displayArea = new JTextArea(15, 40);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Add components to the frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Add button action listener
        createPDBButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pdbNameInput = textField.getText();
                if (pdbNameInput.isEmpty()) {
                    displayArea.append("PDB name cannot be empty.\n");
                    return;
                }

                try (Connection conn = PDB.getConnection("orcl.localdomain", true)) {
                    Name pdbNameObj = new Name(pdbNameInput);

                    String pdbName = pdbNameObj.getUnderscoreUppercase();
                    String pdbAdmin = pdbNameObj.getUnderscoreUppercase();
                    String OraclePluggableDatabaseDirectory = "C:\\oracle-pluggable-database\\";
                    String fileName = pdbNameObj.getDashLowercase();
                    String fileLocation = OraclePluggableDatabaseDirectory + fileName;
                    String adminPassword = "1234";

                    displayArea.append("Processing PDB: " + pdbName + "\n");

                    if (PDB.checkIfPDBExists(conn, pdbName)) {
                        displayArea.append("PDB " + pdbName + " already exists.\n");

                        if (PDB.checkForActiveConnections(conn, pdbName)) {
                            displayArea.append("There are active connections to the PDB. Cannot drop the PDB.\n");
                        } else {
                            int response = JOptionPane.showConfirmDialog(frame,
                                    "No active connections found. Do you want to drop the PDB and recreate it?",
                                    "Confirm",
                                    JOptionPane.YES_NO_OPTION);

                            if (response == JOptionPane.YES_OPTION) {
                                PDB.dropPDB(conn, pdbName);
                                PDB.createPDB(conn, pdbName, pdbAdmin, adminPassword, fileLocation);
                                PDB.grantRolesAndPrivileges(conn, pdbName, pdbAdmin);
                                displayArea.append("PDB " + pdbName + " recreated successfully.\n");
                            } else {
                                displayArea.append("Exiting without recreating the PDB.\n");
                            }
                        }
                    } else {
                        PDB.createPDB(conn, pdbName, pdbAdmin, adminPassword, fileLocation);
                        PDB.grantRolesAndPrivileges(conn, pdbName, pdbAdmin);
                        displayArea.append("PDB " + pdbName + " created successfully.\n");
                    }

                } catch (SQLException ex) {
                    displayArea.append("SQL Exception: " + ex.getMessage() + "\n");
                    ex.printStackTrace();
                }
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }
}
