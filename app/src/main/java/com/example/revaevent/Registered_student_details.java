package com.example.revaevent;

public class Registered_student_details {
    String name,phoneno,email,transaction;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getphoneno() {
        return phoneno;
    }

    public void setphoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public Registered_student_details(String name, String phoneno, String email, String transaction) {
        this.name = name;
        this.phoneno = phoneno;
        this.email = email;
        this.transaction = transaction;
    }
}
