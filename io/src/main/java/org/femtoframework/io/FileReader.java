package org.femtoframework.io;

import java.io.*;
import java.nio.charset.Charset;

/**
 * FileReader can specify Charset
 */
public class FileReader  extends InputStreamReader {

    /**
     * Creates a new <tt>FileReader</tt>, given the name of the
     * file to read from.
     *
     * @param fileName the name of the file to read from
     * @exception  FileNotFoundException  if the named file does not exist,
     *                   is a directory rather than a regular file,
     *                   or for some other reason cannot be opened for
     *                   reading.
     */
    public FileReader(String fileName) throws FileNotFoundException {
        super(new FileInputStream(fileName));
    }

    /**
     * Creates a new <tt>FileReader</tt>, given the <tt>File</tt>
     * to read from.
     *
     * @param file the <tt>File</tt> to read from
     * @exception  FileNotFoundException  if the file does not exist,
     *                   is a directory rather than a regular file,
     *                   or for some other reason cannot be opened for
     *                   reading.
     */
    public FileReader(File file) throws FileNotFoundException {
        super(new FileInputStream(file));
    }

    /**
     * Creates a new <tt>FileReader</tt>, given the
     * <tt>FileDescriptor</tt> to read from.
     *
     * @param fd the FileDescriptor to read from
     */
    public FileReader(FileDescriptor fd) {
        super(new FileInputStream(fd));
    }

    /**
     * Creates a new <tt>FileReader</tt>, given the name of the
     * file to read from.
     *
     * @param fileName the name of the file to read from
     * @exception FileNotFoundException  if the named file does not exist,
     *                   is a directory rather than a regular file,
     *                   or for some other reason cannot be opened for
     *                   reading.
     */
    public FileReader(String fileName, Charset charset) throws FileNotFoundException {
        super(new FileInputStream(fileName), charset);
    }

    /**
     * Creates a new <tt>FileReader</tt>, given the <tt>File</tt>
     * to read from.
     *
     * @param file the <tt>File</tt> to read from
     * @exception  FileNotFoundException  if the file does not exist,
     *                   is a directory rather than a regular file,
     *                   or for some other reason cannot be opened for
     *                   reading.
     */
    public FileReader(File file, Charset charset) throws FileNotFoundException {
        super(new FileInputStream(file), charset);
    }

    /**
     * Creates a new <tt>FileReader</tt>, given the
     * <tt>FileDescriptor</tt> to read from.
     *
     * @param fd the FileDescriptor to read from
     */
    public FileReader(FileDescriptor fd, Charset charset) {
        super(new FileInputStream(fd), charset);
    }

}

