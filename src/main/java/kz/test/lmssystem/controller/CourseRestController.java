package kz.test.lmssystem.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.test.lmssystem.dto.CourseDto;
import kz.test.lmssystem.entity.Course;
import kz.test.lmssystem.mapper.CourseMapper;
import kz.test.lmssystem.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/courses/")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Course Management", description = "APIs for managing courses")
public class CourseRestController {

    private final CourseService courseService;
    private final CourseMapper courseMapper;

    @Operation(summary = "Get all courses", description = "Retrieve a list of all available courses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of courses"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        log.info("REST request to get all courses");
        List<CourseDto> courses = courseMapper.toDtoCourseList(courseService.getAllCourses());
        return ResponseEntity.ok(courses);
    }


    @Operation(summary = "Get course by ID", description = "Retrieve a specific course by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved course"),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<CourseDto> getCourseById(
            @Parameter(description = "ID of the course to retrieve", required = true)
            @PathVariable Long id) {
        log.info("REST request to get course by id: {}", id);
        CourseDto course = courseMapper.toDtoCourse(courseService.getCourseById(id));
        return ResponseEntity.ok(course);
    }


    @Operation(summary = "Get course by name", description = "Retrieve a specific course by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved course"),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/by-name/{name}")
    public ResponseEntity<CourseDto> getCourseByName(
            @Parameter(description = "Name of the course to retrieve", required = true)
            @PathVariable String name) {
        log.info("REST request to get course by name: {}", name);
        CourseDto course = courseMapper.toDtoCourse(courseService.getCourseByName(name));
        return ResponseEntity.ok(course);
    }


    @Operation(summary = "Update existing course", description = "Update an existing course with new data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping
    public ResponseEntity<Void> updateCourse(
            @Parameter(description = "Course object with updated data", required = true)
            @RequestBody Course course) {
        log.info("REST request to update course with id: {}", course.getId());
        courseService.updateCourse(course);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Create new course", description = "Create a new course in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Void> addCourse(
            @Parameter(description = "Course data to create", required = true)
            @RequestBody CourseDto courseDto) {
        log.info("REST request to create new course");
            Course course = courseMapper.toEntityCourse(courseDto);
            courseService.saveCourse(course);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @Operation(summary = "Delete course", description = "Delete a course by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Course deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity <Void> deleteCourse(
            @Parameter(description = "id of the course to delete", required = true)
            @PathVariable Long id) {
        log.info("REST request to delete course with id: {}", id);
        courseService.deleteCourseById(id);
        return ResponseEntity.noContent().build();
    }

}
