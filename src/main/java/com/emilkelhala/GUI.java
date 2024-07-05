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
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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

    private JList<File> fileList;
    private DefaultListModel<File> fileListModel;

    private PDFMerger myMerger;

    // Create gaps
    private Dimension filler = new Dimension(0,10);

    public GUI() {

        //Initialize variables
        this.fileListModel = new DefaultListModel<File>();
        myMerger = new PDFMerger(null);

        frame = new JFrame(TITLE);
        frame.setSize(400, 400);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        // Add instructions
        JPanel instructionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        instructionsPanel.add(new JLabel("Drag & drop to reorder the items"));
        frame.add(instructionsPanel);
        frame.add(Box.createRigidArea(filler));
        // Add file list
        fileList = new JList<File>(fileListModel);
        fileList.setCellRenderer(new FileListCellRenderer());
        fileList.setLayoutOrientation(JList.VERTICAL);
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileList.setDragEnabled(true);
        fileList.setDropMode(DropMode.ON);
        fileList.setTransferHandler(new FileListTransferHandler(fileList));
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
        // Add actions
        frame.add(Box.createRigidArea(new Dimension(0, 10)));
        performMerge = new JButton("Merge");
        performMerge.addActionListener(this);
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actionPanel.add(performMerge);
        frame.add(actionPanel);
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
                    fileListModel.addElement(file);
                }
            }
        }
        else if(eventSource.equals(removeDocument)) {
            int selectedIndex = fileList.getSelectedIndex();
            if(selectedIndex > -1)
                fileListModel.removeElementAt(selectedIndex);
        }
        else if(eventSource.equals(performMerge)) {
            if(fileListModel.isEmpty() || fileListModel.size() < 2) {
                JOptionPane.showMessageDialog(frame, "Too few files", "JMerge - Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                selectDestinationPath();
            }
        }
    }

    private void mergeSelected(String destPath) {
        myMerger.setDestination(destPath);
        try {
            List<PDDocument> docsToMerge = new ArrayList<>();
            for(int i=0;i<fileListModel.size();i++) {
                docsToMerge.add(PDDocument.load(fileListModel.get(i)));
            }
            PDDocument result = myMerger.merge(docsToMerge);
            myMerger.save(result);
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(frame, "Merge failed", "JMerge - Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
            return;
        }
        fileListModel.removeAllElements();
        JOptionPane.showMessageDialog(frame, "Merge done!");
    }

    private void selectDestinationPath() {
        final JFileChooser outputChooser = new JFileChooser();
            outputChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            outputChooser.setFileFilter(new FileNameExtensionFilter("PDF files", "pdf"));
            outputChooser.setDialogTitle("Select output file");
            if(outputChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                String outputPath = outputChooser.getSelectedFile().toPath().toString();
                if(!outputPath.endsWith(".pdf")) {
                    outputPath = outputPath + ".pdf";
                }
                mergeSelected(outputPath);
            }
    }

}
