package com.example.consultationapp.model;

public class ApointementInformation {
    private  String studentName,time, lecturerId, lecturerName, studentId,type,apointementType,chemin;
    private long slot;

    public String getApointementType() {
        return apointementType;
    }

    public void setApointementType(String apointementType) {
        this.apointementType = apointementType;
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public ApointementInformation(){
    }

    public ApointementInformation(String patientName, String time, String doctorId, String doctorName, long slot) {
        this.studentName = patientName;
        this.time = time;
        this.lecturerId = doctorId;
        this.lecturerName = doctorName;
        this.slot = slot;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(String lecturerId) {
        this.lecturerId = lecturerId;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public long getSlot() {
        return slot;
    }

    public void setSlot(long slot) {
        this.slot = slot;
    }
}
