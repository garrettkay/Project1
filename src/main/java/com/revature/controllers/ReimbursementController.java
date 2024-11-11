package com.revature.controllers;

import com.revature.aspects.AdminOnly;
import com.revature.models.Reimbursement;
import com.revature.services.ReimbursementService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:3000")
@RestController //Combines @Controller and @ResponseBody
@RequestMapping("/reimbursements") //any HTTP request with "/reimbursements" will go here
public class ReimbursementController {

    //Autowire a ReimbursementService (with Constructor Injection) to use its method
    private ReimbursementService reimbursementService;

    @Autowired
    public ReimbursementController(ReimbursementService reimbursementService) {
        this.reimbursementService = reimbursementService;
    }

    //A method that inserts a new Reimbursement into the DB
    @PostMapping //POST requests to /reimbursements will come here
    public ResponseEntity<Reimbursement> insertReimbursement(@RequestParam("description") String description,@RequestParam("amount") int amount,@RequestParam("username") String username){
        //send the Reimbursement data to the service, and save the result in a Reimbursement object

        Reimbursement p = reimbursementService.addReimbursement(description,amount,username);

        //send the new Reimbursement data back to the client with 201 - CREATED
        return ResponseEntity.status(201).body(p);

    }
    @AdminOnly
    @PutMapping
    public ResponseEntity<Reimbursement> resolveReimbursement(@RequestParam("reimbursementid") int reimbursementid, @RequestParam("status") String status){
        return ResponseEntity.ok(reimbursementService.resolveReimbursement(reimbursementid,status));
    }

    //A method that gets all Reimbursements from the DB
    @AdminOnly
    @GetMapping("/all/{pending}") //GET requests to /reimbursements will come here
    public ResponseEntity<List<Reimbursement>> getAllReimbursements(@PathVariable("pending") Boolean pending){
        if (pending){
            return ResponseEntity.ok(reimbursementService.getAllPendingReimbursements());
        } else {
            return ResponseEntity.ok(reimbursementService.getAllReimbursements());
        }
    }

    @GetMapping("/user/{pending}/{username}")
    public ResponseEntity<List<Reimbursement>> getUserReimbursements(@PathVariable("pending") Boolean pending, @PathVariable("username") String username){
        if (pending){
            return ResponseEntity.ok(reimbursementService.getPendingUserReimbursements(username));
        } else {
            return ResponseEntity.ok(reimbursementService.getUserReimbursements(username));
        }
    }

    @GetMapping("/amount/{username}")
    public ResponseEntity<Integer> getTotalPendingAmount(@PathVariable("username") String username) {
        return ResponseEntity.ok(reimbursementService.getTotalPendingAmount(username));
    }


    //Exception Handler (stole this from the UserController)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e){
        //Return a 400 (BAD REQUEST) status code with the exception message
        return ResponseEntity.status(400).body(e.getMessage());
    }

}
