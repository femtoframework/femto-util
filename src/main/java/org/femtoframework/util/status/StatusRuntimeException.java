package org.femtoframework.util.status;

/**
 * Runtime Exception
 *
 * @author fengyun
 * @version 1.00 Mar 6, 2002 2:46:52 PM
 * @see StatusThrowable
 */
public class StatusRuntimeException
    extends RuntimeException
    implements StatusThrowable
{
    private int statusCode;

    /**
     * Constructs by code
     *
     * @param status  Status Code
     */
    public StatusRuntimeException(int status)
    {
        this(status, "");
    }

    /**
     * Constructs by code + message
     *
     * @param status  Status Code
     * @param message Status message
     */
    public StatusRuntimeException(int status, String message)
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
    public StatusRuntimeException(int status, String message, Throwable cause)
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
