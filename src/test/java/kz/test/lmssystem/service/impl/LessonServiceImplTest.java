package kz.test.lmssystem.service.impl;

import kz.test.lmssystem.entity.Chapter;
import kz.test.lmssystem.entity.Lesson;
import kz.test.lmssystem.exception.ResourceNotFoundException;
import kz.test.lmssystem.repository.ChapterRepository;
import kz.test.lmssystem.repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceImplTest {

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private LessonServiceImpl lessonService;

    private Lesson testLesson;

    @BeforeEach
    void setUp() {
        testLesson = new Lesson();
        testLesson.setId(1L);
        testLesson.setName("Java Lesson");
        testLesson.setDescription("Learn Java from scratch");
        testLesson.setCreatedTime(LocalDateTime.now());
    }

    @Test
    void getAllLessons_ShouldReturnListOfLessons() {
        // Arrange
        Lesson lesson2 = new Lesson();
        lesson2.setId(2L);
        lesson2.setName("Python Lesson");
        lesson2.setDescription("Learn Python");

        List<Lesson> lessons = Arrays.asList(testLesson, lesson2);
        when(lessonRepository.findAll()).thenReturn(lessons);

        // Act
        List<Lesson> result = lessonService.getAllLessons();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(lessonRepository, times(1)).findAll();
    }

    @Test
    void getLessonById_WhenLessonExists_ShouldReturnLesson() {
        // Arrange
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(testLesson));

        // Act
        Lesson result = lessonService.getLessonById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testLesson.getId(), result.getId());
        assertEquals(testLesson.getName(), result.getName());
        verify(lessonRepository, times(1)).findById(1L);
    }

    @Test
    void getLessonById_WhenLessonNotExists_ShouldThrowException() {
        // Arrange
        when(lessonRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            lessonService.getLessonById(999L);
        });
        verify(lessonRepository, times(1)).findById(999L);
    }

    @Test
    void getLessonByName_WhenLessonExists_ShouldReturnLesson() {
        // Arrange
        when(lessonRepository.findByName("Java Lesson")).thenReturn(testLesson);

        // Act
        Lesson result = lessonService.getLessonsByName("Java Lesson");

        // Assert
        assertNotNull(result);
        assertEquals(testLesson.getName(), result.getName());
        verify(lessonRepository, times(1)).findByName("Java Lesson");
    }

    @Test
    void getLessonByName_WhenLessonNotExists_ShouldThrowException() {
        // Arrange
        when(lessonRepository.findByName(anyString())).thenReturn(null);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            lessonService.getLessonsByName("NonExistent Lesson");
        });
        verify(lessonRepository, times(1)).findByName("NonExistent Lesson");
    }

    @Test
    void saveLesson_ShouldSaveLesson() {
        // Arrange
        when(lessonRepository.save(any(Lesson.class))).thenReturn(testLesson);

        // Act
        lessonService.saveLesson(testLesson);

        // Assert
        verify(lessonRepository, times(1)).save(testLesson);
    }

    @Test
    void updateLesson_WhenLessonExists_ShouldUpdateLesson() {
        // Arrange
        when(lessonRepository.existsById(1L)).thenReturn(true);
        when(lessonRepository.save(any(Lesson.class))).thenReturn(testLesson);

        // Act
        lessonService.updateLesson(testLesson);

        // Assert
        verify(lessonRepository, times(1)).existsById(1L);
        verify(lessonRepository, times(1)).save(testLesson);
    }

    @Test
    void updateLesson_WhenCLessonNotExists_ShouldThrowException() {
        // Arrange
        when(lessonRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            lessonService.updateLesson(testLesson);
        });
        verify(lessonRepository, times(1)).existsById(1L);
        verify(lessonRepository, never()).save(any(Lesson.class));
    }

    @Test
    void deleteLessonById_WhenLessonExists_ShouldDeleteLesson() {
        // Arrange
        when(lessonRepository.existsById(1L)).thenReturn(true);
        doNothing().when(lessonRepository).deleteById(1L);

        // Act
        lessonService.deleteLessonById(1L);

        // Assert
        verify(lessonRepository, times(1)).existsById(1L);
        verify(lessonRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteLessonById_WhenLessonNotExists_ShouldThrowException() {
        // Arrange
        when(lessonRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            lessonService.deleteLessonById(999L);
        });
        verify(lessonRepository, times(1)).existsById(999L);
        verify(lessonRepository, never()).deleteById(anyLong());
    }
}