package com.ciwen.bloom.bitset;


/**
 * 自定义位数组实现
 */
public class CustomBitSet {
    private long[] bits;
    private int size;
    private static final int BITS_PER_WORD = 64;
    private static final int ADDRESS_BITS_PER_WORD = 6;
    private static final long WORD_MASK = 0xffffffffffffffffL;

    public CustomBitSet(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("不合理size: " + size);
        }
        this.size = size;
        bits = new long[wordIndex(size - 1) + 1];
    }

    private static int wordIndex(int bitIndex) {
        return bitIndex >> ADDRESS_BITS_PER_WORD;
    }

    public void set(int bitIndex) {
        if (bitIndex < 0 || bitIndex >= size) {
            throw new IndexOutOfBoundsException("位索引: " + bitIndex);
        }
        int wordIndex = wordIndex(bitIndex);
        bits[wordIndex] |= (1L << bitIndex);
    }

    public boolean get(int bitIndex) {
        if (bitIndex < 0 || bitIndex >= size) {
            throw new IndexOutOfBoundsException("位索引: " + bitIndex);
        }
        int wordIndex = wordIndex(bitIndex);
        return ((bits[wordIndex] & (1L << bitIndex)) != 0);
    }

    public void clear() {
        for (int i = 0; i < bits.length; i++) {
            bits[i] = 0;
        }
    }

    public int size() {
        return size;
    }

    public int cardinality() {
        int sum = 0;
        for (long bit : bits) {
            sum += Long.bitCount(bit);
        }
        return sum;
    }
}
