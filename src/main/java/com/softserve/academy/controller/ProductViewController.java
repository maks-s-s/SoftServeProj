package com.softserve.academy.controller;


import com.softserve.academy.dto.ProductDTO;
import com.softserve.academy.dto.PurchaseDTO;
import com.softserve.academy.model.Customer;
import com.softserve.academy.model.Product;
import com.softserve.academy.model.Role;
import com.softserve.academy.model.Store;
import com.softserve.academy.repository.CustomerRepository;
import com.softserve.academy.repository.ProductRepository;
import com.softserve.academy.repository.StoreRepository;
import com.softserve.academy.service.CategoryService;
import com.softserve.academy.service.ProductService;
import com.softserve.academy.service.StoreService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductViewController {
    StoreService storeService;
    CustomerRepository customerRepository;
    ProductService productService;
    CategoryService categoryService;
    ProductRepository productRepository;
    StoreRepository storeRepository;
    @Autowired
    ProductViewController(StoreService storeService, CustomerRepository customerRepository, ProductService productService, CategoryService categoryService, ProductRepository productRepository,StoreRepository storeRepository) {
        this.storeService = storeService;
        this.customerRepository = customerRepository;
        this.productService = productService;
        this.categoryService = categoryService;
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
    }

    @GetMapping("/ShowProdByStore/{id}")
    public String ShowProdByStore(@PathVariable("id") Long id,
                                  @RequestParam(name = "size", defaultValue = "3") int size,
                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                  Model model,
                                  HttpSession session,
                                  @RequestParam(name="sortUp", defaultValue = "true") boolean sortUp,
                                  @RequestParam(name="sortDown", defaultValue = "false") boolean sortDown) {
        if (session.getAttribute("customer") == null) {return "redirect:/";}
        Pageable pageable = PageRequest.of(page,size);
        Store store = storeService.getStoreById(id);
        Page<Product> products = storeService.getProductsByStore(id,pageable,sortUp,sortDown);
        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("id", id);
        model.addAttribute("store", store);
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", page < products.getTotalPages() - 1);
        model.addAttribute("sortUp", sortUp);
        model.addAttribute("sortDown", sortDown);
        return "products";
    }

    @GetMapping("/byCategory/{catId}")
    public String showProdsByCategory(@PathVariable("catId") Long id,
                                      @RequestParam(name = "page", defaultValue = "0") int page,
                                      Model model, HttpSession session){
        if (session.getAttribute("customer") == null) {return "redirect:/";}
        Pageable pageable = PageRequest.of(page, 5);
        Page<Product> prods = productService.getProductsByCategory(id, pageable);
        model.addAttribute("catId", id);
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", page < prods.getTotalPages() - 1);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", prods.getTotalPages());
        model.addAttribute("prods", prods);
        model.addAttribute("catName", categoryService.getCategoryById(id).getName());
        return "ProdsByCategory";
    }

    @GetMapping("/buy/{prodId}")
    public String buy(@PathVariable("prodId") Long id, Model model, HttpSession session) {
        if(session.getAttribute("customer") == null) {return "redirect:/";}
        model.addAttribute("PurchaseDTO", new PurchaseDTO());
        System.out.println(productService.getProductById(id).getId());
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("customer", session.getAttribute("customer"));
        return "BuyProductPage";
    }

    @GetMapping("/getAllProd")
    public String showProds(HttpSession session, Model model,
                            @RequestParam(name = "size", defaultValue = "8") int size,
                            @RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name="sortUp", defaultValue = "true") boolean sortUp,
                            @RequestParam(name="sortDown", defaultValue = "false") boolean sortDown
    ) {
        if (session.getAttribute("customer") == null) {return "redirect:/";}
        Pageable pageable = PageRequest.of(page,size);
        Page<Product> products = null;
        if(sortDown){
            products = productRepository.findAllOrderByPriceDesc(pageable);
        }
        if(sortUp){
            products=productRepository.findAllOrderByPriceAsc(pageable);
        }

        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", page < products.getTotalPages() - 1);
        model.addAttribute("sortUp", sortUp);
        model.addAttribute("sortDown", sortDown);
        return "allProducts";
    }

    @GetMapping("/product/addProd")
    public String showAddProd(Model model, HttpSession session) {
        if (session.getAttribute("customer") == null) {return "redirect:/";}

        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("mode", "Add Product");
        model.addAttribute("categories", categoryService.findAllCategoriesListType());
        model.addAttribute("ProductDTO", new ProductDTO());
        Customer customer = (Customer) session.getAttribute("customer");
        model.addAttribute("customer", session.getAttribute("customer"));
        return "AddProd";
    }

    @PostMapping("/product/addProd")
    public String processAddProd(Model model,
                                 HttpSession session,
                                 @Valid @ModelAttribute("ProductDTO") ProductDTO productDTO) {
        if (session.getAttribute("customer") == null) {return "redirect:/";}

        Customer customer = (Customer) session.getAttribute("customer");
        boolean errsExist = false;
        if (customer.getRole() != Role.ADMIN){
            model.addAttribute("authError", "You don't have access to this page.");
            return "AddProd";
        }
        productService.addProduct(  Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .discount(productDTO.getDiscount()).build(), productDTO.getCategoryId());
        model.addAttribute("mode", "Add Product");
        model.addAttribute("ProductDTO", new ProductDTO());
        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("categories", categoryService.findAllCategoriesListType());
        model.addAttribute("prodAdded", "Product successfully added.");
        return "AddProd";
    }

    @GetMapping("/product/manageProducts")
    public String manageProducts(Model model, HttpSession session){
        if (session.getAttribute("customer") == null) {return "redirect:/";}

        model.addAttribute("customer", session.getAttribute("customer"));
        return "ProdManage";
    }

    @GetMapping("/product/delProd")
    public String deleteProductPage(Model model, HttpSession session) {
        if (session.getAttribute("customer") == null) {return "redirect:/";}

        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("products", productService.getAllProductsListType());
        model.addAttribute("ProductDTO", new ProductDTO());
        return "delProd";
    }

    @PostMapping("/product/delProd")
    public String deleteProductProcess(Model model, HttpSession session,
                                     @Valid @ModelAttribute("ProductDTO") ProductDTO productDTO) {
        if (session.getAttribute("customer") == null) {return "redirect:/";}

        Customer customer = (Customer) session.getAttribute("customer");
        if (customer.getRole() != Role.ADMIN){
            model.addAttribute("authError", "You wish");
            model.addAttribute("customer", session.getAttribute("customer"));
            return "delProd";
        }
        productService.deleteProduct(productDTO.getId());
        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("products", productService.getAllProductsListType());
        model.addAttribute("prodDeleted", "Product successfully deleted.");
        model.addAttribute("ProductDTO", new ProductDTO());
        return "delProd";
    }

    @GetMapping("/product/alterProd")
    public String editProductPage(Model model, HttpSession session) {
        if (session.getAttribute("customer") == null) {return "redirect:/";}

        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("prods", productService.getAllProductsListType());
        model.addAttribute("ProductDTO", new ProductDTO());
        model.addAttribute("categories", categoryService.findAllCategoriesListType());
        model.addAttribute("mode", "Alter Product");
        return "AddProd";
    }

    @PostMapping("/product/alterProd")
    public String processEditProductPage(Model model, HttpSession session,
                                         @Valid @ModelAttribute("ProductDTO") ProductDTO productDTO) {
        if (session.getAttribute("customer") == null) {return "redirect:/";}

        Customer customer = (Customer) session.getAttribute("customer");
        if (customer.getRole() != Role.ADMIN) {
            model.addAttribute("authError", "You don't have access to this page.");
        }
        productService.updateProduct(productDTO, productService.getProductById(productDTO.getId()));
        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("prods", productService.getAllProductsListType());
        model.addAttribute("ProductDTO", new ProductDTO());
        model.addAttribute("categories", categoryService.findAllCategoriesListType());
        model.addAttribute("mode", "Alter Product");
        model.addAttribute("prodEdited", "Product successfully edited.");
        return "AddProd";
    }

    @GetMapping("/findStoresByProduct")
    public String findStores(@RequestParam("productName") String productName, Model model, @RequestParam(name = "size", defaultValue = "8") int size,
                             @RequestParam(name = "page", defaultValue = "0") int page, HttpSession session) {
        if (session.getAttribute("customer") == null) {return "redirect:/";}

        Product product = productRepository.findByName(productName);
        Pageable pageable = PageRequest.of(page,size);
        Page<Store> stores = storeRepository.findByProductsContaining(product,pageable); // или другой метод
        model.addAttribute("stores", stores);
        model.addAttribute("productName", productName);
        return "storesByProduct";
    }
}
