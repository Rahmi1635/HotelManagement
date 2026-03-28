package com.hotelreservationsystem.context;

import com.hotelreservationsystem.domain.user.Person;

public class SessionContext {
    private static Person currentUser;

    private SessionContext() {}

    public static void setCurrentUser(Person user) {
        currentUser = user;
    }

    public static Person getCurrentUser() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
    }
}
