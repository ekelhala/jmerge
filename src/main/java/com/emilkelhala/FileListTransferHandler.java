package com.emilkelhala;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

public class FileListTransferHandler extends TransferHandler {
    
    private JList<FileListItem> fileList;

    public FileListTransferHandler(JList<FileListItem> fileList) {
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
    DefaultListModel<FileListItem> listModel = (DefaultListModel<FileListItem>)fileList.getModel();
    int index = dl.getIndex(); 
    Transferable t = info.getTransferable();
    String data;
    try {
        data = (String)t.getTransferData(DataFlavor.stringFlavor);
    } 
    catch (Exception e) { return false; }
    FileListItem target = null;
    for(int i = 0; i < listModel.size(); i++) {
        if(data.equals(listModel.get(i).getId())) {
            target = listModel.get(i);
        }
    }
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
    JList<FileListItem> list = (JList<FileListItem>)c;
    return new StringSelection(list.getSelectedValue().getId());
}
}
