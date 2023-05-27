package com.screen.screen001.dto;

import java.sql.Time;
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
@Table(name = "tbl_training_topics")
@Entity
public class TrainingTopics {
    
    @Id
    @Column(nullable = false, length = 5)
    private String trainingId;

    @Column(nullable = false, length = 80)
    private String trainingName;

    @Column(nullable = false)
    private Timestamp trainingDate;

    @Column(nullable = true)
    private Time trainingStartTime;

    @Column(nullable = true)
    private Time trainingEndTime;

    @Column(nullable = true, length = 4000)
    private String trainingDetails;

    @Column(nullable = false, length = 5)
    private String insertMember;

    @Column(nullable = false)
    private Timestamp insertDate;

    @Column(nullable = false, length = 5)
    private String updateMember;

    @Column(nullable = false)
    private Timestamp updateDate;

    public String getTrainingId(){
        return trainingId;
    }

    public void setTrainingId(String trainingId){
        this.trainingId = trainingId;
    }

    public String getTrainingName(){
        return trainingName;
    }

    public void setTrainingName(String trainingName){
        this.trainingName = trainingName;
    }

    public Timestamp getTrainingDate(){
        return trainingDate;
    }

    public void setTrainingDate(Timestamp trainingDate){
        this.trainingDate = trainingDate;
    }

    public Time getTrainingStartTime(){
        return trainingStartTime;
    }

    public void setTrainingStartTime(Time trainingStartTime){
        this.trainingStartTime= trainingStartTime;
    }

    public Time getTrainingEndTime(){
        return trainingEndTime;
    }

    public void setTrainingId(Time trainingEndTime){
        this.trainingEndTime = trainingEndTime;
    }

    public String getTrainingDetails(){
        return trainingDetails;
    }

    public void setTrainingDetails(String trainingDetails){
        this.trainingDetails = trainingDetails;
    }

    public String getInsertMember(){
        return insertMember;
    }

    public void setInsertMember(String insertMember){
        this.insertMember = insertMember;
    }

    public Timestamp getInsertDate(){
        return insertDate;
    }

    public void setInsertDate(Timestamp insertDate){
        this.insertDate = insertDate;
    }

    public String getUpdateMember(){
        return updateMember;
    }

    public void setUpdateMember(String updateMember){
        this.updateMember = updateMember;
    }

    public Timestamp getUpdateDate(){
        return updateDate;
    }

    public void setTrainingId(Timestamp updateDate){
        this.updateDate = updateDate;
    }
}
