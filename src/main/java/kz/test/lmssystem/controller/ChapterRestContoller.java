package kz.test.lmssystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.test.lmssystem.dto.ChapterDto;
import kz.test.lmssystem.entity.Chapter;
import kz.test.lmssystem.mapper.ChapterMapper;
import kz.test.lmssystem.service.ChapterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/chapters/")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Chapter Management", description = "APIs for managing chapters")
public class ChapterRestContoller {

    private final ChapterService chapterService;
    private final ChapterMapper chapterMapper;

    @Operation(summary = "Get all chapters", description = "Retrieve a list of all available chapters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of chapters"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<ChapterDto>> getAllChapters() {
        log.info("REST request to get all chapters");
        List<ChapterDto> chapters = chapterMapper.toDtoChapterList(chapterService.getAllChapters());
        return ResponseEntity.ok(chapters);
    }

    @Operation(summary = "Get chapter by ID", description = "Retrieve a specific chapter by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved chapter"),
            @ApiResponse(responseCode = "404", description = "Chapter not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<ChapterDto> getChapterById(
            @Parameter(description = "ID of the chapter to retrieve", required = true)
            @PathVariable Long id) {
        log.info("REST request to get chapter by id: {}", id);
        ChapterDto chapter = chapterMapper.toDtoChapter(chapterService.getChapterById(id));
        return ResponseEntity.ok(chapter);
    }

    @Operation(summary = "Get chapter by name", description = "Retrieve a specific chapter by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved chapter"),
            @ApiResponse(responseCode = "404", description = "Chapter not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/by-name/{name}")
    public ResponseEntity<ChapterDto> getChapterByName(
            @Parameter(description = "Name of the chapter to retrieve", required = true)
            @PathVariable String name) {
        log.info("REST request to get chapter by name: {}", name);
        ChapterDto chapter = chapterMapper.toDtoChapter(chapterService.getChapterByName(name));
        return ResponseEntity.ok(chapter);
    }


    @Operation(summary = "Update existing chapter", description = "Update an existing chapter with new data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chapter updated successfully"),
            @ApiResponse(responseCode = "404", description = "Chapter not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping
    public ResponseEntity<Void> updateChapter(
            @Parameter(description = "Chapter object with updated data", required = true)
            @RequestBody Chapter chapter) {
        log.info("REST request to update chapter with id: {}", chapter.getId());
        chapterService.updateChapter(chapter);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Create new chapter", description = "Create a new chapter in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Chapter created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Void> addChapter(
            @Parameter(description = "Chapter data to create", required = true)
            @RequestBody ChapterDto chapterDto) {
        log.info("REST request to create new chapter");
        Chapter chapter = chapterMapper.toEntityChapter(chapterDto);
        chapterService.saveChapter(chapter);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Delete chapter", description = "Delete a chapter by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Chapter deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Chapter not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteChapter(
            @Parameter(description = "ID of the chapter to delete", required = true)
            @PathVariable Long id) {
        log.info("REST request to delete chapter with id: {}", id);
        chapterService.deleteChapterById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get chapters by course ID", description = "Retrieve all chapters for a specific course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved chapters"),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/by-course/{courseId}")
    public ResponseEntity<List<ChapterDto>> getChaptersByCourseId(
            @Parameter(description = "ID of the course to retrieve chapters for", required = true)
            @PathVariable Long courseId) {
        log.info("REST request to get chapters by course id: {}", courseId);
        List<ChapterDto> chapters = chapterMapper.toDtoChapterList(chapterService.getChapterByCourseId(courseId));
        return ResponseEntity.ok(chapters);
    }

}
