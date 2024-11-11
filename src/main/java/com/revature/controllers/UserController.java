package com.revature.controllers;

import com.revature.aspects.AdminOnly;
import com.revature.models.User;
import com.revature.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController //Combines @Controller (makes a class a bean) @ResponseBody(HTTP response body -> JSON)
@RequestMapping("/users") //All HTTP Requests made to /users will hit this Controller
public class UserController {

    //We need access to the UserService - let's use Constructor Injection!
    private UserService userService;

    @Autowired //This tells Spring to dependency inject the UserService
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //POST request to insert a new User
    @PostMapping //POST requests to /users will come here
    public ResponseEntity<User> registerUser(@RequestParam("firstName") String firstname, @RequestParam("lastName") String lastName, @RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("role") String role) {
        //Send the new user to the Service to be inserted, and save the returned User
        return ResponseEntity.status(201).body(userService.registerUser(firstname, lastName, username, password, role));

    }

    @AdminOnly
    @DeleteMapping
    public ResponseEntity<User> deleteUser(@RequestParam("userid") int userid){
        return ResponseEntity.ok(userService.deleteUser(userid));
    }
    //GET request to get all Users\
    @AdminOnly
    @GetMapping //GET requests to /users will come here
    public ResponseEntity<List<User>> getAllUsers(){

        //not much error handling in a get all
        List<User> allUsers = userService.getAllUsers();

        //send the users back with a 200 status code
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/search/{username}")
    public ResponseEntity<?> getUserByUsernameStartingWith(@PathVariable("username") String baseUsername){
        String username = baseUsername.substring(1);
        //ResponseEntity<?>??? what's that?
        //It lets us send any data type we want in the response
        //I avoid this when possible, it can make debugs pretty annoying
        //But I'll often use it since it's so flexible
        //if no user is found, we can send a message saying no user found
        if(userService.getUserByUsernameStartingWith(username) == null){
            return ResponseEntity.status(404).body("No user found with username: " + username);
        }
        //Return the found User with a 200 status code
        return ResponseEntity.ok(userService.getUserByUsernameStartingWith(username));
    }

    //GET requests to get a single User by username
    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable("username") String username){
        //ResponseEntity<?>??? what's that?
        //It lets us send any data type we want in the response
        //I avoid this when possible, it can make debugs pretty annoying
        //But I'll often use it since it's so flexible

        //if no user is found, we can send a message saying no user found
        Optional<List<User>> response = Optional.ofNullable(userService.getUserByUsername(username));
        if(response.isEmpty()){
            return ResponseEntity.status(404).body("No user found with username: " + username);
        }

        //Return the found User with a 200 status code
        return ResponseEntity.ok(response.get());
    }

    //Exception Handler for IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e){
        //Return a 400 (BAD REQUEST) status code with the exception message
        return ResponseEntity.status(400).body(e.getMessage());
    }

    //TODO: Handler for SqlExceptions!!

}
