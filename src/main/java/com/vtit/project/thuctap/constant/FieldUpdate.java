package com.vtit.project.thuctap.constant;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FieldUpdate<T> {

    private final Supplier<T> getter;  // Supplier để lấy giá trị
    private final Consumer<T> setter;  // Consumer để cập nhật giá trị
    private final T newValue;          // Giá trị mới

    public FieldUpdate(Supplier<T> getter, Consumer<T> setter, T newValue) {
        this.getter = getter;
        this.setter = setter;
        this.newValue = newValue;
    }

    // Lấy giá trị hiện tại của trường
    public T getCurrentValue() {
        return getter.get();
    }

    // Lấy giá trị mới của trường
    public T getNewValue() {
        return newValue;
    }

    // Trả về setter để cập nhật trường
    public Consumer<T> getSetter() {
        return setter;
    }

    // Kiểm tra và cập nhật nếu cần
    public void updateIfNeeded() {
        if (getCurrentValue() == null ? getNewValue() != null : !getCurrentValue().equals(getNewValue())) {
            getSetter().accept(getNewValue());  // Cập nhật trường
        }
    }
}
