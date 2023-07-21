package com.example.temphumid;

public class Patients {
    private String cnic;
    private String patientname;
    private String fname;

    public Patients() {

    }

    public Patients(String cnic, String patientname, String fname) {
        this.cnic = cnic;
        this.patientname = patientname;
        this.fname = fname;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getPatientname() {
        return patientname;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
}
