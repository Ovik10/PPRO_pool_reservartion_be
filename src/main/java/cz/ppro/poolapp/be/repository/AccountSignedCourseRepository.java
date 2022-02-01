package cz.ppro.poolapp.be.repository;

import cz.ppro.poolapp.be.model.Account;
import cz.ppro.poolapp.be.model.AccountSignedCourse;
import cz.ppro.poolapp.be.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountSignedCourseRepository extends JpaRepository<AccountSignedCourse, Long> {
    List<AccountSignedCourse> findAllByClientLogin(String login);
    AccountSignedCourse getAccountSignedCourseByClientAndAndCourse(Account client, Course course);
}
