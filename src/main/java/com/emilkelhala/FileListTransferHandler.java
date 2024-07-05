package com.emilkelhala;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

public class FileListTransferHandler extends TransferHandler {
    
    private JList<File> fileList;

    public FileListTransferHandler(JList<File> fileList) {
        super();
        this.fileList = fileList;
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport info) {
        if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return false;
        }

        JList.DropLocation dl = (JList.DropLocation)info.getDropLocation();
        if (dl.getIndex() == -1) {
            return false;
        }
        return true;
    }
 @Override
public boolean importData(TransferHandler.TransferSupport info) {
    if (!info.isDrop() || !canImport(info)) {
        return false;
    }
        
    // Check for String flavor
    if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
        return false;
    }

    JList.DropLocation dl = (JList.DropLocation)info.getDropLocation();
    DefaultListModel<File> listModel = (DefaultListModel<File>)fileList.getModel();
    int index = dl.getIndex(); 
    Transferable t = info.getTransferable();
    String data;
    try {
        data = (String)t.getTransferData(DataFlavor.stringFlavor);
    } 
    catch (Exception e) { return false; }
    File target = new File(data);  
    listModel.removeElement(target);
    listModel.add(index, target);
    return true;
}

@Override
public int getSourceActions(JComponent c) {
    return MOVE;
}

@Override
@SuppressWarnings("unchecked")
protected Transferable createTransferable(JComponent c) {
    JList<File> list = (JList<File>)c;
    return new StringSelection(list.getSelectedValue().getAbsolutePath());
}
}
