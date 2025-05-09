package com.softserve.academy.controller;

import com.softserve.academy.model.Category;
import com.softserve.academy.model.Customer;
import com.softserve.academy.model.ProdToCatForm;
import com.softserve.academy.model.Role;
import com.softserve.academy.repository.CustomerRepository;
import com.softserve.academy.service.CategoryService;
import com.softserve.academy.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/category")
@Controller
public class CategoryViewController {
    public final CategoryService catSvc;
    public final ProductService prodSvc;
    CustomerRepository customerRepository;

    @Autowired
    public CategoryViewController(CategoryService categoryService,
                                  ProductService productService, CustomerRepository customerRepository) {
        this.catSvc = categoryService;
        this.prodSvc = productService;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/goToCategories")
    public String showCategories(HttpSession session,
                                 Model model,
                                 @RequestParam(name = "size", defaultValue = "5") int size,
                                 @RequestParam(name = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categories = catSvc.findAllCategories(pageable);
        System.out.println(categories);
        model.addAttribute("categories", categories);
        model.addAttribute("customer", session.getAttribute("customer"));
        return "AllCategories"; // categories.html
    }

    @GetMapping("/addProdToCat")
    public String showProductToCategory(Model model, HttpSession session) {
        model.addAttribute("ProdToCatForm", new ProdToCatForm());
        Customer customer = (Customer) session.getAttribute("customer");
        return "AddProdToCat";
    }

    @PostMapping("/addProdToCat")
    public String processProductToCategory(Model model, HttpSession session,
                                           @Valid @ModelAttribute("ProdToCatForm") ProdToCatForm prodToCatForm) {
        Customer customer = (Customer) session.getAttribute("customer");
        boolean errsExist = false;
        if (customer.getRole() != Role.ADMIN){
            model.addAttribute("authError", "You don't have access to this page.");
            return "AddProdToCat";}
        if (prodSvc.getProductById(prodToCatForm.getProdId())==null) {
            model.addAttribute("prodIDError", "Product with this id doesn't exist.");
            errsExist=true;
        }
        if (catSvc.getCategoryById(prodToCatForm.getCatId())==null) {
            model.addAttribute("catIDError", "Category with this id doesn't exist.");
            errsExist=true;
        }
        if (errsExist){ return "AddProdToCat";}
        if (catSvc.getCategoryById(prodToCatForm.getCatId()).getProducts().contains(prodSvc.getProductById(prodToCatForm.getProdId()))) {
            model.addAttribute("prodInCatError", "Product with this id is already in this category.");
            return "AddProdToCat";
        }
        catSvc.addProdToCategory(prodToCatForm.getProdId(), prodToCatForm.getCatId());
        model.addAttribute("prodAdded", "Product successfully added to category.");
        return "AddProdToCat";
    }

}