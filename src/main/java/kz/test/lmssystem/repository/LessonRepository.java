package kz.test.lmssystem.repository;

import jakarta.transaction.Transactional;
import kz.test.lmssystem.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Lesson findByName(String name);

    List<Lesson> findByNameContainingIgnoreCase(String name);

    // Найти уроки по chapter_id
    List<Lesson> findByChapterId(Long chapterId);

    // Найти уроки по course_id через JOIN
    @Query("SELECT l FROM Lesson l JOIN l.chapter c WHERE c.course.id = :courseId")
    List<Lesson> findByCourseId(@Param("courseId") Long courseId);
}
