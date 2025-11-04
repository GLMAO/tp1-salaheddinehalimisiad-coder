package org.emp.gl.lookup;

import java.util.HashMap;

public class Lookup {
    private static Lookup instance;
    private static Object[] lock = new Object[0];
    public HashMap<Class, Object> serviceMap = new HashMap<Class, Object>();

    private Lookup() {
        System.out.println("You cannot create a instance from here :)");
    }

    public static Lookup getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new Lookup();
                }
            }
        }
        return instance;
    }

    public <T> void addService(Class<T> serviceClass, T serviceObject) {
        serviceMap.put(serviceClass, serviceObject);
    }

    public <T> T getService(Class<T> serviceClass) {
        return (T) serviceMap.get(serviceClass);
    }
}