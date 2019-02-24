/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.femtoframework.util.nutlet;


import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * An utility for writing unit test case,
 * 1. Put some random data
 * 2. Print out data from array etc.
 * 3. Extract resource from jar file
 *
 * @author fengyun
 * @version 1.00 2009-12-9 11:47:50
 */
public class NutletUtil {
    private static Random random = new Random();

    /**
     * 将对象串行化到ByteArrayOutputStream中，然后读回来
     *
     * @param obj 需要串行化的对象
     * @return
     * @throws java.io.IOException
     */
    public static Object serialize(Object obj)
            throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.flush();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        Object newObj = ois.readObject();
        ois.close();
        oos.close();

        return newObj;
    }

    /**
     * Generate random byte array
     *
     * @return Random size byte array
     */
    public static byte[] getBytes() {
        int size = random.nextInt(64 * 1024);
        return getBytes(size);
    }

    /**
     * Generate random byte array by given size
     *
     * @return Random size byte array
     */
    public static byte[] getBytes(int len) {
        if (len > 0) {
            byte[] bytes = new byte[len];
            random.nextBytes(bytes);
            return bytes;
        }
        else if (len == 0) {
            return new byte[0];
        }
        else {
            throw new IllegalArgumentException("Invalid length:" + len);
        }
    }

    /**
     * Generate random char array
     *
     * @return Random size char array
     */
    public static char[] getChars() {
        int size = random.nextInt(64 * 1024);
        return getChars(size);
    }

    /**
     * Generate random char array by given size
     *
     * @return Random char byte array
     */
    public static char[] getChars(int len) {
        if (len > 0) {
            char[] chars = new char[len];
            nextChars(chars);
            return chars;
        }
        else if (len == 0) {
            return new char[0];
        }
        else {
            throw new IllegalArgumentException("Invalid length:" + len);
        }
    }

    private static void nextChars(char[] chars) {
        int numRequested = chars.length;
        int rnd = 0;

        for (int i = 0; i < numRequested; i++) {
            rnd = random.nextInt(0x7FFF);
            chars[i] = (char)rnd;
        }
    }


    private static void nextStringArray(String[] strs) {
        int numRequested = strs.length;

        for (int i = 0; i < numRequested; i++) {
            int randomLen = random.nextInt(256);
            char[] chars = new char[randomLen];
            nextChars(chars);
            strs[i] = new String(chars);
        }
    }

    /**
     * Generate random string
     *
     * @return random string
     */
    public static String getString() {
        int size = random.nextInt(1024);
        return getString(size);
    }

    /**
     * Generate random string by given size
     *
     * @return random string
     */
    public static String getString(int len) {
        if (len > 0) {
            return new String(getChars(len));
        }
        else if (len == 0) {
            return "";
        }
        else {
            throw new IllegalArgumentException("Invalid length:" + len);
        }
    }

    /**
     * Generate random ascii
     *
     * @return random ascii
     */
    public static String getAscii() {
        int size = random.nextInt(1024);
        return getAscii(size);
    }

    /**
     * Generate random ascii by given size
     *
     * @return random ascii
     */
    public static String getAscii(int len) {
        if (len > 0) {
            return nextCode(len, true);
        }
        else if (len == 0) {
            return "";
        }
        else {
            throw new IllegalArgumentException("Invalid length:" + len);
        }
    }

    /**
     * Generate random string array
     *
     * @return random string array
     */
    public static String[] getStringArray() {
        int size = random.nextInt(1024);
        return getStringArray(size);
    }

    /**
     * Generate random string array by given length
     *
     * @return random string array
     */
    public static String[] getStringArray(int len) {
        if (len > 0) {
            String[] strs = new String[len];
            nextStringArray(strs);
            return strs;
        }
        else if (len == 0) {
            return new String[0];
        }
        else {
            throw new IllegalArgumentException("Invalid length:" + len);
        }
    }

    /**
     * Random byte
     *
     * @return Random byte
     */
    public static byte getByte() {
        return (byte)random.nextInt(0xFF);
    }

    /**
     * Random char
     *
     * @return Random char
     */
    public static char getChar() {
        return (char)random.nextInt(0xFFFF);
    }

    /**
     * Random short
     *
     * @return Random short
     */
    public static short getShort() {
        return (short)random.nextInt();
    }

    /**
     * Random int
     *
     * @return Random int
     */
    public static int getInt() {
        return random.nextInt();
    }

    /**
     * Random int by given range
     *
     * @return Random int
     */
    public static int getInt(int max) {
        return random.nextInt(max);
    }

    /**
     * Random boolean
     *
     * @return  Random boolean
     */
    public static boolean getBoolean() {
        return random.nextBoolean();
    }

    /**
     * Random long
     *
     * @return Random long
     */
    public static long getLong() {
        return random.nextLong();
    }

    /**
     * Random unsigned int
     *
     * @return Random unsigned int
     */
    public static long getUnsignedLong() {
        return random.nextLong() & 0x7FFFFFFFFFFFFFFFL;
    }

    /**
     * Random float
     *
     * @return Random float
     */
    public static float getFloat() {
        return random.nextFloat();
    }

    /**
     * Random double
     *
     * @return Random double
     */
    public static double getDouble() {
        return random.nextDouble();
    }

    /**
     * Random int array
     *
     * @return Random int array
     */
    public static int[] getIntArray() {
        int size = random.nextInt(1024);
        return getIntArray(size);
    }

    /**
     * Random int array by given size
     *
     * @return Random int array
     */
    public static int[] getIntArray(int len) {
        if (len > 0) {
            int[] array = new int[len];
            for (int i = 0; i < len; i++) {
                array[i] = random.nextInt();
            }
            return array;
        }
        else if (len == 0) {
            return new int[0];
        }
        else {
            throw new IllegalArgumentException("Invalid length:" + len);
        }
    }

    /**
     * Temp Dir
     *
     * @return
     */
    public static String getTmpDir() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * Reteurn the extension from fileName
     *
     * @param fileName File name
     * @return If no extension return null
     */
    public static String getExtFromName(String fileName)
    {
        if (fileName == null) {
            return null;
        }

        int pos = fileName.lastIndexOf("."); // period index
        if (pos < 0) {
            return null;
        }
        String ext = fileName.substring(pos + 1);
        if (ext.length() == 0) {
            return null;
        }

        return ext;
    }

    /**
     * Create a temporary file under tmp folder
     *
     * @return
     */
    public static File createTmpFile(String file) {
        file = file.replace('/', '.');
        try {
            return File.createTempFile(file, "." + getExtFromName(file));
        }
        catch (IOException e) {
            throw new IllegalStateException("Create temporary file error:" + e.getMessage(), e);
        }
    }


    /**
     * Create a temporary file under tmp folder
     *
     * @return
     */
    public static File createTmpDir(Class<?> testClass) {
        try {
            File tmpFile = File.createTempFile(testClass.getSimpleName(), "tmp");
            String name = tmpFile.getName();
            tmpFile.delete();
            File tmpDir = new File(name.substring(0, name.length() - 3));
            tmpDir.mkdirs();
            tmpDir.deleteOnExit();
            return tmpDir;
        }
        catch (IOException e) {
            throw new IllegalStateException("Create temporary file error:" + e.getMessage(), e);
        }
    }

    /**
     * Copy resource to temporary file
     *
     * @return
     */
    public static File getResourceAsFile(String resource)
            throws IOException {
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        resource = resource.replace('/', '.');
        File file = NutletUtil.createTmpFile(resource);
        FileOutputStream fos = new FileOutputStream(file);
        streamCopy(input, fos);
        fos.close();
        input.close();
        file.deleteOnExit();
        return file;
    }


    /**
     * Copy all files in srcDir into dstDir
     *
     * @param srcDir Source Directory
     * @param dstDir Target Diredtory
     */
    public static void copyAll(File srcDir, File dstDir)
            throws IOException {
        if (!srcDir.isDirectory()) {
            throw new IllegalArgumentException("Invalid source directory:" + srcDir);
        }
        if (!dstDir.isDirectory()) {
            throw new IllegalArgumentException("Invalid destination directory:" + dstDir);
        }
        File[] files = srcDir.listFiles();
        copyTo0(files, dstDir);
    }


    public static void copyTo(File src, File dst)
            throws IOException {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            fis = new FileInputStream(src);
            bis = new BufferedInputStream(fis);
            fos = new FileOutputStream(dst);
            bos = new BufferedOutputStream(fos);

            streamCopy(bis, bos);
        }
        finally {
            bis.close();
            bos.close();
        }
    }

    /**
     * Copy file
     *
     * @param srcFile Source File
     * @param dstDir  Target File
     */
    public static boolean copyToDir(File srcFile, File dstDir)
            throws IOException {
        if (!srcFile.isFile()) {
            throw new IllegalArgumentException("Invalid source file:" + srcFile);
        }
        if (!dstDir.isDirectory()) {
            throw new IllegalArgumentException("Invalid destination directory:" + dstDir);
        }
        File dstFile = new File(dstDir, srcFile.getName());
        if (dstFile.exists()) {
            return false;
        }
        copyTo(srcFile, dstFile);
        return true;
    }

    private static void copyTo0(File[] files, File dstDir)
            throws IOException {
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    copyTo(file, new File(dstDir, file.getName()));
                }
                else if (file.isDirectory()) {
                    File destDir = new File(dstDir, file.getName());
                    if (!destDir.exists()) {
                        if (!destDir.mkdirs()) {
                            throw new IllegalStateException("Can't make directory:" + destDir);
                        }
                    }
                    else if (destDir.isFile()) {
                        throw new IllegalStateException("Has a file same name as:" + destDir);
                    }
                    copyAll(file, destDir);
                }
            }
        }
    }

    private static void streamCopy(InputStream input, OutputStream output) throws IOException {
        byte[] bytes = new byte[8192];
        int read;
        while ((read = input.read(bytes)) > 0) {
            output.write(bytes, 0, read);
        }
        output.flush();
    }

    private static File createRoot(Class<?> type, String resource, boolean returnBaseDir) throws IOException {
        if (!resource.endsWith("/")) {
            resource = resource + "/";
        }
        if (resource.startsWith("/")) {
            resource = resource.substring(1);
        }
        ClassLoader loader = type.getClassLoader();

        URL url = loader.getResource(resource);

        if (url == null) {
            loader = Thread.currentThread().getContextClassLoader();
            url = loader.getResource(resource);
            if (url == null) {
                throw new IllegalStateException("No such resource:" + resource);
            }
        }

        String protocol = url.getProtocol();
        if ("file".equalsIgnoreCase(protocol)) {
            try {
                if (returnBaseDir) {
                    String uri = url.toString();
                    if (uri.endsWith(resource)) {
                        return new File(new URI(uri.substring(0, uri.length() - resource.length())));
                    }
                    else {
                        File baseDir = createTmpDir(type);
                        File root = new File(baseDir, resource);
                        //Copy files
                        copyAll(new File(url.toURI()), root);
                        return baseDir;
                    }
                }
                else {
                    return new File(url.toURI());
                }
            }
            catch (URISyntaxException e) {
                throw new IllegalStateException(e.getMessage());
            }
        }
        else if ("jar".equalsIgnoreCase(protocol)) {
            File baseDir = createTmpDir(type);
            File root = new File(baseDir, resource);
            if (!root.exists() && !root.mkdirs()) {
                throw new IllegalStateException("Can't make directory:" + root + " for @WithResources:" + resource);
            }

            /* A JAR path */
            String jarPath = url.getPath().substring(5, url.getPath().indexOf("!")); //strip out only the JAR file
            JarFile jar = new JarFile(jarPath);
            Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.startsWith(resource)) { //filter according to the path
                    String subName = name.substring(resource.length());
                    int checkSubdir = subName.indexOf("/");
                    if (checkSubdir >= 0) {
                        // if it is a subdirectory, we just return the directory name
                        subName = subName.substring(0, checkSubdir);
                    }

                    extractResource(jar, entry, subName, root);
                }
            }
            jar.close();
            return returnBaseDir ? baseDir : root;
        }
        else {
            throw new UnsupportedOperationException("Cannot list files for URL " + url);
        }
    }

    private static void checkParent(File file) {
        File parentFile = file.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            throw new IllegalStateException("Can't make directory:" + parentFile + " for file:" + file.getName());
        }
    }

    private static void extractResource(JarFile jarFile, JarEntry entry, String destName, File root)
            throws IOException {
        File destFile = new File(root, destName);
        checkParent(destFile);
        InputStream input = jarFile.getInputStream(entry);
        FileOutputStream fos = new FileOutputStream(destFile);
        streamCopy(input, fos);
        fos.close();
        input.close();
    }


    /**
     * Copy all resources with given resource prefix into a directory
     *
     * @return
     */
    public static File getResourceAsDirectory(Class<?> testClass, String resource, boolean returnBaseDir) throws Exception {
        return createRoot(testClass, resource, returnBaseDir);
    }


    /**
     * Print object array
     */
    public static void println(Object[] strs) {
        if (strs == null) {
            System.out.println("null");
            return;
        }
        StringBuilder sb = new StringBuilder();
        String className = strs.getClass().getComponentType().getSimpleName();
        sb.append(className);
        sb.append('[').append(strs.length).append(']');
        sb.append('{').append(toString(strs, ',')).append('}');
        System.out.println(sb.toString());
    }

    /**
     * Convert Object array to string with given separator
     */
    public static String toString(Object[] array, char sep) {
        if (array == null) {
            return null;
        }
        return toString(array, 0, array.length, sep);
    }

    public static String toString(Object[] array, int off,
                                  int len, char sep) {
        if (array == null) {
            return null;
        }

        if (off < 0) {
            throw new ArrayIndexOutOfBoundsException("Off=" + off);
        }
        if (len < 0) {
            throw new ArrayIndexOutOfBoundsException("Len=" + len);
        }

        int end = off + len;
        if (end > array.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid off=" + off + " or len=" + len
                    + " array length=" + array.length);
        }

        if (len == 0) {
            return "";
        }
        else if (len == 1) {
            return String.valueOf(array[off]);
        }
        else {
            StringBuilder sb = new StringBuilder();
            sb.append(array[off]);
            for (int i = off + 1; i < end; i++) {
                sb.append(sep);
                sb.append(array[i]);
            }
            return sb.toString();
        }
    }

    public static String toString(String[] strs, char sep) {
        if (strs == null) {
            return null;
        }
        return toString(strs, 0, strs.length, sep);
    }

    public static void print(byte[] bytes) {
        print(bytes, 0, bytes.length);
    }

    public static void print(byte[] bytes, int radix) {
        print(bytes, 0, bytes.length, radix);
    }

    public static void println(byte[] bytes) {
        println(bytes, 10);
    }

    public static void println(byte[] bytes, int radix) {
        println(bytes, 0, bytes.length, radix);
    }

    public static void print(byte[] bytes, int off, int len) {
        print(bytes, off, len, 10);
    }

    public static void print(byte[] bytes, int off, int len, int radix) {
        StringBuilder sb = new StringBuilder(len * 2 + 2);
        sb.append('{');
        boolean hex = radix == 16;
        for (int i = off; i < (len + off); i++) {
            if (i > off) {
                sb.append(',');
            }

            if (hex) {
                sb.append(Integer.toHexString(bytes[i]));
            }
            else {
                sb.append(Integer.toString((int)bytes[i], radix));
            }
        }
        sb.append('}');
        System.out.print(sb);
    }

    public static void println(byte[] bytes, int off, int len) {
        print(bytes, off, len);
        System.out.println();
    }

    public static void println(byte[] bytes, int off, int len, int radix) {
        print(bytes, off, len, radix);
        System.out.println();
    }

    public static void print(char[] chars) {
        print(chars, 0, chars.length);
    }

    public static void print(char[] chars, int radix) {
        print(chars, 0, chars.length, radix);
    }

    public static void println(char[] chars) {
        println(chars, 10);
    }

    public static void println(char[] chars, int radix) {
        println(chars, 0, chars.length, radix);
    }

    public static void print(char[] chars, int off, int len) {
        print(chars, off, len, 10);
    }

    public static void print(char[] chars, int off, int len, int radix) {
        StringBuilder sb = new StringBuilder(len * 2 + 2);
        sb.append('{');
        boolean hex = radix == 16;
        for (int i = off; i < (len + off); i++) {
            if (i > off) {
                sb.append(',');
            }

            if (hex) {
                sb.append(Integer.toHexString(chars[i]));
            }
            else {
                sb.append(Integer.toString((int)chars[i], radix));
            }
        }
        sb.append('}');
        System.out.print(sb);
    }

    public static void println(char[] chars, int off, int len) {
        print(chars, off, len);
        System.out.println();
    }

    public static void println(char[] chars, int off, int len, int radix) {
        print(chars, off, len, radix);
        System.out.println();
    }

    //////////////////////////////////////////////////////////////////////////

    public static void print(int[] ints) {
        print(ints, 0, ints.length);
    }

    public static void print(int[] ints, int radix) {
        print(ints, 0, ints.length, radix);
    }

    public static void println(int[] ints) {
        println(ints, 10);
    }

    public static void println(int[] ints, int radix) {
        println(ints, 0, ints.length, radix);
    }

    public static void print(int[] ints, int off, int len) {
        print(ints, off, len, 10);
    }

    public static void print(int[] ints, int off, int len, int radix) {
//        int[] b = ints;
        StringBuilder sb = new StringBuilder(len * 2 + 2);
        sb.append('{');
        boolean hex = radix == 16;
        for (int i = off; i < (len + off); i++) {
            if (i > off) {
                sb.append(',');
            }
            if (hex) {
                sb.append(Integer.toHexString(ints[i]));
            }
            else {
                sb.append(Integer.toString(ints[i], radix));
            }
        }
        sb.append('}');
        System.out.print(sb);
    }

    public static void println(int[] ints, int off, int len) {
        print(ints, off, len);
        System.out.println();
    }

    public static void println(int[] ints, int off, int len, int radix) {
        print(ints, off, len, radix);
        System.out.println();
    }


    //UID

    private static final String[] ASCII_ARRAY = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };
    private static final int DEFAULT_LENGTH = 6;

    private static Random factory = new Random();

    /**
     * Retunrn the next UUID, given the specified prefix.
     *
     * @return String - next unique id
     */
    public static String nextString(String prefix)
    {
        StringBuilder sb = new StringBuilder(prefix.length() + 10);
        sb.append(factory.nextLong() & 0x7FFFFFFFFFFFFFFFL);
        return sb.toString();
    }

    /**
     * Return the next UUID
     *
     * @return String - next unique id
     */
    public static String nextString()
    {
        return String.valueOf(factory.nextLong() & 0x7FFFFFFFFFFFFFFFL);
    }

    /**
     * Return the next UUID as a long
     *
     * @return long  - next as long
     */
    public static long next()
    {
        return factory.nextLong() & 0x7FFFFFFFFFFFFFFFL;
    }

    /**
     * Return the next UUID as a long
     *
     * @return long  - next as long
     */
    public static int nextInt()
    {
        return factory.nextInt() & 0x7FFFFFFF;
    }

    /**
     * Return the next UUID as a long
     *
     * @return long  - next as long
     */
    public static int nextInt(int max)
    {
        return factory.nextInt(max);
    }


    public static String nextCode()
    {
        return nextCode(DEFAULT_LENGTH);
    }

    public static String nextCode(boolean ascii)
    {
        return nextCode(DEFAULT_LENGTH, ascii);
    }

    public static String nextCode(int len)
    {
        if (len <= 0) {
            return nextCode();
        }
        StringBuilder code = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            code.append(ASCII_ARRAY[factory.nextInt(10)]);
        }
        return code.toString();
    }

    public static String nextCode(int len, boolean ascii)
    {
        if (len <= 0) {
            return nextCode(ascii);
        }
        if (ascii) {
            StringBuilder code = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                code.append(ASCII_ARRAY[factory.nextInt(ASCII_ARRAY.length)]);
            }
            return code.toString();
        }
        else {
            return nextCode(len);
        }
    }
}
