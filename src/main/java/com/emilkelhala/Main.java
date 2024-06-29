package com.emilkelhala;

import org.apache.commons.cli.ParseException;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

public class Main {

    private static final String FILES = "f";
    private static final String OUTPUT = "o";

    public static void main(String[] args) {
        final Options options = new Options();
        options.addRequiredOption(FILES, null, true, "PDF files to merge");
        options.addRequiredOption(OUTPUT, null, true, "Path to the output file");
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;
        try {
            commandLine = parser.parse(options, args);
            PDFMerger merger = new PDFMerger(commandLine.getOptionValue(OUTPUT));
            List<PDDocument> docs = new ArrayList<>();
            for(String filePath : commandLine.getOptionValues(FILES)) {
                File file = new File(filePath);
                docs.add(PDDocument.load(file));
            }
            merger.merge(docs);
            merger.save();
            System.out.printf("%i documents merged%n", commandLine.getOptionValues(FILES).length);
            System.out.printf("Output saved to %s%n", commandLine.getOptionValue(OUTPUT));
        }
        catch(Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}