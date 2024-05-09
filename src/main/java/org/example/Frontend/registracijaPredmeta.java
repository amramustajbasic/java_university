package org.example.Frontend;

import org.example.Dao.*;
import org.example.Entity.*;
import org.example.Exceptions.UniException;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;


public class registracijaPredmeta {
    public JPanel registracijaPanel;
    private JButton izvrsiRegistracijuButton;
    private JButton nazadButton;
    private JComboBox comboBox1;
    private JList list1;
    private JList list2;
    private JTextField textFieldRok;
    private JButton odaberiButton;
    private JButton izbaciButton;
    private JButton zakljucajRegistracijuButton;
    private JRadioButton iGodinaRadioButton;
    private JRadioButton IIGodinaRadioButton;
    private JRadioButton IIIGodinaRadioButton;
    private JRadioButton IVGodinaRadioButton;

    private List<Enrollment> enrollmentList = new LinkedList<>();
    private List<Enrollment> lockedEnrollments = new LinkedList<>();

    private EnrollmentDaoSQL enrollmentDaoSQL = new EnrollmentDaoSQL("Enrollment");
    private SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
    private StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
    private EnrollmentPeriodDaoSQL enrollmentPeriodDaoSQL = new EnrollmentPeriodDaoSQL("EnrollmentPeriod");
    private TeacherDaoSQL teacherDaoSQL = new TeacherDaoSQL("Teacher");
    private GradeDaoSQL gradeDaoSQL = new GradeDaoSQL("Grade");
    private int ECTSljetni = 0;
    private int ECTSzimski = 0;




