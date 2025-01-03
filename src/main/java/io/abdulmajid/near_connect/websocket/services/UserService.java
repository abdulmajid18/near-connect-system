package io.abdulmajid.near_connect.websocket.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final HashMap<String, Set<String>> userFriends = new HashMap<>();

    // Constructor to initialize users and friendships
    public UserService() {
        initializeUsers();
    }

    // Add a friend relationship between two users
    public void addFriend(String userA, String userB) {
        userFriends.putIfAbsent(userA, new HashSet<>());
        userFriends.putIfAbsent(userB, new HashSet<>());

        userFriends.get(userA).add(userB);
        userFriends.get(userB).add(userA);
    }

    // Get all friends of a given user
    public Set<String> getFriends(String userId) {
        return userFriends.getOrDefault(userId, new HashSet<>());
    }

    // Check if two users are friends
    public boolean areFriends(String userA, String userB) {
        return userFriends.containsKey(userA) &&
                userFriends.get(userA).contains(userB);
    }

    // Initialize default users and friendships
    private void initializeUsers() {
        // Example users
        String amnuhu1 = "amnuhu1";
        String amnuhu2 = "amnuhu2";

        // Adding friendships
        addFriend(amnuhu1, amnuhu2);
        addFriend(amnuhu2, amnuhu1);
    }
}
