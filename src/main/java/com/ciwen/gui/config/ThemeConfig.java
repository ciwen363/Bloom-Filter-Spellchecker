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

    /**
     * 预定义的主题名称
     */
    public enum ThemeName {
        LIGHT("Light Theme"),
        DARK("Dark Theme"),
        SOLARIZED("Solarized"),
        MONOKAI("Monokai"),
        GITHUB("GitHub");

        private final String displayName;

        ThemeName(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
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
                config.setKeywordColor(Color.BLUE);
                config.setErrorColor(Color.RED);
                break;
            case DARK:
                config.setBackgroundColor(Color.web("#1E1E1E"));
                config.setTextColor(Color.web("#D4D4D4"));
                config.setKeywordColor(Color.web("#569CD6"));
                config.setErrorColor(Color.web("#F44747"));
                break;
            case SOLARIZED:
                config.setBackgroundColor(Color.web("#002B36"));
                config.setTextColor(Color.web("#839496"));
                config.setKeywordColor(Color.web("#268BD2"));
                config.setErrorColor(Color.web("#DC322F"));
                break;
            case MONOKAI:
                config.setBackgroundColor(Color.web("#272822"));
                config.setTextColor(Color.web("#F8F8F2"));
                config.setKeywordColor(Color.web("#66D9EF"));
                config.setErrorColor(Color.web("#F92672"));
                break;
            case GITHUB:
                config.setBackgroundColor(Color.web("#FFFFFF"));
                config.setTextColor(Color.web("#24292E"));
                config.setKeywordColor(Color.web("#D73A49"));
                config.setErrorColor(Color.web("#B31D28"));
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