    public registracijaPredmeta(JFrame oldFrame, User user) throws UniException {

        List<Subject> ljetni = new LinkedList<>();
        List<Subject> zimski = new LinkedList<>();

        EnrollmentPeriod enrollmentPeriod = new EnrollmentPeriod();
        try {
            enrollmentPeriod = enrollmentPeriodDaoSQL.getLastPeriodAdded();
            System.out.println(enrollmentPeriod.toString());
        } catch (UniException e) {
            throw new RuntimeException(e);
        }


        if(enrollmentPeriodDaoSQL.getLastPeriodAdded().getEnd_period() == null){
            JOptionPane.showMessageDialog(oldFrame, "Prodekan mora unijeti rok za registraciju, stoga Vi jos uvijek ne mozete registrovati zeljene predmete", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            WindowCreator.create_window_student(oldFrame,user);
        }else {
            int ans = JOptionPane.showConfirmDialog(oldFrame, "Zelite li pogledati dan kada istice vas rok za registraciju?");

            if (ans == JOptionPane.YES_OPTION) {
                textFieldRok.setText(enrollmentPeriod.toString());
            } else if (ans == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(oldFrame, "OK", "OK", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        comboBox1.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
                Subject subject = new Subject();
                DefaultComboBoxModel<Subject> model = new DefaultComboBoxModel<>();

                List<Subject> subjects = new LinkedList<>();
                try {
                    subjects = subjectDaoSQL.getAll();
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }

                for (int i = 0; i < subjects.size(); i++) {
                    model.insertElementAt(subjects.get(i),i);
                }
                comboBox1.setModel(model);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {

            }
        });



        DefaultListModel<Subject> lista = new DefaultListModel<>();
        DefaultListModel<Subject> lista2 = new DefaultListModel<>();

        odaberiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Subject predmet = (Subject) comboBox1.getSelectedItem();

                Student student = new Student();
                student = studentDaoSQL.getStudentByUserId(user);


//                int godina = 0;
//                if(IIGodinaRadioButton.isSelected())    godina = 2;
//                else if(IIIGodinaRadioButton.isSelected())   godina = 3;
//                else if(IVGodinaRadioButton.isSelected())   godina = 4;


                for (int i = 0; i < ljetni.size(); i++) {
                    if (predmet.getId() == ljetni.get(i).getId()) {
                        JOptionPane.showMessageDialog(oldFrame, "Taj predmet ste vec unijeli", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                for (int i = 0; i < zimski.size(); i++) {
                    if (predmet.getId() == zimski.get(i).getId()) {
                        JOptionPane.showMessageDialog(oldFrame, "Taj predmet ste vec unijeli", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                ECTSzimski = 0;
                ECTSljetni = 0;
                for (int i = 0; i<ljetni.size();i++){
                    ECTSljetni+=ljetni.get(i).getEcts();
                }
                for (int i = 0; i<zimski.size();i++){
                    ECTSzimski+=zimski.get(i).getEcts();
                }

                if((ECTSljetni >= 30 && ECTSzimski >= 30)){
                    int ects = ECTSljetni+ECTSzimski;
                    JOptionPane.showMessageDialog(oldFrame, "Unijeli ste maksimalan broj predmeta za narednu akademsku godinu \n Broj ECTS bodova je "+ ects, "Upozorenje", JOptionPane.WARNING_MESSAGE);
                }
//                if (ECTSLjetni >= 30 && ECTSzimski >= 30) {
//                    int unos = (zimski.size() + ljetni.size()) + 1;
//                    String output = new String("Unijeli ste " + unos + " predmeta ");
//                    JOptionPane.showMessageDialog(oldFrame, output, "Informativno", JOptionPane.INFORMATION_MESSAGE);
//                }

                try {
                    if(studentDaoSQL.isEnrolled(predmet.getId(), student.getId()) && subjectDaoSQL.isPassed(student,predmet)){
                        JOptionPane.showMessageDialog(oldFrame, predmet.getName() + " ste polozili, ne mozete ga odabrati");
                        return;
                    }
                    if(studentDaoSQL.isEnrolled(predmet.getId(), student.getId()) && !(subjectDaoSQL.isPassed(student,predmet))){
                        JOptionPane.showMessageDialog(oldFrame, predmet.getName() + " niste polozili, morate ga odabrati");
                    }
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }


                if ((predmet.getSemester().equals("ljetni") || predmet.getSemester().equals("Ljetni")) && ECTSljetni <= 24) {
                    ljetni.add(predmet);
                }
                if ((predmet.getSemester().equals("zimski") || predmet.getSemester().equals("Zimski")) && ECTSzimski <= 24) {
                    zimski.add(predmet);
                }
                lista.clear();
                lista2.clear();

                for (int i = 0; i < ljetni.size(); i++) {
                    lista2.addElement(ljetni.get(i));
                }

                for (int i = 0; i < zimski.size(); i++)
                    lista.addElement(zimski.get(i));

                list1.setModel(lista);
                list2.setModel(lista2);

            }
        });
        izbaciButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Subject predmet = new Subject();
                Subject predmet2 = new Subject();

                Student student = new Student();
                student = studentDaoSQL.getStudentByUserId(user);

                List<Subject> nepolozeni = new LinkedList<>();
                try {
                    nepolozeni = subjectDaoSQL.getAllUnpassedByYear(student,student.getYearOfStudy());
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }

                System.out.println(nepolozeni);
                predmet = (Subject) list2.getSelectedValue();
                if (predmet != null) {
                    for (int i = 0; i < ljetni.size(); i++) {
                        if (predmet.getId() == ljetni.get(i).getId()) {
                            for(int j = 0;j<nepolozeni.size();j++) {
                                if(nepolozeni.get(j).getId() != predmet.getId() ) {
                                    ljetni.remove(predmet);
                                }else {
                                    JOptionPane.showMessageDialog(oldFrame,"Ne mozete izbaciti " + nepolozeni.get(j).getName()+ ", jer ga niste polozili");
                                    return;
                                }
                            }
                        }
                    }
                }


                predmet2 = (Subject) list1.getSelectedValue();
                if (predmet2 != null) {
                    for (int i = 0; i < zimski.size(); i++) {
                        if (predmet2.getId() == zimski.get(i).getId()) {
                            for(int j = 0;j<nepolozeni.size();j++) {
                                if(nepolozeni.get(j).getId() != predmet2.getId() ) {
                                    zimski.remove(predmet2);
                                }else {
                                    JOptionPane.showMessageDialog(oldFrame,"Ne mozete izbaciti " + nepolozeni.get(j).getName()+ ", jer ga niste polozili");
                                    return;
                                }
                            }
                        }
                    }
                }
                lista.clear();
                lista2.clear();

                for (int i = 0; i < ljetni.size(); i++)
                    lista.addElement(ljetni.get(i));

                for (int i = 0; i < zimski.size(); i++)
                    lista2.addElement(zimski.get(i));

                list1.setModel(lista2);
                list2.setModel(lista);

            }
        });
        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_student(oldFrame, user);
            }
        });

        izvrsiRegistracijuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                Enrollment enrollment = new Enrollment();

                Subject subject = new Subject();

