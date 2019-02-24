package org.femtoframework.implement;

/**
 * @author fengyun
 * @version 1.00 2011-08-27 19:07
 */
public class SimpleService implements ServiceInterface {
    @Override
    public void service(String test) {
        System.out.println(test);
    }
}
