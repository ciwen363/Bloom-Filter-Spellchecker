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
        COURIER_NEW("Courier New"),
        CONSOLAS("Consolas"),
        MONACO("Monaco");
        /*SOURCE_CODE_PRO("Source Code Pro"),
        FIRA_CODE("Fira Code");*/

        private final String displayName;

        FontFamily(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
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