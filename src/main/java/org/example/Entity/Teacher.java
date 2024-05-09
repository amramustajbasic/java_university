package org.example.Entity;

public class Teacher implements IDType{
    private int teacherId;
    private int userId;
    private String name;
    private String lastName;
    private String title;


    @Override
    public void setId(int id) {
        this.teacherId = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getId() {
        return teacherId;
    }


    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return name+" "+lastName;}
    public String toTitle(){
        return name+ " " + lastName + " - " + title;
    }
    public String toResult(){
        String rez = "Ime: " + name + "\n" +
                "Prezime: " + lastName + "\n" +
                "Status: " + title + "\n" +
                "Sifra: " + teacherId + "\n";
        return rez;
    }

}
