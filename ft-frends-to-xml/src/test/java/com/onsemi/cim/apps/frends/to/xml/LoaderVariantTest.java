package com.onsemi.cim.apps.frends.to.xml;

import com.onsemi.cim.apps.frends.to.xml.data.Data;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoaderVariantTest {

    private File writeTemp(String name, String content) throws Exception {
        File f = new File("/workspaces/frnds-xml/ft-frends-to-xml/tmp/" + name);
        f.getParentFile().mkdirs();
        try (FileWriter fw = new FileWriter(f)) {
            fw.write(content);
        }
        return f;
    }

    @Test
    public void testSpecBasedParsing() throws Exception {
        String specAsc = "HEADER_BEGIN\nHEADER_END\nCATEGORY_BEGIN\n1,1=9999,1\nCATEGORY_END\nLOG_BEGIN\nITEM_NAME=TEST1\nSPEC_MIN=0\nSPEC_MAX=100\nUNIT=V\nSPEC_END\n1,1=5\nITEM_END\nLOG_END\n";
        File f = writeTemp("spec.asc", specAsc);

        try (InputStream in = new java.io.FileInputStream(f)) {
            Loader loader = new Loader(in, false);
            Data data = loader.loadFile();
            assertEquals(1, data.getTests().size());
            assertEquals(1, (int) data.getTests().get((short)1).getExecCount());
        }
    }

    @Test
    public void testImmediateMeasurementParsingTolerant() throws Exception {
        String immAsc = "HEADER_BEGIN\nHEADER_END\nCATEGORY_BEGIN\n1,1=9999,1\nCATEGORY_END\nLOG_BEGIN\nITEM_NAME=TEST2\n1,1=6\nITEM_END\nLOG_END\n";
        File f = writeTemp("imm.asc", immAsc);

        // non-tolerant should fail (either by throwing or producing zero execCount)
        boolean nonTolerantFailed = false;
        try (InputStream in = new java.io.FileInputStream(f)) {
            Loader loader = new Loader(in, false);
            try {
                Data d = loader.loadFile();
                int count = d.getTests().get((short)1).getExecCount();
                if (count == 0) nonTolerantFailed = true;
            } catch (Exception ex) {
                nonTolerantFailed = true;
            }
        }
        assertTrue("Non-tolerant parsing should fail for immediate-measurement format", nonTolerantFailed);

        // tolerant should succeed
        try (InputStream in = new java.io.FileInputStream(f)) {
            Loader loader = new Loader(in, true);
            Data d = loader.loadFile();
            assertEquals(1, d.getTests().size());
            assertEquals(1, (int) d.getTests().get((short)1).getExecCount());
        }
    }
}
