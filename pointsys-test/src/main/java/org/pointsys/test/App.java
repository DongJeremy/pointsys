package org.pointsys.test;

import java.lang.reflect.Method;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws Exception {
        Demo cls = new Demo();
        Class<?>[] cArg = new Class<?>[1];
        cArg[0] = String.class;
        Method m = cls.getClass().getDeclaredMethod("setMessage", String.class);
        m.invoke(cls, "重新设置msg信息！");
        System.out.println(cls);

        Class<?> cls2 = Class.forName("org.pointsys.test.Demo");
        Method m2 = cls2.getDeclaredMethod("hi", new Class[] { int.class, String.class });
        m2.invoke(cls2.getDeclaredConstructor().newInstance(), 20, "chb");
    }
}
