package kz.test.lmssystem.mapper;

import kz.test.lmssystem.dto.LessonDto;
import kz.test.lmssystem.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "lessonName", source = "name")
    LessonDto toDtoLesson(Lesson lesson);

    @Mapping(target = "name", source = "lessonName")
    Lesson toEntityLesson(LessonDto lessonDto);

    List<LessonDto> toDtoLessonList(List<Lesson> lessons);

    List<Lesson> toEntityLessonList(List<LessonDto> dtoListLessonDto);
}
