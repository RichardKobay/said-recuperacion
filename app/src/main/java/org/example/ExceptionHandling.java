package org.example;

public class ExceptionHandling {
    public static int toInt(Object item) {
        try { return Integer.parseInt(item.toString());
        } catch (NumberFormatException ignored){}
        return 0;
    }
}
