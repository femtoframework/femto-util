package org.femtoframework.bean.info.ext;

import org.femtoframework.bean.annotation.Coined;
import org.femtoframework.bean.annotation.Ignore;
import org.femtoframework.bean.annotation.Property;
import org.femtoframework.bean.info.AbstractFeatureInfo;
import org.femtoframework.bean.info.BeanNamingConvension;
import org.femtoframework.bean.info.PropertyInfo;
import org.femtoframework.lang.reflect.Reflection;
import org.femtoframework.text.NamingConvention;
import org.femtoframework.util.StringUtil;
import org.femtoframework.util.convert.ConverterUtil;
import org.femtoframework.util.convert.DataConverter;

import java.lang.reflect.*;
import java.util.Map;

/**
 * Simple Property Info implementation
 *
 * 1. No any annotations
 *                     name     getter       readable        setter      writable
 *    getXxx only      xxx      getXxx       true            ""          false
 *    setXxx only      xxx      ""           false           setXxx      true
 *    getXxx + setXxx  xxx      getXxx       true            setXxx      true
 *
 *
 * 2. With @Ignore
 *                                       name     getter       readable        setter      writable
 *    getXxx only + Ignore               xxx      getXxx       false            ""         false
 *    setXxx only + Ignore               xxx      ""           false           setXxx      false
 *    getXxx + setXxx  IgnoreOnGetter    xxx      getXxx       false           setXxx      true
 *    getXxx + setXxx  IgnoreOnSetter    xxx      getXxx       true            setXxx      false
 *
 * @Setter or @Getter can redefined the getter or setter method
 * @Property provides information for the property
 * @Description provides the description for the property
 * @Pojo is only supported on BeanClass, it defined the order of properties, it is lower priority than the index on Property.
 *       That means Coined assigns indexes to each properties, then it could be redefined by @Property
 *
 * @author Sheldon Shao
 * @version 1.0
 */
@Coined
public class SimplePropertyInfo extends AbstractFeatureInfo implements PropertyInfo {
    private String type;

    @Ignore
    private transient Type genericType;

    @Ignore
    private transient Class<?> typeClass;

    private boolean readable = true;
    private boolean writable = true;

    private String getter = null;
    private String setter = null;

    @Ignore
    private transient Method getterMethod = null;

    @Ignore
    private boolean noGetterMethod = false;

    @Ignore
    private boolean noSetterMethod = false;

    @Ignore
    private transient Method setterMethod = null;

    private String defaultValue = null;
    private int index = Property.INDEX_UNKNOWN;
    private boolean required = false;

    @Ignore
    private Class<?> declaringClass;

    protected static String getType(String type) {
        if (StringUtil.isValid(type)) {
            return type;
        }
        else {
            return null;
        }
    }

    public SimplePropertyInfo(Property property, Field field) {
        this.declaringClass = field.getDeclaringClass();
        setProperty(property, field);
    }

    public SimplePropertyInfo(Property property, Method method) {
        this.declaringClass = method.getDeclaringClass();
        setProperty(property, method);
    }

    /**
     * Constructs an <CODE>PropertyInfo</CODE> object.
     *
     * @param name        The name of the attribute.
     * @param typeClass   The type or class name of the attribute.
     * @param genericType GenericType of the property
     */
    public SimplePropertyInfo(Class<?> parentClass, String name, Class<?> typeClass, Type genericType) {
        super(name, "");
        this.declaringClass = parentClass;
        setType(typeClass, genericType);
    }

    public void setName(String name) {
        BeanNamingConvension.mustBeValidJavaIdentifier(name);

        super.setName(name);
        if (getter == null && getType() != null) {
            this.getter = NamingConvention.toGetter(name, getType());
        }
        if (setter == null) {
            this.setter = NamingConvention.toSetter(name);
        }
    }

    /**
     * Is the property has getter?
     *
     * @return Is the property readable
     */
    public boolean isReadable() {
        return readable;
    }

    /**
     * Is the property has setter?
     *
     * @return Is the property writable
     */
    public boolean isWritable() {
        return writable;
    }

    /**
     * Return the getter method, default is "getXxx" or "isXxx"
     * But it could be redefined by "@JsonGetter"
     *
     * @see org.femtoframework.bean.annotation.Getter
     *
     * @return The real getter method name
     */
    public String getGetter() {
        return getter == null ? PropertyInfo.super.getGetter() : getter;
    }

    /**
     * Return the setter method, default is "setXxx"
     * It could be redefined by "@JsonSetter"
     *
     * @see org.femtoframework.bean.annotation.Setter
     *
     * @return the real setter method name
     */
    public String getSetter() {
        return setter == null ? PropertyInfo.super.getSetter() : setter;
    }

    /**
     * Invoke setter method to set the value
     *
     * @param bean  Bean
     * @param value Value
     */
    @Override
    public void invokeSetter(Object bean, Object value) {
        if (bean instanceof Map) {
            ((Map)bean).put(getName(), value);
        }
        else {
            Method setterMethod = getSetterMethod();
            if (setterMethod != null) {
                this.setterMethod = setterMethod;
                Object expectedValue = value;
                if (value != null) {
                    DataConverter converter = ConverterUtil.getConverter(getGenericType());
                    if (converter != null) {
                        expectedValue = converter.convert(value);
                    }
                }
                Reflection.invoke(bean, setterMethod, expectedValue);
            }
        }
    }

