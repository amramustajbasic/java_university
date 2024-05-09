package org.example.Frontend;

import org.example.Entity.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentskiProfil extends JFrame{

    public JPanel studentPanel;
    private JButton registracijaButton;
    private JButton zahtjevProdekanuButton;
    private JButton zahtjevNastavnikuButton;
    private JButton pregledPredmetaButton;
    private JButton prenosBodovaButton;
    private JButton odjaviSeButton;
    private JButton pregledZahtjevaButton;
    private JTextField unesiNazivPredmetaTextField;

    public StudentskiProfil(JFrame oldFrame, User user){


        zahtjevProdekanuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_zahtjev_prodekanu(oldFrame,user);
            }
        });
        zahtjevNastavnikuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_zahtjev_nastavniku(oldFrame,user);
            }
        });
        pregledPredmetaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_predmet_info(oldFrame,user);
            }
        });
        prenosBodovaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_prenos_bodova(oldFrame,user);
            }
        });
        registracijaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                    WindowCreator.create_window_registracija(oldFrame,user);
            }
        });
        odjaviSeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_login(oldFrame);
            }
        });
        pregledZahtjevaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_pregled_zahtjeva(oldFrame,user);
            }
        });
    }

    }


