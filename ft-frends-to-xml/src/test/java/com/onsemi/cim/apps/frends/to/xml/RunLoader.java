package com.onsemi.cim.apps.frends.to.xml;

import com.onsemi.cim.apps.frends.to.xml.data.Data;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;

/**
 * Small runner to exercise Loader in tolerant mode from the workspace.
 */
public class RunLoader {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: RunLoader <asc-file> [<asc-file> ...]");
            System.exit(2);
        }

        for (String path : args) {
            System.out.println("--- Parsing: " + path);
            try (InputStream in = new FileInputStream(path)) {
                Loader loader = new Loader(in, true); // tolerant mode
                Data data = loader.loadFile();

                int testCount = data.getTests().size();
                int totalMeasurements = 0;
                for (Short k : data.getTests().keySet()) {
                    totalMeasurements += data.getTests().get(k).getExecCount();
                }
                int chipCount = data.getChips().size();

                System.out.println("Parse succeeded");
                System.out.println("Tests: " + testCount);
                System.out.println("Total measurements: " + totalMeasurements);
                System.out.println("Chips in category: " + chipCount);
            } catch (ParseException pe) {
                System.out.println("ParseException: " + pe.getMessage());
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
                ex.printStackTrace(System.out);
            }
        }
    }
}
