package service;
import model.User;

public class CustomerService extends UserService{
    public void updateAddress (User customer, String newAddress) throws Exception{
        if (customer.getRole() != User.Role.CUSTOMER) {
            throw new Exception("Not a customer!");
        }
        customer.setAddress(newAddress);
        userDAO.update(customer);
    }
    public void changePhone (User customer, String newPhone) throws Exception{
        if (customer.getRole() != User.Role.CUSTOMER) {
            throw new Exception("Not a customer!");
        }
        if (userDAO.findByPhone(newPhone) != null)
        {
            throw new Exception("Phone already in use!");
        }
        customer.setPhone(newPhone);
        userDAO.update(customer);
    }
    public void changePassword (User customer, String newPassword) throws Exception{
        if (customer.getRole() != User.Role.CUSTOMER) {
            throw new Exception("Not a customer!");
        }
        customer.setPassword(newPassword);
        userDAO.update(customer);
    }
    public User viewProfile(User customer) throws Exception {
        if (customer.getRole() != User.Role.CUSTOMER) {
            throw new Exception("Not a customer!");
        }
        return userDAO.findById(customer.getId());
    }
}
