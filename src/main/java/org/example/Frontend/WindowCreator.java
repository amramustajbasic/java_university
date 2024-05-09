package org.example.Frontend;

import org.example.Entity.User;
import org.example.Exceptions.UniException;

import javax.swing.*;

public class WindowCreator {
    public static void create_window_prodekan(JFrame oldFrame,User user) {
        // Add the code from the NazadActionListener here
        oldFrame.getContentPane().removeAll();
        prodekan prod = new prodekan(oldFrame, user);
        oldFrame.setContentPane(prod.panel_prodekan);
        oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        oldFrame.pack();
        oldFrame.setVisible(true);
    }
    public static void create_window_unesi_predmet(JFrame oldFrame) {
        // Add the code from the NazadActionListener here
        oldFrame.getContentPane().removeAll();
        unesi_predmet unesiPredmet = new unesi_predmet(oldFrame);
        oldFrame.setContentPane(unesiPredmet.panel_unesi_predmet);
        oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        oldFrame.pack();
        oldFrame.setVisible(true);
    }
    public static void create_window_unesi_studenta(JFrame oldFrame) {
        // Add the code from the NazadActionListener here
        oldFrame.getContentPane().removeAll();
       unesi_studenta unesiStudenta = new unesi_studenta(oldFrame);
        oldFrame.setContentPane(unesiStudenta.unesi_studenta);
        oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        oldFrame.pack();
        oldFrame.setVisible(true);
    }
    public static void create_window_unesi_nastavnika(JFrame oldFrame) {
        // Add the code from the NazadActionListener here
        oldFrame.getContentPane().removeAll();
        unesi_nastavnika unesiNastavnika = new unesi_nastavnika(oldFrame);
        oldFrame.setContentPane(unesiNastavnika.unesi_nastavnika);
        oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        oldFrame.pack();
        oldFrame.setVisible(true);
    }
    public static void create_window_unesi_prodekana(JFrame oldFrame) {
        // Add the code from the NazadActionListener here
        oldFrame.getContentPane().removeAll();
        unesi_prodekana unesiProdekana = new unesi_prodekana(oldFrame);
        oldFrame.setContentPane(unesiProdekana.panel_unesi_prodekana);
        oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        oldFrame.pack();
        oldFrame.setVisible(true);
    }
    public static void create_window_admin(JFrame oldFrame) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            administrator admin = new administrator(oldFrame);
            oldFrame.setContentPane(admin.admin_panel);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }
    public static void create_window_pregled_podataka (JFrame oldFrame) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
          pregled_podataka pregledPodataka = new pregled_podataka(oldFrame);
            oldFrame.setContentPane(pregledPodataka.pregled_podataka);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }
    public static void create_window_login(JFrame oldFrame) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            login loginForm = new login(oldFrame);
            oldFrame.setContentPane(loginForm.panel_login);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });

    }
    public static void create_window_zvanje(JFrame oldFrame,User user){
        SwingUtilities.invokeLater(() -> {
                    oldFrame.getContentPane().removeAll();
                    zvanje_nast azurirajZvanje = new zvanje_nast(oldFrame, user);
                    oldFrame.setContentPane(azurirajZvanje.panel_zvanje);
                    oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    oldFrame.pack();
                    oldFrame.setVisible(true);
                }
        );
    }
    public static void create_azuriraj_preduslov(JFrame oldFrame, User user){
        SwingUtilities.invokeLater(() -> {
                    oldFrame.getContentPane().removeAll();
                    azuriraj_preduslov preduslov = new azuriraj_preduslov(oldFrame,user);
                    oldFrame.setContentPane(preduslov.panel_azuriraj_preduslov);
                    oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    oldFrame.pack();
                    oldFrame.setVisible(true);
                }
        );
    }
    public static void create_plan_nastave(JFrame oldFrame,User user){
        SwingUtilities.invokeLater(() -> {
                    oldFrame.getContentPane().removeAll();
                    plan_nastave planNastave = new plan_nastave(oldFrame, user);
                    oldFrame.setContentPane(planNastave.panel_plan_nastave);
                    oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    oldFrame.pack();
                    oldFrame.setVisible(true);
                }
        );
    }
    public static void create_period_upisa(JFrame oldFrame,User user){
        SwingUtilities.invokeLater(() -> {
                    oldFrame.getContentPane().removeAll();
                    period_upisa period_upisa = new period_upisa(oldFrame, user);
                    oldFrame.setContentPane(period_upisa.panel_period_upisa);
                    oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    oldFrame.pack();
                    oldFrame.setVisible(true);
                }
        );
    }
    public static void create_window_nastavnik(JFrame oldFrame, User user){
        SwingUtilities.invokeLater(() -> {
                    oldFrame.getContentPane().removeAll();
                    nastavnikPocetna nastavnikPocetna = new nastavnikPocetna(oldFrame, user);
                    oldFrame.setContentPane(nastavnikPocetna.nastavnikPanel);
                    oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    oldFrame.pack();
                    oldFrame.setVisible(true);
                }
        );
    }
    public static void create_window_nastavnik_zahtjevi(JFrame oldFrame,User user){
        SwingUtilities.invokeLater(() -> {
                    oldFrame.getContentPane().removeAll();
                   zahtjevi_nastavnik zahtjeviNastavnik = new zahtjevi_nastavnik(oldFrame,user);
                    oldFrame.setContentPane(zahtjeviNastavnik.panel_zahtj_nastavniku);
                    oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    oldFrame.pack();
                    oldFrame.setVisible(true);
                }
        );
    }
    public static void create_window_nastavnik_zahtjevi_bodovi(JFrame oldFrame,User user){
        SwingUtilities.invokeLater(() -> {
                    oldFrame.getContentPane().removeAll();
                    zahtjevi_nastavniku_prenos_bodova zahtjeviNastavnikuPrenosBodova = new zahtjevi_nastavniku_prenos_bodova(oldFrame,user);
                    oldFrame.setContentPane(zahtjeviNastavnikuPrenosBodova.panel_zah_bod_nas);
                    oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    oldFrame.pack();
                    oldFrame.setVisible(true);
                }
        );
    }
    public static void create_window_pregled_podataka_naredna(JFrame oldFrame,User user){
        SwingUtilities.invokeLater(() -> {
                    oldFrame.getContentPane().removeAll();
                   tekucaSpisakStudenata tekuca_Spisak_Studenata = new tekucaSpisakStudenata(oldFrame,user);
                    oldFrame.setContentPane(tekuca_Spisak_Studenata.panel_studenti_naredna);
                    oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    oldFrame.pack();
                    oldFrame.setVisible(true);
                }
        );
    }
    public static void create_window_pregled_tekuca(JFrame oldFrame, User user){
        SwingUtilities.invokeLater(() -> {
                    oldFrame.getContentPane().removeAll();
                    tekucaSpisakPredmeta tekucaSpisakPredmeta = new tekucaSpisakPredmeta(oldFrame,user);
                    oldFrame.setContentPane(tekucaSpisakPredmeta.panel_tekuca_spisak);
                    oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    oldFrame.pack();
                    oldFrame.setVisible(true);
                }
        );
    }

    public static void create_window_pregled_zahtjeva(JFrame oldFrame, User user){
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            PregledZahtjeva pregledZahtjeva = new PregledZahtjeva(oldFrame,user);
            oldFrame.setContentPane(pregledZahtjeva.panel_zahtjevi);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }

    public static void create_zahtjevi(JFrame oldFrame, User user){
        SwingUtilities.invokeLater(() -> {
                    oldFrame.getContentPane().removeAll();
                    zahtjevi zahtjevi = new zahtjevi(oldFrame,user);
                    oldFrame.setContentPane(zahtjevi.panel_zahtjevi);
                    oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    oldFrame.pack();
                    oldFrame.setVisible(true);
                }
        );
    }

    public static void create_prijavljeni_stud(JFrame oldFrame,User user){
        SwingUtilities.invokeLater(() -> {
                    oldFrame.getContentPane().removeAll();
                    prijavljeni_stud prijavljeniStud = new prijavljeni_stud(oldFrame,user);
                    oldFrame.setContentPane(prijavljeniStud.panel_prijavljeni_stud);
                    oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    oldFrame.pack();
                    oldFrame.setVisible(true);
                }
        );
    }

    public static void create_window_student(JFrame oldFrame, User user){

        SwingUtilities.invokeLater(() -> {
                    oldFrame.getContentPane().removeAll();
                    StudentskiProfil studentskiProfil = new StudentskiProfil(oldFrame,user);
                    oldFrame.setContentPane(studentskiProfil.studentPanel);
                    oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    oldFrame.pack();
                    oldFrame.setVisible(true);
                }
        );
    }

        public static void create_window_registracija (JFrame oldFrame, User user){

            SwingUtilities.invokeLater(() -> {
                        oldFrame.getContentPane().removeAll();
                registracijaPredmeta registracijaPredmeta = null;
                try {
                    registracijaPredmeta = new registracijaPredmeta(oldFrame,user);
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                oldFrame.setContentPane(registracijaPredmeta.registracijaPanel);
                        oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        oldFrame.pack();
                        oldFrame.setVisible(true);
                    }
            );
        }


    public static void create_window_zahtjev_nastavniku (JFrame oldFrame,User user){

        SwingUtilities.invokeLater(() -> {
                    oldFrame.getContentPane().removeAll();
                    ZahtjevNastavniku zahtjevNastavniku = new ZahtjevNastavniku(oldFrame, user);
                    oldFrame.setContentPane(zahtjevNastavniku.panelZahtjevNastavniku);
                    oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    oldFrame.pack();
                    oldFrame.setVisible(true);
                }
        );
    }

    public static void create_window_predmet_info (JFrame oldFrame, User user){

        SwingUtilities.invokeLater(() -> {
                    oldFrame.getContentPane().removeAll();
                    predmetInfo predmetInfo = new predmetInfo(oldFrame,user);
                    oldFrame.setContentPane(predmetInfo.panel_predmet_info);
                    oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    oldFrame.pack();
                    oldFrame.setVisible(true);
                }
        );
    }


    public static void create_window_prenos_bodova (JFrame oldFrame, User user){

        SwingUtilities.invokeLater(() -> {
                    oldFrame.getContentPane().removeAll();
                    PrenosBodova prenosBodova = new PrenosBodova(oldFrame,user);
                    oldFrame.setContentPane(prenosBodova.panel_prenos_bodova);
                    oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    oldFrame.pack();
                    oldFrame.setVisible(true);
                }
        );
    }

    public static void create_window_zahtjev_prodekanu(JFrame oldFrame, User user){

        SwingUtilities.invokeLater(() -> {
                    oldFrame.getContentPane().removeAll();
                    AzuriranjePredmeta azuriranjePredmeta = new AzuriranjePredmeta(oldFrame,user);
                    oldFrame.setContentPane(azuriranjePredmeta.panel_zahtjev_prodekanu);
                    oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    oldFrame.pack();
                    oldFrame.setVisible(true);
                }
        );
    }



    }

//    public static void create_zahtjevi(JFrame oldFrame){
//        SwingUtilities.invokeLater(() -> {
//                    oldFrame.getContentPane().removeAll();
//                    zahtjevi zahtjev = new zahtjevi(oldFrame);
//                    oldFrame.setContentPane(zahtjev.panel_zahtjev);
//                    oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                    oldFrame.pack();
//                    oldFrame.setVisible(true);
//                }
//        );
//    }
//    public static void create_period_upisa(JFrame oldFrame){
//        SwingUtilities.invokeLater(() -> {
//                    oldFrame.getContentPane().removeAll();
//                    period_upisa periodUpisa = new period_upisa(oldFrame);
//                    oldFrame.setContentPane(periodUpisa.panel_period_upisa);
//                    oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                    oldFrame.pack();
//                    oldFrame.setVisible(true);
//                }
//        );
//    }
