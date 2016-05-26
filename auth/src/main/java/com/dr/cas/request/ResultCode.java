package com.dr.cas.request;

/**
 * Result codes that will be returned by our action classes.
 * @author Tom
 *
 */
public final class ResultCode {

    /* main result codes */
    public static final String SUCCESS = "success";         // successful completion
    public static final String INPUT = "input";             // error during validation of input parameters
    public static final String ERROR = "error";             // error during execution
    public static final String LOGIN = "login";		// login required
}


