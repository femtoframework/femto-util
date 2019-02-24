package org.femtoframework.implement;

/**
 * @author fengyun
 * @version 1.00 2011-08-27 19:17
 */
public class SimpleZeroConfigService implements ZeroConfigService {
    @Override
    public void service(String test) {
        System.out.println("Service2:" + test);
    }
}
