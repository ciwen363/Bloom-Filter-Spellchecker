package com.ciwen;

import com.ciwen.bloom.BloomFilter;
import com.ciwen.bloom.SimpleBloomFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class BloomFilterTest {
    private BloomFilter<String> bloomFilter;
    private static final int EXPECTED_ELEMENTS = 100;
    private static final double FALSE_POSITIVE_RATE = 0.01;

    @BeforeEach
    public void setUp() {
        log.info("初始化 BloomFilter，期望的元素数量: {}，假阳性率: {}", EXPECTED_ELEMENTS, FALSE_POSITIVE_RATE);
        bloomFilter = new SimpleBloomFilter<>(EXPECTED_ELEMENTS, FALSE_POSITIVE_RATE);
    }

    @Test
    public void testAdd() {
        log.info("执行 testAdd 测试...");
        bloomFilter.add("test");
        boolean result = bloomFilter.mightContain("test");
        log.info("testAdd 测试: 预期结果为 true，实际结果为 {}", result);
        assertTrue(result);

        // 测试添加多个元素
        bloomFilter.add("test2");
        bloomFilter.add("test3");
        log.info("添加多个元素后的大小: {}", bloomFilter.getCount());
        assertEquals(3, bloomFilter.getCount());
    }

    @Test
    public void testMightContain() {
        log.info("执行 testMightContain 测试...");
        bloomFilter.add("hello");
        boolean result = bloomFilter.mightContain("hello");
        log.info("testMightContain 测试: 预期结果为 true，实际结果为 {}", result);
        assertTrue(result);

        result = bloomFilter.mightContain("world");
        log.info("testMightContain 测试: 预期结果为 false，实际结果为 {}", result);
        assertFalse(result);

        // 测试误判率
        log.info("当前布隆过滤器的误判率: {}", bloomFilter.getFalsePositiveRate());
        assertTrue(bloomFilter.getFalsePositiveRate() <= FALSE_POSITIVE_RATE);
    }

    @Test
    public void testClear() {
        log.info("执行 testClear 测试...");
        bloomFilter.add("test");
        bloomFilter.clear();
        boolean result = bloomFilter.mightContain("test");
        log.info("testClear 测试: 预期结果为 false，实际结果为 {}", result);
        assertFalse(result);
        assertEquals(0, bloomFilter.getCount());
    }

    @Test
    public void testAddNull() {
        log.info("执行 testAddNull 测试...");
        assertThrows(IllegalArgumentException.class, () -> {
            bloomFilter.add(null);
            log.info("testAddNull 测试: 应该抛出异常，输入为 null");
        });
    }

    @Test
    public void testSize() {
        log.info("执行 testSize 测试...");
        int size = bloomFilter.getSize();
        log.info("布隆过滤器的大小: {}", size);
        assertTrue(size > 0);
    }
}