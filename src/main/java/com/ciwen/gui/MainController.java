package com.ciwen.gui;

import com.ciwen.checker.KeywordChecker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController implements Initializable {
    @FXML private TextArea codeArea;
    @FXML private WebView resultView;
    @FXML private CheckBox includeCppKeywordsCheckbox;
    @FXML private Label statisticsLabel;
    @FXML private Button resetStatsButton;
    @FXML private Button clearTextButton;
    @FXML private ToggleButton darkModeToggle;

    private KeywordChecker checker;
    private final SimpleStringProperty statusText = new SimpleStringProperty("");
    private final SimpleDoubleProperty falsePositiveRate = new SimpleDoubleProperty(0.01);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checker = new KeywordChecker(falsePositiveRate.get(), true);

        // 初始化UI组件
        setupUIComponents();

        // 设置事件处理器
        setupEventHandlers();

        // 初始化统计信息显示
        updateStatistics();
    }

    private void setupUIComponents() {
        includeCppKeywordsCheckbox.setSelected(true);
        codeArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 14px;");
        statisticsLabel.textProperty().bind(statusText);
    }

    private void setupEventHandlers() {
        codeArea.addEventHandler(KeyEvent.KEY_RELEASED, event -> updateHighlighting());

        includeCppKeywordsCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            checker.updateKeywords(newVal);
            updateHighlighting();
            updateStatistics();
        });

        resetStatsButton.setOnAction(event -> {
            checker.resetStats();
            updateStatistics();
        });

        clearTextButton.setOnAction(event -> {
            codeArea.clear();
            updateHighlighting();
        });

        darkModeToggle.selectedProperty().addListener((obs, oldVal, newVal) ->
                updateTheme(newVal));
    }

    private void updateHighlighting() {
        String text = codeArea.getText();
        StringBuilder html = new StringBuilder(
                "<html><body style='font-family: Courier New; font-size: 14px; margin: 10px;'>");

        Pattern wordPattern = Pattern.compile("\\b\\w+\\b");
        Matcher matcher = wordPattern.matcher(text);

        int lastIndex = 0;
        while (matcher.find()) {
            String word = matcher.group();
            html.append(escapeHtml(text.substring(lastIndex, matcher.start())));

            if (checker.isKeyword(word)) {
                html.append("<span style='color: #0066cc; font-weight: bold;'>")
                        .append(escapeHtml(word))
                        .append("</span>");
            } else if (!word.matches("\\d+") && !isCommonWord(word)) {
                // 非关键字且不是数字和常用词的单词标为红色
                html.append("<span style='color: #cc0000;'>")
                        .append(escapeHtml(word))
                        .append("</span>");
            } else {
                html.append(escapeHtml(word));
            }

            lastIndex = matcher.end();
        }

        html.append(escapeHtml(text.substring(lastIndex)));
        html.append("</body></html>");

        resultView.getEngine().loadContent(html.toString());
        updateStatistics();
    }

    private boolean isCommonWord(String word) {
        // 添加一些常用词或标识符，避免全部标红
        return word.length() <= 2 || word.matches("^[ijk]$") ||
                word.matches("^(tmp|str|num|val|var|ptr)\\d*$");
    }

    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\n", "<br>")
                .replace(" ", "&nbsp;");
    }

    private void updateStatistics() {
        String stats = String.format(
                "Total Checks: %d | False Positives: %d | FP Rate: %.2f%% | Keywords: %d",
                checker.getTotalChecks(),
                checker.getFalsePositives(),
                checker.getActualFalsePositiveRate() * 100,
                checker.getKnownKeywords().size()
        );
        statusText.set(stats);
    }

    private void updateTheme(boolean isDark) {
        String theme = isDark ? "dark" : "light";
        String baseStyle = String.format(
                "body { background-color: %s; color: %s; }",
                isDark ? "#1e1e1e" : "#ffffff",
                isDark ? "#d4d4d4" : "#000000"
        );

        resultView.getEngine().setUserStyleSheetLocation(
                String.format("data:text/css;charset=utf-8," +
                                "body{background:%s;color:%s}",
                        isDark ? "#1e1e1e" : "#ffffff",
                        isDark ? "#d4d4d4" : "#000000")
        );

        codeArea.setStyle(String.format(
                "-fx-font-family: 'Courier New'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-color: %s; " +
                        "-fx-text-fill: %s;",
                isDark ? "#1e1e1e" : "#ffffff",
                isDark ? "#d4d4d4" : "#000000"
        ));
    }
}