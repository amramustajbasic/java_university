package org.example.Frontend;

import org.example.Dao.EnrollmentPeriodDaoSQL;
import org.example.Entity.EnrollmentPeriod;
import org.example.Entity.User;
import org.example.Exceptions.UniException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

public class period_upisa {
    public JPanel panel_period_upisa;
    private JSpinner spinner1;
    private JSpinner spinner2;
    private JSpinner spinner3;
    private JSpinner spinner4;
    private JSpinner spinner5;
    private JSpinner spinner6;
    private JButton potvrdiButton;
    private JButton nazadButton;
    private JTextField textField1;

    public period_upisa(JFrame oldFrame, User user) {

        EnrollmentPeriodDaoSQL enrollmentPeriodDaoSQL = new EnrollmentPeriodDaoSQL("EnrollmentPeriod");
        EnrollmentPeriod enrollmentPeriod = new EnrollmentPeriod();
        try {
            enrollmentPeriod = enrollmentPeriodDaoSQL.getLastPeriodAdded();
            if(enrollmentPeriodDaoSQL.isOnlyOne()) {
                textField1.setText(enrollmentPeriod.toString());
            }
        } catch (UniException e) {
            throw new RuntimeException(e);
        }



        SpinnerModel dayModel = new SpinnerNumberModel(1, 1, 31, 1);
        SpinnerModel dayModel1 = new SpinnerNumberModel(1, 1, 31, 1);

        SpinnerModel monthModel = new SpinnerNumberModel(1, 1, 12, 1);
        SpinnerModel monthModel1 = new SpinnerNumberModel(1, 1, 12, 1);


        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        SpinnerModel yearModel = new SpinnerNumberModel(2023, 2023, currentYear, 1);
        SpinnerModel yearModel1 = new SpinnerNumberModel(2023, 2023, currentYear, 1);

        spinner1.setModel(dayModel);
        spinner2.setModel(monthModel);
        spinner3.setModel(yearModel);

        spinner4.setModel(dayModel1);
        spinner5.setModel(monthModel1);
        spinner6.setModel(yearModel1);

//        Dimension spinnerSize = new Dimension(50, 30);
//        spinner1.setPreferredSize(spinnerSize);
//        spinner2.setPreferredSize(spinnerSize);
//        spinner3.setPreferredSize(spinnerSize);
//        spinner4.setPreferredSize(spinnerSize);
//        spinner5.setPreferredSize(spinnerSize);
//        spinner6.setPreferredSize(spinnerSize);
        potvrdiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                EnrollmentPeriodDaoSQL enrollmentPeriodDaoSQL = new EnrollmentPeriodDaoSQL("EnrollmentPeriod");
                enrollmentPeriodDaoSQL.deleteAll();


                int beginDay, beginMonth, beginYear;
                int endDay, endMonth, endYear;

                beginDay = (int)spinner1.getValue();
                beginMonth = (int) spinner2.getValue();
                 beginYear = (int) spinner3.getValue();

                 Calendar calendarBegin = Calendar.getInstance();
                 calendarBegin.set(beginYear,beginMonth-1,beginDay);
                 Date dateBegin = calendarBegin.getTime();

                 endDay = (int) spinner4.getValue();
                 endMonth = (int) spinner5.getValue();
                 endYear = (int) spinner6.getValue();

                 Calendar calendarEnd = Calendar.getInstance();
                 calendarEnd.set(endYear,endMonth-1,endDay);
                 Date dateEnd = calendarEnd.getTime();


                EnrollmentPeriod enrollmentPeriod = new EnrollmentPeriod();

                enrollmentPeriodDaoSQL.addEnrollmentPeriodId(enrollmentPeriod,dateBegin, dateEnd);


                try {
                    enrollmentPeriod = enrollmentPeriodDaoSQL.getLastPeriodAdded();
                    if(enrollmentPeriodDaoSQL.isOnlyOne()) {
                        textField1.setText(enrollmentPeriod.toString());
                    }
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_prodekan(oldFrame,user);
            }
        });
    }

}
