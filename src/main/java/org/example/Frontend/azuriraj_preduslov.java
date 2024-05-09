package org.example.Frontend;

import org.example.Dao.SubjectDaoSQL;
import org.example.Entity.Subject;
import org.example.Entity.User;
import org.example.Exceptions.UniException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class azuriraj_preduslov {
    public JPanel panel_azuriraj_preduslov;
    private JButton potvrdiButton;
    private JButton nazadButton;
    private JTextField textField1;
    private JList<Subject> list1;
    private JComboBox comboBox1;
    private JButton ucitajPredmeteButton;
    private JButton dodajButton;

    private JButton izbaciButton;
    private JList list2;
    private List<Subject> preduslovi = new LinkedList<>();
    private Subject oldChoice = new Subject();

    public azuriraj_preduslov(JFrame oldFrame, User user) {

        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_prodekan(oldFrame,user);
            }
        });

        potvrdiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //preduslovi.clear();

                Subject subject = new Subject();
                SubjectDaoSQL sub = new SubjectDaoSQL("Subject");
                subject = list1.getSelectedValue();
                String preduslov = new String("");

                if(subject == null) {
                    JOptionPane.showMessageDialog(null, "Niste odabrali predmet", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }


                if(preduslovi.size() == 1){
                    preduslov = preduslov.concat(preduslovi.get(0).getName());
                }
                if(preduslovi.size()>1) {
                    for (int i = 0; i < preduslovi.size(); i++) {
                        if(i == preduslovi.size()-1){
                            preduslov = preduslov.concat(preduslovi.get(i).getName());
                        }else {
                            preduslov = preduslov.concat(preduslovi.get(i).getName() + ", ");
                        }
                        }
                }

                if(preduslov.isEmpty()){
                    int ans = JOptionPane.showConfirmDialog(oldFrame, "Ovaj predmet nema preduslova, zelite li potvrditi?","Informativno",JOptionPane.INFORMATION_MESSAGE);
                    if(ans == JOptionPane.YES_OPTION){
                        int id = subject.getId();
                        sub.updateSubject_preduslov(subject,preduslov,id);
                        JOptionPane.showMessageDialog(oldFrame, "Uspjesno azuriran/i predulov/i "+ preduslov + " za predmet " + subject.getName());
                        preduslovi.clear();
                        preduslov = "";
                        return;
                    }
                    if(ans == JOptionPane.NO_OPTION){
                        JOptionPane.showMessageDialog(oldFrame, "Ukoliko ne zelite potvrditi, morate odabrati preduslov", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }else {
                    int id = subject.getId();
                    sub.updateSubject_preduslov(subject, preduslov, id);

                    JOptionPane.showMessageDialog(oldFrame, "Uspjesno azuriran/i preduslov/i " + preduslov + " za predmet " + subject.getName());
                    preduslovi.clear();
                    preduslov = "";
                }
                DefaultListModel<Subject> model = new DefaultListModel<>();
                list2.setModel(model);
            }
        });

        ucitajPredmeteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
                comboBox1.setModel(model);

                if(preduslovi.size()>0) {
                    preduslovi.clear();
                }

                List<Subject> lista = new LinkedList<>();
                SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
                try {
                    lista = subjectDaoSQL.getAll();
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                DefaultListModel<Subject> lista_model = new DefaultListModel<>();
                for (int i = 0; i < lista.size(); i++) {
                    lista_model.addElement(lista.get(i));
                }
                list1.setModel(lista_model);
            }
        });

        comboBox1.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
                DefaultComboBoxModel<Subject> model = new DefaultComboBoxModel<>();
                List<Subject> list = new ArrayList<>();
                SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
                try {
                    list = subjectDaoSQL.getAll();
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < list.size(); i++) {
                    model.insertElementAt(list.get(i),i);
                }
                comboBox1.setModel(model);

                System.out.println(preduslovi);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {

            }


        });




        dodajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                Subject subject = new Subject();
                SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
                subject = (Subject) list1.getSelectedValue(); // Predmet za koji se postavljaju preduslovi

                if(subject == null){
                    JOptionPane.showMessageDialog(oldFrame,"Niste odabrali predmet za koji zelite postaviti preduslov");
                    return;
                }

                if(oldChoice != null){
                    if(!(oldChoice.getId() == subject.getId())){
                        if(preduslovi.size()>0) {
                            preduslovi.clear();
                        }
                    }
                }
                oldChoice = subject;

                DefaultListModel<Subject> listaModel = new DefaultListModel<>();

                Subject subjectOdabran = new Subject();
                subjectOdabran = (Subject) comboBox1.getSelectedItem(); // predmet koji ce biti preduslov

                if(subjectOdabran == null){
                    int ans = JOptionPane.showConfirmDialog(oldFrame,"Zelite da predmet " + subject.getName() + " nema preduslova?\nAko zelite, odaberite opciju Yes i kliknite na potrvrdi.");
                    if(ans == JOptionPane.YES_OPTION){
                        if(preduslovi.size()>0) {
                            preduslovi.clear();
                        }
                        return;
                    }else
                        return;
                }

                Subject predmetPreduslov = new Subject();

                String preduslovi_str = new String("");
                //String[] predusloviArray = null;

                preduslovi_str = subject.getPrerequisite_subject();
                String [] predusloviArray = preduslovi_str.split(", ");

