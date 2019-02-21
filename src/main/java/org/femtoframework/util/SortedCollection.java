package org.femtoframework.util;

import java.util.Collection;

public interface SortedCollection<V> extends Collection<V>
{
    int indexOf(Object obj);

    V get(int i);

    V remove(int i);
}
