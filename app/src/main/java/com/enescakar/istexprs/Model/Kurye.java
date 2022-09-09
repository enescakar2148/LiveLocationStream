package com.enescakar.istexprs.Model;

public class Kurye {
    private String kuryeNo;
    private String mail;
    private String plaka;
    private String pass;
    private String kuryeId;

    public String getKuryeId() {
        return kuryeId;
    }

    public void setKuryeId(String kuryeId) {
        this.kuryeId = kuryeId;
    }

    public Kurye(String kuryeNo, String mail, String plaka, String pass, String kuryeId) {
        this.kuryeNo = kuryeNo;
        this.mail = mail;
        this.plaka = plaka;
        this.pass = pass;
        this.kuryeId = kuryeId;
    }

    public String getKuryeNo() {
        return kuryeNo;
    }

    public void setKuryeNo(String kuryeNo) {
        this.kuryeNo = kuryeNo;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPlaka() {
        return plaka;
    }

    public void setPlaka(String plaka) {
        this.plaka = plaka;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
