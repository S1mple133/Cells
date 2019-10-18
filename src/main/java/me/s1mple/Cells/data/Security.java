package me.s1mple.Cells.data;

import me.s1mple.Cells.Cells;

public enum Security {
    LOW,
    MEDIUM,
    HIGH;

    /**
     * Returns security by ID
     * @param id
     * @return
     */
    public Security getSecurityByID(int id) {
        switch (id) {
            case 0:
                return LOW;
            case 1:
                return MEDIUM;
            case 2:
                return HIGH;
            default:
                return null;
        }
    }

    /**
     * Returns name of security type
     * @param security
     * @return
     */
    public static String getSecurityName(Security security) {
        switch (security) {
            case LOW:
                return Cells.getPlugin().getConfig().getString("Security.LOW");
            case MEDIUM:
                return Cells.getPlugin().getConfig().getString("Security.MEDIUM");
            case HIGH:
                return Cells.getPlugin().getConfig().getString("Security.HIGH");
            default:
                return "";
        }
    }
}
