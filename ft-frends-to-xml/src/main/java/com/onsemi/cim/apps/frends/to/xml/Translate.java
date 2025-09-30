package com.onsemi.cim.apps.frends.to.xml;

import java.io.File;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 *  Main class for translator executing.
 * 
 * @author fg7x8c
 */
public class Translate {
    
    @Option(name="-o", aliases = { "--output" }, required = true, usage="Output XML file", metaVar="OUTPUT")
    private File out;

    @Option(name="-i", aliases = { "--input" }, required = true, usage="Input file", metaVar="INPUT")
    private File in;
    
    @Option(name="-data_type", usage="Enable tolerant parsing for alternate ASC dialects (optional)")
    private boolean dataType = false;
    
    public static void main (String[] args){
        new Translate().doMain(args);
    }
    
    private void doMain(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar frends-to-xml-2.0.0-jar-with-dependencies.jar [options...] arguments...");
            parser.printUsage(System.err);
            System.err.println();
            return;
        }

        try {
            Translator translator = new Translator(in, out, false, false, dataType);
            translator.translate();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }
    
}
