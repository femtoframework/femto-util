package org.femtoframework.util.selector;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleListSelectorFactoryTest {

    @Test
    public void createDefaultSelector() {

        SimpleListSelectorFactory simpleListSelectorFactory = new SimpleListSelectorFactory();
        assertNotNull(simpleListSelectorFactory.createDefaultSelector());
    }

    @Test
    public void createSelector() {
        SimpleListSelectorFactory simpleListSelectorFactory = new SimpleListSelectorFactory();
        assertNotNull(simpleListSelectorFactory.createSelector("hash", "abc"));
    }
}