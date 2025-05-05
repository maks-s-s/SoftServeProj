package com.softserve.academy.controller;

import com.softserve.academy.dto.CustomerDTO;
import com.softserve.academy.model.Customer;
import com.softserve.academy.model.Role;
import com.softserve.academy.service.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.softserve.academy.mappers.CustomerMapper.*;

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
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable("id") Long id){
        CustomerDTO customerDTO = toCustomerDTO(custSrv.getCustomerById(id), false);
        return ResponseEntity.ok(customerDTO);
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

    @GetMapping()
    public ResponseEntity<Page<CustomerDTO>> getAllCustomers(
            @RequestParam(name="size", defaultValue = "3") int size,
            @RequestParam(name="page", defaultValue = "0") int page
    ){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(custSrv.getAllCustomers(pageable));
    }

    @PostMapping("/giveAdminAccess")
    public void giveAdminAccess(@RequestParam("email") String email) {
        Customer customer = custSrv.findByEmail(email);
        customer.setRole(Role.ADMIN);
        custSrv.saveCustomer(customer);
    }
}
