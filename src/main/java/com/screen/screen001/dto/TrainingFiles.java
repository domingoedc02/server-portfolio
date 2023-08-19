package com.screen.screen001.dto;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_training_files", schema = "screen")
@Entity
public class TrainingFiles {
    
    @Id
    @Column(nullable= false, length = 5)
    private String trainingId;
    @Column(nullable= false, length = 80)
    private String uploadFile;
    @Column(nullable= false, length = 200)
    private String uploadFilePath;
    @Column(nullable= false, length = 5)
    private String insertUser;
    @Column(nullable= false)
    private Timestamp insertDate;
    @Column(nullable= false, length = 5)
    private String updateUser;
    @Column(nullable= false)
    private Timestamp updateDate;
    @Column(nullable= false, length = 20)
    private byte[] contentType;


    public String getTrainingId(){
        return trainingId;
    }

    public void setTrainingId(String trainingId){
        this.trainingId = trainingId;
    }

    public String getUploadFile(){
        return uploadFile;
    }

    public void setUploadFile(String uploadFile){
        this.uploadFile = uploadFile;
    }

    public String getUploadFilePath(){
        return uploadFilePath;
    }

    public void setUploadFilePath(String uploadFilePath){
        this.uploadFilePath = uploadFilePath;
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

    public byte[] getContentType(){
        return contentType;
    }

    public void setContentType(byte[] contentType){
        this.contentType = contentType;
    }
}
