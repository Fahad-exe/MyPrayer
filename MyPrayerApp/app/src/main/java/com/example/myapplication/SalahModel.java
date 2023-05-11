package com.example.myapplication;

public class SalahModel {
    String fajr;
    String dhur;
    String asr;
    String maghrib;
    String isha;
    public SalahModel(String fajr,String dhur,String asr,String maghrib,String isha){
        this.fajr = fajr;
        this.dhur = dhur;
        this.asr = asr;
        this.maghrib = maghrib;
        this.isha = isha;
    }

    public String getFajr() {
        return fajr;
    }

    public void setFajr(String fajr) {
        this.fajr = fajr;
    }

    public String getDhur() {
        return dhur;
    }

    public void setDhur(String dhur) {
        dhur = dhur;
    }

    public String getAsr() {
        return asr;
    }

    public void setAsr(String asr) {
        asr = asr;
    }

    public String getMaghrib() {
        return maghrib;
    }

    public void setMaghrib(String maghrib) {
        maghrib = maghrib;
    }

    public String getIsha() {
        return isha;
    }

    public void setIsha(String isha) {
        isha = isha;
    }
}
