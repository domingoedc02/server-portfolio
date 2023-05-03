package com.screen.screen001.entity;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.springframework.data.relational.core.mapping.Table;

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
@Table(name = "tbl_training_topics")
public class TrainingTopics {

    @Id
    @Column(nullable = false, length = 5)
    private char training_id;

    @Column(nullable = false, length = 80)
    private String training_name;

    @Column(nullable = false)
    private Date training_date;

    @Column(nullable = true)
    private Time training_start_time;

    @Column(nullable = true)
    private Time training_end_time;

    @Column(nullable = true, length = 4000)
    private String training_details;

    @Column(nullable = false, length = 5)
    private char insert_user;

    @Column(nullable = false)
    private Timestamp insert_date;

    @Column(nullable = false, length = 5)
    private char update_user;

    @Column(nullable = false)
    private Timestamp update_date;
}
