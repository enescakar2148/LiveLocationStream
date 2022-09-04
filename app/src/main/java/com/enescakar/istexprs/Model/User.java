package com.enescakar.istexprs.Model;

public class User {
    private String plaka;
    private String mail;
    private String pass;
    String passAgain;
    private String kuryeNumber;

    public String getPassAgain() {
        return passAgain;
    }

    public void setPassAgain(String passAgain) {
        this.passAgain = passAgain;
    }

    public User(String plaka, String mail, String pass, String passAgain, String kuryeNumber) {
        this.plaka = plaka;
        this.mail = mail;
        this.pass = pass;
        this.passAgain = passAgain;
        this.kuryeNumber = kuryeNumber;
    }

    public String getPlaka() {
        return plaka;
    }

    public void setPlaka(String plaka) {
        this.plaka = plaka;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getKuryeNumber() {
        return kuryeNumber;
    }

    public void setKuryeNumber(String kuryeNumber) {
        this.kuryeNumber = kuryeNumber;
    }
}
