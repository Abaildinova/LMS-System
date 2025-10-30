package kz.test.lmssystem.service;

import kz.test.lmssystem.entity.Lesson;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LessonService {

    List<Lesson> getAllLessons();

    Lesson getLessonById(Long id);

    Lesson getLessonsByName(String name);

    void updateLesson(Lesson lesson);

    void saveLesson(Lesson lesson);

    void deleteLessonById(Long id);

    List<Lesson> getLessonsByChapterId(Long chapterId);

    List<Lesson> getLessonsByCourseId(Long courseId);
}
