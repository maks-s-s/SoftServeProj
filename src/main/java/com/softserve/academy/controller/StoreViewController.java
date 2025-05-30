package com.softserve.academy.controller;


import com.softserve.academy.dto.StoreDTO;

import com.softserve.academy.mappers.StoreMapper;
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
import java.util.concurrent.ConcurrentSkipListMap;

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
        if (session.getAttribute("customer") == null) {return "redirect:/";}

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

    @GetMapping("/manageStores")
    public String manageStores(Model model, HttpSession session){
        if (session.getAttribute("customer") == null) {return "redirect:/";}

        model.addAttribute("customer", session.getAttribute("customer"));
        return "StoreManage";
    }

    @GetMapping("/newStore")
    public String showNewStorePage(Model model, HttpSession session){
        if (session.getAttribute("customer") == null) {return "redirect:/";}

        model.addAttribute("StoreDTO", new StoreDTO());
        model.addAttribute("alterStore", false);
        model.addAttribute("customer", session.getAttribute("customer"));
        return "AddStore";
    }

    @PostMapping("/newStore")
    public String processNewStorePage(Model model, HttpSession session,
                                      @Valid @ModelAttribute("StoreDTO") StoreDTO storeDTO){
        if (session.getAttribute("customer") == null) {return "redirect:/";}

        Customer customer = (Customer) session.getAttribute("customer");
        model.addAttribute("customer", session.getAttribute("customer"));
        if (customer.getRole() != Role.ADMIN){
            model.addAttribute("authError", "You don't have access to this page.");
        }
        storeSrv.addStore(StoreMapper.toStore(storeDTO));
        model.addAttribute("StoreDTO", new StoreDTO());
        model.addAttribute("alterStore", false);
        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("storeAdded", "Store successfully added.");
        return "AddStore";
    }

    @GetMapping("/alterStore")
    public String showAlterStorePage(Model model, HttpSession session){
        if (session.getAttribute("customer") == null) {return "redirect:/";}

        model.addAttribute("StoreDTO", new StoreDTO());
        model.addAttribute("stores", storeSrv.getAllStoresListType());
        model.addAttribute("alterStore", true);
        model.addAttribute("customer", session.getAttribute("customer"));
        return "AddStore";
    }

    @PostMapping("/alterStore")
    public String processAlterStorePage(Model model, HttpSession session,
                                         @Valid @ModelAttribute("StoreDTO") StoreDTO storeDTO){
        if (session.getAttribute("customer") == null) {return "redirect:/";}
        storeSrv.updateStore(storeDTO.id, storeDTO);
        model.addAttribute("StoreDTO", new StoreDTO());
        model.addAttribute("stores", storeSrv.getAllStoresListType());
        model.addAttribute("alterStore", true);
        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("storeEdited", "Store successfully updated.");
        return "AddStore";
    }


    @GetMapping("/pushProdToStore")
    public String pushProdToStore(Model model, HttpSession session) {
        if (session.getAttribute("customer") == null) {return "redirect:/";}

        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("pushOut", false);
        model.addAttribute("ProdToStoreForm", new ProdToStoreForm());
        model.addAttribute("stores", storeSrv.getAllStoresListType());
        model.addAttribute("prods", productService.getAllProductsListType());
        return "PushProdToStore";
    }


    @PostMapping("/pushProdToStore")
    public String processPushProdToStore(Model model, HttpSession session,
                                          @Valid @ModelAttribute("ProdToStoreForm") ProdToStoreForm prodToStoreForm) {
        if (session.getAttribute("customer") == null) {return "redirect:/";}

        Customer customer = (Customer) session.getAttribute("customer");
        if (customer.getRole() != Role.ADMIN){
            model.addAttribute("authError", "You don't have access to this page.");
            return "PushProdToStore";}
        model.addAttribute("pushOut", false);
        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("ProdToStoreForm", new ProdToStoreForm());
        model.addAttribute("stores", storeSrv.getAllStoresListType());
        model.addAttribute("prods", productService.getAllProductsListType());
        if (storeSrv.getStoreById(prodToStoreForm.getStoreId()).getProducts().contains(productService.getProductById(prodToStoreForm.getProdId()))) {
            model.addAttribute("prodExistsInStore", "This product is already selling in this store");
            return "PushProdToStore";
        }
        model.addAttribute("pushSuccess", "Product successfully pushed to store.");
        storeSrv.addProductToStore(prodToStoreForm.getStoreId(), prodToStoreForm.getProdId());
        return "PushProdToStore";
    }

    @GetMapping("/delStore")
    public String deleteStorePage(Model model, HttpSession session) {
        if (session.getAttribute("customer") == null) {return "redirect:/";}

        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("stores", storeSrv.getAllStoresListType());
        model.addAttribute("StoreDTO", new StoreDTO());
        return "delStore";
    }

    @PostMapping("/delStore")
    public String deleteStoreProcess(Model model, HttpSession session,
                                     @Valid @ModelAttribute("StoreDTO") StoreDTO storeDTO) {
        if (session.getAttribute("customer") == null) {return "redirect:/";}

        Customer customer = (Customer) session.getAttribute("customer");
        if (customer.getRole() != Role.ADMIN){
            model.addAttribute("authError", "You wish");
            model.addAttribute("customer", session.getAttribute("customer"));
            return "delStore";
        }
        storeSrv.deleteStoreById(storeDTO.getId());
        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("stores", storeSrv.getAllStoresListType());
        model.addAttribute("storeDeleted", "Store successfully deleted.");
        model.addAttribute("StoreDTO", new StoreDTO());
        return "delStore";
    }

    @GetMapping("/pullProdOffStore")
    public String pushOutProdPage(Model model, HttpSession session){
        model.addAttribute("pushOut", true);
        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("ProdToStoreForm", new ProdToStoreForm());
        model.addAttribute("stores", storeSrv.getAllStoresListType());
        model.addAttribute("prods", productService.getAllProductsListType());
        return "PushProdToStore";
    }

    @PostMapping("/pullProdOffStore")
    public String pushOutProdProcess(Model model, HttpSession session, @Valid @ModelAttribute("ProdToStoreForm") ProdToStoreForm ptsf){
        if (session.getAttribute("customer") == null) {return "redirect:/";}

        Customer customer = (Customer) session.getAttribute("customer");
        System.out.println("!!!!!!!!!!!!!"+ptsf.getStoreId()+"!!!!!!"+ptsf.getProdId()+"!!!");
        if (customer.getRole() != Role.ADMIN){
            model.addAttribute("authError", "You don't have access to this page.");
            return "PushProdToStore";}
        model.addAttribute("pushOut", true);
        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("ProdToStoreForm", new ProdToStoreForm());
        model.addAttribute("stores", storeSrv.getAllStoresListType());
        model.addAttribute("prods", productService.getAllProductsListType());
        if (!storeSrv.getStoreById(ptsf.getStoreId()).getProducts().contains(productService.getProductById(ptsf.getProdId()))){
            model.addAttribute("doesntSellError", "This product isn't selling in this store");
            return "PushProdToStore";
        }
        storeSrv.pullProdOffStore(ptsf.getProdId(), ptsf.getStoreId());
        model.addAttribute("pulledOffSuccess", "Product pulled off the store");
        return "PushProdToStore";
    }

}

