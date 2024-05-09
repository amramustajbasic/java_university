package org.example.Entity;


public class Subject implements IDType {
    private int subjectId;
    private String name;
    private String semester;
    private int responsibleTeacher;
    private int ects;
    private String prerequisite_subject;

    private int godina;


    @Override
    public void setId(int id) {
        this.subjectId = id;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }

    public int getEcts() {
        return ects;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setResponsibleTeacher(int responsibleTeacher) {
        this.responsibleTeacher = responsibleTeacher;
    }

    public void setPrerequisite_subject(String prerequisite_subject) {
        this.prerequisite_subject = prerequisite_subject;
    }

    public void setGodina(int godina) {
        this.godina = godina;
    }

    @Override
    public int getId() {
        return subjectId;
    }

    public String getName() {
        return name;
    }

    public String getSemester() {
        return semester;
    }

    public int getResponsibleTeacher() {
        return responsibleTeacher;
    }

    public String getPrerequisite_subject() {
        return prerequisite_subject;
    }

    public int getGodina() {
        return godina;
    }

    @Override
    public String toString() {
        return name + ", semestar: " + semester + ", godina: " + godina;
    }

    public String getResult(String nastavnik){
        String rez = "Ime predmeta: " + name + "\n"+
                "Semestar: " + semester + "\n"+
                "ECTS: " + ects + "\n"+
                "Preduslovi: " + prerequisite_subject + "\n" +
                "Odgovorni nastavnik: " + nastavnik + "\n" +
                "Sifra predmeta: " + subjectId + "\n" +
                "Godina: " + godina + "\n";
            return rez;
    }

    public String zelimPrenijeti() {
        String ispis;
        ispis = "Ja Vam saljem zahtjev za prenos bodova iz predmeta: " + name ;
        return ispis;
    }

    public String ispisPreduslova(){
        String[] preduslovi = prerequisite_subject.split(", ");
        String ispis = new String();
        for(int i = 0; i<preduslovi.length;i++){
            ispis+=preduslovi[i] + '\n';
        }
        return ispis;
    }

    }
