package com.emilkelhala;

import java.io.File;
import java.util.UUID;

public class FileListItem {
    private File file;
    private UUID id;

    public FileListItem(File file) {
        this.file = file;
        id = UUID.randomUUID();
    }

    public File getFile() {
        return file;
    }

    public String getId() {
        return id.toString();
    }

    @Override
    public boolean equals(Object other) {
        FileListItem otherListItem = (FileListItem) other;
        return otherListItem.id == this.id;
    }
}
