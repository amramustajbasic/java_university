package org.example;

import org.example.Frontend.WindowCreator;
import org.example.Frontend.login;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        WindowCreator.create_window_login(frame);

    }
}

