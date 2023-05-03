package com.screen.screen001.entity;

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
@Entity
@Table(name = "tbl_training_browse_checks")
public class TrainingBrowseChecks {

    @Id
    @Column(nullable = false, length = 5)
    private char username;

    @Id
    @Column(nullable = false, length = 5)
    private char training_id;

    @Id
    @Column(nullable = false)
    private Timestamp browse_date;

    @Column(nullable = false, length = 5)
    private char insert_user;

    @Column(nullable = false)
    private Timestamp insert_date;
}
