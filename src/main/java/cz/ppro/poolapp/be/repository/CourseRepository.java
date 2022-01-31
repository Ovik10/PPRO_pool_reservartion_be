package cz.ppro.poolapp.be.repository;

import cz.ppro.poolapp.be.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {

    List<Course> findAllByTrainerLogin(String login);

}
