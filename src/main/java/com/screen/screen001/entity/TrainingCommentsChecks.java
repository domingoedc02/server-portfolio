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
@Table(name = "tbl_training_comments_checks")
public class TrainingCommentsChecks {
    @Id
    @Column(nullable = false, length = 5)
    private char training_id;

    @Id
    @Column(nullable = false, length = 5)
    private char comment_id;

    @Id
    @Column(nullable = false, length = 5)
    private char insert_user;

    @Column(nullable = false)
    private Timestamp insert_date;
}
