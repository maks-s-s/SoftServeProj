package com.softserve.academy.controller;


import com.softserve.academy.dto.ProductDTO;
import com.softserve.academy.model.Product;
import com.softserve.academy.model.Store;
import com.softserve.academy.service.ProductService;
import com.softserve.academy.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductViewController {
    StoreService storeService;
    @Autowired
    ProductViewController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/ShowProdByStore/{id}")
    public String ShowProdByStore(@PathVariable("id") Long id, Model model, @RequestParam(name = "size", defaultValue = "3") int size, @RequestParam(name = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page,size);
        Store store = storeService.getStoreById(id);
        Page<Product> products = storeService.getProductsByStore(id,pageable);
        model.addAttribute("id", id);
        model.addAttribute("store", store);
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", page < products.getTotalPages() - 1);
        return "products";
    }
}