                Student student = new Student();
                student = studentDaoSQL.getStudentByUserId(user);

                int nepolozeni = 0;
                int polozeni = 0;
                int odslusani = 0;



                // REGISTRACIJA ZAKLJUCANA
                try {
                    if (enrollmentDaoSQL.isLocked(student)) {
                        int ans = JOptionPane.showConfirmDialog(oldFrame, "Vi ste vec zakljucali svoju registraciju \n Zelite li prodekanu poslati zahtjev?", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                        if (ans == JOptionPane.YES_OPTION) {
                            WindowCreator.create_window_zahtjev_prodekanu(oldFrame, user);
                        }
                        return;
                    }
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }



                int godina = 0;
                if (iGodinaRadioButton.isSelected()) {
                    godina = 1;
                }
                if (IIGodinaRadioButton.isSelected()) {
                    godina = 2;
                }
                if (IIIGodinaRadioButton.isSelected()) {
                    godina = 3;
                }
                if (IVGodinaRadioButton.isSelected()) {
                    godina = 4;
                }


                //PROVJERA VALIDNOSTI PREDMETA

                List<Subject> izbaceniPredmeti = new LinkedList<>();

                if(ECTSljetni <= 30 && ECTSzimski <= 30){

                    Grade grade = new Grade();


                    Subject predmet2 = new Subject();

                    boolean polozen = false;
                    boolean odlusan = false;
                    boolean neispunjen = false;
                    boolean nepolozen = false;

                    List<Subject> preduslovi = new LinkedList<>();
                    Subject predmetPreduslov = new Subject();
                    String preduslovi_str = new String("");
                    String[] predusloviArray = null;


                    try {
                        for (int i = 0; i < ljetni.size(); i++) {
                            predmet2 = ljetni.get(i);
                            if (studentDaoSQL.isEnrolled(predmet2.getId(), student.getId()) && !(subjectDaoSQL.isPassed(student, predmet2))) {
                                JOptionPane.showMessageDialog(oldFrame, "Predmet " + predmet2.getName() + " morate odabrati");
                                enrollmentDaoSQL.updateEnrollmentStatus(student,predmet2.getId(),"Obnova");
                                nepolozeni++;
                                odlusan = true;
                                nepolozen = true;
                            }
                            if (studentDaoSQL.isEnrolled(predmet2.getId(), student.getId()) && subjectDaoSQL.isPassed(student, predmet2)) {
                                polozen = true;
                                odlusan = true;
                                izbaceniPredmeti.add(predmet2);
                                ljetni.remove(i);
                                polozeni++;
                            }

//                            if (!(studentDaoSQL.isEnrolled(predmet2.getId(), student.getId()))) {
//                                odlusan = false;
//                                polozen = false;
//                                nepolozen = true;
//                            }

                            if (!(studentDaoSQL.isEnrolled(predmet2.getId(),student.getId()))) {
                                preduslovi_str = predmet2.getPrerequisite_subject();
                                predusloviArray = preduslovi_str.split(", ");
                                if (preduslovi_str.length() > 5) {
                                    for (int j = 0; j < predusloviArray.length; j++) {
                                        predmetPreduslov = subjectDaoSQL.getSubjectByName(predusloviArray[j]);
                                        if(predmetPreduslov.getGodina() == predmet2.getGodina()){//OOP.2 == SP.2
                                            continue;
                                        }
                                        if (!(subjectDaoSQL.isPassed(student, predmetPreduslov))) {
                                            System.out.println("Nepolozen preduslov za " + predmet2.getName() + " je " + predusloviArray[j]);
                                            izbaceniPredmeti.add(predmet2);
                                            ljetni.remove(i);
                                            break;
                                        }
                                    }
                                    enrollmentDaoSQL.updateEnrollmentStatus(student,predmet2.getId(),"Prvi put");
                                }
                            }
                        }

                        polozen = false;
                        odlusan = false;
                        neispunjen = false;
                        nepolozen = false;

                        for (int i = 0; i < zimski.size(); i++) {
                            predmet2 = zimski.get(i);
                            if (studentDaoSQL.isEnrolled(predmet2.getId(), student.getId()) && !(subjectDaoSQL.isPassed(student, predmet2))) {
                                JOptionPane.showMessageDialog(oldFrame, "Predmet " + predmet2.getName() + " morate odabrati");
                                enrollmentDaoSQL.updateEnrollmentStatus(student,predmet2.getId(),"Obnova");
                                nepolozeni++;
                                odslusani++;
                                odlusan = true;
                                nepolozen = true;
                            }
                            if (studentDaoSQL.isEnrolled(predmet2.getId(), student.getId()) && subjectDaoSQL.isPassed(student, predmet2)) {
                                izbaceniPredmeti.add(predmet2);
                                zimski.remove(i);
                                polozeni++;
                                odslusani++;
                                polozen = true;
                                odlusan = true;
                            }

//                            if (!(studentDaoSQL.isEnrolled(predmet2.getId(), student.getId()))) {
//                                odlusan = false;
//                                polozen = false;
//                                nepolozen = true;
//                            }

                            preduslovi_str = predmet2.getPrerequisite_subject();
                            predusloviArray = preduslovi_str.split(", ");

                            if (!(studentDaoSQL.isEnrolled(predmet2.getId(),student.getId()))) {
                                if (preduslovi_str.length() > 5) {
                                    for (int j = 0; j < predusloviArray.length; j++) {
                                        predmetPreduslov = subjectDaoSQL.getSubjectByName(predusloviArray[j]);
                                        if(predmetPreduslov.getGodina() == predmet2.getGodina()){//OOP.2 == SP.2
                                            continue;
                                        }
                                        if (!(subjectDaoSQL.isPassed(student, predmetPreduslov))) {
                                            System.out.println("Nepolozen preduslov za " + predmet2.getName() + " je " + predusloviArray[j]);
                                            izbaceniPredmeti.add(predmet2);
                                            zimski.remove(i);
                                            break;
                                        }
                                    }
                                    enrollmentDaoSQL.updateEnrollmentStatus(student,predmet2.getId(),"Prvi put");
                                }
                            }
                        }

                        lista.clear();
                        lista2.clear();

                        for (int i = 0; i < ljetni.size(); i++) {
                            lista2.addElement(ljetni.get(i));
                        }

                        for (int i = 0; i < zimski.size(); i++)
                            lista.addElement(zimski.get(i));

                        list1.setModel(lista);
                        list2.setModel(lista2);

                    } catch (UniException e) {
                        throw new RuntimeException(e);
                    }

                }

                        int size = ljetni.size() + zimski.size();
                        int ans = JOptionPane.showConfirmDialog(oldFrame, "Odabrali ste " + size+ " predmeta." + "\n Zelite li sada zakljucati rezervaciju?");
                        if (ans == JOptionPane.YES_OPTION) {
                            try {
                                enrollmentDaoSQL.addLocked(student, 1);
                            } catch (UniException e) {
                                throw new RuntimeException(e);
                            }
                        }



                    Date end = null;
                    try {
                        end = enrollmentPeriodDaoSQL.getLastPeriodAdded().getEnd_period();
                    } catch (UniException e) {
                        throw new RuntimeException(e);
                    }

                    if (compareDates(end)) {
                        try {
                            enrollmentDaoSQL.addLocked(student, 1);
                            //lockedEnrollments = enrollmentDaoSQL.getAllLocked(student,true);
                        } catch (UniException e) {
                            throw new RuntimeException(e);
                        }

                        int prodekanZahtjev = JOptionPane.showConfirmDialog(oldFrame, "Rok istekao, mozete prodekanu poslati zahtjev \n Zelite li?", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                        if (prodekanZahtjev == JOptionPane.YES_OPTION) {
                            WindowCreator.create_window_zahtjev_prodekanu(oldFrame, user);
                            return;
                        }
                    }

                    try {
                        //enrollmentList = enrollmentDaoSQL.get10LastEnrollments(student.getId());
                        enrollmentList = enrollmentDaoSQL.getAllByStudent(student);
                    } catch (UniException e) {
                        throw new RuntimeException(e);
                    }
                    if (ECTSljetni <= 30 && ECTSzimski <= 30) {

                        if (enrollmentList.size() > 0) {
                            JOptionPane.showMessageDialog(oldFrame,"Vi ste vrsili registraciju prije, ali mozete mijenjati");
                            //enrollmentList.clear();
                            try {
                                enrollmentDaoSQL.deleteAllByStudent(student.getId());
                            } catch (UniException e) {
                                throw new RuntimeException(e);
                            }
                            for (int i = 0; i < ljetni.size(); i++) {
                                try {
                                    enrollment.setId(enrollmentDaoSQL.getMaxEnrollmentId());
                                    enrollment.setStudentId(student.getId());
                                    enrollment.setSubjectId(ljetni.get(i).getId());
                                    //enrollment.setStatus("Prvi put");
                                    enrollmentDaoSQL.addEnrollment(enrollment);
                                    enrollmentList.add(enrollment);
                                } catch (UniException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            for (int i = 0; i < zimski.size(); i++) {
                                try {
                                    enrollment.setId(enrollmentDaoSQL.getMaxEnrollmentId());
                                    enrollment.setStudentId(student.getId());
                                    enrollment.setSubjectId(zimski.get(i).getId());
                                    enrollmentDaoSQL.addEnrollment(enrollment);
                                    enrollmentList.add(enrollment);
                                } catch (UniException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }

                        if(enrollmentList.size() == 0){
                            JOptionPane.showMessageDialog(oldFrame, "Registrujete se prvi put");
                            for (int i = 0; i < ljetni.size(); i++) {
                                try {
                                    enrollment.setId(enrollmentDaoSQL.getMaxEnrollmentId());
                                    enrollment.setStudentId(student.getId());
                                    enrollment.setSubjectId(ljetni.get(i).getId());
                                    enrollmentDaoSQL.addEnrollment(enrollment);
                                    enrollmentList.add(enrollment);
                                } catch (UniException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            for (int i = 0; i < zimski.size(); i++) {
                                try {
                                    enrollment.setId(enrollmentDaoSQL.getMaxEnrollmentId());
                                    enrollment.setStudentId(student.getId());
                                    enrollment.setSubjectId(zimski.get(i).getId());
                                    enrollmentDaoSQL.addEnrollment(enrollment);
                                    enrollmentList.add(enrollment);
                                } catch (UniException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }


                if(!(izbaceniPredmeti.isEmpty())) {

                    String output = new String("");
                    for (int i = 0; i < izbaceniPredmeti.size(); i++) {
                        output += izbaceniPredmeti.get(i).getName() + "\n";
                        enrollmentDaoSQL.insertUnpassed(student,izbaceniPredmeti.get(i).getId());
                    }
                    JOptionPane.showMessageDialog(oldFrame,"Predmeti koje ste vi odabrali, ali ih niste u mogucnosti registrovati su: \n"+ output +
                            "Odgovornim nastavnicima za te predmete mozete poslati zahtjev za slusanje predmeta");
                    int saglasnost = JOptionPane.showConfirmDialog(oldFrame,"Zelite li poslati zahtjev nastavniku?");
                    if(saglasnost == JOptionPane.YES_OPTION){
                        WindowCreator.create_window_zahtjev_nastavniku(oldFrame,user);
                        return;
                    }
                }

                int prod = JOptionPane.showConfirmDialog(oldFrame,"Ukoliko ste pogrijesili pri registraciji ili promijenili odluku, a registraciju zakljucali, zahtjev mozete poslati prodekanu za predmete sa ispunjenim preduslovima");
                if(prod == JOptionPane.YES_OPTION){
                    WindowCreator.create_window_zahtjev_prodekanu(oldFrame,user);
                    return;
                }


                if(student.getYearOfStudy() == 4 && odslusani == 10){
                    studentDaoSQL.updateStudentStatus(student,"Apsolvent");
                    JOptionPane.showMessageDialog(oldFrame, "Vas status je apsolvent");
                }
                if(student.getYearOfStudy() == 4 && polozeni == 39){
                    JOptionPane.showMessageDialog(oldFrame, "Ostao Vam je samo diplomski rad");
                    JOptionPane.showMessageDialog(oldFrame, "Vama je ostao diplomski rad");

                }
                if(nepolozeni > 2){
                    studentDaoSQL.updateStudentStatus(student,"Obnova");
                    JOptionPane.showMessageDialog(oldFrame, "Vas status je Obnova");

                }else{
                    studentDaoSQL.updateStudentStatus(student,"Prvi put");
                    JOptionPane.showMessageDialog(oldFrame, "Vas status je Prvi put");
                }

            }

            });



        iGodinaRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                IIGodinaRadioButton.setSelected(false);
                IIIGodinaRadioButton.setSelected(false);
                IVGodinaRadioButton.setSelected(false);
                JOptionPane.showMessageDialog(oldFrame, "Vi se ne mozete registrovati na I godinu studija, jer se to vrsi automatski", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            }
        });


        IIGodinaRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int god = 2;
                iGodinaRadioButton.setSelected(false);
                IIIGodinaRadioButton.setSelected(false);
                IVGodinaRadioButton.setSelected(false);

                ljetni.clear();
                zimski.clear();

                Student student = new Student();
                StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
                student = studentDaoSQL.getStudentByUserId(user);
                if (student.getYearOfStudy() == 4 || student.getYearOfStudy() == 3) {
                    JOptionPane.showMessageDialog(oldFrame, "Vi ste student " + student.getYearOfStudy() + ". godine studija \n Ne mozete se registrovati na 2.godinu", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
                Subject predmet = new Subject();
                List<Subject> godina = null;

                try {
                    godina = subjectDaoSQL.getAllByYearOfStudy(2);
                } catch (UniException | SQLException e) {
                    throw new RuntimeException(e);
                }

                List<Subject> nepolozeni = new LinkedList<>();
                try {
                    nepolozeni = subjectDaoSQL.getAllUnpassedByYear(student,student.getYearOfStudy());
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }



                for(int i = 0; i<nepolozeni.size();i++) {
                    predmet = nepolozeni.get(i);
                    if ((predmet.getSemester().equals("ljetni") || predmet.getSemester().equals("Ljetni")) && ljetni.size() < 5) {
                        ljetni.add(predmet);
                    }
                    if ((predmet.getSemester().equals("zimski") || predmet.getSemester().equals("Zimski")) && zimski.size() < 5) {
                        zimski.add(predmet);
                    }
                }

//                for(int i = 0; i<godina.size();i++) {
//                    predmet = godina.get(i);
//                    if ((predmet.getSemester().equals("ljetni") || predmet.getSemester().equals("Ljetni")) && ljetni.size() < 5) {
//                        ljetni.add(predmet);
//                    }
//                    if ((predmet.getSemester().equals("zimski") || predmet.getSemester().equals("Zimski")) && zimski.size() < 5) {
//                        zimski.add(predmet);
//                    }
//                }
                list1.removeAll();
                list2.removeAll();
                lista.clear();
                lista2.clear();

                for (int i = 0; i < ljetni.size(); i++) {
                    lista2.addElement(ljetni.get(i));
                }

                for (int i = 0; i < zimski.size(); i++)
                    lista.addElement(zimski.get(i));

                //lista.addElement(subject);

                list1.setModel(lista);
                list2.setModel(lista2);

                JOptionPane.showMessageDialog(oldFrame, "Dobili ste predmete na godini za koju se zelite registrovati. \n" +
                        "Vi mozete izbaciti predmete koje ne zelite, a sistem moze izbaciti predmete za koje nemate ispunjene preduslove");
            }
        });
        IIIGodinaRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int god = 3;
                iGodinaRadioButton.setSelected(false);
                IIGodinaRadioButton.setSelected(false);
                IVGodinaRadioButton.setSelected(false);

                ljetni.clear();
                zimski.clear();

                Student student = new Student();
                StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
                student = studentDaoSQL.getStudentByUserId(user);
                if (student.getYearOfStudy() == 1 || student.getYearOfStudy() == 4) {
                    JOptionPane.showMessageDialog(oldFrame, "Vi ste student " + student.getYearOfStudy() + ". godine studija \n Ne mozete se registrovati na 3.godinu", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
                Subject predmet = new Subject();
                List<Subject> godina = null;
                try {
                    godina = subjectDaoSQL.getAllByYearOfStudy(3);
                } catch (UniException | SQLException e) {
                    throw new RuntimeException(e);
                }

                List<Subject> nepolozeni = new LinkedList<>();
                try {
                    nepolozeni = subjectDaoSQL.getAllUnpassedByYear(student,student.getYearOfStudy());
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }

                for(int i = 0; i<nepolozeni.size();i++) {
                    predmet = nepolozeni.get(i);
                    if ((predmet.getSemester().equals("ljetni") || predmet.getSemester().equals("Ljetni")) && ljetni.size() < 5) {
                        ljetni.add(predmet);
                    }
                    if ((predmet.getSemester().equals("zimski") || predmet.getSemester().equals("Zimski")) && zimski.size() < 5) {
                        zimski.add(predmet);
                    }
                }

//                for(int i = 0; i<godina.size();i++) {
//                    predmet = godina.get(i);
//                    if ((predmet.getSemester().equals("ljetni") || predmet.getSemester().equals("Ljetni")) && ljetni.size() < 5) {
//                        ljetni.add(predmet);
//                    }
//                    if ((predmet.getSemester().equals("zimski") || predmet.getSemester().equals("Zimski")) && zimski.size() < 5) {
//                        zimski.add(predmet);
//                    }
//                }
                list1.removeAll();
                list2.removeAll();
                lista.clear();
                lista2.clear();

                for (int i = 0; i < ljetni.size(); i++) {
                    lista2.addElement(ljetni.get(i));
                }

                for (int i = 0; i < zimski.size(); i++)
                    lista.addElement(zimski.get(i));

                //lista.addElement(subject);

                list1.setModel(lista);
                list2.setModel(lista2);

                JOptionPane.showMessageDialog(oldFrame, "Dobili ste predmete na godini za koju se zelite registrovati. \n" +
                        "Vi mozete izbaciti predmete koje ne zelite, a sistem moze izbaciti predmete za koje nemate ispunjene preduslove");

            }
        });
        IVGodinaRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int god = 4;
                iGodinaRadioButton.setSelected(false);
                IIIGodinaRadioButton.setSelected(false);
                IIGodinaRadioButton.setSelected(false);

                ljetni.clear();
                zimski.clear();

                Student student = new Student();
                StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
                student = studentDaoSQL.getStudentByUserId(user);
                if (student.getYearOfStudy() == 1 || student.getYearOfStudy() == 2) {
                    JOptionPane.showMessageDialog(oldFrame, "Vi ste student " + student.getYearOfStudy() + ". godine studija \n Ne mozete se registrovati na 4.godinu", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
                Subject predmet = new Subject();
                List<Subject> cetvrtaGodina = null;
                try {
                    cetvrtaGodina = subjectDaoSQL.getAllByYearOfStudy(4);
                } catch (UniException | SQLException e) {
                    throw new RuntimeException(e);
                }

                List<Subject> nepolozeni = new LinkedList<>();
                try {
                    nepolozeni = subjectDaoSQL.getAllUnpassedByYear(student,student.getYearOfStudy());
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }

                for(int i = 0; i<nepolozeni.size();i++) {
                    predmet = nepolozeni.get(i);
                    if ((predmet.getSemester().equals("ljetni") || predmet.getSemester().equals("Ljetni")) && ljetni.size() < 5) {
                        ljetni.add(predmet);
                    }
                    if ((predmet.getSemester().equals("zimski") || predmet.getSemester().equals("Zimski")) && zimski.size() < 5) {
                        zimski.add(predmet);
                    }
                }

//                for(int i = 0; i<cetvrtaGodina.size();i++) {
//                    predmet = cetvrtaGodina.get(i);
//                    if ((predmet.getSemester().equals("ljetni") || predmet.getSemester().equals("Ljetni")) && ljetni.size() < 5) {
//                        ljetni.add(predmet);
//                    }
//                    if ((predmet.getSemester().equals("zimski") || predmet.getSemester().equals("Zimski")) && zimski.size() < 5) {
//                        zimski.add(predmet);
//                    }
//                }
                list1.removeAll();
                list2.removeAll();
                lista.clear();
                lista2.clear();

                for (int i = 0; i < ljetni.size(); i++) {
                    lista2.addElement(ljetni.get(i));
                }

                for (int i = 0; i < zimski.size(); i++)
                    lista.addElement(zimski.get(i));

                //lista.addElement(subject);

                list1.setModel(lista);
                list2.setModel(lista2);

                JOptionPane.showMessageDialog(oldFrame, "Dobili ste predmete na godini za koju se zelite registrovati. \n" +
                        "Vi mozete izbaciti predmete koje ne zelite, a sistem moze izbaciti predmete za koje nemate ispunjene preduslove");

            }
        });

    }
    public boolean compareDates(Date date2) {
        Date currentDate = new Date();

        int m, d, y, m1, d1, y1;
        m = currentDate.getMonth();
        d = currentDate.getDay();
        y = currentDate.getYear();
        m1 = date2.getMonth();
        d1 = date2.getDay();
        y1 = date2.getYear();

        if (y == y1) {
            if (m > m1) {
                return true;
            } else if (m == m1) {
                if (d > d1) {
                    return true;
                } else
                    return false;
            } else
                return false;
        } else if (y > y1) {
            return true;
        }else
            return false;
    }
    }