    /**
     * Invoke getter method to get the value
     *
     * @param bean Bean
     * @return property value
     */
    @Override
    public <T> T invokeGetter(Object bean) {
        if (bean instanceof Map) {
            return (T)((Map)bean).getOrDefault(getName(), getExpectedDefaultValue());
        }
        else {
            Method getterMethod = getGetterMethod();
            if (getterMethod != null) {
                this.getterMethod = getterMethod;
                Object value = Reflection.invoke(bean, getterMethod, null);
                return value != null ? (T)value : getExpectedDefaultValue();
            }
            return null;
        }
    }

    /**
     * Return Getter Method
     *
     * @return Getter Method
     */
    public Method getGetterMethod() {
        Method getterMethod = this.getterMethod;
        if (getterMethod == null) {
            if (noGetterMethod) { //Return null to avoid check again
                return null;
            }
            getterMethod = Reflection.getMethod(declaringClass, getGetter());
            if (getterMethod == null) {
                noGetterMethod = true;
            }
        }
        return getterMethod;
    }

    /**
     * Return Setter Method
     *
     * @return Setter Method
     */
    public Method getSetterMethod() {
        Method setterMethod = this.setterMethod;
        if (setterMethod == null) {
            if (noSetterMethod) { //Return null to avoid check again
                return null;
            }
            Class<?> typeClass = getTypeClass();
            setterMethod = Reflection.getMethod(declaringClass, getSetter(), typeClass);
            if (setterMethod == null) {
                Method[] methods = Reflection.getMethods(declaringClass, getSetter());
                if (methods == null || methods.length == 0) {
                    noSetterMethod = true;
                }
                else {
                    for(Method method: methods) {
                        int count = method.getParameterCount();
                        if (count == 1) {
                            if (typeClass.isAssignableFrom(method.getParameterTypes()[0])) {
                                setterMethod = method;
                                break;
                            }
                        }
                    }
                    if (setterMethod == null) {
                        noSetterMethod = true;
                    }
                }
            }
        }
        return setterMethod;
    }

    /**
     * Default value defined in String
     *
     * @see Property
     */
    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Get default value as expected type
     *
     * @return default value as expected type
     */
    @Override
    @Ignore
    public <T> T getExpectedDefaultValue() {
        if (defaultValue != null) {
            DataConverter converter = ConverterUtil.getConverter(getGenericType());
            if (converter != null) {
                return (T)converter.convert(defaultValue);
            }
        }
        return null;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Class<?> getTypeClass() {
        return typeClass;
    }

    /**
     * Return type generic type of this property
     */
    @Override
    public Type getGenericType() {
        return genericType;
    }

    /**
     * Index of this property
     *
     * @return Index of this property, for ProtocolBuf or other schema enabled serialization
     */
    @Override
    public int getIndex() {
        return index;
    }

    /**
     * Is required or not
     *
     * @return is the properties a must have property? if so when the property is not set during deserialization, deserializer returns a validation exception.
     */
    @Override
    public boolean isRequired() {
        return required;
    }

    /**
     * Set the properties from Property
     *
     * @param property Property
     */
    @Ignore
    public void setProperty(Property property, Method method) {
        boolean setter = false;
        if (StringUtil.isInvalid(type) && method != null) {
            if (method.getParameterTypes().length == 1) {
                setType(method.getParameterTypes()[0], method.getGenericParameterTypes()[0]);
                setter = true;
            }
            else {
                setType(method.getReturnType(), method.getGenericReturnType());
            }
        }

        String name = property.value();
        if (StringUtil.isInvalid(name) && method != null) {
            name = NamingConvention.toPropertyName(method.getName(), setter ? null : getTypeClass());
        }

        setName(name);

        setProperty(property);
    }

    @Ignore
    protected void setProperty(Property property) {
        defaultValue = property.defaultValue();
        setIndex(property.index());
        this.required = property.required();
        this.readable = property.readable();
        this.writable = property.writable();
    }

    /**
     * Set the properties from Property
     *
     * @param property Property
     */
    @Ignore
    public void setProperty(Property property, Field field) {
        if (StringUtil.isInvalid(type) && field != null) {
            setType(field.getType(), field.getGenericType());
        }

        String name = property.value();
        if (StringUtil.isInvalid(name) && field != null) {
            name = field.getName();
        }

        setName(name);
        setProperty(property);
    }

    public void setType(Class<?> typeClass, Type type) {
        this.genericType = type;
        this.typeClass = typeClass;
        this.type = typeClass.getName();
    }

    public void setReadable(boolean readable) {
        this.readable = readable;
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    @Ignore
    public void setGetterMethod(Method getterMethod) {
        this.getterMethod = getterMethod;
        this.getter = getterMethod.getName();
    }

    @Ignore
    public void setSetterMethod(Method setterMethod) {
        this.setterMethod = setterMethod;
        this.setter = setterMethod.getName();
        this.writable = true;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Property Info
     *
     * @return Property Info
     */
    public String toString() {
        return "{name:" + getName() + ",type:" + getType() + ", readable:" + readable
                + ",writable:" + writable + ",getter:" + getter + ",setter:" + setter
                + ",index:" + index + ",required:" + required + ",defaultValue:" + defaultValue + "}";
    }
}
