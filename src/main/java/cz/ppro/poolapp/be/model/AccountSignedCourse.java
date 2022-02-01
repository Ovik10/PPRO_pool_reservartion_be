package cz.ppro.poolapp.be.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name="account_signed_course")
public class AccountSignedCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_signed_curse_id")
    private Long id;
    @NotNull
    @Column(name = "account_signed_curse_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm")
    private Date signDate;
    @JsonIgnore
    @ManyToOne
    private Account client;
    @ManyToOne
    @JsonIgnore
    private Course course;

    public AccountSignedCourse(@NotNull Date signDate, Account client, Course course) {
        this.signDate = signDate;
        this.client = client;
        this.course = course;
    }
    public AccountSignedCourse(){}

    public Long getId() {
        return id;
    }


    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public Account getClient() {
        return client;
    }

    public void setClient(Account client) {
        this.client = client;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
