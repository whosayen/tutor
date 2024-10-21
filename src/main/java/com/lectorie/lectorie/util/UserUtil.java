package com.lectorie.lectorie.util;

import com.lectorie.lectorie.exception.custom.TutorNotFoundException;
import com.lectorie.lectorie.model.Tutor;
import com.lectorie.lectorie.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class UserUtil {

    public User extractUser(Principal currentUser) {
        return (User) ((UsernamePasswordAuthenticationToken) currentUser).getPrincipal();

        // if error exist redirect to login page
    }

    public Tutor getTutorFromPrincipal(Principal currentUser) {
        var user = extractUser(currentUser);

        var tutor = user.getTutor();
        if (tutor == null) {
            throw new TutorNotFoundException("You are not the TUTOR :<", 404);
        }

        return tutor;
    }



}