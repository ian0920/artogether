package com.artogether.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

//這邊可以換成Stream呈現
@Component
public class BinaryTools {

    //String轉Integer
    public static Integer toBinaryInteger(String binary) {
        // 確保字符串只包含合法二進位字符
        if (!binary.matches("[01]+")) {
            throw new IllegalArgumentException("Invalid binary string: " + binary);
        }
        return Integer.parseInt(binary, 2);
    }

    //BitSet轉Integer
    public static Integer toBinaryInteger(BitSet bitSet, int length) {
        int result = 0;
        for (int i = 0; i < bitSet.length(); i++) {
            if (bitSet.get(i)) {
                result += (1 << i);
            }
        }
        return result;
    }

    //String轉BitSet
    public static BitSet toBitSet(String binaryString) {
        BitSet bitSet = new BitSet();
        int length = binaryString.length();

        for (int i = 0; i < length; i++) {
            if (binaryString.charAt(length - i - 1) == '1') { // 從右到左解析
                bitSet.set(i);
            }
        }
        return bitSet;
    }

    //List轉BitSet
    public static BitSet toBitSet(List<Integer> tslot) {
        BitSet bitSet = new BitSet();
        for (Integer position : tslot) {
            if (position >= 0) { // 確保索引有效
                bitSet.set(position);
            }
        }
        return bitSet;
    }

    //BitSet轉String
    public static String toBinaryString(BitSet bitSet, int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(bitSet.get(i) ? '1' : '0');
        }
        //.reverse() 是 Java 中 StringBuilder 類別的一個方法，用於反轉 StringBuilder 中的字元順序。
        return stringBuilder.reverse().toString(); // 從左到右還原
    }

    //List轉String
    public static String toBinaryString(List<Integer> tslot, int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            if (tslot.get(i) > i) {
                for (int j = i; j < tslot.get(i); j++) {
                    stringBuilder.append('0');
                }
            } else {
                stringBuilder.append('1');
            }
        }
        return stringBuilder.reverse().toString();
    }

    //BitSet轉List
    public static List<Integer> toList(BitSet bitSet) {
        List<Integer> list = new ArrayList<>();
        //.nextSetBit(i)滿有趣的，他會從找i開始找，所以i+1並不會跳過
        for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
            list.add(i);
        }
        return list;
    }

    //Integer轉List<Integer>
    public static List<Integer> toList(int number, int bitLength) {
        List<Integer> positions = new ArrayList<>();

        for (int i = 0; i < bitLength; i++) {
            // 檢查第 i 位是否為 1
            if ((number & (1 << (bitLength - 1 - i))) != 0) {
                positions.add(i); // 如果為 1，加入該位索引
            }
        }
        return positions;
    }

    //String轉List
    public static List<Integer> toList(String binaryString) {
        List<Integer> positions = new ArrayList<>();
        int length = binaryString.length();
        for (int i = 0,j = 1; i < length; i++,j++) {
            if (binaryString.charAt(i) == '1') {
                positions.add(j);
            }
        }
        return positions;
    }

    //String.first
    public static Integer first(String binaryString) {
        return toList(binaryString).get(0);
    }
    //String.last
    public static Integer last(String binaryString) {
        List<Integer> list = toList(binaryString);
        return list.get(list.size() - 1);
    }
}

