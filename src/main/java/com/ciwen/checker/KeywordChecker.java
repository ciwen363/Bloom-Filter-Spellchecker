package com.ciwen.checker;

import com.ciwen.bloom.BloomFilter;
import com.ciwen.bloom.SimpleBloomFilter;
import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*关键词检查的具体实现类*/
public class KeywordChecker {
    private final BloomFilter<String> bloomFilter;
    private final Set<String> knownKeywords;
    private final KeywordLoader keywordLoader;

    @Getter
    private int totalChecks = 0;
    @Getter
    private int falsePositives = 0;

    public KeywordChecker( double falsePositiveRate, boolean includeCppKeywords) {
        // 先创建KeywordLoader获取预期的关键字数量
        this.keywordLoader = new KeywordLoader(includeCppKeywords);
        int expectedKeywords = keywordLoader.getKeywordCount();

        // 使用预期的关键字数量创建布隆过滤器
        this.bloomFilter = new SimpleBloomFilter<>(expectedKeywords, falsePositiveRate);
        this.knownKeywords = new HashSet<>();

        // 初始化关键字
        initializeKeywords();
    }

    private void initializeKeywords() {
        Set<String> keywords = keywordLoader.getKeywords();
        for (String keyword : keywords) {// 统一转换为小写
            bloomFilter.add(keyword.toLowerCase()); // 添加到布隆过滤器
            knownKeywords.add(keyword.toLowerCase());// 同时保存到已知关键字集合中
        }
    }

    // 检查单词是否是关键字
    public boolean isKeyword(String word) {
        if (word == null || word.trim().isEmpty()) {
            return false;
        }
        totalChecks++;
        String normalizedWord = word.trim().toLowerCase();
        boolean mightBeKeyword = bloomFilter.mightContain(normalizedWord);

        // 检查是否为误判
        if (mightBeKeyword && !knownKeywords.contains(normalizedWord)) {
            falsePositives++;
        }

        return mightBeKeyword;
    }

    public void updateKeywords(boolean includeCppKeywords) {
        keywordLoader.setIncludeCppKeywords(includeCppKeywords);
        bloomFilter.clear();
        knownKeywords.clear();
        initializeKeywords();
    }

    public double getActualFalsePositiveRate() {
        return totalChecks > 0 ? (double) falsePositives / totalChecks : 0.0;
    }

    public Set<String> getKnownKeywords() {
        return Collections.unmodifiableSet(knownKeywords);
    }

    public double getTheoreticalFalsePositiveRate() {
        return bloomFilter.getFalsePositiveRate();
    }


    public void resetStats() {
        totalChecks = 0;
        falsePositives = 0;
    }
}