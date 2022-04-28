import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Controller implements Initializable {

    @FXML
    private Button btnBack;

    @FXML
    private Button btnForward;

    @FXML
    private Button btnRefresh;

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
    void goWeb(ActionEvent event) {
        loadUrl();
    }

    @FXML
    void zoomIn(ActionEvent event) {
        size += 0.25;
        webView.setZoom(size);
    }

    @FXML
    void zoomOut(ActionEvent event) {
        size -= 0.25;
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
        loadUrl();
    }

}
