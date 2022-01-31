package cz.ppro.poolapp.be.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "course_id")
    private Long id;
    @Column(name = "course_name")
    @NotBlank
    private String name;
    @Column(name = "course_description")
    @NotBlank
    private String description;
    @Column(name = "course_price")
    @NotNull
    private double price;
    @Column(name = "course_max_capacity")
    @NotNull
    private int maxCapacity;
    @ManyToOne
    private Account trainer;
    @OneToMany(mappedBy = "course")
    private List<AccountSignedCourse> accountSignedCourses;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm")
    private Date beginDate;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm")
    private Date endDate;
    @NotNull
    private int count;

    public Course(@NotBlank String name, @NotBlank String description, @NotNull double price, @NotNull int maxCapacity, Account trainer, List<AccountSignedCourse> accountSignedCourses, @NotNull Date beginDate, @NotNull Date endDate, @NotNull int count) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.maxCapacity = maxCapacity;
        this.trainer = trainer;
        this.accountSignedCourses = accountSignedCourses;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.count = count;
    }

    public List<AccountSignedCourse> getAccountSignedCourses() {
        return accountSignedCourses;
    }

    public void setAccountSignedCourses(List<AccountSignedCourse> accountSignedCourses) {
        this.accountSignedCourses = accountSignedCourses;
    }

    public Course(){}

    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }


    public Account getTrainer() {
        return trainer;
    }

    public void setTrainer(Account trainer) {
        this.trainer = trainer;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
