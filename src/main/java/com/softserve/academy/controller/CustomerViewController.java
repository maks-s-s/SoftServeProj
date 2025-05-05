package com.softserve.academy.controller;

import com.softserve.academy.model.Customer;
import com.softserve.academy.model.Role;
import com.softserve.academy.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class CustomerViewController {
    public final CustomerService custSrv;

    public CustomerViewController(CustomerService custSrv) {
        this.custSrv = custSrv;
    }


    @PostMapping("/customer/add")
    public String addCustomer(  @RequestParam("name") String name,
                                @RequestParam("email") String email,
                                @RequestParam("password") String password,
                                @RequestParam("phoneNumber") String phoneNumber,
                                Model model) {
        Customer customer = Customer.builder()
                .name(name)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .build();
        System.out.println(customer);
        custSrv.saveCustomer(customer);

        model.addAttribute("customer", customer);

        return "redirect:/";
    }

    @PostMapping("/customer/verify")
    public String verify (@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session, Model model) {
        Customer customer = custSrv.findByEmail(email);
        if (customer != null && customer.getPassword().equals(password)) {
            session.setAttribute("customer", customer);
            return "redirect:/home";
        }
        else {
            model.addAttribute("error", "Wrong password");
            return "signIn";
        }
    }

    @GetMapping("/customer/logout")
    public String logout (HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

/*       @GetMapping("/customer/get/{id}")
        public Customer getCustomer(@PathVariable("id") Long id) {
            return custSrv.getCustomerById(id);
        }*/

/*        @GetMapping("/customer/getAll")
        public List<Customer> getAllCustomer() {return custRepo.findAll();}*/
}
