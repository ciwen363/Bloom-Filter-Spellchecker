package com.ciwen.gui;

import com.ciwen.checker.KeywordChecker;
import com.ciwen.gui.components.LineNumberTextArea;
import com.ciwen.gui.config.StyleConfig;
import com.ciwen.gui.config.ThemeConfig;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JavaFX主控制器类
 * 负责处理用户界面交互和显示逻辑
 */
public class MainController implements Initializable {
    // FXML注入的UI组件
    @FXML private LineNumberTextArea codeArea;
    @FXML private WebView resultView;
    @FXML private ComboBox<ThemeConfig.ThemeName> themeComboBox;
    @FXML private ComboBox<StyleConfig.FontFamily> fontFamilyComboBox;
    @FXML private ComboBox<Integer> fontSizeComboBox;
    @FXML private CheckBox keywordBoldCheckBox;
    @FXML private CheckBox errorBoldCheckBox;
    @FXML private CheckBox includeCppKeywordsCheckbox;
    @FXML private Label statisticsLabel;
    @FXML private Button resetStatsButton;
    @FXML private Button clearTextButton;
    @FXML private ToggleButton languageToggle;

    // 业务逻辑组件
    private KeywordChecker checker;
    private ThemeConfig currentTheme;
    private StyleConfig currentStyle;
    private boolean isEnglish = true;
    // 属性
    private final SimpleStringProperty statusText = new SimpleStringProperty("");
    private final SimpleDoubleProperty falsePositiveRate = new SimpleDoubleProperty(0.01);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 初始化检查器
        checker = new KeywordChecker(falsePositiveRate.get(), true);

        // 修改代码区域样式设置
        codeArea.setTextAreaStyle("-fx-font-family: 'Courier New'; -fx-font-size: 14px; -fx-background-color: white; -fx-border-color: #ddd;");

        // 初始化UI组件
        initializeComponents();

        // 初始化主题和样式
        initializeThemeAndStyle();

        // 设置事件处理器
        setupEventHandlers();

