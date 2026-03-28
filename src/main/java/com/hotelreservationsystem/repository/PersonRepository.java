package com.hotelreservationsystem.repository;

import com.hotelreservationsystem.domain.user.Customer;
import com.hotelreservationsystem.domain.user.Person;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PersonRepository {

    Customer saveCustomer(Customer customer);

    Customer updateCustomer(Customer customer,long id);

    void deleteCustomerById(Long id);

    List<Customer> findAllCustomers();

    List<Customer> findCustomerByFirstName(String first_name);

    List<Customer> findCustomerByLastName(String last_name);

    Optional<Customer> findCustomerByPhone(String phone);

    Optional<Customer> findCustomerByTC(String tcno);

    Optional<Customer> findCustomerByID(long id);

    Optional<Person> findByUsername(String username);

    boolean existsByIdentityNumber(String tcno);

    Map<Long, String> findCustomerIdNameMap();
}
