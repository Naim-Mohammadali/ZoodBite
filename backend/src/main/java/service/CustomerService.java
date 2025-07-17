package service;

import model.Customer;

public class CustomerService extends UserService {


    public void updateAddress(Customer customer, String newAddress) {
        customer.setAddress(newAddress);
        userDAO.update(customer);
    }

    public void changePhone(Customer customer, String newPhone) throws Exception {
        if (userDAO.findByPhone(newPhone) != null) {
            throw new Exception("Phone already in use!");
        }
        customer.setPhone(newPhone);
        userDAO.update(customer);
    }

    public void changePassword(Customer customer, String newPassword) {
        customer.setPassword(newPassword);
        userDAO.update(customer);
    }

    public Customer viewProfile(Customer customer) {
        return (Customer) userDAO.findById(customer.getId());
    }
}
