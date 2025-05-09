package com.softserve.academy.controller;


import com.softserve.academy.dto.StoreDTO;

import com.softserve.academy.model.*;
import com.softserve.academy.repository.CustomerRepository;
import com.softserve.academy.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
    private final ProductService productService;
    StoreService storeSrv;
    CustomerRepository customerRepository;

    @Autowired
    public StoreViewController(StoreService storeSrv, CustomerRepository customerRepository, ProductService productService) {
        this.storeSrv = storeSrv;
        this.customerRepository = customerRepository;
        this.productService = productService;
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

    @GetMapping("/pushProdToStore")
    public String pushProdToStore(Model model, HttpSession session) {
        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("ProdToStoreForm", new ProdToStoreForm());
        model.addAttribute("stores", storeSrv.getAllStoresListType());
        model.addAttribute("prods", productService.getAllProductsListType());
        Customer customer = (Customer) session.getAttribute("customer");
        return "PushProdToStore";
    }

    @PostMapping("/pushProdToStore")
    public String processPushProdToStore(Model model, HttpSession session,
                                          @Valid @ModelAttribute("ProdToStoreForm") ProdToStoreForm prodToStoreForm) {
        Customer customer = (Customer) session.getAttribute("customer");
        boolean errsExist = false;
        if (customer.getRole() != Role.ADMIN){
            model.addAttribute("authError", "You don't have access to this page.");
            return "PushProdToStore";}
        if (storeSrv.getStoreById(prodToStoreForm.getStoreId()).getProducts().contains(productService.getProductById(prodToStoreForm.getProdId()))) {
            model.addAttribute("customer", session.getAttribute("customer"));
            model.addAttribute("ProdToStoreForm", new ProdToStoreForm());
            model.addAttribute("stores", storeSrv.getAllStoresListType());
            model.addAttribute("prods", productService.getAllProductsListType());
            model.addAttribute("prodExistsInStore", "This product is already selling in this store");
            return "PushProdToStore";
        }
        model.addAttribute("pushSuccess", "Product successfully pushed to store.");
        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("ProdToStoreForm", new ProdToStoreForm());
        model.addAttribute("stores", storeSrv.getAllStoresListType());
        model.addAttribute("prods", productService.getAllProductsListType());
        storeSrv.addProductToStore(prodToStoreForm.getStoreId(), prodToStoreForm.getProdId());
        return "PushProdToStore";
    }

}

