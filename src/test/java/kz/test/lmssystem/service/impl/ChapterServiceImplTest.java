package kz.test.lmssystem.service.impl;

import kz.test.lmssystem.entity.Chapter;
import kz.test.lmssystem.exception.ResourceNotFoundException;
import kz.test.lmssystem.repository.ChapterRepository;
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
class ChapterServiceImplTest {

    @Mock
    private ChapterRepository chapterRepository;

    @InjectMocks
    private ChapterServiceImpl chapterService;

    private Chapter testChapter;

    @BeforeEach
    void setUp() {
        testChapter = new Chapter();
        testChapter.setId(1L);
        testChapter.setName("Java Chapter");
        testChapter.setDescription("Learn Java from scratch");
        testChapter.setCreatedTime(LocalDateTime.now());
    }

    @Test
    void getAllChapters_ShouldReturnListOfChapters() {
        // Arrange
        Chapter chapter2 = new Chapter();
        chapter2.setId(2L);
        chapter2.setName("Python Chapter");
        chapter2.setDescription("Learn Python");

        List<Chapter> chapters = Arrays.asList(testChapter, chapter2);
        when(chapterRepository.findAll()).thenReturn(chapters);

        // Act
        List<Chapter> result = chapterService.getAllChapters();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(chapterRepository, times(1)).findAll();
    }

    @Test
    void getChapterById_WhenChapterExists_ShouldReturnChapter() {
        // Arrange
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(testChapter));

        // Act
        Chapter result = chapterService.getChapterById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testChapter.getId(), result.getId());
        assertEquals(testChapter.getName(), result.getName());
        verify(chapterRepository, times(1)).findById(1L);
    }

    @Test
    void getChapterById_WhenChapterNotExists_ShouldThrowException() {
        // Arrange
        when(chapterRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            chapterService.getChapterById(999L);
        });
        verify(chapterRepository, times(1)).findById(999L);
    }

    @Test
    void getChapterByName_WhenChapterExists_ShouldReturnChapter() {
        // Arrange
        when(chapterRepository.findByName("Java Chapter")).thenReturn(testChapter);

        // Act
        Chapter result = chapterService.getChapterByName("Java Chapter");

        // Assert
        assertNotNull(result);
        assertEquals(testChapter.getName(), result.getName());
        verify(chapterRepository, times(1)).findByName("Java Chapter");
    }

    @Test
    void getChapterByName_WhenChapterNotExists_ShouldThrowException() {
        // Arrange
        when(chapterRepository.findByName(anyString())).thenReturn(null);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            chapterService.getChapterByName("NonExistent Chapter");
        });
        verify(chapterRepository, times(1)).findByName("NonExistent Chapter");
    }

    @Test
    void saveChapter_ShouldSaveChapter() {
        // Arrange
        when(chapterRepository.save(any(Chapter.class))).thenReturn(testChapter);

        // Act
        chapterService.saveChapter(testChapter);

        // Assert
        verify(chapterRepository, times(1)).save(testChapter);
    }

    @Test
    void updateChapter_WhenChapterExists_ShouldUpdateChapter() {
        // Arrange
        when(chapterRepository.existsById(1L)).thenReturn(true);
        when(chapterRepository.save(any(Chapter.class))).thenReturn(testChapter);

        // Act
        chapterService.updateChapter(testChapter);

        // Assert
        verify(chapterRepository, times(1)).existsById(1L);
        verify(chapterRepository, times(1)).save(testChapter);
    }

    @Test
    void updateChapter_WhenChapterNotExists_ShouldThrowException() {
        // Arrange
        when(chapterRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            chapterService.updateChapter(testChapter);
        });
        verify(chapterRepository, times(1)).existsById(1L);
        verify(chapterRepository, never()).save(any(Chapter.class));
    }

    @Test
    void deleteChapterById_WhenChapterExists_ShouldDeleteChapter() {
        // Arrange
        when(chapterRepository.existsById(1L)).thenReturn(true);
        doNothing().when(chapterRepository).deleteById(1L);

        // Act
        chapterService.deleteChapterById(1L);

        // Assert
        verify(chapterRepository, times(1)).existsById(1L);
        verify(chapterRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteChapterById_WhenChapterNotExists_ShouldThrowException() {
        // Arrange
        when(chapterRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            chapterService.deleteChapterById(999L);
        });
        verify(chapterRepository, times(1)).existsById(999L);
        verify(chapterRepository, never()).deleteById(anyLong());
    }
}