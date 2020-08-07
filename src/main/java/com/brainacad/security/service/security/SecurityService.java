package com.brainacad.security.service.security;

public interface SecurityService {
    String findLoggedUsername ();

    void autoLogin (String username, String password);
}
