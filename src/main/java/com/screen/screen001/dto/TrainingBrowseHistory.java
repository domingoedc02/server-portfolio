package com.screen.screen001.dto;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "tbl_training_browse_history", schema = "screen")
@Entity
public class TrainingBrowseHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 5)
    private String memberId;

    @Column(nullable = false, length = 5)
    private String trainingId;

    @Column(nullable = false)
    private Timestamp browseDate;

    @Column(nullable = false, length = 5)
    private String insertUser;

    @Column(nullable = false)
    private Timestamp insertDate;

    @Column(nullable = false, length = 5)
    private String updateUser;

    @Column(nullable = false)
    private Timestamp updateDate;
}
