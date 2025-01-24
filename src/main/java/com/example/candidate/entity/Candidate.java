package com.example.candidate.entity;

import jakarta.persistence.*;

@Entity
@Table(name="candid")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String city;
    private String age;
//    private String graduation;
//    private String graduationYear;
    private String gender;
//    private String resumeFile;

//    public String getResumeFile() {
//        return resumeFile;
//    }
//
//    public void setResumeFile(String resumeFile) {
//        this.resumeFile = resumeFile;
//    }

    public Candidate() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

//    public String getGraduation() {
//        return graduation;
//    }
//
//    public void setGraduation(String graduation) {
//        this.graduation = graduation;
//    }
//
//    public String getGraduationYear() {
//        return graduationYear;
//    }
//
//    public void setGraduationYear(String graduationYear) {
//        this.graduationYear = graduationYear;
//    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
