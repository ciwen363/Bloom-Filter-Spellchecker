package com.ciwen.bloom.hash;

public class FNVHash<T> implements HashFunction<T> {
    private static final long FNV_64_PRIME = 0x100000001b3L; //初始值
    private static final long FNV_64_INIT = 0xcbf29ce484222325L; //质数

    @Override
    public int hash(Object item, int seed) {
        String string = item.toString();
        long hash = FNV_64_INIT;
        for (int i = 0; i < string.length(); i++) {
            hash ^= string.charAt(i);
            hash *= FNV_64_PRIME;
        }
        hash += seed;
        return (int) hash;
    }
}