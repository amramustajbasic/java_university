package org.example.Frontend;

import org.example.Entity.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class prodekan {
    public JPanel panel_prodekan;
    private JButton azurirajZvanjeNastavnikaButton;
    private JButton periodUpisaButton;
    private JButton azurirajPredusloveZaPredmeteButton;
    private JButton zahtjeviButton;
    private JButton planRealizacijeNastaveButton;
    private JButton prijavljeniStudentiButton;
    private JButton odjavaButton;

    public prodekan(JFrame oldFrame, User user) {
    azurirajZvanjeNastavnikaButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            WindowCreator.create_window_zvanje(oldFrame,user);
        }
    });
        odjavaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
               WindowCreator.create_window_login(oldFrame);

            }
        });
        azurirajPredusloveZaPredmeteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_azuriraj_preduslov(oldFrame,user);
            }
        });
        planRealizacijeNastaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
WindowCreator.create_plan_nastave(oldFrame,user);
            }
        });
        periodUpisaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_period_upisa(oldFrame,user);
            }
        });
        zahtjeviButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_zahtjevi(oldFrame, user);
            }
        });
        prijavljeniStudentiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
WindowCreator.create_prijavljeni_stud(oldFrame,user);
            }
        });
    }
}

