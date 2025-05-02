package com.softserve.academy.services;


import com.softserve.academy.dto.CustomerDTO;
import com.softserve.academy.mappers.CustomerMapper;
import com.softserve.academy.models.*;
import com.softserve.academy.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import static com.softserve.academy.mappers.CustomerMapper.*;

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

/*    public CustomerDTO getCustomerDTOById(Long id, boolean withPassword){
        Customer customer = custRepo.findById(id).orElse(null);
        if (customer==null) return null;
        return CustomerDTO.builder()
                .id(customer.getId())
                .password(withPassword?customer.getPassword():null)
                .name(customer.getName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .build();
    }*/

    public Customer getCustomerById(Long id){
        return custRepo.findById(id).orElse(null);
    }

    @Transactional
    public boolean updateCustomer(Long id, CustomerDTO customer){
        Customer cust = custRepo.findById(id).orElse(null);
        if (cust!=null){
            cust.setName(customer.getName()!=null?customer.getName():cust.getName());
            cust.setEmail(customer.getEmail()!=null?customer.getEmail():cust.getEmail());
            cust.setPhoneNumber(customer.getPhoneNumber()!=null?customer.getPhoneNumber():cust.getPhoneNumber());
            cust.setPassword(customer.getPassword()!=null?customer.getPassword():cust.getPassword());
            return true;
        }
        return false;
    }

    public boolean deleteCustomer(Long id){
        if (custRepo.findById(id).isEmpty()) return false;
        custRepo.deleteById(id);
        return true;
    }

    public Page<CustomerDTO> getAllCustomers(Pageable pageable){
        return custRepo.findAll(pageable).map(customer ->toCustomerDTO(customer, false));
    }
}
