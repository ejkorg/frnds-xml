package com.onsemi.cim.apps.frends.to.xml.exception;

/**
 *
 * @author fg7x8c
 */
public class FrendsToXmlTranslateException extends Exception {

    /**
     * Creates a new instance of <code>FrendsToXmlTranslateException</code>
     * without detail message.
     */
    public FrendsToXmlTranslateException() {
    }

    /**
     * Constructs an instance of <code>FrendsToXmlTranslateException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public FrendsToXmlTranslateException(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of <code>FrendsToXmlTranslateException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     * @param cause
     */
    public FrendsToXmlTranslateException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
