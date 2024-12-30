package com.ciwen;

import com.ciwen.checker.KeywordChecker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class KeywordCheckerTest {
    private KeywordChecker checker;
    // 考虑到C/C++关键字总数，调整期望元素数量
    private static final int EXPECTED_ELEMENTS = 100; // 改为100以适应所有关键字
    private static final double FALSE_POSITIVE_RATE = 0.01; // 1%的误判率合适

    @BeforeEach
    public void setUp() {
        checker = new KeywordChecker( FALSE_POSITIVE_RATE, true);
        log.info("初始化 KeywordChecker，期望的元素数量: {}，假阳性率: {}", EXPECTED_ELEMENTS, FALSE_POSITIVE_RATE);
    }

    // 1. 功能性测试：关键词是否能被正确识别
    @Test
    public void testKeywordRecognition() {
        log.info("执行 testKeywordRecognition 测试...");
        boolean resultInt = checker.isKeyword("int");
        boolean resultWhile = checker.isKeyword("while");
        boolean resultReturn = checker.isKeyword("return");

        log.info("testKeywordRecognition 测试: 预期结果为 true，'int' 存在：{}，'while' 存在：{}，'return' 存在：{}",
                resultInt, resultWhile, resultReturn);

        assertTrue(resultInt);
        assertTrue(resultWhile);
        assertTrue(resultReturn);

        // 检查统计信息
        log.info("检查次数: {}, 误判次数: {}", checker.getTotalChecks(), checker.getFalsePositives());
    }

    // 2. 功能性测试：非关键词是否能被正确识别
    @Test
    public void testNonKeywordRecognition() {
        log.info("执行 testNonKeywordRecognition 测试...");
        boolean resultNotAKeyword = checker.isKeyword("notakeyword");
        boolean resultTesting = checker.isKeyword("testing");

        log.info("testNonKeywordRecognition 测试: 预期结果为 false，'notakeyword' 存在：{}，'testing' 存在：{}",
                resultNotAKeyword, resultTesting);

        assertFalse(resultNotAKeyword);
        assertFalse(resultTesting);

        // 检查误判率
        log.info("当前实际误判率: {}", checker.getActualFalsePositiveRate());
        log.info("理论误判率: {}", checker.getTheoreticalFalsePositiveRate());
    }

    // 3. 大小写敏感性测试
    @Test
    public void testCaseInsensitive() {
        log.info("执行 testCaseInsensitive 测试...");
        boolean resultIntUpperCase = checker.isKeyword("INT");
        boolean resultWhileMixedCase = checker.isKeyword("While");

        log.info("testCaseInsensitive 测试: 预期结果为 true，'INT' 存在：{}，'While' 存在：{}",
                resultIntUpperCase, resultWhileMixedCase);

        assertTrue(resultIntUpperCase);
        assertTrue(resultWhileMixedCase);
    }

    // 4. 测试C++特有关键字
    @Test
    public void testCppKeywords() {
        log.info("执行 testCppKeywords 测试...");
        boolean resultClass = checker.isKeyword("class");
        boolean resultTemplate = checker.isKeyword("template");

        log.info("testCppKeywords 测试: 预期结果为 true，'class' 存在：{}，'template' 存在：{}",
                resultClass, resultTemplate);

        assertTrue(resultClass);
        assertTrue(resultTemplate);

        // 切换到仅C关键字模式
        checker.updateKeywords(false);
        resultClass = checker.isKeyword("class");
        log.info("切换到仅C关键字后，'class' 存在：{}", resultClass);
        assertFalse(resultClass);
    }

    // 5. 边缘情况测试：空字符串、null等
    @Test
    public void testEdgeCases() {
        log.info("执行 testEdgeCases 测试...");
        // 测试空字符串
        assertFalse(checker.isKeyword(""));
        // 测试空格
        assertFalse(checker.isKeyword("   "));
        // 测试null
        assertFalse(checker.isKeyword(null));
        // 测试带空格的关键字
        assertTrue(checker.isKeyword("  int  "));
        log.info("边界情况测试完成");
    }

    // 6. 性能测试：大规模查询
    @Test
    public void testPerformance() {
        log.info("执行 testPerformance 测试...");
        long startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            checker.isKeyword("int");
            checker.isKeyword("notakeyword");
        }
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000.0; // 转换为毫秒
        log.info("性能测试: 20000次查询耗时 {} 毫秒", duration);
        assertTrue(duration < 1000); // 确保性能在合理范围内
    }

    // 7. 统计功能测试：检查和重置
    @Test
    public void testStatistics() {
        log.info("执行 testStatistics 测试...");

        // 记录初始统计数据
        int initialChecks = checker.getTotalChecks();
        int initialFalsePositives = checker.getFalsePositives();

        // 进行一系列检查
        for (int i = 0; i < 100; i++) {
            checker.isKeyword("test" + i);
        }

        // 验证统计数据的增长
        assertTrue(checker.getTotalChecks() > initialChecks);
        log.info("检查总数从 {} 增加到 {}", initialChecks, checker.getTotalChecks());
        // 验证误判数据的增长
        assertTrue(checker.getFalsePositives() >= initialFalsePositives);
        log.info("误判总数从 {} 增加到 {}", initialFalsePositives, checker.getFalsePositives());

        // 测试重置统计
        checker.resetStats();
        assertEquals(0, checker.getTotalChecks());
        assertEquals(0, checker.getFalsePositives());
        log.info("统计重置成功");
    }
}