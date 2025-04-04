package com.vtit.project.thuctap.constant.enums;

public enum ResponseObject {
    BOOK, BORROW, BORROW_ITEM, CATEGORY,
    COMMENT, INTERACT, PERMISSION, POST,
    ROLE, USER,
    USERNAME, EMAIL, IDENTITY_NUMBER, CODE
    ;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
