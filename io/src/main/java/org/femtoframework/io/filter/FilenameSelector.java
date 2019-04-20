package org.femtoframework.io.filter;

import org.femtoframework.util.selector.SelectorUtil;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 文件名选取器
 *
 * @author fengyun
 */
public class FilenameSelector
    implements FilenameFilter
{
    private String pattern;
    private boolean isCaseSensitive = true;

    public FilenameSelector(String pattern)
    {
        this(pattern, false);
    }

    public FilenameSelector(String pattern, boolean isCaseSensitive)
    {
        this.pattern = pattern;
        this.isCaseSensitive = isCaseSensitive;
    }

    /**
     * Tests if a specified file should be included in a file list.
     *
     * @param dir  the directory in which the file was found.
     * @param name the name of the file.
     * @return <code>true</code> if and only if the name should be
     *         included in the file list; <code>false</code> otherwise.
     */
    public boolean accept(File dir, String name)
    {
        return SelectorUtil.match(pattern, name, isCaseSensitive);
    }

}
