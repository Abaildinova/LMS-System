package kz.test.lmssystem.service.impl;

import kz.test.lmssystem.entity.Chapter;
import kz.test.lmssystem.exception.ResourceNotFoundException;
import kz.test.lmssystem.repository.ChapterRepository;
import kz.test.lmssystem.service.ChapterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;

    @Override
    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }

    @Override
    public Chapter getChapterById(Long id) {
        log.info("Fetching chapter by id: {}", id);
        return chapterRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Chapter with id {} not found", id);
                    return new ResourceNotFoundException("Chapter", id);
                });
    }

    @Override
    public Chapter getChapterByName(String name) {
        return chapterRepository.findByName(name);
    }

    @Override
    public void updateChapter(Chapter chapter) {
        chapterRepository.save(chapter);
    }

    @Override
    public void saveChapter(Chapter chapter) {
        chapterRepository.save(chapter);
    }

    @Override
    public void deleteChapterById(Long id) {
        chapterRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Chapter> getChapterByCourseId(Long courseId) {
        log.info("Getting chapters by course id: {}\", courseId {}", courseId);
        return chapterRepository.findByCourseId(courseId);
    }

}