        // 初始化统计信息
        updateStatistics();
    }

    /**
     * 初始化UI组件
     */
    private void initializeComponents() {
        // 配置统计标签
        statisticsLabel.textProperty().bind(statusText);

        // 设置C++关键字选项默认值
        includeCppKeywordsCheckbox.setSelected(true);

        // 设置左侧输入框样式为固定的白色背景
        codeArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 14px; -fx-background-color: white; -fx-border-color: #ddd;");

        // 初始化语言切换按钮
        languageToggle.setSelected(isEnglish);
        updateLanguageText();
        /*// 设置代码区域初始样式
        codeArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 14px;");*/

        // 设置语言切换按钮的初始状态
        languageToggle.setSelected(isEnglish);
        updateLanguageText();
        updateUILanguage();
    }

    /**
     * 更新UI语言
     */
    private void updateUILanguage() {
        if (isEnglish) {
            // 按钮和复选框文本
            keywordBoldCheckBox.setText("Bold Keywords");
            errorBoldCheckBox.setText("Bold Errors");
            includeCppKeywordsCheckbox.setText("Include C++ Keywords");
            resetStatsButton.setText("Reset Statistics");
            clearTextButton.setText("Clear Text");
            codeArea.setPromptText("Enter your code here...");

            // 主题相关标签
            ((Label)themeComboBox.getParent().getChildrenUnmodifiable().stream()
                    .filter(node -> node instanceof Label)
                    .findFirst()
                    .get()).setText("Theme:");
            ((Label)fontFamilyComboBox.getParent().getChildrenUnmodifiable().stream()
                    .filter(node -> node instanceof Label)
                    .findFirst()
                    .get()).setText("Font:");
            ((Label)fontSizeComboBox.getParent().getChildrenUnmodifiable().stream()
                    .filter(node -> node instanceof Label)
                    .findFirst()
                    .get()).setText("Size:");

        } else {
            // 按钮和复选框文本
            keywordBoldCheckBox.setText("关键字加粗");
            errorBoldCheckBox.setText("错误单词加粗");
            includeCppKeywordsCheckbox.setText("包含C++关键字");
            resetStatsButton.setText("重置统计");
            clearTextButton.setText("清空文本");
            codeArea.setPromptText("在此输入代码...");

            // 主题相关标签
            ((Label)themeComboBox.getParent().getChildrenUnmodifiable().stream()
                    .filter(node -> node instanceof Label)
                    .findFirst()
                    .get()).setText("主题：");
            ((Label)fontFamilyComboBox.getParent().getChildrenUnmodifiable().stream()
                    .filter(node -> node instanceof Label)
                    .findFirst()
                    .get()).setText("字体：");
            ((Label)fontSizeComboBox.getParent().getChildrenUnmodifiable().stream()
                    .filter(node -> node instanceof Label)
                    .findFirst()
                    .get()).setText("大小：");
        }
    }

    /**
     * 初始化主题和样式配置
     */
    private void initializeThemeAndStyle() {
        // 设置主题选项
        themeComboBox.getItems().setAll(ThemeConfig.ThemeName.values());
        themeComboBox.setValue(ThemeConfig.ThemeName.LIGHT);
        updateComboBoxItems();

        // 设置字体选项
        fontFamilyComboBox.getItems().addAll(StyleConfig.FontFamily.values());
        fontFamilyComboBox.setValue(StyleConfig.FontFamily.COURIER_NEW);

        // 设置字体大小选项
        fontSizeComboBox.getItems().addAll(12, 14, 16, 18, 20, 22, 24);
        fontSizeComboBox.setValue(14);

        // 设置默认粗体选项
        keywordBoldCheckBox.setSelected(true);
        errorBoldCheckBox.setSelected(false);

        // 初始化配置对象
        currentTheme = ThemeConfig.createTheme(ThemeConfig.ThemeName.LIGHT);
        currentStyle = StyleConfig.createDefault();
    }

    /**
     * 设置事件处理器
     */
    private void setupEventHandlers() {
        // 代码区域变化监听
        codeArea.addEventHandler(KeyEvent.KEY_RELEASED, event -> updateHighlighting());

        // 主题切换监听
        themeComboBox.setOnAction(event -> {
            currentTheme = ThemeConfig.createTheme(themeComboBox.getValue());
            updateHighlighting(); // 只更新右侧显示
        });

        // 修改语言切换事件处理
        languageToggle.setOnAction(event -> {
            isEnglish = languageToggle.isSelected();
            updateLanguageText();
            updateUILanguage();
            updateComboBoxItems();
            updateStatistics(); // 更新统计信息的语言
        });

        // 字体设置监听
        fontFamilyComboBox.setOnAction(event -> {
            currentStyle.setFontFamily(fontFamilyComboBox.getValue().toString());
            updateDisplay();
        });

        // 字体大小监听
        fontSizeComboBox.setOnAction(event -> {
            currentStyle.setFontSize(fontSizeComboBox.getValue());
            updateDisplay();
        });

        // 粗体设置监听
        keywordBoldCheckBox.setOnAction(event -> {
            currentStyle.setKeywordBold(keywordBoldCheckBox.isSelected());
            updateDisplay();
        });

        errorBoldCheckBox.setOnAction(event -> {
            currentStyle.setErrorBold(errorBoldCheckBox.isSelected());
            updateDisplay();
        });

        // C++关键字包含选项监听
        includeCppKeywordsCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            checker.updateKeywords(newVal);
            updateHighlighting();
            updateStatistics();
        });

        // 重置统计按钮
        resetStatsButton.setOnAction(event -> {
            checker.resetStats();
            updateStatistics();
        });

        // 清除文本按钮
        clearTextButton.setOnAction(event -> {
            codeArea.clear();
            updateHighlighting();
        });
    }

    /**
     * 更新显示效果
     */
    private void updateDisplay() {
        updateCodeAreaStyle();
        updateHighlighting();
    }

    /**
     * 更新代码区域样式
     */
    private void updateCodeAreaStyle() {
        String style = String.format(
                "-fx-font-family: '%s'; " +
                        "-fx-font-size: %dpx; " +
                        "-fx-background-color: %s; " +
                        "-fx-text-fill: %s;",
                currentStyle.getFontFamily(),
                currentStyle.getFontSize(),
                ThemeConfig.toRGBA(currentTheme.getBackgroundColor()),
                ThemeConfig.toRGBA(currentTheme.getTextColor())
        );
        codeArea.setTextAreaStyle(style);
    }

    /**
     * 更新高亮显示
     */
    private void updateHighlighting() {
        String text = codeArea.getText();
        StringBuilder html = new StringBuilder();

        // 设置HTML文档样式
        html.append(String.format("""
            <html><head><style>
            body {
                font-family: %s;
                font-size: %dpx;
                background-color: %s;
                color: %s;
                margin: 0;
                padding: 0;
                display: flex;
            }
            .line-numbers {
                background-color: #f0f0f0;
                padding: 10px;
                text-align: right;
                border-right: 1px solid #ddd;
                color: #666;
                font-family: monospace;
                min-width: 30px;
                margin-right: 10px;
            }
            .code-content {
                flex: 1;
                padding: 10px;
                white-space: pre-wrap;
                word-wrap: break-word;
            }
            .keyword { color: %s; font-weight: %s; }
            .error { color: %s; font-weight: %s; }
            </style></head><body>
            """,
                currentStyle.getFontFamily(),
                currentStyle.getFontSize(),
                ThemeConfig.toRGBA(currentTheme.getBackgroundColor()),
                ThemeConfig.toRGBA(currentTheme.getTextColor()),
                ThemeConfig.toRGBA(currentTheme.getKeywordColor()),
                currentStyle.isKeywordBold() ? "bold" : "normal",
                ThemeConfig.toRGBA(currentTheme.getErrorColor()),
                currentStyle.isErrorBold() ? "bold" : "normal"
        ));

        // 添加行号容器
        html.append("<div class='line-numbers'>");
        String[] lines = text.split("\n", -1);
        for (int i = 1; i <= lines.length; i++) {
            html.append(i).append("<br>");
        }
        html.append("</div><div class='code-content'>");

        // 处理文本内容
        Pattern wordPattern = Pattern.compile("\\b\\w+\\b");
        Matcher matcher = wordPattern.matcher(text);
        int lastIndex = 0;

        while (matcher.find()) {
            // 添加匹配词之前的文本
            html.append(escapeHtml(text.substring(lastIndex, matcher.start())));

            String word = matcher.group();
            // 这里会更新统计信息
            boolean isKeyword = checker.isKeyword(word);

            if (isKeyword) {
                // 关键字高亮
                html.append("<span class='keyword'>")
                        .append(escapeHtml(word))
                        .append("</span>");
            } else if (!word.matches("\\d+") && !isCommonWord(word)) {
                // 可能的错误单词高亮，非关键字且不是数字和常用词的情况
                html.append("<span class='error'>")
                        .append(escapeHtml(word))
                        .append("</span>");
            } else {
                // 普通文本
                html.append(escapeHtml(word));
            }

            lastIndex = matcher.end();
        }

        // 添加剩余文本
        html.append(escapeHtml(text.substring(lastIndex)));
        html.append("</div></body></html>");

        // 更新WebView内容
        resultView.getEngine().loadContent(html.toString());
        updateStatistics();

        // 更新统计信息
        updateStatistics();
        updateStatistics();
    }

    /**
     * 中英文标签映射
     */
    private final String[][] labels = {
            {"English", "中文"},
            {"Theme:", "主题:"},
            {"Font:", "字体:"},
            {"Size:", "大小:"},
            {"Bold Keywords", "关键字加粗"},
            {"Bold Errors", "错误加粗"},
            {"Include C++ Keywords", "包含C++关键字"},
            {"Reset Statistics", "重置统计"},
            {"Clear Text", "清除文本"}
    };

    /**
     * 更新语言显示
     */
    private void updateLanguageText() {
        boolean isEnglish = languageToggle.isSelected();

        // 更新所有标签文本
        for (Node node : ((HBox)themeComboBox.getParent()).getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                for (String[] pair : labels) {
                    if (label.getText().equals(pair[0]) || label.getText().equals(pair[1])) {
                        label.setText(isEnglish ? pair[0] : pair[1]);
                        break;
                    }
                }
            }
        }

        // 更新按钮和复选框文本
        languageToggle.setText(isEnglish ? "English" : "中文");
        resetStatsButton.setText(isEnglish ? "Reset Statistics" : "重置统计");
        clearTextButton.setText(isEnglish ? "Clear Text" : "清除文本");
        keywordBoldCheckBox.setText(isEnglish ? "Bold Keywords" : "关键字加粗");
        errorBoldCheckBox.setText(isEnglish ? "Bold Errors" : "错误加粗");
        includeCppKeywordsCheckbox.setText(isEnglish ? "Include C++ Keywords" : "包含C++关键字");

        // 刷新下拉框显示
        updateComboBoxItems();
    }

    /**
     * 更新下拉框显示
     */
    private void updateComboBoxItems() {
        // 保存当前选中值
        ThemeConfig.ThemeName currentTheme = themeComboBox.getValue();

        // 更新主题下拉框
        themeComboBox.getItems().clear();
        for (ThemeConfig.ThemeName theme : ThemeConfig.ThemeName.values()) {
            themeComboBox.getItems().add(theme);
        }
        themeComboBox.setValue(currentTheme);

        // 更新字体族名称
        fontFamilyComboBox.getItems().clear();
        for (StyleConfig.FontFamily family : StyleConfig.FontFamily.values()) {
            fontFamilyComboBox.getItems().add(family);
        }
        fontFamilyComboBox.setValue(StyleConfig.FontFamily.COURIER_NEW);
    }

    /**
     * 更新统计信息
     */
    private void updateStatistics() {
        String statsFormat = isEnglish ?
                "Total Checks: %d | False Positives: %d | False Positive Rate: %.2f%% | Keywords: %d" :
                "总检查数: %d | 误判数: %d | 误判率: %.2f%% | 关键字数: %d";

        int totalChecks = checker.getTotalChecks();
        int falsePositives = checker.getFalsePositives();
        double rate = totalChecks > 0 ?
                (double) falsePositives / totalChecks * 100 : 0.0;

        String stats = String.format(statsFormat,
                totalChecks,
                falsePositives,
                rate,
                checker.getKnownKeywords().size()
        );
        statusText.set(stats);
    }

    /**
     * 检查是否为常用词
     */
    private boolean isCommonWord(String word) {
        return word.length() <= 2 ||
                word.matches("^[ijk]$") ||
                word.matches("^(tmp|str|num|val|var|ptr)\\d*$");
    }

    /**
     * HTML特殊字符转义
     */
    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\n", "<br>")
                .replace(" ", "&nbsp;")
                .replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
    }
}