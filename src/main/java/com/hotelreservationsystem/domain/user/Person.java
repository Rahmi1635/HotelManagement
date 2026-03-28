package com.hotelreservationsystem.domain.user;

import java.time.LocalDate;

public abstract class Person {

    private long user_id;
    private String first_name;
    private String last_name;
    private String username;
    private String email;
    private String phone;
    private String tc;
    private String hashed_password;
    private Role role;
    private LocalDate created_at;

    public Person(String first_name,String last_name,String username,String email,String phone
            ,String tc,String hashed_password)
    {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.tc = tc;
        this.hashed_password = hashed_password;
        this.created_at=LocalDate.now();

    }

    public Person(long user_id, String first_name, String last_name, String username, String email, String phone
            , String tc, String hashed_password, Role role)
    {
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.tc = tc;
        this.hashed_password = hashed_password;
        this.role = role;
        this.created_at=LocalDate.now();

    }
    public Person()
    {

    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getHashed_password() {
        return hashed_password;
    }

    public void setHashed_password(String hashed_password) {
        this.hashed_password = hashed_password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public void logIn(String username, String hashed_password)
    {

    }

    public void showInfo()
    {
        System.out.println("ID - "+getUser_id()+"\n"+
                "AD - SOYAD : "+getFirst_name()+" "+getLast_name()+"\n"
                +"USERNAME : "+getUsername()+"\n"+
                "EMAIL : "+getEmail()+"\n"+
                " PHONE : "+getPhone()+"\n"+
                " TC : "+getTc()+" \n "+
                "PASSWORD : "+getHashed_password()+"\n"+
                "ROLE : "+getRole()+"\n"+
                "TARİH : "+getCreated_at());

    }
}
