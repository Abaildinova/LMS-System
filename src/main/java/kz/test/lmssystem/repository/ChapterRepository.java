package kz.test.lmssystem.repository;

import jakarta.transaction.Transactional;
import kz.test.lmssystem.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    Chapter findByName(String name);

    List<Chapter> findByCourseId(Long courseId);

}
