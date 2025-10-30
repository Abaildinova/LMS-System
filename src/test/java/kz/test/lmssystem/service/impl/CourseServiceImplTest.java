package kz.test.lmssystem.service.impl;

import kz.test.lmssystem.entity.Course;
import kz.test.lmssystem.exception.ResourceNotFoundException;
import kz.test.lmssystem.repository.CourseRepository;
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
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setName("Java Programming");
        testCourse.setDescription("Learn Java from scratch");
        testCourse.setCreatedTime(LocalDateTime.now());
    }

    @Test
    void getAllCourses_ShouldReturnListOfCourses() {
        // Arrange
        Course course2 = new Course();
        course2.setId(2L);
        course2.setName("Python Programming");
        course2.setDescription("Learn Python");

        List<Course> courses = Arrays.asList(testCourse, course2);
        when(courseRepository.findAll()).thenReturn(courses);

        // Act
        List<Course> result = courseService.getAllCourses();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void getCourseById_WhenCourseExists_ShouldReturnCourse() {
        // Arrange
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        // Act
        Course result = courseService.getCourseById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testCourse.getId(), result.getId());
        assertEquals(testCourse.getName(), result.getName());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void getCourseById_WhenCourseNotExists_ShouldThrowException() {
        // Arrange
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            courseService.getCourseById(999L);
        });
        verify(courseRepository, times(1)).findById(999L);
    }

    @Test
    void getCourseByName_WhenCourseExists_ShouldReturnCourse() {
        // Arrange
        when(courseRepository.findByName("Java Programming")).thenReturn(testCourse);

        // Act
        Course result = courseService.getCourseByName("Java Programming");

        // Assert
        assertNotNull(result);
        assertEquals(testCourse.getName(), result.getName());
        verify(courseRepository, times(1)).findByName("Java Programming");
    }

    @Test
    void getCourseByName_WhenCourseNotExists_ShouldThrowException() {
        // Arrange
        when(courseRepository.findByName(anyString())).thenReturn(null);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            courseService.getCourseByName("NonExistent Course");
        });
        verify(courseRepository, times(1)).findByName("NonExistent Course");
    }

    @Test
    void saveCourse_ShouldSaveCourse() {
        // Arrange
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        // Act
        courseService.saveCourse(testCourse);

        // Assert
        verify(courseRepository, times(1)).save(testCourse);
    }

    @Test
    void updateCourse_WhenCourseExists_ShouldUpdateCourse() {
        // Arrange
        when(courseRepository.existsById(1L)).thenReturn(true);
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        // Act
        courseService.updateCourse(testCourse);

        // Assert
        verify(courseRepository, times(1)).existsById(1L);
        verify(courseRepository, times(1)).save(testCourse);
    }

    @Test
    void updateCourse_WhenCourseNotExists_ShouldThrowException() {
        // Arrange
        when(courseRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            courseService.updateCourse(testCourse);
        });
        verify(courseRepository, times(1)).existsById(1L);
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void deleteCourseById_WhenCourseExists_ShouldDeleteCourse() {
        // Arrange
        when(courseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(courseRepository).deleteById(1L);

        // Act
        courseService.deleteCourseById(1L);

        // Assert
        verify(courseRepository, times(1)).existsById(1L);
        verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCourseById_WhenCourseNotExists_ShouldThrowException() {
        // Arrange
        when(courseRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            courseService.deleteCourseById(999L);
        });
        verify(courseRepository, times(1)).existsById(999L);
        verify(courseRepository, never()).deleteById(anyLong());
    }
}