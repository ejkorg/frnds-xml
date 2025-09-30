package com.onsemi.cim.apps.frends.to.xml;

import com.onsemi.cim.apps.frends.to.xml.data.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;

/**
 *
 * @author fg7x8c
 */
public class Loader {
    
    private final LookaheadReader br;
    private Data data;
    
    private final String EQUALS_DELIMITER = "=";
    private final String COMMA_DELIMITER = ",";

    private final String ITEM_NAME = "ITEM_NAME";
    private final String SPEC_MAX = "SPEC_MAX";
    private final String SPEC_MIN = "SPEC_MIN";
    private final String UNIT = "UNIT";

    private final boolean tolerantMode;

    public Loader(InputStream inStream) {
        this(inStream, false);
    }

    public Loader(InputStream inStream, boolean tolerantMode) {
        this.br = new LookaheadReader(new InputStreamReader(inStream));
        this.tolerantMode = tolerantMode;
    }
    
    public Data loadFile() throws IOException, ParseException {
        data = new Data();
        loadMetadata();
        loadCategory();
        loadMeasurements();
        return data;
    }
    
    private void loadMetadata() throws IOException, ParseException{
        String line;
        int delimiterIndex;

        while (!(line = br.readLine()).startsWith("HEADER_END")) {

            delimiterIndex = line.indexOf(EQUALS_DELIMITER);

            if (delimiterIndex != -1) {
                data.getMetadata().put(line.substring(0, delimiterIndex), ((delimiterIndex + 1) <= line.length()) ? line.substring(delimiterIndex + 1) : null);
            }
        }
    }
        
    private void loadCategory() throws IOException{
        
        String line;
        
        while (!(line = br.readLine()).startsWith("CATEGORY_END")) {
            
            int firstComma = line.indexOf(COMMA_DELIMITER);
            int equals = line.indexOf(EQUALS_DELIMITER);
            int secondComma = line.lastIndexOf(COMMA_DELIMITER);

            if (firstComma != -1 && equals != -1 && secondComma != -1) {
                Short x = Short.parseShort(line.substring(0, firstComma).trim());
                Short y = Short.parseShort(line.substring(firstComma + 1, equals).trim());
                Short hBin = Short.parseShort(line.substring(secondComma + 1).trim());
                Short sBin = Short.parseShort(line.substring(equals + 1, secondComma).trim());
                
                data.getChips().put(new Coords(x, y), new Chip(hBin, sBin));
                
                //increase count for hbin
                if(!data.gethBins().containsKey(hBin)) {
                    data.gethBins().put(hBin, (short)1);
                } else {
                    Short currentCount = data.gethBins().get(hBin);
                    data.gethBins().put(hBin, (short) (currentCount + 1));
                }
                
                //increase count for sbin
                if(!data.getsBins().containsKey(sBin)) {
                    data.getsBins().put(sBin, (short)1);
                } else {
                    Short currentCount = data.getsBins().get(sBin);
                    data.getsBins().put(sBin, (short) (currentCount + 1));
                }
            }
        }
    }
    
    private void loadMeasurements() throws IOException {
        
        String line = null;
        short testNum = 1;
               
        do { 
            data.getTests().put(testNum, loadTestInfo(line));
            loadTestResults(testNum);
            testNum++;
        } while (!(line = br.readLine()).startsWith("LOG_END"));
    }

    private Test loadTestInfo(String lastLine) throws IOException {
        
        String line;
        
        String testname = null;
        String lowSpecLimit = null;
        String highSpecLimit = null;
        String unit = null;
        
        if(lastLine != null && lastLine.startsWith(ITEM_NAME) && lastLine.contains(EQUALS_DELIMITER)){
            testname = lastLine.substring(lastLine.indexOf(EQUALS_DELIMITER) + 1);
        }

        while (true) {
            line = br.readLine();
            if (line == null) {
                break;
            }

            // If tolerant mode, allow files that have measurements immediately after ITEM_NAME
            if (tolerantMode) {
                String trimmed = line.trim();
                // detect a measurement line like: "12,34=VALUE" (numbers,comma,numbers,equals)
                if (trimmed.matches("^\\d+\\s*,\\s*\\d+\\s*=.*")) {
                    if (testname != null) {
                        br.unreadLine(trimmed);
                    }
                    break;
                }
            }

            if (line.startsWith("SPEC_END")) {
                break;
            }

            if(line != null && line.contains(EQUALS_DELIMITER)){
                if (line.startsWith(ITEM_NAME)){
                    testname = line.substring(line.indexOf(EQUALS_DELIMITER) + 1);
                } else if (line.startsWith(SPEC_MAX)) {
                    highSpecLimit = line.substring(line.indexOf(EQUALS_DELIMITER) + 1).trim();
                } else if (line.startsWith(SPEC_MIN)) {
                    lowSpecLimit = line.substring(line.indexOf(EQUALS_DELIMITER) + 1).trim();
                } else if (line.startsWith(UNIT)) {
                    unit = line.substring(line.indexOf(EQUALS_DELIMITER) + 1).trim();
                }     
            }
        }

        return new Test(testname, lowSpecLimit, highSpecLimit, unit);
    }
    
    private void loadTestResults(short testNum) throws IOException {
        
        String line;
        Short execCount = 0;
        // any pushed line will be returned by br.readLine() first because of unreadLine

        while (true) {
            line = br.readLine();
            if (line == null) break;
            if (line.startsWith("ITEM_END")) break;
            int firstComma = line.indexOf(COMMA_DELIMITER);
            int equals = line.indexOf(EQUALS_DELIMITER);

            if (firstComma != -1 && equals != -1) {
                Short x = Short.parseShort(line.substring(0, firstComma).trim());
                Short y = Short.parseShort(line.substring(firstComma + 1, equals).trim());
                String result = line.substring(equals + 1).trim();
                Coords key = new Coords(x, y);
                Chip chip = data.getChips().get(key);
                if (chip == null && tolerantMode) {
                    chip = new Chip((short)0, (short)0);
                    data.getChips().put(key, chip);
                }
                if (chip != null) {
                    chip.getTestResults().put(testNum, result);
                    execCount++;
                }
            }
        }
        data.getTests().get(testNum).setExecCount(execCount);
    }
    
}
