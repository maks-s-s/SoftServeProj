package com.softserve.academy.controllers.view;

import com.softserve.academy.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductsViewController {

    public static ProductService prodSrv;

    @Autowired
    public ProductsViewController(ProductService prodSrv){
        this.prodSrv=prodSrv;
    }


    @GetMapping("/products")
    public String getProductsView(Model model){
        model.addAttribute("title", prodSrv.getProductsByCategory(0, RequestPa));
        return "products";
    }
}
