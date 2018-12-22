package org.femtoframework.util.convert;


/**
 * <p>A <strong>ConversionException</strong> indicates that a call to
 * <code>Converter.convert()</code> has failed to complete successfully.
 */
public class ConversionException extends RuntimeException
{
    /**
     * Construct a new exception with the specified message.
     *
     * @param message The message describing this exception
     */
    public ConversionException(String message)
    {
        super(message);
    }


    /**
     * Construct a new exception with the specified message and root cause.
     *
     * @param message The message describing this exception
     * @param cause   The root cause of this exception
     */
    public ConversionException(String message, Throwable cause)
    {
        super(message, cause);
    }


    /**
     * Construct a new exception with the specified root cause.
     *
     * @param cause The root cause of this exception
     */
    public ConversionException(Throwable cause)
    {
        super(cause);
    }
}
