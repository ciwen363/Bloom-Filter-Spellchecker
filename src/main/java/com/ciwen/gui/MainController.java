package com.ciwen.gui;

import com.ciwen.checker.KeywordChecker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*JavaFX 控制器类*/
public class MainController implements Initializable {
    @FXML
    private TextArea codeArea;
    @FXML
    private WebView resultView;

    private KeywordChecker checker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 创建检查器，设置1%的误判率，并包含C++关键字
        checker = new KeywordChecker(0.01, true);

        codeArea.addEventHandler(KeyEvent.KEY_RELEASED, event -> updateHighlighting());
        codeArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 14px;");
    }

    private void updateHighlighting() {
        String text = codeArea.getText();
        StringBuilder html = new StringBuilder("<html><body style='font-family: Courier New; font-size: 14px;'>");

        Pattern wordPattern = Pattern.compile("\\b\\w+\\b");
        Matcher matcher = wordPattern.matcher(text);

        int lastIndex = 0;
        while (matcher.find()) {
            String word = matcher.group();
            html.append(text.substring(lastIndex, matcher.start()));

            if (checker.isKeyword(word)) {
                html.append("<span style='color: blue; font-weight: bold;'>")
                        .append(word)
                        .append("</span>");
            } else {
                html.append(word);
            }

            lastIndex = matcher.end();
        }

        html.append(text.substring(lastIndex));
        html.append("</body></html>");

        resultView.getEngine().loadContent(html.toString());
    }
}
