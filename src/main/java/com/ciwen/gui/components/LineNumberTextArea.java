
package com.ciwen.gui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * 带行号的文本编辑器组件
 */
public class LineNumberTextArea extends HBox {
    private final TextArea textArea;
    private final VBox lineNumbers;
    private final ScrollPane lineNumbersScroll;

    public LineNumberTextArea() {
        // 创建行号容器
        lineNumbers = new VBox();
        lineNumbers.setAlignment(Pos.TOP_RIGHT);
        lineNumbers.setStyle("-fx-background-color: #eee; -fx-padding: 5 5 5 5;");

        // 创建行号滚动面板
        lineNumbersScroll = new ScrollPane(lineNumbers);
        lineNumbersScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        lineNumbersScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        lineNumbersScroll.setStyle("-fx-background-color: transparent;");
        lineNumbersScroll.setPrefWidth(40);

        // 创建文本编辑区
        textArea = new TextArea();
        textArea.setWrapText(true);
        HBox.setHgrow(textArea, Priority.ALWAYS);

        // 设置行号样式
        lineNumbers.setStyle("-fx-background-color: #eee; -fx-padding: 5 5 5 5;");

        // 监听文本变化，更新行号
        textArea.textProperty().addListener((obs, oldText, newText) -> updateLineNumbers());
        textArea.scrollTopProperty().addListener((obs, oldVal, newVal) ->
                lineNumbersScroll.setVvalue(newVal.doubleValue() / textArea.getScrollTop()));

        // 组装组件
        getChildren().addAll(lineNumbersScroll, textArea);

        // 初始化第一行行号
        updateLineNumbers();
    }

    /**
     * 更新行号显示
     */
    private void updateLineNumbers() {
        lineNumbers.getChildren().clear();
        String[] lines = textArea.getText().split("\n", -1);
        for (int i = 0; i < lines.length; i++) {
            Label lineNum = new Label(String.format("%d", i + 1));
            lineNum.setStyle("-fx-text-fill: #666; -fx-font-family: monospace;");
            lineNumbers.getChildren().add(lineNum);
        }
    }

    /**
     * 获取文本编辑器组件
     */
    public TextArea getTextArea() {
        return textArea;
    }

    /**
     * 设置文本内容
     */
    public void setText(String text) {
        textArea.setText(text);
    }

    /**
     * 获取文本内容
     */
    public String getText() {
        return textArea.getText();
    }

    /**
     * 设置样式
     */
    public void setTextAreaStyle(String style) {
        textArea.setStyle(style);
    }

    /**
     * 设置提示文本
     */
    public void setPromptText(String text) {
        textArea.setPromptText(text);
    }

    /**
     * 清除文本按钮
     */
    public void clear() {
        textArea.clear();
    }
}