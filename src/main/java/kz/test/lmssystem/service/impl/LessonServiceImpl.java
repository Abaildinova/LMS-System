package kz.test.lmssystem.service.impl;


import kz.test.lmssystem.entity.Lesson;
import kz.test.lmssystem.repository.LessonRepository;
import kz.test.lmssystem.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    @Override
    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    @Override
    public Lesson getLessonById(Long id) {
        return lessonRepository.findById(id).get();
    }

    @Override
    public Lesson getLessonsByName(String name) {
        return lessonRepository.findByName(name);
    }

    @Override
    public void updateLesson(Lesson lesson) {
        lessonRepository.save(lesson);
    }

    @Override
    public void saveLesson(Lesson lesson) {
        lessonRepository.save(lesson);
    }

    @Override
    public void deleteLessonById(Long id) {
        lessonRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lesson> getLessonsByChapterId(Long chapterId) {
        log.info("Getting lessons by chapter id: {}", chapterId);
        return lessonRepository.findByChapterId(chapterId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lesson> getLessonsByCourseId(Long courseId) {
        log.info("Getting lessons by course id: {}", courseId);
        return lessonRepository.findByCourseId(courseId);
    }

}
