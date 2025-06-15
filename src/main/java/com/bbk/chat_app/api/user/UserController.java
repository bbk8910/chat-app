package com.bbk.chat_app.api.user;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository repo;

    public UserController(UserRepository repo) {
        this.repo = repo;
    }


    @GetMapping("/{username}")
    public Optional<User> getUserByUsername(@PathVariable String username) {
        return repo.findByUsername(username);
    }

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
      var userOptional=getUserByUsername(user.getUsername());
        return userOptional.orElseGet(() -> repo.save(user));
    }
}
