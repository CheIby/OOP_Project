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
    void back() {
        if (engine.getHistory().getCurrentIndex() > 0) {
            engine.getHistory().go(-1);
        }
    }

    @FXML
    void forward() {
        if (engine.getHistory().getCurrentIndex() + 1 < engine.getHistory().getEntries().size()) {
            engine.getHistory().go(1);
        }
    }

    @FXML
    void refresh() {
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
    void source() {
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
    void zoomIn() {
        size += 0.1;
        if (size > 3)
            size = 3;
        webView.setZoom(size);
    }

    @FXML
    void zoomOut() {
        size -= 0.1;
        if (size < 0.25)
            size = 0.25;
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
                source();
            }
            if (event.isControlDown()) {
                if (event.getCode() == KeyCode.MINUS) {
                    zoomOut();
                } else if (event.getCode() == KeyCode.EQUALS) {
                    zoomIn();
                }
            }
            if (event.getCode() == KeyCode.F5) {
                refresh();
            }
            if (event.isAltDown()) {
                if (event.getCode() == KeyCode.HOME) {
                    engine.load(homePage);
                } else if (event.getCode() == KeyCode.LEFT) {
                    back();
                } else if (event.getCode() == KeyCode.RIGHT) {
                    forward();
                }
            }
        });
        loadUrl();
    }
}
