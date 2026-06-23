package br.edu.usp.javalibrary.javalibrary.service;

import br.edu.usp.javalibrary.javalibrary.service.domains.User;
import br.edu.usp.javalibrary.javalibrary.service.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

public class SessionService {

    private User user;

    private static SessionService instance;

    public static SessionService getInstance(){
        if (instance == null) instance = new SessionService();
        return instance;
    }

    private SessionService(){}

    public boolean login(String emailAddress, String password) {
      
        final Optional<User> user = UserRepository.getInstance().getUser(emailAddress);
        if (user.isPresent() && user.get().isPasswordCorrect(password)){
            this.user = user.get();
            return true;
        }
        return false;
    }

    public void logout(){
        user = null;
    }

    public boolean isLogged(){
        return user != null;
    }

    public String getUsername() {
        return user.getName();
    }

    public UUID getUserId() {
        return user.getId();
    }

}
