package com.revature.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component //This Class will be registered as a Spring Bean
@Entity //This Class will be created as a table in the DB (In other words, a DB ENTITY)
@Table(schema="project1", name = "users") //@Table lets us set properties like table name. THIS IS NOT WHAT MAKES IT A TABLE
public class User {

    @Id //This makes the field the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //This makes our PK auto-increment (like serial)
    private int userId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;
    /* @Column isn't necessary UNLESS you want to set a name, or set constraints
       -nullable = NOT NULL constraint
       -unique = UNIQUE constraint */

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role = "default user"; //setting default role to 'user'


    /*One to Many relationship (goes hand in hand with the @ManyToOne in Reimbursement)

     mappedBy: This refers to the @ManyToOne field in Reimbursement that maps this relationship (user)

     fetch: refer to the Reimbursement class for info on this guy

     cascade: This lets us define what operations cascade down to dependent records\
        -CascadeType.ALL = all operations cascade down to dependent records (update, delete, etc) */
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore //prevents the circular reference in our JSON responses
    private List<Reimbursement> reimbursements;

    //boilerplate----------------------------------no args, all args, getter/setter/ toString

    public User() {}

    public User(int userid, String firstName, String lastName, String username, String password, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Reimbursement> getReimbursements() {
        return reimbursements;
    }

    public void setReimbursements(List<Reimbursement> reimbursements) {
        this.reimbursements = reimbursements;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
