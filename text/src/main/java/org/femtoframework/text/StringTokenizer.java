package org.femtoframework.text;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 只采用一个作为打断符号的StringTokenizer<br>
 * 比采用字符串作为分隔的要快速
 *
 * @author fengyun
 */
public class StringTokenizer implements Enumeration, Iterator
{
    private int currentPosition;
    private int newPosition;
    private int maxPosition;
    private String str;
    private char delimiter;
    private boolean retDelims;

    /**
     * Constructs a string tokenizer for the specified string. All
     * characters in the <code>delim</code> argument are the delimiters
     * for separating tokens.
     * <p/>
     * If the <code>returnDelims</code> flag is <code>true</code>, then
     * the delimiter characters are also returned as tokens. Each
     * delimiter is returned as a string of length one. If the flag is
     * <code>false</code>, the delimiter characters are skipped and only
     * serve as separators between tokens.
     * <p/>
     * Note that if <tt>delim</tt> is <tt>null</tt>, this constructor does
     * not throw an exception. However, trying to invoke other methods on the
     * resulting <tt>StringTokenizer</tt> may result in a
     * <tt>NullPointerException</tt>.
     *
     * @param str          a string to be parsed.
     * @param delim        the delimiters.
     * @param returnDelims flag indicating whether to return the delimiters
     *                     as tokens.
     */
    public StringTokenizer(String str, char delim, boolean returnDelims)
    {
        currentPosition = 0;
        newPosition = -1;
        this.str = str;
        maxPosition = str.length();
        delimiter = delim;
        retDelims = returnDelims;
    }

    /**
     * Constructs a string tokenizer for the specified string. The
     * characters in the <code>delim</code> argument are the delimiters
     * for separating tokens. Delimiter characters themselves will not
     * be treated as tokens.
     *
     * @param str   a string to be parsed.
     * @param delim the delimiters.
     */
    public StringTokenizer(String str, char delim)
    {
        this(str, delim, false);
    }

    /**
     * Skips delimiters starting from the specified position. If retDelims
     * is false, returns the index of the first non-delimiter character at or
     * after startPos. If retDelims is true, startPos is returned.
     */
    private final int skipDelimiters(int startPos)
    {
        int position = startPos;
        if (!retDelims) {
            char c = 0;
            while (position < maxPosition) {
                c = str.charAt(position);
                if (c != delimiter) {
                    break;
                }
                position++;
            }
        }
        return position;
    }

    /**
     * Skips ahead from startPos and returns the index of the next delimiter
     * character encountered, or maxPosition if no such delimiter is found.
     */
    private final int scanToken(int startPos)
    {
        int position = startPos;
        char c = 0;
        while (position < maxPosition) {
            c = str.charAt(position);
            if (c == delimiter) {
                break;
            }
            position++;
        }
        if (retDelims && (startPos == position)) {
            c = str.charAt(position);
            if (c == delimiter) {
                position++;
            }
        }
        return position;
    }

    /**
     * Tests if there are more tokens available from this tokenizer's string.
     * If this method returns <tt>true</tt>, then a subsequent call to
     * <tt>nextToken</tt> with no argument will successfully return a token.
     *
     * @return <code>true</code> if and only if there is at least one token
     *         in the string after the current position; <code>false</code>
     *         otherwise.
     */
    public boolean hasMoreTokens()
    {
        /*
         * Temporary store this position and use it in the following
         * nextToken() method only if the delimiters have'nt been changed in
         * that nextToken() invocation.
         */
        newPosition = skipDelimiters(currentPosition);
        return (newPosition < maxPosition);
    }

    /**
     * Returns the next token from this string tokenizer.
     *
     * @return the next token from this string tokenizer.
     * @throws NoSuchElementException
     *          if there are no more tokens in this
     *          tokenizer's string.
     */
    public String nextToken()
    {
        /*
         * If next position already computed in hasMoreElements() and
         * delimiters have changed between the computation and this invocation,
         * then use the computed value.
         */
        if (newPosition >= 0) {
            currentPosition = newPosition;
            newPosition = -1;
        }
        else {
            currentPosition = skipDelimiters(currentPosition);
        }

        if (currentPosition >= maxPosition) {
            throw new NoSuchElementException();
        }
        int start = currentPosition;
        currentPosition = scanToken(currentPosition);
        return str.substring(start, currentPosition);
    }

    /**
     * Returns the same value as the <code>hasMoreTokens</code>
     * method. It exists so that this class can implement the
     * <code>Enumeration</code> interface.
     *
     * @return <code>true</code> if there are more tokens;
     *         <code>false</code> otherwise.
     * @see Enumeration
     * @see java.util.StringTokenizer#hasMoreTokens()
     */
    public boolean hasMoreElements()
    {
        return hasMoreTokens();
    }

    /**
     * Returns the same value as the <code>nextToken</code> method,
     * except that its declared return value is <code>Object</code> rather than
     * <code>String</code>. It exists so that this class can implement the
     * <code>Enumeration</code> interface.
     *
     * @return the next token in the string.
     * @throws NoSuchElementException if there are no more tokens in this
     *                                tokenizer's string.
     * @see Enumeration
     * @see java.util.StringTokenizer#nextToken()
     */
    public Object nextElement()
    {
        return nextToken();
    }

    /**
     * Calculates the number of times that this tokenizer's
     * <code>nextToken</code> method can be called before it generates an
     * exception. The current position is not advanced.
     *
     * @return the number of tokens remaining in the string using the current
     *         delimiter set.
     * @see java.util.StringTokenizer#nextToken()
     */
    public int countTokens()
    {
        int count = 0;
        int currpos = currentPosition;
        while (currpos < maxPosition) {
            currpos = skipDelimiters(currpos);
            if (currpos >= maxPosition) {
                break;
            }
            currpos = scanToken(currpos);
            count++;
        }
        return count;
    }

    /**
     * Removes from the underlying collection the last element returned by the
     * iterator (optional operation).  This method can be called only once per
     * call to <tt>next</tt>.  The behavior of an iterator is unspecified if
     * the underlying collection is modified while the iteration is in
     * progress in any way other than by calling this method.
     *
     * @throws UnsupportedOperationException if the <tt>remove</tt>
     *                                       operation is not supported by this Iterator.
     * @throws IllegalStateException         if the <tt>next</tt> method has not
     *                                       yet been called, or the <tt>remove</tt> method has already
     *                                       been called after the last call to the <tt>next</tt>
     *                                       method.
     */
    public void remove()
    {
    }

    /**
     * Returns <tt>true</tt> if the iteration has more elements. (In other
     * words, returns <tt>true</tt> if <tt>next</tt> would return an element
     * rather than throwing an exception.)
     *
     * @return <tt>true</tt> if the iterator has more elements.
     */
    public boolean hasNext()
    {
        return hasMoreTokens();
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration.
     * @throws NoSuchElementException
     *          iteration has no more elements.
     */
    public Object next()
    {
        return nextToken();
    }
}
