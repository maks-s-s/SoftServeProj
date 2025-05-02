package com.softserve.academy.services;


import com.softserve.academy.dto.CustomerDTO;
import com.softserve.academy.dto.ObscuredCustomerDTO;
import com.softserve.academy.models.*;
import com.softserve.academy.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    public final CustomerRepository custRepo;

    @Autowired
    public CustomerService(CustomerRepository custRepo) {
        this.custRepo = custRepo;
    }
    public void saveCustomer(Customer customer){
        custRepo.save(customer);
    }

    public boolean userNameExists(String username){
        return custRepo.getCustomersByUserName(username)!=null;
    }

    public ObscuredCustomerDTO getCustomerById(Long id){
        Customer customer = custRepo.findById(id).orElse(null);
        return ObscuredCustomerDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .build();
    }

    @Transactional
    public boolean updateCustomer(Long id, CustomerDTO customer){
        Customer cust = custRepo.findById(id).orElse(null);
        if (cust!=null){
            cust.setName(customer.getName()!=null?customer.getName():cust.getName());
            cust.setEmail(customer.getEmail()!=null?customer.getEmail():cust.getEmail());
            cust.setPhoneNumber(customer.getPhoneNumber()!=null?customer.getPhoneNumber():cust.getPhoneNumber());
            cust.setPasswd(customer.getPasswd()!=null?customer.getPasswd():cust.getPasswd());
            cust.setUserName(customer.getUserName()!=null?customer.getUserName():cust.getUserName());
            return true;
        }
        return false;
    }

    public boolean deleteCustomer(Long id){
        if (custRepo.findById(id).isEmpty()) return false;
        custRepo.deleteById(id);
        return true;
    }
}
