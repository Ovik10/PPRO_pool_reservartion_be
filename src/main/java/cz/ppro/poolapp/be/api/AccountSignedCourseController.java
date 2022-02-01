package cz.ppro.poolapp.be.api;

import cz.ppro.poolapp.be.model.AccountSignedCourse;
import cz.ppro.poolapp.be.repository.AccountRepository;
import cz.ppro.poolapp.be.repository.AccountSignedCourseRepository;
import cz.ppro.poolapp.be.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequestMapping("/accountSignedCourse")
@RestController
public class AccountSignedCourseController {


    private CourseRepository courseRepository;
    private AccountRepository accountRepository;
    private AccountSignedCourseRepository accountSignedCourseRepository;

    @Autowired
    public AccountSignedCourseController(CourseRepository courseRepository, AccountRepository accountRepository, AccountSignedCourseRepository accountSignedCourseRepository){
        this.courseRepository=courseRepository;
        this.accountRepository=accountRepository;
        this.accountSignedCourseRepository=accountSignedCourseRepository;
    }

    @RequestMapping(value = "/allByClient/{clientLogin}", method = RequestMethod.GET)
    public List<AccountSignedCourse> getAllByClient(@PathVariable(value = "clientLogin") String clientLogin){
        return accountSignedCourseRepository.findAllByClientLogin(clientLogin);
    }
}
