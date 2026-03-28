package com.hotelreservationsystem.repository.jdbc;

import com.hotelreservationsystem.config.SingletonDatabaseManager;
import com.hotelreservationsystem.domain.user.Customer;
import com.hotelreservationsystem.domain.user.Person;
import com.hotelreservationsystem.domain.user.Role;
import com.hotelreservationsystem.domain.user.Staff;
import com.hotelreservationsystem.repository.PersonRepository;

import java.sql.*;
import java.util.*;

public class JdbcPersonRepository implements PersonRepository {

    private final Connection connection;

    public JdbcPersonRepository() {
        this.connection = SingletonDatabaseManager.getInstance().getConnection();
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        String sql = "INSERT INTO person " +
                "(first_name, last_name, username, email, phone, tcno, hashed_password, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, 'CUSTOMER')";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, customer.getFirst_name());
            ps.setString(2, customer.getLast_name());
            ps.setString(3, customer.getUsername());
            ps.setString(4, customer.getEmail());
            ps.setString(5, customer.getPhone());
            ps.setString(6, customer.getTc());
            ps.setString(7, customer.getHashed_password());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    customer.setUser_id(rs.getLong(1));
                }
            }
            return customer;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving customer: " + e.getMessage(), e);
        }
    }

    @Override
    public Customer updateCustomer(Customer customer, long id) {
        String sql = "UPDATE person SET first_name = ?, last_name = ?, username = ?," +
                " email = ?, phone = ?, tcno = ?, hashed_password = ? WHERE id = ? AND role = 'CUSTOMER'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, customer.getFirst_name());
            ps.setString(2, customer.getLast_name());
            ps.setString(3, customer.getUsername());
            ps.setString(4, customer.getEmail());
            ps.setString(5, customer.getPhone());
            ps.setString(6, customer.getTc());
            ps.setString(7, customer.getHashed_password());
            ps.setLong(8, id);

            ps.executeUpdate();

            return customer;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating customer: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteCustomerById(Long id) {
        String sql = "DELETE FROM person WHERE id = ? AND role = 'CUSTOMER'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting customer: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Customer> findAllCustomers() {
        String sql = "SELECT * FROM person WHERE role = 'CUSTOMER'";
        List<Customer> customers = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String tcno = rs.getString("tcno");
                String hashed_password = rs.getString("hashed_password");

                Customer c = new Customer(id, first_name, last_name, username, email, phone, tcno, hashed_password, Role.CUSTOMER);
                customers.add(c);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all customers: " + e.getMessage(), e);
        }
        return customers;
    }

    @Override
    public List<Customer> findCustomerByFirstName(String first_name) {
        String sql = "SELECT * FROM person WHERE role = 'CUSTOMER' AND first_name = ?";
        List<Customer> customers = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, first_name);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long id = rs.getLong("id");
                    String last_name = rs.getString("last_name");
                    String username = rs.getString("username");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String tcno = rs.getString("tcno");
                    String hashed_password = rs.getString("hashed_password");

                    Customer c = new Customer(id, first_name, last_name, username, email, phone, tcno, hashed_password, Role.CUSTOMER);
                    customers.add(c);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding customers by first name: " + e.getMessage(), e);
        }
        return customers;
    }

    @Override
    public List<Customer> findCustomerByLastName(String last_name) {
        String sql = "SELECT * FROM person WHERE role = 'CUSTOMER' AND last_name = ?";
        List<Customer> customers = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, last_name);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long id = rs.getLong("id");
                    String first_name = rs.getString("first_name");
                    String username = rs.getString("username");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String tcno = rs.getString("tcno");
                    String hashed_password = rs.getString("hashed_password");

                    Customer c = new Customer(id, first_name, last_name, username, email, phone, tcno, hashed_password, Role.CUSTOMER);
                    customers.add(c);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding customers by last name: " + e.getMessage(), e);
        }
        return customers;
    }

    @Override
    public Optional<Customer> findCustomerByPhone(String phone) {
        String sql = "SELECT * FROM person WHERE role = 'CUSTOMER' AND phone = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, phone);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("id");
                    String first_name = rs.getString("first_name");
                    String last_name = rs.getString("last_name");
                    String username = rs.getString("username");
                    String email = rs.getString("email");
                    String tcno = rs.getString("tcno");
                    String hashed_password = rs.getString("hashed_password");

                    Customer c = new Customer(id, first_name, last_name, username, email, phone, tcno, hashed_password, Role.CUSTOMER);
                    return Optional.of(c);
                } else return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding customers by phone: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Customer> findCustomerByTC(String tcno) {
        String sql = "SELECT * FROM person WHERE role = 'CUSTOMER' AND tcno = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, tcno);


            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("id");
                    String first_name = rs.getString("first_name");
                    String last_name = rs.getString("last_name");
                    String username = rs.getString("username");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String hashed_password = rs.getString("hashed_password");

                    Customer c = new Customer(id, first_name, last_name, username, email, phone, tcno, hashed_password, Role.CUSTOMER);
                    return Optional.of(c);
                } else return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding customers by TCNO: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Customer> findCustomerByID(long id) {
        String sql = "SELECT * FROM person WHERE role = 'CUSTOMER' AND id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String first_name = rs.getString("first_name");
                    String last_name = rs.getString("last_name");
                    String username = rs.getString("username");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String tcno = rs.getString("tcno");
                    String hashed_password = rs.getString("hashed_password");

                    Customer c = new Customer(id, first_name, last_name, username, email, phone, tcno, hashed_password, Role.CUSTOMER);
                    return Optional.of(c);
                } else return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding customers by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Person> findByUsername(String username) {
        String sql = "SELECT * FROM person WHERE username = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("id");
                    String first_name = rs.getString("first_name");
                    String last_name = rs.getString("last_name");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String tcno = rs.getString("tcno");
                    String hashed_password = rs.getString("hashed_password");
                    String roleString = rs.getString("role");

                    if ("STAFF".equals(roleString)) {
                        Staff s = new Staff(id, first_name, last_name, username, email, phone, tcno, hashed_password, Role.STAFF);
                        return Optional.of(s);
                    } else if ("CUSTOMER".equals(roleString)) {
                        Customer c = new Customer(id, first_name, last_name, username, email, phone, tcno, hashed_password, Role.CUSTOMER);
                        return Optional.of(c);
                    } else return Optional.empty();
                } else return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding person by username: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByIdentityNumber(String tcno) {
        String sql="SELECT 1 FROM person WHERE tcno=? AND role='CUSTOMER' ";

        try(PreparedStatement ps=connection.prepareStatement(sql))
        {
            ps.setString(1,tcno);

            try(ResultSet rs=ps.executeQuery()){
                return rs.next();
            }

        }
        catch(SQLException e)
        {
            throw new RuntimeException("Error checking customer existence by TCNO", e);
        }

    }


    @Override
    public Map<Long, String> findCustomerIdNameMap() {

        String sql = """
        SELECT id, first_name, last_name
        FROM person
        WHERE role = 'CUSTOMER'
        """;

        Map<Long, String> map = new HashMap<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                String fullName =
                        rs.getString("first_name") + " " + rs.getString("last_name");

                map.put(id, fullName);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error loading customer id-name map", e);
        }

        return map;
    }


}