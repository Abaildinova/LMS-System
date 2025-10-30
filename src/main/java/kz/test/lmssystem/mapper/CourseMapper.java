package kz.test.lmssystem.mapper;

import kz.test.lmssystem.dto.CourseDto;
import kz.test.lmssystem.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "courseName", source = "name")
    CourseDto toDtoCourse(Course course);

    @Mapping(target = "name", source = "courseName")
    Course toEntityCourse(CourseDto courseDto);

    List<CourseDto> toDtoCourseList(List<Course> courses);

    List<Course> toEntityCourseList(List<CourseDto> dtoListCourseDto);
}
