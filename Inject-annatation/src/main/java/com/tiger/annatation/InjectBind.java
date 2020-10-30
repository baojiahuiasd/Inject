package com.tiger.annatation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class InjectBind {

    public static void bind(Object host) {
        Class bindViewClazz = null;
        try {
            bindViewClazz = Class.forName(host.getClass().getName() + "_IntjectBinding");
            Method method = bindViewClazz.getMethod("bind", host.getClass());
            method.invoke(bindViewClazz.newInstance(), host);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
