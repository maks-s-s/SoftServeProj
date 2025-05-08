package com.softserve.academy.controller;

import com.softserve.academy.model.Customer;
import com.softserve.academy.model.Purchase;
import com.softserve.academy.model.Role;
import com.softserve.academy.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignController {

    @Autowired
    private PurchaseRestController purchaseRestController;
    @Autowired
    private CustomerService custSvc;

    @GetMapping("/")
    public String signInHome(HttpSession session) {
        if (!(session.getAttribute("customer") == null)) {return "redirect:/home";}
        return "signIn";
    }

    @GetMapping("/SignUp")
    public String signUp(HttpSession session) {
        if (!(session.getAttribute("customer") == null)) {return "redirect:/home";}
        return "signUp";
    }

    @GetMapping("/home")
    public String goHome(Model model, HttpSession session) {
        if (session.getAttribute("customer") == null) {return "redirect:/";}
        model.addAttribute("customer", session.getAttribute("customer"));
        return "homePage";
    }

    @GetMapping("/purchaseHistory")
    public String purchaseHistory(  Model model, HttpSession session, RedirectAttributes redirectAttributes,
                                    @RequestParam(name="byDate", defaultValue = "true") boolean sortByDate,
                                    @RequestParam(name="byPrice", defaultValue = "false") boolean sortByPrice,
                                    @RequestParam(name="otherCustomer", defaultValue = "null") String otherCustomer,
                                    @RequestParam(name="size", defaultValue = "4") int size,
                                    @RequestParam(name="page", defaultValue = "0") int page) {
        if (session.getAttribute("customer") == null) {
            return "redirect:/";
        }
        Customer customer = (Customer) session.getAttribute("customer");

        model.addAttribute("access", customer.getRole());
        if (!otherCustomer.equals("null")){
            if (customer.getRole() != Role.ADMIN) {
                return "redirect:/error-403";
            }
            if (!custSvc.existsByEmail(otherCustomer)) {
                redirectAttributes.addFlashAttribute("error", "There aren't any users with this email.");
                return "redirect:/purchaseHistoryPage";
            }
            customer = custSvc.findByEmail(otherCustomer);
        }


        Page<Purchase> purchases = purchaseRestController.getPurchasesByCustomerId(customer.getId(), size, page, sortByDate, sortByPrice);

        int maxPage = purchases.getTotalPages() - 1;
        if (maxPage > 0 && page > maxPage) {
            page = maxPage;
        }
        purchases = purchaseRestController.getPurchasesByCustomerId(customer.getId(), size, page, sortByDate, sortByPrice);
        model.addAttribute("customer", customer);
        model.addAttribute("purchases", purchases);
        return "purchaseHistoryPage";
    }
}