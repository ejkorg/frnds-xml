package com.onsemi.cim.apps.frends.to.xml;

import java.io.File;

public class GenerateXmls {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: GenerateXmls <asc-file> <out-xml>");
            System.exit(2);
        }

        File asc = new File(args[0]);
        File out = new File(args[1]);
        out.getParentFile().mkdirs();

        Translator t = new Translator(asc, out, false, false, true);
        t.translate();
        System.out.println("Wrote: " + out.getAbsolutePath());
    }
}
