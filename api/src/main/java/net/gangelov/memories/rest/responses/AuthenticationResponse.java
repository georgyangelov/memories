package net.gangelov.memories.rest.responses;

import net.gangelov.memories.models.User;

public class AuthenticationResponse {
    public User user;
    public String accessToken;

    public AuthenticationResponse(User user) {
        this.user = user;
        this.accessToken = user.accessToken;
    }
}
