package org.femtoframework.io;

import org.femtoframework.io.filter.FilenameSelector;
import org.femtoframework.util.ArrayUtil;

import java.io.*;
import java.nio.channels.FileChannel;

public interface FileUtil {
    /**
     * Reteurn the extension from fileName
     *
     * @param file File
     * @return If no extension return null
     */
    public static String getExtFromName(File file)
    {
        return getExtFromName(file.getName());
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
     * Reteurn the prefix from fileName
     *
     * @param fileName File name
     * @return If no extension return null
     */
    public static String getPrefixFromName(String fileName)
    {
        if (fileName == null) {
            return null;
        }

        int pos = fileName.lastIndexOf("."); // period index
        if (pos < 0) {
            return fileName;
        }
        return fileName.substring(0, pos);
    }

    /**
     * Reteurn the prefix from fileName
     *
     * @param file File
     * @return If no extension return null
     */
    public static String getPrefixFromName(File file)
    {
        return getPrefixFromName(file.getName());
    }

    /**
     * 删除文件或者文件目录，采用遍历方式
     *
     * @param file 目录或者文件
     * @return 是否已经删除
     */
    public static boolean delete(File file)
    {
        if (file == null) {
            return true;
        }
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        else {
            boolean del = true;
            File[] files = file.listFiles();
            if (files == null) {
                return file.delete();
            }
            for (File file1 : files) {
                del = delete(file1);
                if (!del) {
                    break;
                }
            }
            if (del) {
                return file.delete();
            }
            return del;
        }
    }

    /**
     * 清除该目录
     *
     * @param path 目录
     * @return 是否清除成功，如果对象是文件或者不存在，返回<code>false</code>
     */
    public static boolean clear(File path)
    {
        if (path == null) {
            return false;
        }
        if (!path.exists()) {
            return false;
        }
        if (path.isFile()) {
            return false;
        }
        else {
            boolean del = false;
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for (File file : files) {
                del = delete(file);
                if (!del) {
                    break;
                }
            }
            return del;
        }
    }

    /**
     * 创建文件，如果文件的上一级目录不存在
     * 则创建之
     *
     * @param file 文件
     */
    public static boolean create(File file)
    {
        File parent = getParentFile(file);
        if (parent == null) {
            return false;
        }
        if (parent.exists()) {
            return (parent.isDirectory() && parent.canRead() && parent.canWrite());
        }
        else {
            return parent.mkdirs();
        }
    }

    /**
     * 返回ParentFile
     *
     * @return file 如果不存在返回<code>null</code>
     */
    public static File getParentFile(File file)
    {
        File parent = file.getParentFile();
        if (parent == null) {
            try {
                parent = file.getCanonicalFile().getParentFile();
            }
            catch (IOException e) {
                return null;
            }
        }
        return parent;
    }

    static String getOSName() {
        return System.getProperty("os.name");
    }

    /**
     * 重命名文件
     *
     * @param src  当前文件
     * @param dest 目标文件
     */
    public static boolean renameTo(File src, File dest)
    {
        create(dest);
        if (!src.renameTo(dest)) {
            //Rename不成功
            if (dest.exists() && getOSName().startsWith("Windows")) {
                //如果目标文件已经存在，并且在windows平台上，则删除之
                if (dest.delete()) {
                    return src.renameTo(dest);
                }
                else {
                    throw new IllegalStateException("Can't delete the file:" + dest);
                }
            }
            return false;
        }
        return true;
    }

    /**
     * 创建文件，如果文件的上一级目录不存在
     * 则创建之
     *
     * @param file 文件
     * @return 如果文件顺利创建，则返回，否则返回<code>null</code>
     */
    public static File create(String file)
    {
        File f = new File(file);
        if (create(f)) {
            return f;
        }
        else {
            return null;
        }
    }

    /**
     * 安全的合并两个目录
     * <p/>
     * Will concatenate 2 paths, dealing with ..
     * ( /a/b/c + d = /a/b/d, /a/b/c + ../d = /a/d )
     * Used in Request.getRD
     *
     * @return null if error occurs
     */
    public static String catPath(String lookupPath, String path)
    {
        // Cut off the last slash and everything beyond
        int index = lookupPath.lastIndexOf("/");
        lookupPath = lookupPath.substring(0, index);

        // Deal with .. by chopping dirs off the lookup path
        while (path.startsWith("../")) {
            if (lookupPath.length() > 0) {
                index = lookupPath.lastIndexOf("/");
                lookupPath = lookupPath.substring(0, index);
            }
            else {
                // More ..'s than dirs, return null
                return null;
            }

            index = path.indexOf("../") + 3;
            path = path.substring(index);
        }

        if (path.startsWith("/")) {
            return path;
        }
        if (lookupPath.endsWith("/")) {
            return lookupPath + path;
        }
        else {
            return lookupPath + "/" + path;
        }
    }

    /**
     * 将当中 "/../"去掉
     *
     * @param base 基础目录
     * @param path 路径
     * @return "/../"去掉
     */
    public static String safePath(String base, String path)
    {
        // Hack for Jsp ( and other servlets ) that use rel. paths
        // if( ! path.startsWith("/") ) path="/"+ path;
        String normP = path;
        if (path.indexOf('\\') >= 0) {
            normP = path.replace('\\', '/');
        }

        if (!normP.startsWith("/")) {
            normP = "/" + normP;
        }

        int index = normP.indexOf("/../");
        if (index >= 0) {

            // Clean out "//" and "/./" so they will not be confused
            // with real parent directories
            int index2 = 0;
            while ((index2 = normP.indexOf("//", index2)) >= 0) {
                normP = normP.substring(0, index2) +
                        normP.substring(index2 + 1);
                if (index2 < index) {
                    index--;
                }
            }
            index2 = 0;
            while ((index2 = normP.indexOf("/./", index2)) >= 0) {
                normP = normP.substring(0, index2) +
                        normP.substring(index2 + 2);
                if (index2 < index) {
                    index -= 2;
                }
            }

            // Remove cases of "/{directory}/../"
            while (index >= 0) {
                // If no parent directory to remove, return null
                if (index == 0) {
                    return (null);   // Trying to leave our context
                }
                index2 = normP.lastIndexOf('/', index - 1);
                normP = normP.substring(0, index2) +
                        normP.substring(index + 3);
                index = normP.indexOf("/../", index2);
            }

        }

        String realPath = base + normP;

        // Probably not needed - it will be used on the local FS
        realPath = patch(realPath);
        String canPath;

        try {
            canPath = new File(realPath).getCanonicalPath();
        }
        catch (IOException ex) {
            return null;
        }

        // This absPath/canPath comparison plugs security holes...
        // On Windows, makes "x.jsp.", "x.Jsp", and "x.jsp%20"
        // return 404 instead of the JSP source
        // On all platforms, makes sure we don't let ../'s through
        // Unfortunately, on Unix, it prevents symlinks from working
        // So, a check for File.separatorChar='\\' ..... It hopefully
        // happens on flavors of Windows.
        if (File.separatorChar == '\\') {
            // On Windows check ignore case....
            if (!realPath.equals(canPath)) {
                int ls = realPath.lastIndexOf('\\');
                if ((ls > 0) && !realPath.substring(0, ls).equals(canPath)) {
                    return null;
                }
            }
        }

        // The following code on Non Windows disallows ../
        // in the path but also disallows symlinks....
        //
        // if( ! canPath.startsWith(base) ) {
        //    // no access to files in a different context.
        //      return null;
        //   }
        // if(!absPath.equals(canPath)) {
        // response.sendError(response.SC_NOT_FOUND);
        // return;
        // }
        // instead lets look for ".." in the absolute path
        // and disallow only that.
        // Why should we loose out on symbolic links?
        //

        if (realPath.indexOf("..") != -1) {
            // We have .. in the path...
            return null;
        }
        // extra-extra safety check, ( but slow )
        return realPath;
    }

    public static String patch(String path)
    {
        String patchPath = path.trim();

        // Move drive spec to the front of the path
        if (patchPath.length() >= 3 &&
                patchPath.charAt(0) == '/' &&
                Character.isLetter(patchPath.charAt(1)) &&
                patchPath.charAt(2) == ':') {
            patchPath = patchPath.substring(1, 3) + "/" + patchPath.substring(3);
        }

        // Eliminate consecutive slashes after the drive spec
        if (patchPath.length() >= 2 &&
                Character.isLetter(patchPath.charAt(0)) &&
                patchPath.charAt(1) == ':') {
            char[] ca = patchPath.replace('/', '\\').toCharArray();
            char c;
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < ca.length; i++) {
                if ((ca[i] != '\\') ||
                        (ca[i] == '\\' &&
                                i > 0 &&
                                ca[i - 1] != '\\')) {
                    if (i == 0 &&
                            Character.isLetter(ca[i]) &&
                            i < ca.length - 1 &&
                            ca[i + 1] == ':') {
                        c = Character.toUpperCase(ca[i]);
                    }
                    else {
                        c = ca[i];
                    }

                    sb.append(c);
                }
            }

            patchPath = sb.toString();
        }

        // fix path on NetWare - all '/' become '\\' and remove duplicate '\\'
        if (getOSName().startsWith("NetWare") &&
                path.length() >= 3 &&
                path.indexOf(':') > 0) {
            char ca[] = patchPath.replace('/', '\\').toCharArray();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ca.length; i++) {
                if ((ca[i] != '\\') ||
                        (ca[i] == '\\' && i > 0 && ca[i - 1] != '\\')) {
                    sb.append(ca[i]);
                }
            }
            patchPath = sb.toString();
        }
        return patchPath;
    }

    public static boolean isAbsolute(String path)
    {
        // normal file
        if (path.startsWith("/")) {
            return true;
        }

        if (path.startsWith(File.separator)) {
            return true;
        }

        // win c:
        if (path.length() >= 3 &&
                Character.isLetter(path.charAt(0)) &&
                path.charAt(1) == ':') {
            return true;
        }

        // NetWare volume:
        return getOSName().startsWith("NetWare") &&
                path.length() >= 3 &&
                path.indexOf(':') > 0;

    }

    // Used in few places.
    public static String getCanonicalPath(String name)
    {
        if (name == null) {
            return null;
        }
        File f = new File(name);
        try {
            return f.getCanonicalPath();
        }
        catch (IOException ioe) {
            return name; // oh well, we tried...
        }
    }

    char SEP = '/';

    static boolean isSep(char c)
    {
        return c == '/' || c == '\\';
    }

    /**
     * 规格化路径
     */
    public static String normalize(String path)
    {
        if (path == null) {
            return null;
        }

        final int len = path.length();
        StringBuilder sb = new StringBuilder(len);
        boolean changed = false;
        int pos = 0;
        char c;
        while (pos < len) {
            c = path.charAt(pos);
            if (isSep(c)) {
                /*
                 * multiple path separators.
                 * 'foo///bar' -> 'foo/bar'
                 */
                while (pos + 1 < len && isSep(path.charAt(pos + 1))) {
                    ++pos;
                    changed = true;
                }

                if (pos + 1 < len && path.charAt(pos + 1) == '.') {
                    /*
                     * a single dot at the end of the path - we are done.
                     */
                    if (pos + 2 >= len) {
                        break;
                    }

                    switch (path.charAt(pos + 2)) {
                        /*
                         * self directory in path
                         * foo/./bar -> foo/bar
                         */
                        case'/':
                        case'\\':
                            pos += 2;
                            continue;

                            /*
                             * two dots in a path: go back one hierarchy.
                             * foo/bar/../baz -> foo/baz
                             */
                        case'.':
                            // only if we have exactly _two_ dots.
                            if (pos + 3 < len && isSep(path.charAt(pos + 3))) {
                                pos += 3;
                                int separatorPos = sb.length() - 1;
                                while (separatorPos >= 0 &&
                                        !isSep(sb.charAt(separatorPos))) {
                                    --separatorPos;
                                }
                                if (separatorPos >= 0) {
                                    sb.setLength(separatorPos);
                                    changed = true;
                                }
                                continue;
                            }
                    }
                }
            }
            sb.append(c);
            ++pos;
        }
        return changed ? sb.toString() : path;
    }

    public static void copyTo(String file, OutputStream out)
            throws IOException
    {
        copyTo(new File(file), out);
    }

    public static void copyTo(File file, OutputStream out)
            throws IOException
    {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);

            IOUtil.copy(bis, out);
        }
        finally {
            IOUtil.close(bis, fis);
        }
    }

    public static void copyTo(File src, File dst)
            throws IOException
    {
        if (src.length() < 64 * 1024) {
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            try {
                fis = new FileInputStream(src);
                bis = new BufferedInputStream(fis);
                fos = new FileOutputStream(dst);
                bos = new BufferedOutputStream(fos);

                IOUtil.copy(bis, bos);
            }
            finally {
                IOUtil.close(bis, fis);
                IOUtil.close(bos, fos);
            }
        }
        else {
            FileInputStream fis = null;
            FileOutputStream fos = null;
            FileChannel fic = null;
            FileChannel foc = null;
            try {
                fis = new FileInputStream(src);
                fic = fis.getChannel();
                fos = new FileOutputStream(dst);
                foc = fos.getChannel();
                fic.transferTo(0, src.length(), foc);
//                foc.force(true);
            }
            finally {
                IOUtil.close(fic);
                IOUtil.close(fis);
                IOUtil.close(foc);
                IOUtil.close(fos);
            }
        }
    }

    /**
     * 将srcDir目录中所有文件或者目录拷贝到目标目录
     *
     * @param srcDir 源目录
     * @param dstDir 目标目录
     */
    public static void copyAll(File srcDir, File dstDir)
            throws IOException
    {
        if (!srcDir.isDirectory()) {
            throw new IllegalArgumentException("Invalid source directory:" + srcDir);
        }
        if (!dstDir.isDirectory()) {
            throw new IllegalArgumentException("Invalid destination directory:" + dstDir);
        }
        File[] files = srcDir.listFiles();
        copyTo0(files, dstDir);
    }

    /**
     * 将srcDir目录中满足条件的文件或者目录拷贝到目标目录
     *
     * @param srcDir  源目录
     * @param pattern 模式
     * @param dstDir  目标目录
     */
    public static void copyTo(File srcDir, String pattern, File dstDir) throws IOException
    {
        if (!srcDir.isDirectory()) {
            throw new IllegalArgumentException("Invalid source directory:" + srcDir);
        }
        if (!dstDir.isDirectory()) {
            throw new IllegalArgumentException("Invalid destination directory:" + dstDir);
        }

        FilenameSelector selector = new FilenameSelector(pattern);
        File[] files = srcDir.listFiles(selector);
        copyTo0(files, dstDir);
    }

    /**
     * 将一个文件Copy到目标目录，文件名保持不变
     *
     * @param srcFile 源文件
     * @param dstDir  目标目录
     */
    public static boolean copyToDir(File srcFile, File dstDir)
            throws IOException
    {
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

    static void copyTo0(File[] files, File dstDir) throws IOException
    {
        if (ArrayUtil.isValid(files)) {
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

    public static void copyTo(String src, File dst)
            throws IOException
    {
        copyTo(new File(src), dst);
    }

    public static void copyTo(File src, String dst)
            throws IOException
    {
        copyTo(src, new File(dst));
    }

    public static void copyTo(String src, String dst)
            throws IOException
    {
        copyTo(new File(src), new File(dst));
    }

    public static void copyTo(String src, FileChannel fch)
            throws IOException
    {
        FileInputStream fis = null;
        FileChannel fic = null;
        File file = new File(src);
        try {
            fis = new FileInputStream(file);
            fic = fis.getChannel();

//            fic.transferTo(0, file.length(), fch);
            fch.transferFrom(fic, fch.size(), fic.size());
//            fch.force(true);
        }
        finally {
            IOUtil.close(fic);
            IOUtil.close(fis);
        }
    }

    public static void copyTo(InputStream in, String dst)
            throws IOException
    {
        copyTo(in, new File(dst));
    }

    public static void copyTo(InputStream in, File dst)
            throws IOException
    {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            fos = new FileOutputStream(dst);
            bos = new BufferedOutputStream(fos);

            IOUtil.copy(in, bos);
        }
        finally {
            IOUtil.close(bos, fos);
        }
    }
}
