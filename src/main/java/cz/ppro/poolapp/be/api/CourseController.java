package cz.ppro.poolapp.be.api;

import cz.ppro.poolapp.be.exception.ResourceNotFoundException;
import cz.ppro.poolapp.be.exception.UnauthorizedAccessException;
import cz.ppro.poolapp.be.model.Account;
import cz.ppro.poolapp.be.model.AccountSignedCourse;
import cz.ppro.poolapp.be.model.Course;
import cz.ppro.poolapp.be.model.CustomUserDetails;
import cz.ppro.poolapp.be.repository.AccountRepository;
import cz.ppro.poolapp.be.repository.AccountSignedCourseRepository;
import cz.ppro.poolapp.be.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RequestMapping("/courses")
@RestController
public class CourseController {

    private CourseRepository courseRepository;
    private AccountRepository accountRepository;
    private AccountSignedCourseRepository accountSignedCourseRepository;

    @Autowired
    public CourseController(CourseRepository courseRepository, AccountRepository accountRepository, AccountSignedCourseRepository accountSignedCourseRepository){
        this.courseRepository=courseRepository;
        this.accountRepository=accountRepository;
        this.accountSignedCourseRepository=accountSignedCourseRepository;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Course> getAll(){
        return courseRepository.findAll();
    }
    @RequestMapping(value = "/allByTrainer", method = RequestMethod.GET)
    public List<Course> getAllByTrainer(@RequestAttribute String trainerLogin){
        return courseRepository.findAllByTrainerLogin(trainerLogin);
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public Course getById(@PathVariable(value = "id") Long id){
        return courseRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Course", "id", id));
    }

    @RequestMapping(value = "/create/{id}", method = RequestMethod.POST)
    public @ResponseBody Course create(@Valid @NonNull @RequestBody Course course,
                                       @PathVariable(value = "id") Long trainerId){
        Account trainer = accountRepository.findById(trainerId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", trainerId));
        course.setTrainer(trainer);
        trainer.getCreatedCourses().add(course);
        accountRepository.save(trainer);
        return courseRepository.save(course);

    }
    @RequestMapping(value = "/sign/{id}", method = RequestMethod.POST)
    public AccountSignedCourse sign (@PathVariable(value = "id") Long courseId, @RequestBody Date date,@AuthenticationPrincipal CustomUserDetails user){
       Account client = accountRepository.findById(user.getId()).orElseThrow(() -> new UnauthorizedAccessException(user.getUsername()));
       Course course = courseRepository.findById(courseId).orElseThrow(()->new ResourceNotFoundException("Course", "id", courseId));

       if(accountSignedCourseRepository.getAccountSignedCourseByClientAndAndCourse(client, course)==null) {
           if (course.getAccountSignedCourses().size() < course.getMaxCapacity()) {
               if(date.before(course.getBeginDate())) {

                   AccountSignedCourse accountSignedCourse = new AccountSignedCourse(date, client, course);

                   client.getSignedCourses().add(accountSignedCourse);
                   accountRepository.save(client);
                   course.getAccountSignedCourses().add(accountSignedCourse);
                   courseRepository.save(course);
                   return accountSignedCourseRepository.save(accountSignedCourse);
               }else{
                   throw new ResponseStatusException(
                           HttpStatus.BAD_REQUEST, "Doba možná pro zapsání kurzu již uplynula");
               }
           } else {
               throw new ResponseStatusException(
                       HttpStatus.BAD_REQUEST, "Kurz je plně obsazen");
           }
       }else {
           throw new ResponseStatusException(
                   HttpStatus.BAD_REQUEST, "Uživatel je již na tento kurz registrovaný");
       }
    }

    @RequestMapping(value = "/signout/{id}", method = RequestMethod.POST)
    public void signout (@PathVariable(value = "id") Long courseId, @RequestBody Date date, @AuthenticationPrincipal CustomUserDetails user){
        Account client = accountRepository.findById(user.getId()).orElseThrow(() -> new UnauthorizedAccessException(user.getUsername()));
        Course course = courseRepository.findById(courseId).orElseThrow(()->new ResourceNotFoundException("Course", "id", courseId));
        if(accountSignedCourseRepository.getAccountSignedCourseByClientAndAndCourse(client, course)!=null){
            if(date.before(course.getBeginDate())) {
                AccountSignedCourse accountSignedCourse = accountSignedCourseRepository.getAccountSignedCourseByClientAndAndCourse(client, course);
                client.getSignedCourses().remove(accountSignedCourse);
                course.getAccountSignedCourses().remove(accountSignedCourse);
                accountRepository.save(client);
                courseRepository.save(course);
                accountSignedCourseRepository.delete(accountSignedCourse);
            }else {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Doba možná pro odepsání z kurzu již uplynula");
            }
        }else{
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Uživatel není na tomto kurzu registrovaný");
        }
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.DELETE)
    public void remove(@PathVariable(value = "id") Long id){
        Course course = courseRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Course", "id", id));
        Account trainer = course.getTrainer();
        trainer.getCreatedCourses().remove(course);
        accountRepository.save(trainer);

        List<AccountSignedCourse> accountSignedCourses = course.getAccountSignedCourses();
        for(AccountSignedCourse asc : accountSignedCourses){
            Account client = asc.getClient();
            client.getSignedCourses().remove(asc);
            accountRepository.save(client);
            accountSignedCourseRepository.delete(asc);
        }
        courseRepository.delete(course);
    }
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public Course update(@PathVariable(value = "id") Long id,
                       @Valid @RequestBody Course courseDetails){
        Course course = courseRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Course", "id", id));
        int currentCapacity = course.getAccountSignedCourses().size();

        course.setName(courseDetails.getName());
        course.setBeginDate(courseDetails.getBeginDate());
        course.setCount(courseDetails.getCount());
        course.setDescription(courseDetails.getDescription());
        course.setEndDate(courseDetails.getEndDate());
        course.setPrice(courseDetails.getPrice());
        course.setTrainer(courseDetails.getTrainer());
        if(currentCapacity>courseDetails.getMaxCapacity()){
            course.setMaxCapacity(course.getAccountSignedCourses().size());
        }else{
            course.setMaxCapacity(courseDetails.getMaxCapacity());
        }
        Course updatedCourse = courseRepository.save(course);
        return updatedCourse;
    }


}
