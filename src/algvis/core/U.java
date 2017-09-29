package algvis.core;

public abstract class U {

    public static <T> T withDefault(T value, T def) {
        return value == null ? def : value;
    }
}
