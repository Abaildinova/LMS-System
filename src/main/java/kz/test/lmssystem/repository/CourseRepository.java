package kz.test.lmssystem.repository;

import jakarta.transaction.Transactional;
import kz.test.lmssystem.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findByName(String name);
}
