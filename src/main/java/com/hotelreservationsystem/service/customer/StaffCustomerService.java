package com.hotelreservationsystem.service.customer;

import com.hotelreservationsystem.domain.user.Customer;
import com.hotelreservationsystem.repository.jdbc.JdbcPersonRepository;

import java.util.*;

public class StaffCustomerService {

    private final JdbcPersonRepository personRepository = new JdbcPersonRepository();

    public List<Customer> listAllCustomers() {
        return personRepository.findAllCustomers();
    }

    public Optional<Customer> findById(long id) {
        return personRepository.findCustomerByID(id);
    }

    public List<Customer> searchCustomers(String firstName,
                                          String lastName,
                                          String phone,
                                          String tcno,
                                          String idText) {

        String idTrim = normalize(idText);
        if (idTrim != null) {
            try {
                long id = Long.parseLong(idTrim);
                return personRepository.findCustomerByID(id).map(List::of).orElseGet(List::of);
            } catch (NumberFormatException ignored) {
            }
        }

        String tcTrim = normalize(tcno);
        if (tcTrim != null) {
            return personRepository.findCustomerByTC(tcTrim).map(List::of).orElseGet(List::of);
        }

        String phoneTrim = normalize(phone);
        if (phoneTrim != null) {
            return personRepository.findCustomerByPhone(phoneTrim).map(List::of).orElseGet(List::of);
        }

        String fn = normalize(firstName);
        String ln = normalize(lastName);

        if (fn == null && ln == null) {
            return personRepository.findAllCustomers();
        }

        List<Customer> fnList = (fn != null) ? personRepository.findCustomerByFirstName(fn) : personRepository.findAllCustomers();
        List<Customer> lnList = (ln != null) ? personRepository.findCustomerByLastName(ln) : personRepository.findAllCustomers();

        Set<Long> lnIds = new HashSet<>();
        for (Customer c : lnList) lnIds.add(c.getUser_id());

        List<Customer> result = new ArrayList<>();
        for (Customer c : fnList) {
            if (ln == null || lnIds.contains(c.getUser_id())) {
                result.add(c);
            }
        }

        return result;
    }

    private String normalize(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
