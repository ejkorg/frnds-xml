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
    
    @Option(name="-data_type", usage="Data type hint (use 'FT' to enable tolerant parsing). Tolerant parsing is enabled only when -data_type=FT.")
    private String dataType = null;
    
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
            // decide tolerant parsing: true only if explicit -data_type=FT
            boolean tolerant = (dataType != null && dataType.equalsIgnoreCase("FT"));
            Translator translator = new Translator(in, out, false, false, tolerant);
            translator.translate();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }
    
}
