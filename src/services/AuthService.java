package services;

import DAOlayer.UserDAO;
import models.User;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private final UserDAO userDAO;

    public AuthService(){
        this.userDAO=new UserDAO();
    }

    public User register(String username,String password,String fullName){
        validateRegisterInput(username,password);

        if(userDAO.existsByUsername(username)){
            throw new IllegalArgumentException("Username already exists.");
        }

        String passwordHash=hashPassword(password);

        User user=new User(username,passwordHash,fullName);
        userDAO.addUser(user);

        return user;
    }

    public User login(String username,String password){
        validateLoginInput(username,password);

        User user=userDAO.findByUsername(username);

        if(user==null){
            throw new IllegalArgumentException("Invalid username or password");
        }

        if(!verifyPassword(password,user.getPasswordHash())){
            throw new IllegalArgumentException("Invalid username or password");
        }
        return user;
    }

    private void validateLoginInput(String username, String password) {
        validateUsername(username);

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    private boolean verifyPassword(String password,String passwordHash){
        return BCrypt.checkpw(password,passwordHash);
    }


    private String hashPassword(String password){
        return BCrypt.hashpw(password,BCrypt.gensalt());
    }

    private void validateRegisterInput(String username,String password){
        validateUsername(username);
        validatePassword(password);
    }

    private void validateUsername(String username){
        if(username==null || username.trim().isEmpty()){
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        if (username.trim().length() < 3) {
            throw new IllegalArgumentException("Username must contain at least 3 characters.");
        }

        if (username.trim().length() > 30) {
            throw new IllegalArgumentException("Username cannot exceed 30 characters.");
        }
    }

    private void validatePassword(String password){
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must contain at least 6 characters.");
        }
    }
}
