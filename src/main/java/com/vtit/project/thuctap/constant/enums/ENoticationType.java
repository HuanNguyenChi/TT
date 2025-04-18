package com.vtit.project.thuctap.constant.enums;

public enum ENoticationType {
    OVERDUE,DUE_SOON;

    @Override
    public String toString() {
        return name().toUpperCase();
//        switch (this) {
//            case OVERDUE: return "OVERDUE";
//            case DUE_SOON: return "DUE_SOON";
//            default: return name();
//        }
    }
}
