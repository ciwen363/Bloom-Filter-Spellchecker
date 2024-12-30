package com.ciwen.checker;

import java.util.*;

/*关键词加载的具体实现类*/
public class KeywordLoader {
    // C语言关键字
    private static final String[] C_KEYWORDS = {
            "auto", "break", "case", "char", "const", "continue", "default", "do",
            "double", "else", "enum", "extern", "float", "for", "goto", "if",
            "int", "long", "register", "return", "short", "signed", "sizeof", "static",
            "struct", "switch", "typedef", "union", "unsigned", "void", "volatile", "while"
    };

    // C++关键字（包括C++98/03, C++11, C++14, C++17, C++20）
    private static final String[] CPP_KEYWORDS = {
            // C++98/03
            "asm", "bool", "catch", "class", "const_cast", "delete", "dynamic_cast",
            "explicit", "export", "false", "friend", "inline", "mutable", "namespace",
            "new", "operator", "private", "protected", "public", "reinterpret_cast",
            "static_cast", "template", "this", "throw", "true", "try", "typeid",
            "typename", "using", "virtual", "wchar_t",

            // C++11
            "alignas", "alignof", "char16_t", "char32_t", "constexpr", "decltype",
            "noexcept", "nullptr", "static_assert", "thread_local",

            // C++14
            "deprecated",

            // C++17
            "fallthrough", "maybe_unused", "nodiscard",

            // C++20
            "concept", "consteval", "constinit", "co_await", "co_return", "co_yield",
            "requires", "module"
    };

    private Set<String> allKeywords;
    private boolean includeCppKeywords;

    public KeywordLoader(boolean includeCppKeywords) {
        this.includeCppKeywords = includeCppKeywords;
        loadKeywords();
    }

    private void loadKeywords() {
        allKeywords = new HashSet<>();

        // 添加C语言关键字
        Collections.addAll(allKeywords, C_KEYWORDS);

        // 根据设置决定是否添加C++关键字
        if (includeCppKeywords) {
            Collections.addAll(allKeywords, CPP_KEYWORDS);
        }
    }

    /**
     * 获取所有已加载的关键字
     * @return 关键字集合
     */
    public Set<String> getKeywords() {
        return Collections.unmodifiableSet(allKeywords);
    }

    /**
     * 检查一个词是否是关键字
     * @param word 要检查的词
     * @return 如果是关键字返回true，否则返回false
     */
    public boolean isKeyword(String word) {
        return allKeywords.contains(word.toLowerCase());
    }

    /**
     * 获取当前加载的关键字数量
     * @return 关键字数量
     */
    public int getKeywordCount() {
        return allKeywords.size();
    }

    /**
     * 判断是否包含C++关键字
     * @return 如果包含C++关键字返回true，否则返回false
     */
    public boolean isIncludingCppKeywords() {
        return includeCppKeywords;
    }

    /**
     * 切换是否包含C++关键字
     * @param include 是否包含C++关键字
     */
    public void setIncludeCppKeywords(boolean include) {
        if (this.includeCppKeywords != include) {
            this.includeCppKeywords = include;
            loadKeywords();  // 重新加载关键字
        }
    }

    /**
     * 获取仅C语言关键字数量
     * @return C语言关键字数量
     */
    public static int getCKeywordsCount() {
        return C_KEYWORDS.length;
    }

    /**
     * 获取C++特有关键字数量
     * @return C++特有关键字数量
     */
    public static int getCppOnlyKeywordsCount() {
        return CPP_KEYWORDS.length;
    }
}