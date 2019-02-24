package org.femtoframework.util.status;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Simple Status
 *
 * @author fengyun
 * @version 1.00 Mar 6, 2002 2:43:37 PM
 */
public class SimpleStatus
    implements Status, Externalizable
{
    private int code;

    private String message;

    /**
     * Constructor
     */
    public SimpleStatus()
    {
    }

    /**
     * Constructs
     *
     * @param status Status code
     */
    public SimpleStatus(int status)
    {
        this(status, null);
    }

    /**
     * Constructs by status + message
     *
     * @param code Status Code
     * @param message Status Message
     */
    public SimpleStatus(int code, String message)
    {
        this.code = code;
        this.message = message == null ? "" : message;
    }


    @Override
    public void writeExternal(ObjectOutput oos) throws IOException {
        oos.writeInt(code);
        oos.writeUTF(message);
    }

    @Override
    public void readExternal(ObjectInput ois) throws IOException, ClassNotFoundException {
        code = ois.readInt();
        message = ois.readUTF();
    }

    /**
     * Status code
     *
     * @return Code
     */
    @Override
    public int getCode() {
        return code;
    }

    /**
     * Status message
     *
     * @return message
     */
    @Override
    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

