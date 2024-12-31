package com.ciwen.bloom.hash;

public class JenkinsHash<T> implements HashFunction<T> {
    @Override
    public int hash(Object item, int seed) {
        String string = item.toString();
        long hash = seed;
        for (int i = 0; i < string.length(); i++) {
            hash += string.charAt(i);
            hash += (hash << 10);
            hash ^= (hash >> 6);
        }
        hash += (hash << 3);
        hash ^= (hash >> 11);
        hash += (hash << 15);
        return (int) hash;
    }
}