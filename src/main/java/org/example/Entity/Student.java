package org.example.Entity;

import javax.security.auth.Subject;

public class Student implements IDType{
    private int studentId;
    private int userId;
    private int yearOfStudy;
    private String status;
    private String name;
    private String lastName;
//    private  int request_id;


    @Override
    public void setId(int id) {
        this.studentId = id;
    }

    public void setUserId(int id) {
        this.userId = id;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }



    @Override
    public int getId() {
        return studentId;
    }

    public int getUserId() {
        return userId;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

//    public int getRequest_id() {
//        return request_id;
//    }

    @Override
    public String toString() {
        return studentId+ " " + name + " " + lastName ;
    }

    public String toRezultat(){
        String rezultat;
        rezultat = "Ime: " + name + "\n" +
                "Prezime: " + lastName +"\n" +
                "Godina studija: " + yearOfStudy +"\n"+
                "Status: " + status +"\n"+
                "Broj indeksa: " + studentId+"\n";
        return rezultat;
    }


    public boolean equals(Student student) {
        if(this.getId() == student.getId())
            return true;
        else
            return false;
    }
}

