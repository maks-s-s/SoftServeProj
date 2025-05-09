package com.softserve.academy.controller;

import com.softserve.academy.dto.PurchaseDTO;
import com.softserve.academy.model.Customer;
import com.softserve.academy.model.Purchase;
import com.softserve.academy.service.PurchaseService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@AllArgsConstructor
public class PurchaseViewController {
    private PurchaseService purchaseService;

    @PostMapping("/addPurchase")
    public String addPurchase(@RequestParam("prodId") Long prodId, @RequestParam("quantity") int quantity, Model model, HttpSession session) {
        PurchaseDTO purchase = new PurchaseDTO();
        Customer customer = (Customer) session.getAttribute("customer");
        purchase.setCustomerId(customer.getId());
        purchase.setQuantity(quantity);
        purchase.setProductId(prodId);
        purchase.setTime(LocalDateTime.now());
        purchaseService.addPurchase(purchase);
        return "redirect:/buy";
    }
}
