package com.enescakar.istexprs.Model;

import com.google.android.gms.maps.model.LatLng;

public class Kurye {
    private String kuryeNo;
    private String mail;
    private String plaka;
    private String pass;
    private String isim;
    private String soyIsim;
    private String kuryeId;
    private LatLng currentLocation;

    public String getKuryeId() {
        return kuryeId;
    }

    public void setKuryeId(String kuryeId) {
        this.kuryeId = kuryeId;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getSoyIsim() {
        return soyIsim;
    }

    public void setSoyIsim(String soyIsim) {
        this.soyIsim = soyIsim;
    }

    public Kurye(String kuryeNo, String mail, String plaka, String pass, String kuryeId) {
        this.kuryeNo = kuryeNo;
        this.mail = mail;
        this.plaka = plaka;
        this.pass = pass;
        this.kuryeId = kuryeId;
    }
    public Kurye(String kuryeNo, String mail, String plaka, String pass, String kuryeId, LatLng currentLocation, String isim, String soyisim) {
        this.kuryeNo = kuryeNo;
        this.mail = mail;
        this.plaka = plaka;
        this.pass = pass;
        this.kuryeId = kuryeId;
        this.currentLocation = currentLocation;
        this.isim = isim;
        this.soyIsim = soyisim;

    }


    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
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
