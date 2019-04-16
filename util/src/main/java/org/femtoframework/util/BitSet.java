package org.femtoframework.util;


import org.femtoframework.util.crypto.Hex;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * 最长只有32bit的集合
 * <p/>
 *
 * @author fengyun
 * @version 1.00 2005-2-7 7:41:49
 */
public class BitSet implements Externalizable, Cloneable {
    /**
     * The value
     */
    protected int bits;

    /**
     * The length
     * Maximum length is 32
     */
    protected int length;

    /**
     * Constructor
     * The constructor is to set the initialized value.
     *
     * @param value
     */
    public BitSet(int value) {
        this(value, 32);
    }

    public BitSet() {
        this(0);
    }

    /**
     * Constructor
     * The constructor is to set the initialized value.
     *
     * @param value
     * @param length
     */

    public BitSet(int value, int length) {
        setLength(length);
        setValue(value);
    }

    /**
     * Return the length
     */
    public int length() {
        return getLength();
    }

    /**
     * Return the value
     */
    public int getValue() {
        return bits;
    }

    /**
     * Return the value of given indexes
     *
     * @param start
     * @param end
     * @return int
     */
    public int get(int start, int end) {
        if (start < 0 || start > end || end >= length) {
            throw new IndexOutOfBoundsException("Error Index start="
                    + start + " end=" + end);
        }

        int value = bits << (length - end - 1);
        value = value >>> (length - end + start - 1);

        return value;
    }

    /**
     * Return the value of given index
     *
     * @param index
     * @return boolean
     */
    public boolean get(int index) {
        return (bits & bit(index)) != 0;
    }

    /**
     * Set the value
     */
    public void setValue(int value) {
        if (length == 32) {
            this.bits = value;
        }
        else {
            int max = (1 << length) - 1;

            if (value > max) {
                throw new IllegalArgumentException("value > max");
            }

            this.bits = value;
        }
    }

    /**
     * get the length
     *
     * @return
     */
    public int getLength() {
        return this.length;
    }

    /**
     * Set the length
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Set the value by given index
     *
     * @param index which bit
     * @param value if true the bit will be set as 1
     */
    public synchronized void set(int index, boolean value) {
        if (value) {
            bits |= bit(index);
        }
        else {
            bits &= ~bit(index);
        }
    }

    /**
     * Set the bits by given start index and end index
     *
     * @param start 包括该位置
     * @param end   包括该位置
     * @param value
     */
    public void set(int start, int end, int value) {
        if (start < 0 || start > end || end >= length) {
            throw new IndexOutOfBoundsException("Error Index start="
                    + start + " end=" + end);
        }
        int max = bit(end - start + 1) - 1;

        if (value > max) {
            throw new IllegalArgumentException("value > max");
        }

        value <<= start;
        max <<= start;
        set(max, value);
    }

    /**
     * Set the value by mask
     * <p/>
     * Sample:
     * orignal bits = 110011100B
     * mask  = 000011100B
     * value = 000001000B
     * the new bits = 110001000B
     * bits = (bits&(~mask))|value;
     */
    public synchronized void set(int mask, int value) {
        bits &= ~mask;
        bits |= value;
    }

    /**
     * Given a bit index, return a unit that masks that bit in its unit.
     */
    private int bit(int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Error Index " + index);
        }

        return 1 << index;
    }

    /**
     *
     */
    public String toString() {
        return Hex.toString(getValue());
    }

    public boolean equals(Object object) {
        if (object instanceof BitSet) {
            return bits == ((BitSet)object).bits;
        }
        return false;
    }

    /**
     * Clone 实现
     *
     * @return
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public void writeExternal(ObjectOutput oos) throws IOException {
        oos.writeInt(bits);
        oos.writeInt(length);
    }

    @Override
    public void readExternal(ObjectInput ois) throws IOException, ClassNotFoundException {
        bits = ois.readInt();
        length = ois.readInt();
    }
}
