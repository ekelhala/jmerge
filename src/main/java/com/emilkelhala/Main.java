package com.emilkelhala;

import org.apache.commons.cli.ParseException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

public class Main {
    public static void main(String[] args) {
        final Options options = new Options();
        options.addRequiredOption("f", "files", true, "PDF files to merge");
        options.addRequiredOption("o", "output", true, "Path to the output file");
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);

        }
        catch(ParseException exception) {
            System.out.println(exception.getMessage());
        }
    }
}