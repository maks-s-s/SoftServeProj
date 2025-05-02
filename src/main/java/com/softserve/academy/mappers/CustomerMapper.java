package com.softserve.academy.mappers;

import com.softserve.academy.dto.CustomerDTO;
import com.softserve.academy.model.Customer;

public class CustomerMapper {

    public static CustomerDTO toCustomerDTO(Customer customer, boolean withPassword){
        return CustomerDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .password(withPassword?customer.getPassword():null)
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .build();
    }
}
