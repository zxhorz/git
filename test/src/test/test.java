package test;

import java.lang.reflect.Field;

import javax.enterprise.inject.New;

public class test {
    public static void main(String [] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        A t = new A();

        Field f = t.getClass().getDeclaredField("name");

        f.setAccessible(true);

        f.set(t, "this is test1");

        System.out.println(t.getName());
    }
}
