package com.hotelreservationsystem.domain.user;

public class Staff extends Person {

    public Staff(String first_name, String last_name,
             String username, String email, String phone,
             String tc, String hashed_password)
    {
        super(first_name, last_name,
                username, email, phone,
                tc, hashed_password);
    }

    public Staff(long user_id, String first_name, String last_name,
                    String username, String email, String phone,
                    String tc, String hashed_password, Role role)
    {
        super(user_id, first_name, last_name,
                username, email, phone,
                tc, hashed_password, role);
    }

    @Override
    public void showInfo() {
        super.showInfo();
    }
}
