package com.artogether.util;

import javax.persistence.AttributeConverter;
import java.time.DayOfWeek;

// 定義一個抽象類 EnumConverter，
// 它實現了 JPA 提供的 AttributeConverter 接口，用於處理 Java Enum 和數據庫列類型的轉換。
// 這個抽象類是泛型的，E 是一個 Enum 類型，並且需要實現 EnumInterface 接口，T 是數據庫列的類型。
public abstract class EnumConverter<E extends Enum<E> & EnumInterface, T> implements AttributeConverter<E, T> {

    // 一個私有的成員變數，用於保存 Enum 類型的 Class 對象，
    // 它將用於在轉換中遍歷和操作 Enum 類型的值。
    private final Class<E> enumClass;

    // 受保護的構造函數，用於在子類中初始化該轉換器。
    // 傳入的參數是 Enum 類型的 Class 對象，用於確定具體的 Enum 類型。
    protected EnumConverter(Class<E> enumClass) {
        this.enumClass = enumClass; // 將傳入的 Enum 類型保存到成員變數中，供後續使用。
    }

    @Override
    public T convertToDatabaseColumn(E attribute) {
        return attribute != null ? (T) attribute.getCode() : null; // 泛型方法取 Enum 對應的代碼
    }

    @Override
    public E convertToEntityAttribute(T dbData) {
        if (dbData == null) {
            return null;
        }
        for (E enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.getCode().equals(dbData)) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException("未知的數據庫值: " + dbData);
    }
}
