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
package org.femtoframework.util;

import org.femtoframework.lang.reflect.Reflection;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;

/**
 * Load properties from classpath
 *
 * @author fengyun
 * @version 1.00 2005-2-12 15:29:36
 */
public class ClasspathProperties extends MessageProperties {
    /**
     * Constructer
     *
     * @param resourceName properties file name
     */
    public ClasspathProperties(String resourceName)
            throws IOException
    {
        super(resourceName);
    }

    /**
     * Constructer
     *
     * @param resourceName properties file name
     * @param charset      character set
     */
    public ClasspathProperties(String resourceName, Charset charset) throws IOException
    {
        super(resourceName, charset);
    }

    protected void load1(String resourceName, Charset charset) throws IOException
    {
        setCharset(charset);

        Enumeration en = Reflection.getResources(resourceName);
        while (en.hasMoreElements()) {
            URL url = (URL)en.nextElement();
            try (InputStream input = url.openStream()){
                load(input, this.charset);
            }
        }
    }
}
