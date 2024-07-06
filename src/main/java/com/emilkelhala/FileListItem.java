package com.emilkelhala;

import java.io.File;
import java.util.UUID;

/**
 * @author Emil Kelhälä
 * FileListItem class represents a single PDF document in the list of documents to be merged.
 * Each list item contains the File it represents, and an unique id to distinquish between multiple 
 * instances of the same document being used.
 */

public class FileListItem {
    private File file;
    private UUID id;

    /**
     * Create a new list item to be added to a list of files. An unique id is generated at creation for every FileListItem
     * @param file File object this list item references to
     */
    public FileListItem(File file) {
        this.file = file;
        id = UUID.randomUUID();
    }

    /**
     * Get the file represented by this list item
     * @return The file this list item represents
     */
    public File getFile() {
        return file;
    }

    /**
     * Get the unique id of this list item
     * @return The unique id of this list item, as String
     */
    public String getId() {
        return id.toString();
    }

    @Override
    public boolean equals(Object other) {
        FileListItem otherListItem = (FileListItem) other;
        return otherListItem.id == this.id;
    }
}
