package com.onsemi.cim.apps.frends.to.xml;

import com.onsemi.cim.apps.frends.to.xml.constant.*;
import com.onsemi.cim.apps.frends.to.xml.data.*;
import java.io.OutputStream;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author fg7x8c
 */
public class XMLWriter {

    private final XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
    private final XMLEventFactory eventFactory = XMLEventFactory.newInstance();
    private XMLEventWriter writer = null;
    private final String filename;
    private final String TYPE = "Frends";
    private final String HWBIN = "Hardware";
    private final String SWBIN = "Software";

    private final XMLEvent END_OF_XML_LINE = eventFactory.createCharacters("\n");

    public XMLWriter(OutputStream os, String filename) throws XMLStreamException {
        writer = outputFactory.createXMLEventWriter(os);
        this.filename = filename;
    }
    
    /**
     * Write data to XML file
     * @param data - Data loaded from Frends
     * @throws XMLStreamException 
     */
    public void writeToFile(Data data) throws XMLStreamException {
        
        writeStartDocument();
        writeFileElement();
        writeLotElement(data.getMetadata());
        writeParameters(data.getTests());
        writeSummary(data.gethBins(), data.getsBins(), data.getTests());
        writeParametricData(data);
        
        writeLastElements();
        if (writer != null) {
            writer.flush();
            writer.close();
        }    
    }

    /**
     * Write start document element.
     *
     * @throws javax.xml.stream.XMLStreamException
     */
    private void writeStartDocument() throws XMLStreamException {
        writer.add(eventFactory.createStartDocument());
        writer.add(END_OF_XML_LINE);
    }

    /**
     * Write start element.
     *
     * @param name - Name of element
     * @throws javax.xml.stream.XMLStreamException
     */
    private void writeStartElement(String name) throws XMLStreamException {
        writer.add(eventFactory.createStartElement("", null, name));
    }
    
    /**
     * Write start element with line break
     * @param name - Name of element
     * @throws XMLStreamException 
     */
    private void writeStartElementWithLineBreak(String name) throws XMLStreamException {
        writeStartElement(name);
        writer.add(END_OF_XML_LINE);
    }
    
    

    /**
     * Write end element.
     *
     * @param name - Name of element
     * @throws javax.xml.stream.XMLStreamException
     */
    private void writeEndElement(String name) throws XMLStreamException {
        writer.add(eventFactory.createEndElement(new QName(name), null));
    }
    
    /**
     * Write end element with line break
     * @param name - Name of element
     * @throws XMLStreamException 
     */
    private void writeEndElementWithLineBreak(String name) throws XMLStreamException {
        writeEndElement(name);
        writer.add(END_OF_XML_LINE);
    }

    /**
     * Write attribute of start element.
     *
     * @param name Attribute name.
     * @param name Attribute value.
     */
    private void writeAtt(String name, String value) throws XMLStreamException {
        if (value != null) {
            String trimmed = value.trim();
            writer.add(eventFactory.createAttribute(name, trimmed));
        }
    }

    /**
     * Write file element
     * @throws XMLStreamException 
     */
    private void writeFileElement() throws XMLStreamException {
        writeStartElement(Element.FILE);
        writeAtt(Attribute.FILENAME, filename);
        writeAtt(Attribute.TYPE, TYPE);
        writer.add(END_OF_XML_LINE);
    }

    /**
     * Write end element for lot and file
     * @throws XMLStreamException 
     */
    private void writeLastElements() throws XMLStreamException {
        writeEndElementWithLineBreak(Element.LOT);
        writeEndElementWithLineBreak(Element.FILE);
    }

    /**
     * Write lot element
     * @param metadata - Metadata values from header
     * @throws XMLStreamException 
     */
    private void writeLotElement(Map<String, String> metadata) throws XMLStreamException {
        writeStartElement(Element.LOT);
        for (Map.Entry<String,String> entry : metadata.entrySet()){
            writeAtt(entry.getKey(), entry.getValue());
        }  
        writer.add(END_OF_XML_LINE);
    }

    /**
     * Write parameters section
     * @param tests - List of parameters
     * @throws XMLStreamException 
     */
    private void writeParameters(Map<Short, Test> tests) throws XMLStreamException {
       
        writeStartElementWithLineBreak(Element.PARAMETERS);
        Short scale;
        String exponentPattern = "[Ee][+-]?[0-9]+$";
        Pattern exp = Pattern.compile(exponentPattern);
        Matcher matcher;
        
        for (Map.Entry<Short, Test> test : tests.entrySet()) {
            writeStartElement(Element.PARAM);
            writeAtt(Attribute.PARAM_ID, String.valueOf(test.getKey()));
            writeAtt(Attribute.ITEM_NAME, test.getValue().getName());
            writeAtt(Attribute.SPEC_MIN, test.getValue().getLowSpecLimit());
            writeAtt(Attribute.SPEC_MAX, test.getValue().getHighSpecLimit());
            writeAtt(Attribute.UNIT, test.getValue().getUnit());
            scale = 0;
            if(test.getValue().getHighSpecLimit() != null && !test.getValue().getHighSpecLimit().isEmpty()){
                matcher = exp.matcher(test.getValue().getHighSpecLimit());
                if(matcher.find()){
                    scale = Short.parseShort(matcher.group().substring(1));
                }
            } else if(test.getValue().getLowSpecLimit() != null && !test.getValue().getLowSpecLimit().isEmpty()){
                matcher = exp.matcher(test.getValue().getLowSpecLimit());
                if(matcher.find()){
                    scale = Short.parseShort(matcher.group().substring(1));
                }
            }
            writeAtt(Attribute.UNIT_SCALE, String.valueOf(scale * -1));
            writeEndElementWithLineBreak(Element.PARAM);
        }

        writeEndElementWithLineBreak(Element.PARAMETERS);
    }

