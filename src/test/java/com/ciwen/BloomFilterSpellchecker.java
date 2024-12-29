package com.ciwen;

import com.ciwen.bloom.BloomFilter;
import com.ciwen.bloom.SimpleBloomFilter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BloomFilterSpellchecker {

   @Test
    public void testBloomFilter() {
        // 创建一个简单的布隆过滤器，位数组大小为 1000，哈希函数数量为 3
        /* BloomFilter bloomFilter = new SimpleBloomFilter(1000, 3);

        // 添加单词到布隆过滤器
        bloomFilter.add("hello");
        bloomFilter.add("world");

        // 检查单词是否存在
        assertTrue(bloomFilter.contains("hello"));
        assertTrue(bloomFilter.contains("world"));
        assertFalse(bloomFilter.contains("java"));*/
    }
}
