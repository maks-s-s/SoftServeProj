package com.softserve.academy.controller;

import com.softserve.academy.dto.PurchaseDTO;
import com.softserve.academy.model.Customer;
import com.softserve.academy.model.Purchase;
import com.softserve.academy.service.ProductService;
import com.softserve.academy.service.PurchaseService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@AllArgsConstructor
public class PurchaseViewController {
    private final ProductService productService;
    private PurchaseService purchaseService;

    @PostMapping("/addPurchase")
    public String addPurchase(Model model, HttpSession session,
                              @ModelAttribute("PurchaseDTO") PurchaseDTO purchaseDTO) {
        if (session.getAttribute("customer") == null) {return "redirect:/";}
        System.out.println("!!!!!!!!!!!!!!!!!"+purchaseDTO.productId+"!!!!"+purchaseDTO.quantity);
        Customer customer = (Customer) session.getAttribute("customer");
        purchaseDTO.setCustomerId(customer.getId());
        purchaseDTO.setTime();
        purchaseService.addPurchase(purchaseDTO);
        model.addAttribute("customer", session.getAttribute("customer"));
        model.addAttribute("success", true);
        model.addAttribute("purchaseDTO", new PurchaseDTO());
        model.addAttribute("product", productService.getProductById(purchaseDTO.productId));
        return "BuyProductPage";
    }
}