    /**
     * Write SummaryData section
     * @param hBins - Hardware bins
     * @param sBins - Software bins
     * @throws XMLStreamException 
     */
    private void writeSummary(Map<Short, Short> hBins, Map<Short, Short> sBins, Map<Short, Test> tests) throws XMLStreamException {
        writeStartElementWithLineBreak(Element.SUMMARY_DATA);
        writeBinInfo(hBins, sBins);
        writeEndElementWithLineBreak(Element.SUMMARY_DATA);
    }
    
    /**
     * Write BinInfo section
     * @param hBins
     * @param sBins
     * @throws XMLStreamException 
     */
    private void writeBinInfo(Map<Short, Short> hBins, Map<Short, Short> sBins) throws XMLStreamException {
        writeStartElementWithLineBreak(Element.BIN_INFO);
        for (Map.Entry<Short, Short> hbin : hBins.entrySet()) {
            writeBin(hbin.getKey(), hbin.getValue(), HWBIN);
        }
        for (Map.Entry<Short, Short> sbin : sBins.entrySet()) {
            writeBin(sbin.getKey(), sbin.getValue(), SWBIN);
        }
        writeEndElementWithLineBreak(Element.BIN_INFO);
    }

    /**
     * Write bin element
     * @param binNumber - Bin number
     * @param count - Count
     * @param type - Hardware/Software bin
     * @throws XMLStreamException 
     */
    private void writeBin(Short binNumber, Short count, String type) throws XMLStreamException{
        writeStartElement(Element.BIN);
        writeAtt(Attribute.BIN_CATEGORY, String.valueOf(binNumber));
        writeAtt(Attribute.BIN_COUNT, String.valueOf(count));
        writeAtt(Attribute.BIN_TYPE, type);
        writeEndElementWithLineBreak(Element.BIN);
    }

    /**
     * Write parametricData section
     * @param chips - List of units
     * @throws XMLStreamException 
     */
    private void writeParametricData(Data data) throws XMLStreamException {
        Map<Coords, Chip> chips = data.getChips();
        writeStartElementWithLineBreak(Element.PARAMETRIC_DATA);
        Short id = 1;
        for (Map.Entry<Coords, Chip> chip : chips.entrySet()) {
            writeUnit(chip.getKey(), chip.getValue(), id, data);
            id++;
        }
        writeEndElementWithLineBreak(Element.PARAMETRIC_DATA);
    }

    /**
     * Write unit section
     * @param coords - X and Y coordinates
     * @param chip - Chip information - hw bin, sw bin, measurements
     * @throws XMLStreamException 
     */
    private void writeUnit(Coords coords, Chip chip, Short id, Data data) throws XMLStreamException {
        writeStartElement(Element.UNIT);
        writeAtt(Attribute.UNIT_ID, String.valueOf(id));
        writeAtt(Attribute.UNIT_X, String.valueOf(coords.getX()));
        writeAtt(Attribute.UNIT_Y, String.valueOf(coords.getY()));
        writeAtt(Attribute.UNIT_HARDBIN, String.valueOf(chip.gethBin()));
        writeAtt(Attribute.UNIT_SOFTBIN, String.valueOf(chip.getsBin()));
        writer.add(END_OF_XML_LINE);
        writeMeasurements(chip.getTestResults(), data);
        writeEndElementWithLineBreak(Element.UNIT);
    }
    
    /**
     * Write measurement elements
     * @param testResults - Map where key is test number and value is result
     * @throws XMLStreamException 
     */
    private void writeMeasurements(Map<Short, String> testResults, Data data) throws XMLStreamException {
        
        Map<Short, Test> tests = data.getTests();
        Short pf;
        Test test;
        Double specMin, specMax, val;      
        
        for (Map.Entry<Short, String> result : testResults.entrySet()) {
            writeStartElement(Element.MEAS);
            writeAtt(Attribute.MEAS_ID, String.valueOf(result.getKey()));
            // Normalize placeholders: write "NA" for NULL/empty values
            String measVal = result.getValue();
            if (measVal != null) {
                String trimmed = measVal.trim();
                if (trimmed.equalsIgnoreCase("NULL") || trimmed.isEmpty()) {
                    measVal = "NA";
                } else {
                    measVal = trimmed;
                }
            }
            writeAtt(Attribute.MEAS_RESULT, measVal);
            
            test = tests.get(result.getKey());
            specMin = test.getLowSpecLimitAsNumber();
            specMax = test.getHighSpecLimitAsNumber();
            pf = null;
            try {
                // try parsing the normalized value
                val = Double.parseDouble(measVal);
                short pfCalc = 0;
                if(specMin != null  && specMax != null){
                    if(!((val >= specMin) && (val <= specMax))){
                        pfCalc = 1;
                    }
                } else if(specMax != null && val > specMax){
                    pfCalc = 1;
                } else if(specMin != null && val < specMin){
                    pfCalc = 1;
                }
                pf = pfCalc;
            } catch (NumberFormatException nfe) {
                // Non-numeric measurement (e.g., NULL, X): do not set PF
                pf = null;
            }

            if (pf != null) {
                writeAtt(Attribute.MEAS_PF, String.valueOf(pf));
            }
            writeEndElementWithLineBreak(Element.MEAS);
        } 
    }
}
