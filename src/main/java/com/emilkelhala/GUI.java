package com.emilkelhala;

import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI implements ActionListener {

    private final static String TITLE = "JMerge";
    private JFrame frame;
    private JButton addDocument;
    private JButton performMerge;
    private JTextField outputFile;

    public GUI() {
        frame = new JFrame(TITLE);
        frame.setSize(400, 400);
        frame.setLayout(new FlowLayout());
        addDocument = new JButton("Add document");
        addDocument.addActionListener(this);
        frame.add(addDocument);
        frame.add(Box.createRigidArea(new Dimension(0, 10)));
        performMerge = new JButton("Merge");
        frame.add(performMerge);
        frame.add(Box.createRigidArea(new Dimension(0, 10)));
        frame.add(new JLabel("Output file name:"));
        frame.add(Box.createRigidArea(new Dimension(0, 2)));
        outputFile = new JTextField(20);
        outputFile.setToolTipText("Output file name");
        outputFile.setSize(new Dimension(100, 30));
        frame.add(outputFile);
        frame.add(Box.createRigidArea(new Dimension(0, 10)));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    
    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(addDocument)) {
            final JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileNameExtensionFilter("PDF files", "pdf"));
            int ret = fc.showOpenDialog(frame);
        }
    }

}
