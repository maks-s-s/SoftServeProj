package com.softserve.academy.controller;


import com.softserve.academy.dto.ProductDTO;
import com.softserve.academy.model.Customer;
import com.softserve.academy.model.Product;
import com.softserve.academy.model.Store;
import com.softserve.academy.repository.CustomerRepository;
import com.softserve.academy.repository.ProductRepository;
import com.softserve.academy.service.CategoryService;
import com.softserve.academy.service.ProductService;
import com.softserve.academy.service.StoreService;
import jakarta.servlet.http.HttpSession;
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
    CustomerRepository customerRepository;
    ProductService productService;
    CategoryService categoryService;
    ProductRepository productRepository;
    @Autowired
    ProductViewController(StoreService storeService, CustomerRepository customerRepository, ProductService productService, CategoryService categoryService, ProductRepository productRepository) {
        this.storeService = storeService;
        this.customerRepository = customerRepository;
        this.productService = productService;
        this.categoryService = categoryService;
        this.productRepository = productRepository;
    }

    @GetMapping("/ShowProdByStore/{id}")
    public String ShowProdByStore(HttpSession session,@PathVariable("id") Long id, Model model, @RequestParam(name = "size", defaultValue = "3") int size, @RequestParam(name = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page,size);
        Store store = storeService.getStoreById(id);
        Page<Product> products = storeService.getProductsByStore(id,pageable);
        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("id", id);
        model.addAttribute("store", store);
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", page < products.getTotalPages() - 1);
        return "products";
    }

    @GetMapping("/byCategory/{catId}")
    public String showProdsByCategory(@PathVariable("catId") Long id,
                                      Model model){
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> prods = productService.getProductsByCategory(id, pageable);
        model.addAttribute("prods", prods);
        model.addAttribute("catName", categoryService.getCategoryById(id).getName());
        return "ProdsByCategory";
    }

    @GetMapping("/buy/{prodId}")
    public String buy(@PathVariable("prodId") Long id, Model model, HttpSession session) {
        if(session.getAttribute("customer") == null) {return "redirect:/";}

        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("customer", session.getAttribute("customer"));
        return "BuyProductPage";
    }

    @GetMapping("/getAllProd")
    public String showProds(HttpSession session, Model model,
                            @RequestParam(name = "size", defaultValue = "6") int size,
                            @RequestParam(name = "page", defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page,size);
        Page<Product> products = productRepository.findAll(pageable);
        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", page < products.getTotalPages() - 1);
        return "allProducts";
    }
}
