import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Controller implements Initializable {

    @FXML
    private Button btnBack;

    @FXML
    private Button btnForward;

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnSource;

    @FXML
    private Button btnZoomIn;

    @FXML
    private Button btnZoomOut;

    @FXML
    private TextField txtFieldUrl;

    @FXML
    private WebView webView;

    private WebEngine engine;
    private double size;

    @FXML
    void back(ActionEvent event) {
        engine.getHistory().go(-1);
    }

    @FXML
    void forward(ActionEvent event) {
        engine.getHistory().go(1);
    }

    @FXML
    void refresh(ActionEvent event) {
        engine.reload();
    }

    private void loadUrl() {
        String URL = txtFieldUrl.getText();
        if (!URL.contains(".")) {
            webView.getEngine().load("https://www.google.com/search?q=" + URL);
            return;
        }
        if (!URL.startsWith("http://") && !URL.startsWith("https://")) {
            URL = "https://" + URL;
        }
        webView.getEngine().load(URL);
    }

    @FXML
    void source(ActionEvent event) {
        showSource("test", (String) engine.executeScript("document.documentElement.outerHTML"));
    }

    private void showSource(String title, String source) {
        TextArea newRoot = new TextArea(source);
        Scene newScene = new Scene(newRoot, 600, 400);
        Stage newWindow = new Stage();
        newWindow.setTitle(title);
        newWindow.setScene(newScene);
        newWindow.show();
    }

    @FXML
    void goWeb(ActionEvent event) {
        loadUrl();
    }

    @FXML
    void zoomIn(ActionEvent event) {
        size += 0.25;
        if (size > 5)
            size = 5;
        webView.setZoom(size);
    }

    @FXML
    void zoomOut(ActionEvent event) {
        size -= 0.25;
        if (size < 0.25)
            size = 0.25;
        System.out.println(size);
        webView.setZoom(size);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        size = 1.00;
        engine = webView.getEngine();
        String homePage = "https://www.google.com";
        txtFieldUrl.setText(homePage);
        engine.locationProperty().addListener((ov, oldstr, newstr) -> {
            txtFieldUrl.setText(newstr);
        });
        webView.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.F12) {
                source(null);
            }
        });
        loadUrl();
    }
}
