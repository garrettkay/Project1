package com.revature.daos;

import com.revature.models.Reimbursement;
import com.revature.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//Check UserDAO for general notes about how Spring Data DAOs work

@Repository //make this class a Bean (1 of the 4 steretype annotations)
public interface ReimbursementDAO extends JpaRepository<Reimbursement, Integer> {

    Reimbursement findByReimbursementId(int reimbursementId);

    List<Reimbursement> findByDescription(String description);

    List<Reimbursement> findByAmountBetween(int lower, int higher);

    List<Reimbursement> findByStatus(String status);

    List<Reimbursement> findByUser(User user);

    List<Reimbursement> findByUserAndStatus(User user, String status);

}
