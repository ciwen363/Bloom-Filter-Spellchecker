package com.ciwen.gui.config;

import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

/**
 * 主题配置类，用于存储和管理主题相关的配置
 */
@Getter
@Setter
public class ThemeConfig {
    // 背景颜色
    private Color backgroundColor;
    // 文本颜色
    private Color textColor;
    // 关键字颜色
    private Color keywordColor;
    // 错误文本颜色
    private Color errorColor;
    // 关键字是否加粗
    private boolean keywordBold;
    // 错误文本是否加粗
    private boolean errorBold;
    // 用于控制语言
    private boolean isEnglish;
    /**
     * 预定义的主题名称
     */
    public enum ThemeName {
        LIGHT("Light Theme", "亮色主题"),
        DARK("Dark Theme", "暗色主题"),
        MONOKAI("Monokai", "Monokai主题");

        private final String englishName;
        private final String chineseName;

        ThemeName(String englishName, String chineseName) {
            this.englishName = englishName;
            this.chineseName = chineseName;
        }

        public String getDisplayName(boolean isEnglish) {
            return isEnglish ? englishName : chineseName;
        }
    }

    /**
     * 创建预定义主题配置
     * @param theme 主题名称
     * @return 主题配置对象
     */
    public static ThemeConfig createTheme(ThemeName theme) {
        ThemeConfig config = new ThemeConfig();
        switch (theme) {
            case LIGHT:
                config.setBackgroundColor(Color.WHITE);
                config.setTextColor(Color.BLACK);
                config.setKeywordColor(Color.CORNFLOWERBLUE);
                config.setErrorColor(Color.RED);
                break;
            case DARK:
                config.setBackgroundColor(Color.web("#1E1E1E"));
                config.setTextColor(Color.web("#D4D4D4"));
                config.setKeywordColor(Color.CORNFLOWERBLUE);
                config.setErrorColor(Color.RED);
                break;
            case MONOKAI:
                config.setBackgroundColor(Color.web("#272822"));
                config.setTextColor(Color.web("#F8F8F2"));
                config.setKeywordColor(Color.CORNFLOWERBLUE);
                config.setErrorColor(Color.RED);
                break;
        }
        return config;
    }

    /**
     * 将Color对象转换为CSS颜色字符串
     * @param color 颜色对象
     * @return CSS颜色字符串
     */
    public static String toRGBA(Color color) {
        return String.format("rgba(%d, %d, %d, %.2f)",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255),
                color.getOpacity());
    }
}
