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
    public static Integer toBinaryInteger(String tslot) {
        // 確保字符串只包含合法二進位字符
        if (!tslot.matches("[01]+")) {
            throw new IllegalArgumentException("Invalid binary string: " + tslot);
        }
        return Integer.parseInt(tslot, 2);
    }

    //BitSet轉Integer
    public static Integer toBinaryInteger(BitSet tslot, int length) {
        int result = 0;
        for (int i = 0; i < tslot.length(); i++) {
            if (tslot.get(i)) {
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

    public static BitSet toBitSet(int tslot,int bitLength) {
        BitSet bitSet = new BitSet();
        for (int i = 0; i < bitLength; i++) {
            if((tslot & 1<<(bitLength-1-i)) != 0) {
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
    public static String toBinaryString(BitSet tslot, int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(tslot.get(i) ? '1' : '0');
        }
        //.reverse() 是 Java 中 StringBuilder 類別的一個方法，用於反轉 StringBuilder 中的字元順序。
        return stringBuilder.toString(); // 從左到右還原
    }

    //List轉String
    public static String toBinaryString(List<Integer> tslot, int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(tslot.contains(i) ? '1' : '0');
        }
        return stringBuilder.toString();
    }


    //BitSet轉List
    public static List<Integer> toList(BitSet tslot) {
        List<Integer> list = new ArrayList<>();
        //.nextSetBit(i)滿有趣的，他會從找i開始找，所以i+1並不會跳過
        for (int i = tslot.nextSetBit(0); i >= 0; i = tslot.nextSetBit(i + 1)) {
            list.add(i);
        }
        return list;
    }

    //Integer轉List<Integer>
    public static List<Integer> toList(int tslot, int bitLength) {
        List<Integer> positions = new ArrayList<>();

        for (int i = 0; i < bitLength; i++) {
            // 檢查第 i 位是否為 1
            if ((tslot & (1 << (bitLength - 1 - i))) != 0) {
                positions.add(i); // 如果為 1，加入該位索引
            }
        }
        return positions;
    }

    //String轉List
    public static List<Integer> toList(String tslot) {
        List<Integer> positions = new ArrayList<>();
        int length = tslot.length();
        for (int i = 0; i < length; i++) {
            if (tslot.charAt(i) == '1') {
                positions.add(i);
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

