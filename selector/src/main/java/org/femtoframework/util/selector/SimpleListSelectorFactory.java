package org.femtoframework.util.selector;

import org.femtoframework.lang.reflect.Reflection;
import org.femtoframework.pattern.ext.BaseFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * List选取器工厂
 *
 * @author fengyun
 * @version 1.00 2004-3-14 21:35:55
 */
public class SimpleListSelectorFactory extends BaseFactory<Class<? extends ListSelector>>
    implements ListSelectorFactory
{
    //因为Random无需维护什么状态
    public static final String DEFAULT_SELECTOR_NAME = "random";

    {
        add("random", RandomSelector.class);
        add("round_robin", RoundRobinSelector.class);
        add("hash", HashSelector.class);
    }

    /**
     * 返回默认的选取器
     */
    public ListSelector createDefaultSelector()
    {
        return createSelector(DEFAULT_SELECTOR_NAME);
    }

    /**
     * 根据选取器类型和过滤器名称返回选取器（选取器必需是单例）
     *
     * @param type      类型
     * @param arguments Arguments for constructor
     */
    @Override
    public ListSelector createSelector(String type, Object... arguments) {
        Class<? extends ListSelector> clazz = get(type);
        if (clazz == null) {
            throw new IllegalArgumentException("No such type:" + type);
        }
        if (arguments == null || arguments.length == 0) {
            return Reflection.newInstance(clazz);
        }
        else {
            Constructor[] constructors = clazz.getConstructors();
            if (constructors != null) {
                for(Constructor constructor : constructors) {
                    if (constructor.getParameterTypes().length == arguments.length) {
                        Class<?>[] types = constructor.getParameterTypes();
                        boolean allMatch = true;
                        int i = 0;
                        for(Class<?> t: types) {
                            if (!t.isAssignableFrom(arguments[i].getClass())) {
                                allMatch = false;
                                break;
                            }
                        }
                        if (allMatch) {
                            try {
                                return (ListSelector)constructor.newInstance(arguments);
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                                throw new IllegalStateException("Not able to create ListSelector for:" + clazz + " by arguments:" + arguments);
                            }
                        }
                    }
                }
            }
            return null;
        }
    }
}
