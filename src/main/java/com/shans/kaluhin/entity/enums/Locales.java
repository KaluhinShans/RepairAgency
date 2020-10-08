package com.shans.kaluhin.entity.enums;

public enum Locales {
    En, Ru, Ua, Ja;

    public static boolean contains(String test) {
        for (Locales l : Locales.values()) {
            if (l.name().equals(test)) {
                return true;
            }
        }
        return false;
    }
}
