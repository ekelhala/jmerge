package com.emilkelhala;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

public class PDFMerger {
    
    private PDFMergerUtility merger;
    private String dest;

    /**
     * Setup the merger
     * @param dest The destination file to be written
     */
    public PDFMerger(String dest){
        merger = new PDFMergerUtility();
        this.dest = dest;
    }

    /**
     * 
     * Merge documents to each other and return the result
     * 
     * @param documents Documents to be merged
     * @return Document containing the merged documents, in the order they were given
     */
    public PDDocument merge(List<PDDocument> documents) throws IOException {
        PDDocument ret = new PDDocument();
        for(PDDocument document : documents) {
                merger.appendDocument(ret, document);
        }
        return ret;
    }

    public void save(PDDocument document) throws IOException {
        document.save(dest);
    }

    public void setDestination(String dest) {
        this.dest = dest;
    }
}