//                for(int i = 0;i<predusloviArray.length;i++){
//                    preduslovi.add(subjectDaoSQL.getSubjectByName(predusloviArray[i]));
//                }


                for(int i = 0; i < preduslovi.size(); i++){
                    if(preduslovi.get(i).getId() == subjectOdabran.getId()){
                        JOptionPane.showMessageDialog(oldFrame,"Vec ste odabrali predmet " + subjectOdabran.getName());
                        return;
                    }
                }

                if(subjectOdabran.getId() == subject.getId()){
                    JOptionPane.showMessageDialog(oldFrame,"Ne mozete predmet "+ subject.getName() + " postaviti kao preduslov za isti");
                    return;
                }

                for(int i = 0; i< predusloviArray.length;i++) {
                    predmetPreduslov = subjectDaoSQL.getSubjectByName(predusloviArray[i]); //predmet koji je vec preduslov za predmet subject
                    if(predmetPreduslov.getId() == subjectOdabran.getId()){
                        JOptionPane.showMessageDialog(oldFrame,"Odabrani predmet je vec preduslov za "+ subject.getName());
                        return;
                    }
                }

                if(predusloviArray.length > 0) {
                    for (int i = 0; i < predusloviArray.length; i++) {
                        preduslovi.add(subjectDaoSQL.getSubjectByName(predusloviArray[i]));
                    }
                }

                preduslovi.add(subjectOdabran);

                for (int i = 0; i< preduslovi.size(); i++){
                    listaModel.addElement(preduslovi.get(i));
                }

                list2.setModel(listaModel);

            }
        });
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                DefaultListModel<Subject> model = new DefaultListModel<>();
                //list2.removeAll();
                //list2.setModel(model);



                Subject subject = (Subject) list1.getSelectedValue();
                SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");

                if(subject == null) return;

                if(preduslovi.size()>0) {
                        preduslovi.clear();
                }

                String preduslovi_str = new String("");
                String[] predusloviArray = null;

                preduslovi_str = subject.getPrerequisite_subject();
                predusloviArray = preduslovi_str.split(", ");

                if(preduslovi_str.isEmpty()){
                    list2.setModel(model);
                    return;
                }

                List<Subject> ListPreduslovi = new LinkedList<>();

                if(preduslovi_str.isEmpty()) return;

                for(int i = 0; i<predusloviArray.length;i++){
                    preduslovi.add(subjectDaoSQL.getSubjectByName(predusloviArray[i]));
                }
                for (int i = 0; i<preduslovi.size(); i++)
                    model.addElement(preduslovi.get(i));

                list2.removeAll();
                list2.setModel(model);


            }
        });
        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                Subject subject = new Subject();
                SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
                String predmetText = textField1.getText();


                DefaultListModel<Subject> list = new DefaultListModel<>();
                List<Subject> listaPredmeta = subjectDaoSQL.getSubjectsBySearch(predmetText);

                if(listaPredmeta.size()>0){
                    for(int i = 0; i<listaPredmeta.size(); i++){
                        list.addElement(listaPredmeta.get(i));
                    }
                }
                if(listaPredmeta.size() == 0){
                    JOptionPane.showMessageDialog(oldFrame,"Ne postoji u sistemu predmet sa unesenim nazivom");
                }

                list1.setModel(list);
            }
        });
        izbaciButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                Subject subject = new Subject();
                SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
                subject = (Subject) list2.getSelectedValue(); // Predmet za koji se postavljaju preduslovi

                DefaultListModel<Subject> listaModel = new DefaultListModel<>();

                if (subject != null) {
                    for (int i = 0; i < preduslovi.size(); i++) {
                        if (subject.getId() == preduslovi.get(i).getId()) {
                            preduslovi.remove(i);
                        }
                    }
                }
               listaModel.clear();


                for (int i = 0; i<preduslovi.size();i++){
                    listaModel.addElement(preduslovi.get(i));
                }

                list2.setModel(listaModel);


            }
        });
    }





}
