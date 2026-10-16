package com.kmie.demo_booking.entity;

public enum DemoSlot {

    SLOT_1_PM("1 PM"),
    SLOT_3_PM("3 PM"),
    SLOT_5_PM("5 PM"),
    SLOT_7_PM("7 PM");

    private final String displayName;

    DemoSlot(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}