package kz.test.lmssystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.test.lmssystem.dto.LessonDto;
import kz.test.lmssystem.entity.Lesson;
import kz.test.lmssystem.mapper.LessonMapper;
import kz.test.lmssystem.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/lessons/")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Lesson Management", description = "APIs for managing lessons")
public class LessonRestContoller {

    private final LessonService lessonService;
    private final LessonMapper lessonMapper;

    @Operation(summary = "Get all lessons", description = "Retrieve a list of all available lessons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of lessons"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<LessonDto>> getAllLessons() {
        log.info("REST request to get all lessons");
        List<LessonDto> lessons = lessonMapper.toDtoLessonList(lessonService.getAllLessons());
        return ResponseEntity.ok(lessons);
    }

    @Operation(summary = "Get lesson by ID", description = "Retrieve a specific lesson by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved lesson"),
            @ApiResponse(responseCode = "404", description = "Lesson not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<LessonDto> getLessonById(
            @Parameter(description = "ID of the lesson to retrieve", required = true)
            @PathVariable Long id) {
        log.info("REST request to get lesson by id: {}", id);
        LessonDto lesson = lessonMapper.toDtoLesson(lessonService.getLessonById(id));
        return ResponseEntity.ok(lesson);
    }

    @Operation(summary = "Get lesson by name", description = "Retrieve a specific lesson by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved lesson"),
            @ApiResponse(responseCode = "404", description = "Lesson not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/by-name/{name}")
    public ResponseEntity<LessonDto> getLessonByName(
            @Parameter(description = "Name of the lesson to retrieve", required = true)
            @PathVariable String name) {
        log.info("REST request to get lesson by name: {}", name);
        LessonDto lesson = lessonMapper.toDtoLesson(lessonService.getLessonsByName(name));
        return ResponseEntity.ok(lesson);
    }

    @Operation(summary = "Get lessons by chapter ID", description = "Retrieve all lessons for a specific chapter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved lessons"),
            @ApiResponse(responseCode = "404", description = "Chapter not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/by-chapter/{chapterId}")
    public ResponseEntity<List<LessonDto>> getLessonsByChapterId(
            @Parameter(description = "ID of the chapter to retrieve lessons for", required = true)
            @PathVariable Long chapterId) {
        log.info("REST request to get lessons by chapter id: {}", chapterId);
        List<LessonDto> lessons = lessonMapper.toDtoLessonList(lessonService.getLessonsByChapterId(chapterId));
        return ResponseEntity.ok(lessons);
    }

    @Operation(summary = "Update existing lesson", description = "Update an existing lesson with new data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lesson updated successfully"),
            @ApiResponse(responseCode = "404", description = "Lesson not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping
    public ResponseEntity<Void> updateLesson(
            @Parameter(description = "Lesson object with updated data", required = true)
            @RequestBody Lesson lesson) {
        log.info("REST request to update lesson with id: {}", lesson.getId());
        lessonService.updateLesson(lesson);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Create new lesson", description = "Create a new lesson in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lesson created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Void> addLesson(
            @Parameter(description = "Lesson data to create", required = true)
            @RequestBody LessonDto lessonDto) {
        log.info("REST request to create new lesson");
        Lesson lesson = lessonMapper.toEntityLesson(lessonDto);
        lessonService.saveLesson(lesson);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Delete lesson", description = "Delete a lesson by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Lesson deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Lesson not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteLesson(
            @Parameter(description = "ID of the lesson to delete", required = true)
            @PathVariable Long id) {
        log.info("REST request to delete lesson with id: {}", id);
        lessonService.deleteLessonById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get lessons by course ID", description = "Retrieve all lessons for a specific course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved lessons"),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/by-course/{courseId}")
    public ResponseEntity<List<LessonDto>> getLessonsByCourseId(
            @Parameter(description = "ID of the course to retrieve lessons for", required = true)
            @PathVariable Long courseId) {
        log.info("REST request to get lessons by course id: {}", courseId);
        List<LessonDto> lessons = lessonMapper.toDtoLessonList(lessonService.getLessonsByCourseId(courseId));
        return ResponseEntity.ok(lessons);
    }

}
