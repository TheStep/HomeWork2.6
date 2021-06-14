package ru.geekbrains.home.chat.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    TextArea mainTextArea;
    @FXML
    TextField mainTextField;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            Thread chatThread = new Thread(() -> {
                    try {
                        while (true) {
                            String inputMessage = in.readUTF();
                            mainTextArea.appendText(inputMessage + "\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
    });
            chatThread.start();
        } catch (IOException e) {
            System.out.println("Сервер не отвечает....");
            System.exit(0);
        }
    }
    public void clickMeBtnAction() {
        if (!mainTextField.getText().trim().isEmpty()) {
            try {
                out.writeUTF(mainTextField.getText());
                mainTextField.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
