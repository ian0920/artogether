package com.artogether.util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BitSetHelper {

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

    public static String toBinaryString(BitSet bitSet, int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(bitSet.get(i) ? '1' : '0');
        }
        return sb.reverse().toString(); // 從左到右還原
    }

    public static List<Integer> bitSetToList(BitSet bitSet) {
        List<Integer> list = new ArrayList<>();
        for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
            list.add(i);
        }
        return list;
    }
}
