package com.shans.kaluhin;

public enum Sort {
    AZ("(A-Z)"), //(A-z)
    ZA("(Z-A)"), //(Z-a)
    ASC("value asc"), //value ascending
    DSC("value desc");  //value descending

    public final String val;

    private Sort(String val) {
        this.val = val;
    }
}
