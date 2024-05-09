package org.example.Frontend;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.example.Entity.User;

public class nastavnikPocetna {
    public JPanel nastavnikPanel;
    private JButton zahtjeviButton;
    private JButton zahtjeviZaPrenosBodovaButton;
    private JButton odjavaButton;
    private JButton pregledPodatakaButton;
    private JButton pregledPodatakaZaNarednuButton;

    public nastavnikPocetna(JFrame oldFrame, User user) {
    odjavaButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
        WindowCreator.create_window_login(oldFrame);
        }
    });

        pregledPodatakaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_pregled_tekuca(oldFrame, user);
            }
        });
        zahtjeviButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_nastavnik_zahtjevi(oldFrame,user);
            }
        });
        zahtjeviZaPrenosBodovaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_nastavnik_zahtjevi_bodovi(oldFrame,user);
            }
        });
        pregledPodatakaZaNarednuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
            WindowCreator.create_window_pregled_podataka_naredna(oldFrame,user);
            }
        });
    }
}
