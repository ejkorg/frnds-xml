package com.onsemi.cim.apps.frends.to.xml;

import com.onsemi.cim.apps.frends.to.xml.data.Data;
import com.onsemi.cim.apps.frends.to.xml.exception.FrendsToXmlTranslateException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.xml.stream.XMLStreamException;

/**
 *
 * @author fg7x8c
 */
public class Translator {

    private InputStream inStream = null;
    private OutputStream outStream = null;
    private File inputFile = null;
    private File outputFile = null;
    private Boolean inCompressed = null;
    private Boolean outCompressed = null;
    private boolean dataTypeTolerant = false;
    private final String filename;
    

    public Translator(File inputFile, File outputFile, Boolean inCompressed, Boolean outCompressed, boolean dataTypeTolerant) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.inCompressed = inCompressed;
        this.outCompressed = outCompressed;
        this.dataTypeTolerant = dataTypeTolerant;
        this.filename = inputFile.getName();
    }
    
    public Translator(InputStream inStream, OutputStream outStream, String filename) {
        this.inStream = inStream;
        this.outStream = outStream;
        this.filename = filename;
    }
    
    public void translate() throws FrendsToXmlTranslateException {

        try {
            if (inStream == null && inputFile != null && inCompressed != null) {
                inStream = new BufferedInputStream((inCompressed) ? new GZIPInputStream(new FileInputStream(inputFile)) : (new FileInputStream(inputFile)));
            }
            if (outStream == null && outputFile != null && outCompressed != null) {
                outStream = new BufferedOutputStream((outCompressed) ? new GZIPOutputStream(new FileOutputStream(outputFile)) : (new FileOutputStream(outputFile)));
            }
            
            Loader loader = new Loader(inStream, dataTypeTolerant);
            Data data = loader.loadFile();     
            XMLWriter writer = new XMLWriter(outStream, filename);
            writer.writeToFile(data);
                
        } catch (FileNotFoundException ex) {
            throw new FrendsToXmlTranslateException(ex.getMessage(), ex);
        } catch (IOException | XMLStreamException | ParseException ex) {
            throw new FrendsToXmlTranslateException(ex.getMessage(), ex);
        } finally {

            try {
                if (inStream != null) {
                    inStream.close();
                }
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException ex) {
                throw new FrendsToXmlTranslateException(ex.getMessage(), ex);
            }
        }
    }
}
