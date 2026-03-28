package com.hotelreservationsystem.domain.user;

import java.time.LocalDate;

public class Customer extends Person{

   public Customer(String first_name, String last_name,
            String username, String email, String phone,
            String tc, String hashed_password)
   {
        super(first_name, last_name,
           username, email, phone,
           tc, hashed_password);
   }

    public Customer(long user_id, String first_name, String last_name,
                    String username, String email, String phone,
                    String tc, String hashed_password, Role role)
    {
        super(user_id, first_name, last_name,
                username, email, phone,
                tc, hashed_password, role);
    }

    public Customer()
    {
        super();
    }

    @Override
    public void showInfo() {
        super.showInfo();
    }

    public String toString() {
        return getFirst_name() + " " + getLast_name();
    }

}
