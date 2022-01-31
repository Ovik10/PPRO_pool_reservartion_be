package cz.ppro.poolapp.be.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name="account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id")
    private Long id;
    @Column(name = "account_first_name")
    @NotBlank
    private String firstName;
    @Column(name = "account_last_name")
    @NotBlank
    private String lastName;
    @Column(name = "account_email")
    @NotBlank
    private String email;
    @Column(name = "account_phone_number")
    @NotBlank
    private String phoneNumber;
    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private List<Ticket> tickets;
    @Column(name = "account_login")
    @NotBlank
    private String login;
    @Column(name = "account_password")
    @NotBlank
    private String password;
    @JsonIgnore
    @OneToMany(mappedBy = "trainer")
    private List<Course> createdCourses;
    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private List<AccountSignedCourse> signedCourses;
    @ManyToOne
    private Role role;

    public Account(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String email, @NotBlank String phoneNumber, List<Ticket> tickets, @NotBlank String login, @NotBlank String password, List<Course> createdCourses, List<AccountSignedCourse> signedCourses, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.tickets = tickets;
        this.login = login;
        this.password = password;
        this.createdCourses = createdCourses;
        this.signedCourses = signedCourses;
        this.role = role;
    }

    public Account() {
    }

    public Long getId() {
        return id;
    }

    public List<AccountSignedCourse> getSignedCourses() {
        return signedCourses;
    }

    public void setSignedCourses(List<AccountSignedCourse> signedCourses) {
        this.signedCourses = signedCourses;
    }

    public List<Course> getCreatedCourses() {
        return createdCourses;
    }

    public void setCreatedCourses(List<Course> createdCourses) {
        this.createdCourses = createdCourses;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }



}