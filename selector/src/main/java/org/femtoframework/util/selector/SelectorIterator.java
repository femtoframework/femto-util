package org.femtoframework.util.selector;

import java.io.File;
import java.util.Iterator;

/**
 * 选取器枚举
 *
 * @author fengyun
 * @version 1.00 May 9, 2003 4:48:50 PM
 */
public class SelectorIterator implements Iterator<String>
{
    private Iterator<String> iterator;
    private boolean isPath;
    private String pattern;
    private String[] patterns;
    private String current = null;
    private boolean isCaseSensitive;
    private char sep = '/';

    public SelectorIterator(Iterator<String> iterator, String pattern, boolean path)
    {
        this(iterator, pattern, path, true);
    }

    public SelectorIterator(Iterator<String> iterator,
                            String[] pattern, char sep)
    {
        this(iterator, pattern, true, sep);
    }

    public SelectorIterator(Iterator<String> iterator, String pattern)
    {
        this(iterator, pattern, true);
    }

    public SelectorIterator(Iterator<String> iterator, String pattern,
                            boolean path, boolean isCaseSensitive)
    {
        this(iterator, pattern, path, isCaseSensitive, File.separatorChar);
    }

    public SelectorIterator(Iterator<String> iterator, String pattern,
                            boolean path, boolean isCaseSensitive,
                            char sep)
    {
        this.iterator = iterator;
        this.pattern = pattern;
        this.isPath = path;
        if (isPath) {
            patterns = SelectorUtil.tokenizePath(pattern);
        }
        this.isCaseSensitive = isCaseSensitive;
        this.sep = sep;
    }

    public SelectorIterator(Iterator<String> iterator, String[] pattern, boolean isCaseSensitive)
    {
        this(iterator, pattern, isCaseSensitive, File.separatorChar);
    }

    public SelectorIterator(Iterator<String> iterator, String[] pattern,
                            boolean isCaseSensitive, char sep)
    {
        this.iterator = iterator;
        this.patterns = pattern;
        this.isPath = true;
        this.isCaseSensitive = isCaseSensitive;
        this.sep = sep;
    }

    public boolean hasNext()
    {
        current = null;
        boolean matched;
        while (iterator.hasNext()) {
            current = iterator.next();
            if (isPath) {
                if (patterns != null) {
                    matched = SelectorUtil.matchPath(patterns, current,
                        isCaseSensitive, sep);
                }
                else {
                    matched = SelectorUtil.match(pattern, current, isCaseSensitive);
                }
            }
            else {
                matched = SelectorUtil.match(pattern, current, isCaseSensitive);
            }
            if (matched) {
                return true;
            }
        }
        current = null;
        return false;
    }

    public String next()
    {
        return current;
    }
}
