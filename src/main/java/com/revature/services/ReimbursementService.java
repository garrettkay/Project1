package com.revature.services;

//Check UserService for general notes on Services

import com.revature.daos.ReimbursementDAO;
import com.revature.daos.UserDAO;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service //Makes a class a bean. Stereotype annotation.
public class ReimbursementService {

    //autowire the ReimbursementDAO with constructor injection so we can use the ReimbursementDAO methods
    private ReimbursementDAO rDAO;
    private UserDAO uDAO; //we also need some UserDAO methods!

    @Autowired
    public ReimbursementService(ReimbursementDAO rDAO, UserDAO uDAO) {
        this.rDAO = rDAO;
        this.uDAO = uDAO;
    }

    //This method takes in a new Reimbursement object and inserts it into the DB
    public Reimbursement addReimbursement(String description, int amount, String username) {

        //Another important role of the Service layer: data processing -
        //Turn the ReimbursementDTO into a Reimbursement to send to the DAO (DAO takes Reimbursement objects, not ReimbursementDTOs)

        //reimbursementId will be generated (so 0 is just a placeholder)
        //species and name come from the DTO
        //user will be set with the userId in the DTO
        Reimbursement newReimbursement = new Reimbursement(description, amount, null);

        //Use the UserDAO to get a User by id
        Optional<User> u = Optional.ofNullable(uDAO.findByUsername(username).getFirst());

        /*findById returns an OPTIONAL... What does that mean?
         it will either hold the value requested, or it won't. This helps us avoid NullPointerExc.
         BECAUSE... we can't access the data if we don't use the .get() method
         Check out how it helps us write error handling functionality: */
        if (u.isEmpty()) {
            throw new IllegalArgumentException("No user found with username: " + newReimbursement.getUser().getUsername());
        } else {
            //set the user object in the new Reimbursement
            newReimbursement.setUser(u.get()); //.get() is what extracts the value from the Optional
            //send the Reimbursement to the DAO
            return rDAO.save(newReimbursement);
        }
    }

    @Transactional
    public Reimbursement resolveReimbursement(int id, String status) {
        if (!(status.equals("pending") || status.equals("approved") || status.equals("denied"))) {
            throw new IllegalArgumentException("Invalid status");
        }

        Reimbursement r = rDAO.findByReimbursementId(id);

        if (r == null) {
            throw new IllegalArgumentException("No reimbursement found with id: " + id);
        } else {
            r.setStatus(status);
            return r;
        }
    }




    public List<Reimbursement> getUserReimbursements(String username) {
        Optional<User> u = Optional.ofNullable(uDAO.findByUsername(username).getFirst());
        if (u.isEmpty()) {
            throw new IllegalArgumentException("No user found with username: " + username);
        } else {
            return rDAO.findByUser(u.get());
        }
    }
    public List<Reimbursement> getPendingUserReimbursements(String username) {
        Optional<User> u = Optional.ofNullable(uDAO.findByUsername(username).getFirst());
        if (u.isEmpty()) {
            throw new IllegalArgumentException("No user found with username: " + username);
        } else {

            return rDAO.findByUserAndStatus(u.get(),"pending");
        }
    }

    public int getTotalPendingAmount(String username) {
        Optional<User> u = Optional.ofNullable(uDAO.findByUsername(username).getFirst());
        if (u.isEmpty()) {
            throw new IllegalArgumentException("No user found with id: " + username);
        } else {

            List<Reimbursement> pendingReimbursements = rDAO.findByUserAndStatus(u.get(),"pending");

            int totalAmount = 0;
            for (Reimbursement r: pendingReimbursements) {
                totalAmount = totalAmount + r.getAmount();
            }
            return totalAmount;
        }
    }

    public List<Reimbursement> getAllPendingReimbursements() {
        return rDAO.findByStatus("pending");
    }
    //This method gets all reimbursements from the DB
    public List<Reimbursement> getAllReimbursements() {
        //not much error handling in a get all
        return rDAO.findAll();
    }
}