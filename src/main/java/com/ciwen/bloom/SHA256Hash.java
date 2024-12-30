package com.ciwen.bloom;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Hash<T> implements HashFunction<T> {
    @Override
    public int hash(Object item, int seed) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((seed + item.toString()).getBytes());
            byte[] bytes = md.digest();
            return ByteBuffer.wrap(bytes).getInt();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
