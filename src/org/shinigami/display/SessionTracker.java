package org.shinigami.display;

/**
 * Created by akshirsa on 6/1/2015.
 */
public class SessionTracker {
    private static boolean isNewInstance = true;

    public static boolean isNewInstance() {
        return isNewInstance;
    }

    public static void setNewInstance(boolean isNewInstance) {
        SessionTracker.isNewInstance = isNewInstance;
    }
}
