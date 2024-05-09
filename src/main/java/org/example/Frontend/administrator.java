package org.example.Frontend;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class administrator {
    private JButton unosKorisnikaUSistemButton;
    private JButton unesiProdekanaButton;
    private JButton unesiPredmetButton;
    private JButton nazadButton;
    public JPanel admin_panel;
    private JButton unesiNastavnikaButton;
    private JButton pregledPodatakaButton;

    public administrator(JFrame oldFrame) {
    unosKorisnikaUSistemButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
        //WindowCreator.create_window_unesi_studenta(oldFrame);
        }
    });
    nazadButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
        WindowCreator.create_window_login(oldFrame);
        }
    });
        unesiPredmetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
            WindowCreator.create_window_unesi_predmet(oldFrame);

            }
        });
        unesiProdekanaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
            WindowCreator.create_window_unesi_prodekana(oldFrame);
            }
        });
        unosKorisnikaUSistemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_unesi_studenta(oldFrame);
            }
        });
        unesiNastavnikaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_unesi_nastavnika(oldFrame);
            }
        });
        pregledPodatakaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_pregled_podataka(oldFrame);
            }
        });
    }
}
