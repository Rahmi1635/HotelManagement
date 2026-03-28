package com.hotelreservationsystem.service.auth;

import com.hotelreservationsystem.domain.user.Customer;
import com.hotelreservationsystem.domain.user.Person;
import com.hotelreservationsystem.repository.PersonRepository;
import com.hotelreservationsystem.repository.jdbc.JdbcPersonRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class AuthService {
    private final PersonRepository userRepository = new JdbcPersonRepository();

    public Optional<Person> login(String username, String password) {

        Optional<Person> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        Person user = userOpt.get();

        if (!BCrypt.checkpw(password, user.getHashed_password())) {
            return Optional.empty();
        }

        return Optional.of(user);
    }
}
