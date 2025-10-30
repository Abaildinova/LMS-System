package kz.test.lmssystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.test.lmssystem.dto.CourseDto;
import kz.test.lmssystem.entity.Course;
import kz.test.lmssystem.exception.ResourceNotFoundException;
import kz.test.lmssystem.mapper.CourseMapper;
import kz.test.lmssystem.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseRestController.class)
class CourseRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CourseService courseService;

    @Mock
    private CourseMapper courseMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Course testCourse;
    private CourseDto testCourseDto;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setName("Java Programming");
        testCourse.setDescription("Learn Java from scratch");
        testCourse.setCreatedTime(LocalDateTime.now());

        testCourseDto = CourseDto.builder()
                .id(1L)
                .courseName("Java Programming")
                .description("Learn Java from scratch")
                .createdTime(LocalDateTime.now())
                .build();
    }

    @Test
    void getAllCourses_ShouldReturnListOfCourses() throws Exception {
        // Arrange
        List<Course> courses = Arrays.asList(testCourse);
        List<CourseDto> courseDtos = Arrays.asList(testCourseDto);

        when(courseService.getAllCourses()).thenReturn(courses);
        when(courseMapper.toDtoCourseList(courses)).thenReturn(courseDtos);

        // Act & Assert
        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].courseName").value("Java Programming"));

        verify(courseService, times(1)).getAllCourses();
    }

    @Test
    void getCourseById_WhenCourseExists_ShouldReturnCourse() throws Exception {
        // Arrange
        when(courseService.getCourseById(1L)).thenReturn(testCourse);
        when(courseMapper.toDtoCourse(testCourse)).thenReturn(testCourseDto);

        // Act & Assert
        mockMvc.perform(get("/api/courses/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.courseName").value("Java Programming"))
                .andExpect(jsonPath("$.description").value("Learn Java from scratch"));

        verify(courseService, times(1)).getCourseById(1L);
    }

    @Test
    void getCourseById_WhenCourseNotExists_ShouldReturn404() throws Exception {
        // Arrange
        when(courseService.getCourseById(anyLong()))
                .thenThrow(new ResourceNotFoundException("Course", 999L));

        // Act & Assert
        mockMvc.perform(get("/api/courses/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Course with id 999 not found"));

        verify(courseService, times(1)).getCourseById(999L);
    }

    @Test
    void getCourseByName_WhenCourseExists_ShouldReturnCourse() throws Exception {
        // Arrange
        when(courseService.getCourseByName("Java Programming")).thenReturn(testCourse);
        when(courseMapper.toDtoCourse(testCourse)).thenReturn(testCourseDto);

        // Act & Assert
        mockMvc.perform(get("/api/courses/by-name/Java Programming"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.courseName").value("Java Programming"));

        verify(courseService, times(1)).getCourseByName("Java Programming");
    }

    @Test
    void addCourse_ShouldCreateCourse() throws Exception {
        // Arrange
        when(courseMapper.toEntityCourse(any(CourseDto.class))).thenReturn(testCourse);
        doNothing().when(courseService).saveCourse(any(Course.class));

        // Act & Assert
        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCourseDto)))
                .andExpect(status().isCreated());

        verify(courseService, times(1)).saveCourse(any(Course.class));
    }

    @Test
    void updateCourse_ShouldUpdateCourse() throws Exception {
        // Arrange
        doNothing().when(courseService).updateCourse(any(Course.class));

        // Act & Assert
        mockMvc.perform(put("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCourse)))
                .andExpect(status().isOk());

        verify(courseService, times(1)).updateCourse(any(Course.class));
    }

    @Test
    void deleteCourse_ShouldDeleteCourse() throws Exception {
        // Arrange
        doNothing().when(courseService).deleteCourseById(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/courses/1"))
                .andExpect(status().isNoContent());

        verify(courseService, times(1)).deleteCourseById(1L);
    }

    @Test
    void deleteCourse_WhenCourseNotExists_ShouldReturn404() throws Exception {
        // Arrange
        doThrow(new ResourceNotFoundException("Course", 999L))
                .when(courseService).deleteCourseById(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/courses/999"))
                .andExpect(status().isNotFound());

        verify(courseService, times(1)).deleteCourseById(999L);
    }
}