package com.softserve.academy.controller;


import com.softserve.academy.dto.StoreDTO;

import com.softserve.academy.model.Customer;
import com.softserve.academy.model.Store;
import com.softserve.academy.repository.CustomerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import com.softserve.academy.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Controller
public class StoreViewController {
    StoreService storeSrv;
    CustomerRepository customerRepository;

    @Autowired
    public StoreViewController(StoreService storeSrv,CustomerRepository customerRepository) {
        this.storeSrv = storeSrv;
        this.customerRepository = customerRepository;
    }
    @GetMapping("/StoreViewAll")
    public String StoreViewAll(HttpSession session,
                               Model model,
                               @RequestParam(name = "size", defaultValue = "3") int size,
                               @RequestParam(name = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Store> store = storeSrv.getAllStores(pageable);
        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("store", store);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", store.getTotalPages());
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", page < store.getTotalPages() - 1);


        return "Stores";

    }

}

