package org.femtoframework.util.status;

/**
 * Exception with status
 *
 * @author fengyun
 * @version 1.1 Mar 6, 2002 2:46:52 PM
 * @see Status
 * @see StatusThrowable
 */
public class StatusException extends Exception
    implements StatusThrowable
{
    private int statusCode;

    /**
     * Constructs by code
     *
     * @param status  Status Code
     */
    public StatusException(int status)
    {
        this(status, "");
    }

    /**
     * Constructs by code + message
     *
     * @param status  Status Code
     * @param message Status message
     */
    public StatusException(int status, String message)
    {
        this(status, message, null);
    }

    /**
     * Constructs by code + message + Cause
     *
     * @param status  Status Code
     * @param message Status message
     * @param cause   Root cause
     */
    public StatusException(int status, String message, Throwable cause)
    {
        super(message, cause);
        this.statusCode = status;
    }

    /**
     * Set status code
     *
     * @param code [int] Status code
     */
    protected void setCode(int code)
    {
        this.statusCode = code;
    }

    /**
     * Returns status code
     *
     * @return [int] status code
     */
    public int getCode()
    {
        return statusCode;
    }
}

