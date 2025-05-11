package com.softserve.academy.controller;

import com.softserve.academy.dto.CategoryDTO;
import com.softserve.academy.mappers.CategoryMapper;
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
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
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
                                 @RequestParam(name = "page", defaultValue = "0") int page) {
        if (session.getAttribute("customer") == null) {return "redirect:/";}
        Pageable pageable = PageRequest.of(page, 5);
        Page<Category> categories = catSvc.findAllCategories(pageable);
        System.out.println(categories);
        model.addAttribute("categories", categories);
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", page < categories.getTotalPages() - 1);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categories.getTotalPages());
        model.addAttribute("customer", session.getAttribute("customer"));
        return "AllCategories";
    }

    @GetMapping("/addProdToCat")
    public String showProductToCategory(Model model, HttpSession session) {
        if (session.getAttribute("customer") == null) {return "redirect:/";}

        model.addAttribute("ProdToCatForm", new ProdToCatForm());
        model.addAttribute("prods", prodSvc.getAllProductsListType());
        model.addAttribute("cats", catSvc.findAllCategoriesListType());
        Customer customer = (Customer) session.getAttribute("customer");
        model.addAttribute("customer", session.getAttribute("customer"));
        return "AddProdToCat";
    }

    @PostMapping("/addProdToCat")
    public String processProductToCategory(Model model, HttpSession session,
                                           @Valid @ModelAttribute("ProdToCatForm") ProdToCatForm prodToCatForm) {
        if (session.getAttribute("customer") == null) {return "redirect:/";}

        Customer customer = (Customer) session.getAttribute("customer");
        model.addAttribute("customer", session.getAttribute("customer"));
        if (catSvc.getCategoryById(prodToCatForm.getCatId()).getProducts().contains(prodSvc.getProductById(prodToCatForm.getProdId()))) {
            model.addAttribute("prods", prodSvc.getAllProductsListType());
            model.addAttribute("cats", catSvc.findAllCategoriesListType());
            model.addAttribute("ProdToCatForm", new ProdToCatForm());
            model.addAttribute("prodInCatError", "Product with this id is already in this category.");
            return "AddProdToCat";
        }
        catSvc.addProdToCategory(prodToCatForm.getProdId(), prodToCatForm.getCatId());
        model.addAttribute("prods", prodSvc.getAllProductsListType());
        model.addAttribute("cats", catSvc.findAllCategoriesListType());
        model.addAttribute("ProdToCatForm", new ProdToCatForm());
        model.addAttribute("catAdded", "Product successfully added to category.");
        return "AddProdToCat";
    }

    @GetMapping("/manageCategories")
    public String manageCategories(Model model, HttpSession session){
        if (session.getAttribute("customer") == null) {return "redirect:/";}

        model.addAttribute("customer", session.getAttribute("customer"));
        return "CatManage";
    }

    @GetMapping("/newCategory")
    public String showNewCategoryPage(Model model, HttpSession session){
        if (session.getAttribute("customer") == null) {return "redirect:/";}

        model.addAttribute("CategoryDTO", new Category());
        model.addAttribute("alterCategory", false);
        model.addAttribute("customer", session.getAttribute("customer"));
        return "AddCategory";
    }

    @PostMapping("/newCategory")
    public String processNewCategoryPage(Model model, HttpSession session,
                                         @Valid @ModelAttribute("CategoryDTO") CategoryDTO categoryDTO){
        Customer customer = (Customer) session.getAttribute("customer");
        for (Category cat:catSvc.findAllCategoriesListType()){
            if (cat.getName().equals(categoryDTO.getName())){
                model.addAttribute("categoryNameError", "Category with this name already exists.");
                model.addAttribute("alterCategory", false);
                model.addAttribute("CategoryDTO", new Category());
                model.addAttribute("customer", session.getAttribute("customer"));
                return "AddCategory";
            }
        }
        if (customer.getRole() != Role.ADMIN){
            model.addAttribute("authError", "You wish");
            return "AddCategory";
        }
        catSvc.addCategory(CategoryMapper.toCategory(categoryDTO));
        model.addAttribute("CategoryDTO", new Category());
        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("alterCategory", false);
        model.addAttribute("categoryAdded", "Category successfully added.");
        return "AddCategory";
    }


    @GetMapping("/alterCategory")
    public String alterCategoryPage(Model model, HttpSession session){
        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("cats", catSvc.findAllCategoriesListType());
        model.addAttribute("CategoryDTO", new Category());
        model.addAttribute("alterCategory", true);
        return "AddCategory";
    }

    @PostMapping("/alterCategory")
    public String processAlterCategory(Model model, HttpSession session,
                                       @Valid @ModelAttribute CategoryDTO categoryDTO){
        if (session.getAttribute("customer") == null) {return "redirect:/";}
        catSvc.updateCategory(categoryDTO.getId(), categoryDTO);
        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("cats", catSvc.findAllCategoriesListType());
        model.addAttribute("CategoryDTO", new Category());
        model.addAttribute("alterCategory", true);
        return "AddCategory";
    }


    @GetMapping("/delCat")
    public String deleteCategoryPage(Model model, HttpSession session) {
        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("cats", catSvc.findAllCategoriesListType());
        model.addAttribute("CategoryDTO", new Category());
        return "delCat";
    }

    @PostMapping("/delCat")
    public String deleteCategoryProcess(Model model, HttpSession session,
                                       @Valid @ModelAttribute("CategoryDTO") CategoryDTO categoryDTO) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer.getRole() != Role.ADMIN){
            model.addAttribute("authError", "You wish");
            model.addAttribute("customer", session.getAttribute("customer"));
            return "delCat";
        }
        catSvc.deleteCategory(categoryDTO.getId());
        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("cats", catSvc.findAllCategoriesListType());
        model.addAttribute("catDeleted", "Category successfully deleted.");
        model.addAttribute("CategoryDTO", new Category());
        return "delCat";
    }
}