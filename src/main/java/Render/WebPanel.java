/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Render;

/**
 *
 * @author psira
 */
import java.awt.BorderLayout;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

import javax.swing.*;

public class WebPanel extends JPanel {
    private final JFXPanel jfxPanel = new JFXPanel();
    private WebView webView;

    public WebPanel() {
        Platform.runLater(() -> {
            webView = new WebView();
            jfxPanel.setScene(new Scene(webView));
            webView.getEngine().setJavaScriptEnabled(true);              

        });

        setLayout(new BorderLayout());
        add(jfxPanel, BorderLayout.CENTER);
    }

    public void loadURL(String url) {
        Platform.runLater(() -> webView.getEngine().load(url));
    }
}

