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
package org.femtoframework.util.convert;

import org.femtoframework.util.DataTypes;
import org.femtoframework.util.DataUtil;

import java.util.List;

/**
 * Convert object to array list
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class ListConverter extends AbstractConverter<List> {

    public ListConverter() {
        super(DataTypes.TYPE_LIST);
    }

    /**
     * Convert the object to expected type，returns <code>default value</code> if is not convertible.
     *
     * @param obj      Object, The object could not be <code>null</code>
     * @param defValue Default Value
     * @return Converted object or default value
     */
    @Override
    protected List doConvert(Object obj, List defValue) {
        return DataUtil.getList(obj, defValue);
    }
}
