package com.ivan_pc.codeforcesreviewer;

import org.xml.sax.Parser;

/**
 * Created by Ivan-PC on 18.12.2016.
 */

public class MyException extends Exception {
    String cause;

    MyException(String cause) {
        super();
        this.cause = cause;
    }
}
