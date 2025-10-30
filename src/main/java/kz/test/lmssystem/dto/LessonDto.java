package kz.test.lmssystem.dto;

import kz.test.lmssystem.entity.Chapter;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LessonDto {

    private Long id;
    private String lessonName;
    private String description;
    private int order;
    private Chapter chapter;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
