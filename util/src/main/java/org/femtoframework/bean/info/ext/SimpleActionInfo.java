package org.femtoframework.bean.info.ext;

import org.femtoframework.bean.annotation.Action;
import org.femtoframework.bean.annotation.Coined;
import org.femtoframework.bean.annotation.Description;
import org.femtoframework.bean.annotation.Ignore;
import org.femtoframework.bean.info.AbstractFeatureInfo;
import org.femtoframework.bean.info.ActionInfo;
import org.femtoframework.bean.info.ArgumentInfo;

import javax.inject.Named;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple Action Info
 */
@Coined
public class SimpleActionInfo extends AbstractFeatureInfo implements ActionInfo {

    private String returnType;
    private List<ArgumentInfo> arguments;
    private Action.Impact impact = Action.Impact.UNKNOWN;

    @Ignore
    private Method method;

    /**
     * Return the method name, usually it is same as action name,
     * The name could be redefined in the @Action, we have to remember the method name for finding the method again.
     * @return Method Name
     */
    public String getMethodName() {
        return getMethod() == null ? getName() : getMethod().getName();
    }

    @Override
    public String getReturnType() {
        return returnType;
    }

    @Override
    public List<ArgumentInfo> getArguments() {
        return arguments;
    }

    @Override
    public Action.Impact getImpact() {
        return impact;
    }

    /**
     * Invoke the action method
     *
     * @param bean The bean
     * @param arguments Invoke the action
     * @return value
     */
    @Override
    public <T> T invoke(Object bean, Object... arguments) throws Exception {

        return (T)method.invoke(bean, arguments);
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public void setArguments(List<ArgumentInfo> arguments) {
        this.arguments = arguments;
    }

    public void setImpact(Action.Impact impact) {
        this.impact = impact;
    }

    @Ignore
    public Method getMethod() {
        return method;
    }

    @Ignore
    public void setMethod(Method method) {
        this.method = method;
        setArguments(method);
    }

    /**
     * Set Arguments in the action info
     *
     * @param method Method
     */
    protected void setArguments(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();

        List<ArgumentInfo> argumentInfos = new ArrayList<>(parameterTypes.length);
        int index = 1;
        for(Class<?> type: parameterTypes) {
            SimpleArgumentInfo info = new SimpleArgumentInfo();
            Named named = type.getAnnotation(Named.class);
            String typeName = type.getSimpleName();
            String argumentName = null;
            if (named != null) {
                argumentName = named.value();
            }
            else {
                argumentName = String.valueOf(index);
            }
            info.setType(typeName);
            Description description = type.getAnnotation(Description.class);
            if (description != null) {
                info.setDescription(description.value());
            }
            info.setName(argumentName);
            index ++;

            argumentInfos.add(info);
        }
        setArguments(argumentInfos);
    }
}
