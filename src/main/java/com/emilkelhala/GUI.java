package com.emilkelhala;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.pdmodel.PDDocument;

public class GUI implements ActionListener {

    private final static String TITLE = "JMerge";
    private JFrame frame;

    private JButton addDocument;
    private JButton performMerge;
    private JButton removeDocument;

    private JTextField outputFile;
    private JList<String> fileList;
    private DefaultListModel<String> fileListModel;

    private List<File> selectedFiles;
    private PDFMerger myMerger;

    // Create gaps
    private Dimension filler = new Dimension(0,10);

    public GUI() {

        //Initialize variables
        this.selectedFiles = new ArrayList<>();
        this.fileListModel = new DefaultListModel<String>();
        myMerger = new PDFMerger(null);

        frame = new JFrame(TITLE);
        frame.setSize(400, 400);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(Box.createRigidArea(filler));
        frame.add(new JLabel("Files to merge: "));
        frame.add(Box.createRigidArea(filler));
        // Add file list
        fileList = new JList<String>(fileListModel);
        fileList.setLayoutOrientation(JList.VERTICAL);
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        frame.add(fileList);
        frame.add(Box.createRigidArea(filler));
        // Add buttons
        JPanel fileActionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addDocument = new JButton("Add document");
        addDocument.addActionListener(this);
        fileActionButtonPanel.add(addDocument);
        removeDocument = new JButton("Remove selected");
        removeDocument.addActionListener(this);
        fileActionButtonPanel.add(removeDocument);
        frame.add(fileActionButtonPanel);
        frame.add(Box.createRigidArea(new Dimension(0, 10)));
        
        frame.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel outputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        outputPanel.add(new JLabel("Output file name:"));
        outputPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        outputFile = new JTextField(20);
        outputFile.setToolTipText("Output file path");
        outputFile.setSize(new Dimension(100, 30));
        outputPanel.add(outputFile);
        frame.add(outputPanel);
        frame.add(Box.createRigidArea(new Dimension(0, 10)));
        performMerge = new JButton("Merge");
        performMerge.addActionListener(this);
        frame.add(performMerge);
        frame.add(Box.createRigidArea(filler));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    
    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public void actionPerformed(ActionEvent e) {
        Object eventSource = e.getSource();
        if(eventSource.equals(addDocument)) {
            final JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileNameExtensionFilter("PDF files", "pdf"));
            fc.setMultiSelectionEnabled(true);
            int ret = fc.showOpenDialog(frame);
            if(ret == JFileChooser.APPROVE_OPTION) {
                for(File file : fc.getSelectedFiles()) {
                    selectedFiles.add(file);
                    fileListModel.addElement(file.getName());
                }
            }
        }
        else if(eventSource.equals(removeDocument)) {
            int selectedIndex = fileList.getSelectedIndex();
            if(selectedIndex > -1)
                fileListModel.removeElementAt(selectedIndex);
                selectedFiles.remove(selectedIndex);
        }
        else if(eventSource.equals(performMerge)) {
            if(outputFile.getText().isBlank()) {
                JOptionPane.showMessageDialog(frame, "No output file specified", "JMerge - Error", JOptionPane.ERROR_MESSAGE);
            }
            else if(selectedFiles.isEmpty() || selectedFiles.size() < 2) {
                JOptionPane.showMessageDialog(frame, "Too few files", "JMerge - Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                mergeSelected(outputFile.getText());
                fileListModel.removeAllElements();
                selectedFiles.clear();
                outputFile.setText("");
            }
        }
    }

    private void mergeSelected(String destPath) {
        myMerger.setDestination(destPath);
        try {
            List<PDDocument> docsToMerge = new ArrayList<>();
            for(File file : selectedFiles) {
                docsToMerge.add(PDDocument.load(file));
            }
            PDDocument result = myMerger.merge(docsToMerge);
            myMerger.save(result);
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(frame, "Merge failed", "JMerge - Error", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(frame, "Merge done!");
    }

}
