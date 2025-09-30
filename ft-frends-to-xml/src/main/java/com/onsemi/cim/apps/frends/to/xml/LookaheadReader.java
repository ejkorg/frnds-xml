package com.onsemi.cim.apps.frends.to.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Minimal reader that supports unreading a single line (lookahead of one).
 */
public class LookaheadReader {

    private final BufferedReader br;
    private String pushedLine = null;

    public LookaheadReader(Reader in) {
        this.br = (in instanceof BufferedReader) ? (BufferedReader) in : new BufferedReader(in);
    }

    public String readLine() throws IOException {
        if (pushedLine != null) {
            String tmp = pushedLine;
            pushedLine = null;
            return tmp;
        }
        return br.readLine();
    }

    /**
     * Push a single line back so it will be returned on the next readLine().
     * Only one line can be pushed at a time.
     */
    public void unreadLine(String line) {
        if (line == null) throw new IllegalArgumentException("line cannot be null");
        if (pushedLine != null) throw new IllegalStateException("Only one line can be unread");
        this.pushedLine = line;
    }

    public boolean hasPushedLine() {
        return pushedLine != null;
    }

    public void close() throws IOException {
        br.close();
    }
}
