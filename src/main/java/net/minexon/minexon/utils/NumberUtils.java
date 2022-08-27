package net.minexon.minexon.utils;

public final class NumberUtils {

    /**
     * Source: apache utils
     */
    public static boolean isNumeric(final CharSequence cs) {
        if (cs == null || cs.length() == 0) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
                }
            }
        return true;
    }

}
