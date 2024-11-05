package com.revature.services;

import com.revature.daos.UserDAO;
import com.revature.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*What is the Service layer?? It's also known as the "Business Logic Layer" because...

    -This is where we do any data processing/error handling that DOESN'T have to do with the DB or HTTP
        -DAO handles DB operations
        -Controller handles HTTP requests/responses
    -EVERYTHING in between should live in the Service layer! */

@Service //1 of the 4 stereotype annotations. It registers this Class as a Spring Bean
public class UserService {

    //We can't instantiate Interfaces like Classes... how do we get access to our DAO methods?
    //DEPENDENCY INJECTION! With the @Autowired dependency
    private UserDAO uDAO;

    //This is CONSTRUCTOR INJECTION (not setter injection, not field injection)
    @Autowired
    public UserService(UserDAO userDAO) {
        this.uDAO = userDAO;
    }

    //This method inserts new Users into the DB
    public User registerUser(String firstName, String lastName, String username, String password, String role){
        User newUser = new User(0, firstName, lastName, username, password, role);
        //TODO: Check that the username is unique (get user by username, see if it's null)
        User u = uDAO.findByUsername(newUser.getUsername());
        //User u = findByUsername(newUser.getUsername());
        //If u is not null, throw an exception because the username already exists
        if(u != null){
            //It will be the Controller's job to handle this
            throw new IllegalArgumentException("Username already exists!");
        }
        //Make sure the username is present in the new User (TODO: password too)
        if(newUser.getUsername() == null || newUser.getUsername().isBlank()) {
            //It will be the Controller's job to handle this
            throw new IllegalArgumentException("Username cannot be empty!");
        }

        if(newUser.getPassword() == null || newUser.getPassword().isBlank()){
            //It will be the Controller's job to handle this
            throw new IllegalArgumentException("Password cannot be empty!");
        }
        //.save() is the JPA method to insert data into the DB. We can also use this for updates
        //It also returns the saved object, so we can just return the method call. Convenient!
        return uDAO.save(newUser);
    }

    public User deleteUser(int userid) {
        User u = uDAO.findByUserId(userid);
        if (u == null) {
            throw new IllegalArgumentException("No user found with id: " + userid);
        }

        uDAO.deleteById(userid);
        return u;
    }

    //This method gets a user by username
    public User getUserByUsername(String username){

        //a little error handling
        if(username == null || username.isBlank()){
            throw new IllegalArgumentException("Please search for a valid username!");
        }

        //TODO: we could check if the returned user is null and throw an exception
        //if(userDAO.findByUsername(username) == null){throw Exp}

        //findByUsername is a method WE DEFINED in the UserDAO (but didn't have to implement!)
        return uDAO.findByUsername(username);
    }
    //This method gets all users from the DB
    public List<User> getAllUsers(){
        //findAll() is a JPA method that returns all records in a table
        return uDAO.findAll();

        //Not much error handling in a get all... maybe checking to see if it's empty?
    }
}
