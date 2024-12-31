package com.ciwen.gui.config;

import lombok.Getter;
import lombok.Setter;

/**
 * 样式配置类，用于存储和管理字体相关的配置
 */
@Getter
@Setter
public class StyleConfig {
    private String fontFamily;
    private int fontSize;
    private boolean keywordBold;
    private boolean errorBold;

    /**
     * 预定义字体族
     */
    public enum FontFamily {
        COURIER_NEW("Courier New", "新宋体"),
        CONSOLAS("Consolas", "等线"),
        MONACO("Monaco", "黑体");

        private final String englishName;
        private final String chineseName;

        FontFamily(String englishName, String chineseName) {
            this.englishName = englishName;
            this.chineseName = chineseName;
        }

        @Override
        public String toString() {
            // 获取MainController的isEnglish值
            boolean isEnglish = true; // 需要通过某种方式获取语言设置
            return isEnglish ? englishName : chineseName;
        }
    }

    /**
     * 创建默认样式配置
     * @return 样式配置对象
     */
    public static StyleConfig createDefault() {
        StyleConfig config = new StyleConfig();
        config.setFontFamily(FontFamily.COURIER_NEW.toString());
        config.setFontSize(14);
        config.setKeywordBold(true);
        config.setErrorBold(false);
        return config;
    }
}