package service;

import dao.UserDAO;
import model.Customer;

public class CustomerService extends UserService {

    public CustomerService() {
        super();
    }

    public Customer viewProfile(Long id) {
        return (Customer) userDAO.findById(id);
    }

    public void updateAddress(Long customerId, String newAddress) {
        Customer customer = (Customer) userDAO.findById(customerId);
        customer.setAddress(newAddress);
        userDAO.update(customer);
    }

    public void changePhone(Long customerId, String newPhone) {
        if (userDAO.findByPhone(newPhone) != null)
            throw new IllegalArgumentException("Phone already in use!");

        Customer customer = (Customer) userDAO.findById(customerId);
        customer.setPhone(newPhone);
        userDAO.update(customer);
    }

    public void changePassword(Long customerId, String newPassword) {
        Customer customer = (Customer) userDAO.findById(customerId);
        customer.setPassword(newPassword);
        userDAO.update(customer);
    }
}
