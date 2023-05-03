package com.screen.screen001.entity;

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
@Table(name = "tbl_training_topics_checks")
public class TrainingTopicsChecks {

    @Id
    @Column(nullable = false, length = 5)
    private char training_id;

    @Id
    @Column(nullable = false, length = 5)
    private char insert_user;

    @Column(nullable = false)
    private Timestamp insert_date;
}