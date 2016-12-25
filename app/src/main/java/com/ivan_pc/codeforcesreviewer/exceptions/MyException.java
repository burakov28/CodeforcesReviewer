package com.ivan_pc.codeforcesreviewer.exceptions;

import org.xml.sax.Parser;

/**
 * Created by Ivan-PC on 18.12.2016.
 */

public class MyException extends Exception {
    public String cause;

    public MyException(String cause) {
        super();
        this.cause = cause;
    }
}
