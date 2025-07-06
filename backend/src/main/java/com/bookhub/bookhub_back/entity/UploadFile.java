package com.bookhub.bookhub_back.entity;

import com.bookhub.bookhub_back.common.enums.FileTargetType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "upload_files")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class UploadFile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upload_file_id")
    private Long id;

    @Column(name = "original_name", nullable = false)
    private String originalName;
    @Column(name = "file_name", nullable = false)
    private String fileName;
    @Column(name = "file_path", nullable = false)
    private String filePath;
    @Column(name = "file_type", nullable = false)
    private String fileType;
    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "target_id", nullable = false)
    private String targetId;
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private FileTargetType targetType;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @Builder
    public UploadFile(String originalName, String fileName, String filePath,
                      String fileType, long fileSize, String targetId,
                      FileTargetType targetType
    ) {
        this.originalName = originalName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.targetId = targetId;
        this.targetType = targetType;
    }
}
