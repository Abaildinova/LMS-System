package kz.test.lmssystem.dto;


import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class ChapterDto {

    private Long id;
    private String chapterName;
    private String description;
    private int order;
    private CourseDto course;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
