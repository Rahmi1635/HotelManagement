package com.hotelreservationsystem.service.customer;

import com.hotelreservationsystem.domain.user.Customer;
import com.hotelreservationsystem.domain.user.Person;
import com.hotelreservationsystem.repository.PersonRepository;
import com.hotelreservationsystem.repository.jdbc.JdbcPersonRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Map;

public class CustomerService {

    private final PersonRepository personRepository = new JdbcPersonRepository();

    public List<Customer> getAllCustomers() {
        return personRepository.findAllCustomers();
    }


    public void updateProfile(
            String username,
            String firstName,
            String lastName,
            String email,
            String phone,
            String password
    ) {

        Person person = personRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!(person instanceof Customer customer)) {
            throw new RuntimeException("User is not a customer");
        }

        if (firstName != null && !firstName.isBlank()) {
            customer.setFirst_name(firstName);
        }

        if (lastName != null && !lastName.isBlank()) {
            customer.setLast_name(lastName);
        }

        if (email != null && !email.isBlank()) {
            customer.setEmail(email);
        }


        if (phone != null && !phone.isBlank()) {
            customer.setPhone(phone);
        }

        if (password != null && !password.isBlank()) {
            if (password.length() < 6) {
                throw new IllegalArgumentException("Password must be at least 6 characters");
            }

            customer.setHashed_password(
                    BCrypt.hashpw(password, BCrypt.gensalt())
            );
        }

        personRepository.updateCustomer(customer, customer.getUser_id());
    }

    public Customer createCustomerByPersonnel(
            String firstName,
            String lastName,
            String email,
            String phone,
            String identityNumber
    ) {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }

        if (identityNumber == null || identityNumber.isBlank()) {
            throw new IllegalArgumentException("Identity number is required");
        }

        if (personRepository.existsByIdentityNumber(identityNumber)) {
            throw new IllegalStateException("Customer already exists");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        Customer customer = new Customer();
        customer.setFirst_name(firstName);
        customer.setLast_name(lastName);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setTc(identityNumber);

        return personRepository.saveCustomer(customer);
    }









}
