package com.revature.models;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Entity //This Class will be a DB table thanks to Spring Data JPA
@Table(schema="P1",name = "reimbursements") //This lets us change the name of our DB table
@Component //1 of 4 stereotype annotations. Registers this class as a Spring Bean
public class Reimbursement {

    @Id //This is the primary key field
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Makes a serial incrementing PK
    private int reimbursementId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private String status;


    /*Primary Key / Foreign Key relationship!! (Many to One)

     fetch - defines whether the Dependency (User) is eagerly or lazily loaded
        -eager = loads dependency as soon as the app starts
        -lazy = loads dependency only when it's called

     @JoinColumn - defines the column that will be used to link these tables (PK of User)
        -We have to supply the name of the PK field that this FK is referring to */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId",referencedColumnName = "userId") //this links our FK to the PK in User (has to be the same amount!!!)
    private User user;


    //boilerplate code-----------------------

    public Reimbursement() {
    }

    public Reimbursement(String description, int amount, String status, User user) {
        this.description = description;
        this.amount = amount;
        this.status = status;
        this.user = user;
    }

    public int getReimbursementId() {
        return reimbursementId;
    }

    public void setReimbursementId(int reimbursementId) {
        this.reimbursementId = reimbursementId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Reimbursement{" +
                "reimbursementId=" + reimbursementId +
                ", description='" + description + '\'' +
                ", amount='" + amount + '\'' +
                ", status='" + status + '\'' +
                ", user=" + user +
                '}';
    }

}
