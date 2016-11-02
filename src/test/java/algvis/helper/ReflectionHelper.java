package algvis.helper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionHelper {
    static Class<?> getClass(String classFullName) {
        Class<?> cls = null;
        try {
            cls = Class.forName(classFullName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cls;
    }

    public static Object getFieldValue(Object object, String fieldName) {
        if (object == null) {
            return null;
        }

        Field field = null;

        try {
            field = object.getClass().getDeclaredField(fieldName);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        if (field == null) {
            Class<?> superClass = object.getClass().getSuperclass();

            while (superClass != null) {
                try {
                    field = superClass.getDeclaredField(fieldName);
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }

                if (field != null) {
                    break;
                }
                superClass = superClass.getSuperclass();
            }
        }

        if (field == null) {
            return null;
        }

        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);

        Object value = null;
        try {
            value = field.get(object);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        field.setAccessible(isAccessible);

        return value;
    }

    public static Object createObject(String classFullName) {
        Class<?> cls = getClass(classFullName);

        if (cls == null) {
            return null;
        }

        Constructor<?> constructor = null;
        try {
            constructor = cls.getConstructor();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Object object = null;

        if (constructor == null) {
            try {
                object = cls.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            boolean constructorIsAccessible = constructor.isAccessible();
            constructor.setAccessible(true);
            try {
                object = constructor.newInstance();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            constructor.setAccessible(constructorIsAccessible);
        }

        return object;
    }

    public static Method getMethod(String classFullName, String methodName, Class<?>... parameterTypes) {
        Class<?> cls = getClass(classFullName);

        if (cls == null || "".equals(methodName)) {
            return null;
        }

        Method method = null;

        try {
            method = cls.getDeclaredMethod(methodName, parameterTypes);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return method; 
    }
}
