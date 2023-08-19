package com.screen.screen001.entity;

import jakarta.persistence.Table;

import java.sql.Date;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_member_profiles", schema = "screen")
public class MemberProfile {
    
    @Id
    @Column(nullable = false, length = 5, unique = true)
    private String memberId;

    @Column(nullable = false, length = 40)
    private String memberName;

    @Column(nullable = true)
    private Date birthDate;

    @Column(nullable = true, length = 20)
    private String address1;

    @Column(nullable = true, length = 80)
    private String address2;

    @Column(nullable = true, length = 200)
    private String hobby;

    @Column(nullable = true, length = 200)
    private String speciality;

    @Column(nullable = true, length = 2)
    private String bloodType;

    @Column(nullable = true, length = 200)
    private String favoriteFood;

    @Column(nullable = true, length = 200)
    private String hatedFood;

    @Column(nullable = true, length = 400)
    private String goalsYear;

    @Column(nullable = true, length = 5)
    private String insertUser;

    @Column(nullable = true)
    private Timestamp insertDate;

    @Column(nullable = true, length = 5)
    private String updateUser;

    @Column(nullable = true)
    private Timestamp updateDate;


    public String getMemberId(){
        return memberId;
    }

    public void setMemberId(String memberId){
        this.memberId = memberId;
    }

    public String getMemberName(){
        return memberName;
    }

    public void setMemberName(String memberName){
        this.memberName = memberName;
    }

    public Date getBirthDate(){
        return birthDate;
    }
    
    public void setBirthDate(Date birthDate){
        this.birthDate = birthDate;
    }

    public String getAdress1(){
        return address1;
    }

    public void setAddress1(String address1){
        this.address1 = address1;
    }

    public String getAddress2(){
        return address2;
    }

    public void setAddress2(String address2){
        this.address2 = address2;
    }

    public String getHobby(){
        return hobby;
    }

    public void setHobby(String hobby){
        this.hobby = hobby;
    }

    public String getSpeciality(){
        return speciality;
    }

    public void setSpeciality(String speciality){
        this.speciality = speciality;
    }

    public String getBloodType(){
        return bloodType;
    }

    public void setBloodType(String bloodType){
        this.bloodType = bloodType;
    }

    public String getFavoriteFood(){
        return favoriteFood;
    }

    public void setFavoriteFood(String favoriteFood){
        this.favoriteFood = favoriteFood;
    }

    public String getHatedFood(){
        return hatedFood;
    }

    public void setHatedFood(String hatedFood){
        this.hatedFood = hatedFood;
    }

    public String getGoalsYear(){
        return goalsYear;
    }

    public void setGoalsYear(String goalsYear){
        this.goalsYear = goalsYear;
    }

    public String getInsertUser(){
        return insertUser;
    }

    public void setInsertUser(String insertUser){
        this.insertUser = insertUser;
    }

    public Timestamp getInsertDate(){
        return insertDate;
    }

    public void setInsertDate(Timestamp insertDate){
        this.insertDate = insertDate;
    }

    public String getUpdateUser(){
        return updateUser;
    }

    public void setUpdateUser(String updateUser){
        this.updateUser = updateUser;
    }

    public Timestamp getUpdateDate(){
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate){
        this.updateDate = updateDate;
    }
}
