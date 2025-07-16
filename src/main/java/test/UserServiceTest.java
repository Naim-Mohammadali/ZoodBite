package test;

import model.User;
import service.CustomerService;

public class CustomerServiceTest {
    public static void main(String[] args) {
        CustomerService cs = new CustomerService();

        User customer = new User();
        customer.setName("Ali");
        customer.setEmail("ali@abc.com");
        customer.setPhone("123456789");
        customer.setPassword("secret");
        customer.setRole(User.Role.CUSTOMER);
        customer.setAddress("Beirut");

        try {
            cs.registerUser(customer);
            System.out.println("Registered customer: " + customer.getId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
