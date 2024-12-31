package com.ciwen.bloom.hash;

public class DJBHash<T> implements HashFunction<T> {
    @Override
    public int hash(Object item, int seed) {
        String string = item.toString();
        long hash = 5381L + seed;
        for (int i = 0; i < string.length(); i++) {
            hash = ((hash << 5) + hash) + string.charAt(i);
        }
        return (int) hash;
    }
}

