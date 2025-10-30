package kz.test.lmssystem.dto;

import lombok.*;


import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class CourseDto {

    private Long id;
    private String courseName;
    private String description;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
