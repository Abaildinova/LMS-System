package kz.test.lmssystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.test.lmssystem.dto.LessonDto;
import kz.test.lmssystem.entity.Lesson;
import kz.test.lmssystem.exception.ResourceNotFoundException;
import kz.test.lmssystem.mapper.LessonMapper;
import kz.test.lmssystem.service.LessonService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(LessonRestContoller.class)
public class LessonRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private LessonService lessonService;

    @Mock
    private LessonMapper lessonMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Lesson testLesson;

    private LessonDto testLessonDto;

    @BeforeEach
    void setUp() {
        testLesson = new Lesson();
        testLesson.setId(1L);
        testLesson.setName("Java Lesson");
        testLesson.setDescription("Learn Java from scratch");
        testLesson.setCreatedTime(LocalDateTime.now());

        testLessonDto = LessonDto.builder()
                .id(1L)
                .lessonName("Java Lesson")
                .description("Learn Java from scratch")
                .createdTime(LocalDateTime.now())
                .build();
    }

    @Test
    void getAllLessons_ShouldReturnAllLessons() throws Exception {

        List<Lesson> lessons = Arrays.asList(testLesson);
        List<LessonDto> lessonDtos = Arrays.asList(testLessonDto);

        when(lessonService.getAllLessons()).thenReturn(lessons);
        when(lessonMapper.toDtoLessonList(lessons)).thenReturn(lessonDtos);

        mockMvc.perform(get("/api/lessons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].lessonName").value("Java Lesson"));

        verify(lessonService, times(1)).getAllLessons();
    }

    @Test
    void getLessonById_WhenLessonExists_ShouldReturnLessons() throws Exception {
        // Arrange
        when(lessonService.getLessonById(1L)).thenReturn(testLesson);
        when(lessonMapper.toDtoLesson(testLesson)).thenReturn(testLessonDto);

        // Act & Assert
        mockMvc.perform(get("/api/lessons/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.lessonName").value("Java Lesson"))
                .andExpect(jsonPath("$.description").value("Learn Java from scratch"));

        verify(lessonService, times(1)).getLessonById(1L);
    }

    @Test
    void getLessonById_WhenLessonNotExists_ShouldReturn404() throws Exception {
        // Arrange
        when(lessonService.getLessonById(anyLong()))
                .thenThrow(new ResourceNotFoundException("Lesson", 999L));

        // Act & Assert
        mockMvc.perform(get("/api/lessons/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Lesson with id 999 not found"));

        verify(lessonService, times(1)).getLessonById(999L);
    }

    @Test
    void getLessonByName_WhenLessonExists_ShouldReturnLesson() throws Exception {
        // Arrange
        when(lessonService.getLessonsByName("Java Lesson")).thenReturn(testLesson);
        when(lessonMapper.toDtoLesson(testLesson)).thenReturn(testLessonDto);

        // Act & Assert
        mockMvc.perform(get("/api/lessons/by-name/Java Lesson"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.lessonName").value("Java Lesson"));

        verify(lessonService, times(1)).getLessonsByName("Java Lesson");
    }

    @Test
    void addLesson_ShouldCreateLesson() throws Exception {
        // Arrange
        when(lessonMapper.toEntityLesson(any(LessonDto.class))).thenReturn(testLesson);
        doNothing().when(lessonService).saveLesson(any(Lesson.class));

        // Act & Assert
        mockMvc.perform(post("/api/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testLessonDto)))
                .andExpect(status().isCreated());

        verify(lessonService, times(1)).saveLesson(any(Lesson.class));
    }

    @Test
    void updateLesson_ShouldUpdateLesson() throws Exception {
        // Arrange
        doNothing().when(lessonService).updateLesson(any(Lesson.class));

        // Act & Assert
        mockMvc.perform(put("/api/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testLesson)))
                .andExpect(status().isOk());

        verify(lessonService, times(1)).updateLesson(any(Lesson.class));
    }

    @Test
    void deleteLesson_ShouldDeleteLesson() throws Exception {
        // Arrange
        doNothing().when(lessonService).deleteLessonById(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/lessons/1"))
                .andExpect(status().isNoContent());

        verify(lessonService, times(1)).deleteLessonById(1L);
    }

    @Test
    void deleteLesson_WhenLessonNotExists_ShouldReturn404() throws Exception {
        // Arrange
        doThrow(new ResourceNotFoundException("Lesson", 999L))
                .when(lessonService).deleteLessonById(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/lessons/999"))
                .andExpect(status().isNotFound());

        verify(lessonService, times(1)).deleteLessonById(999L);
    }

}
