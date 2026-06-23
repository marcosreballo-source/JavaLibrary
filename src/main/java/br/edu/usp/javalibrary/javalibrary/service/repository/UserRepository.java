package br.edu.usp.javalibrary.javalibrary.service.repository;

import br.edu.usp.javalibrary.javalibrary.service.JsonService;
import br.edu.usp.javalibrary.javalibrary.service.domains.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;


public class UserRepository {
    static final String userFilePath = "users.json";

    private HashMap<UUID, User> users;

    private static UserRepository instance;

    public static UserRepository getInstance() {
        if (instance == null) instance = new UserRepository();
        return instance;
    }

    private UserRepository() {
    }

    private void loadUsersFile() {
        try {
            Type listType = new TypeToken<ArrayList<User>>(){}.getType();
            final ArrayList<User> users = JsonService.getInstance().loadJson(userFilePath, listType);
            this.users = new HashMap<>(users.stream().collect(Collectors.toMap(User::getId, user -> user)));
        } catch (Exception e) {
            users = new HashMap<>();
        }
    }

    private boolean saveUsersFile() {
        try {
            JsonService.getInstance().saveJson(userFilePath, getUsers());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ArrayList<User> getUsers() {
        if (users == null) loadUsersFile();
        return new ArrayList<>(users.values());
    }

    public Optional<User> getUser(UUID id) {
        if (users == null) loadUsersFile();
        return Optional.ofNullable(users.get(id));
    }

    public Optional<User> getUser(String emailAddress) {
        return getUsers().stream()
                .filter(user -> user.getEmailAddress().equals(emailAddress))
                .findFirst();
    }

    public boolean saveUsers(ArrayList<User> users) {
        this.users = new HashMap<>(users.stream().collect(Collectors.toMap(User::getId, user -> user)));
        return saveUsersFile();
    }

    public boolean saveUser(User user) {
        if (users == null) loadUsersFile();
        users.put(user.getId(), user);
        return saveUsersFile();
    }

    public boolean removeUser(UUID id) {
        if (users == null) loadUsersFile();
        users.remove(id);
        return saveUsersFile();
    }

}
