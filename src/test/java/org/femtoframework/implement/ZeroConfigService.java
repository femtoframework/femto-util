package org.femtoframework.implement;

import org.femtoframework.annotation.ImplementedBy;

/**
 * Lazy Service to show the sample of <code>@ImplementedBy</code>
 *
 * @author fengyun
 * @version 1.00 2011-08-27 19:17
 */
@ImplementedBy("org.femtoframework.implement.SimpleZeroConfigService")
public interface ZeroConfigService {

    void service(String test);
}
