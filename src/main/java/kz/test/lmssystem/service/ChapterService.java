package kz.test.lmssystem.service;

import kz.test.lmssystem.entity.Chapter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChapterService {

    List<Chapter> getAllChapters();

    Chapter getChapterById(Long id);

    Chapter getChapterByName(String name);

    void updateChapter(Chapter chapter);

    void saveChapter(Chapter chapter);

    void deleteChapterById(Long id);

    List<Chapter> getChapterByCourseId(Long courseId);
}
