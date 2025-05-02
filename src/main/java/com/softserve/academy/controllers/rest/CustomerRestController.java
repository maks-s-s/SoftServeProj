package com.softserve.academy.controllers.rest;

import com.softserve.academy.dto.CustomerDTO;
import com.softserve.academy.dto.ObscuredCustomerDTO;
import com.softserve.academy.models.Customer;
import com.softserve.academy.services.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.executable.ValidateOnExecution;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/api/customers")
public class CustomerRestController {
    public final CustomerService custSrv;

    @PostMapping()
    public ResponseEntity<Void> addCustomer(@Valid @RequestBody CustomerDTO customer){
        if (customer.validate()){
            custSrv.saveCustomer(
                    Customer.builder()
                            .name(customer.getName())
                            .email(customer.getEmail())
                            .password(customer.getPassword())
                            .phoneNumber(customer.getPhoneNumber())
                            .build()
            );
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.unprocessableEntity().build();

    }
    @GetMapping("/{id}")
    public ResponseEntity<ObscuredCustomerDTO> getCustomer(@PathVariable("id") Long id){
        ObscuredCustomerDTO obscuredCustomerDTO = custSrv.getCustomerById(id);
        return ResponseEntity.ok(obscuredCustomerDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCustomer(@PathVariable("id") Long id, @Valid @RequestBody CustomerDTO customer){
        if(custSrv.updateCustomer(id, customer)) return ResponseEntity.accepted().build();
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") Long id){
        if (custSrv.deleteCustomer(id)) return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }
}
