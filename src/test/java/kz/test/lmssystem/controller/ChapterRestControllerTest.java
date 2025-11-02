package kz.test.lmssystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.test.lmssystem.dto.ChapterDto;
import kz.test.lmssystem.entity.Chapter;
import kz.test.lmssystem.exception.ResourceNotFoundException;
import kz.test.lmssystem.mapper.ChapterMapper;
import kz.test.lmssystem.service.ChapterService;
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

@WebMvcTest(ChapterRestContoller.class)
public class ChapterRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ChapterService chapterService;

    @Mock
    private ChapterMapper chapterMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Chapter testChapter;

    private ChapterDto testChapterDto;

    @BeforeEach
    void setUp() {
        testChapter = new Chapter();
        testChapter.setId(1L);
        testChapter.setName("Java Chapter");
        testChapter.setDescription("Learn Java from scratch");
        testChapter.setCreatedTime(LocalDateTime.now());

        testChapterDto = ChapterDto.builder()
                .id(1L)
                .chapterName("Java Chapter")
                .description("Learn Java from scratch")
                .createdTime(LocalDateTime.now())
                .build();
    }

    @Test
    void getAllChapters_ShouldReturnAllChapters() throws Exception {

        List<Chapter> chapters = Arrays.asList(testChapter);
        List<ChapterDto> chapterDtos = Arrays.asList(testChapterDto);

        when(chapterService.getAllChapters()).thenReturn(chapters);
        when(chapterMapper.toDtoChapterList(chapters)).thenReturn(chapterDtos);

        mockMvc.perform(get("/api/chapters"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].chapterName").value("Java Chapter"));

        verify(chapterService, times(1)).getAllChapters();
    }

    @Test
    void getChapterById_WhenCourseExists_ShouldReturnChapters() throws Exception {
        // Arrange
        when(chapterService.getChapterById(1L)).thenReturn(testChapter);
        when(chapterMapper.toDtoChapter(testChapter)).thenReturn(testChapterDto);

        // Act & Assert
        mockMvc.perform(get("/api/chapters/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.chapterName").value("Java Chapter"))
                .andExpect(jsonPath("$.description").value("Learn Java from scratch"));

        verify(chapterService, times(1)).getChapterById(1L);
    }

    @Test
    void getChapterById_WhenChapterNotExists_ShouldReturn404() throws Exception {
        // Arrange
        when(chapterService.getChapterById(anyLong()))
                .thenThrow(new ResourceNotFoundException("Chapter", 999L));

        // Act & Assert
        mockMvc.perform(get("/api/chapters/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Chapter with id 999 not found"));

        verify(chapterService, times(1)).getChapterById(999L);
    }

    @Test
    void getChapterByName_WhenCourseExists_ShouldReturnChapter() throws Exception {
        // Arrange
        when(chapterService.getChapterByName("Java Chapter")).thenReturn(testChapter);
        when(chapterMapper.toDtoChapter(testChapter)).thenReturn(testChapterDto);

        // Act & Assert
        mockMvc.perform(get("/api/chapters/by-name/Java Lesson"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.chapterName").value("Java Lesson"));

        verify(chapterService, times(1)).getChapterByName("Java Lesson");
    }

    @Test
    void addChapter_ShouldCreateChapter() throws Exception {
        // Arrange
        when(chapterMapper.toEntityChapter(any(ChapterDto.class))).thenReturn(testChapter);
        doNothing().when(chapterService).saveChapter(any(Chapter.class));

        // Act & Assert
        mockMvc.perform(post("/api/chapters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testChapterDto)))
                .andExpect(status().isCreated());

        verify(chapterService, times(1)).saveChapter(any(Chapter.class));
    }

    @Test
    void updateChapter_ShouldUpdateChapter() throws Exception {
        // Arrange
        doNothing().when(chapterService).updateChapter(any(Chapter.class));

        // Act & Assert
        mockMvc.perform(put("/api/chapters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testChapter)))
                .andExpect(status().isOk());

        verify(chapterService, times(1)).updateChapter(any(Chapter.class));
    }

    @Test
    void deleteChapter_ShouldDeleteChapter() throws Exception {
        // Arrange
        doNothing().when(chapterService).deleteChapterById(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/chapters/1"))
                .andExpect(status().isNoContent());

        verify(chapterService, times(1)).deleteChapterById(1L);
    }

    @Test
    void deleteChapter_WhenChapterNotExists_ShouldReturn404() throws Exception {
        // Arrange
        doThrow(new ResourceNotFoundException("Chapter", 999L))
                .when(chapterService).deleteChapterById(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/chapters/999"))
                .andExpect(status().isNotFound());

        verify(chapterService, times(1)).deleteChapterById(999L);
    }

}
