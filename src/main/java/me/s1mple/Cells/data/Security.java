package me.s1mple.Cells.data;

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

    public String getSecurityName(Security security) {
        switch (security) {
            case LOW:
                return
        }
    }
}
