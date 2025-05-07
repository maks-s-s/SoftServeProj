package com.softserve.academy.controller;

import com.softserve.academy.model.*;
import com.softserve.academy.service.CustomerService;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class SignControllerTest {

    @Mock
    private PurchaseRestController purchaseRestController;
    @Mock
    private CustomerService custSvc;
    @InjectMocks
    private CustomerViewController customerViewController;

    private MockMvc mockMvc;
    private MockHttpSession session;
    List<Customer> customers;
    List<Purchase> purchases;
    List<Product> products;
    Set<Store> stores;
    Category phonesCategory;
    Customer customer;
    Purchase purchase;
    Product product;
    Store store;

    @BeforeEach
    void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("classpath:/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(customerViewController)
                .setViewResolvers(viewResolver)
                .build();

        stores = new HashSet<>();
        store = new Store();
        store.setId(1L);
        store.setName("Comfy");
        store.setLocation("Ukraine");
        store.setContactNumber("+380960000000");
        HashSet<Product> pr = new HashSet<>();
        pr.add(product);
        store.setProducts(pr);
        store.setEmail("comfy@example.com");
        stores.add(store);

        phonesCategory = new Category();
        phonesCategory.setId(1L);
        phonesCategory.setName("phones");
        phonesCategory.setProducts(products);

        products = new ArrayList<>();
        product = new Product();
        product.setId(1L);
        product.setName("Iphone");
        product.setPrice(BigDecimal.valueOf(50));
        product.setCategory(phonesCategory);
        product.setStores(stores);
        products.add(product);

        customers = new ArrayList<>();
        customer = new Customer();
        customer.setId(1L);
        customer.setRole(Role.USER);
        customer.setName("Example");
        customer.setEmail("example@example.com");
        customer.setPhoneNumber("+380950000000");
        customer.setPassword("example");
        customers.add(customer);

        purchases = new ArrayList<>();
        purchase = new Purchase();
        purchase.setId(1L);
        purchase.setCustomer(customer);
        purchase.setProduct(product);
        purchase.setQuantity(2);
        purchase.setTotalPrice(BigDecimal.valueOf(100));
        purchase.setPurchaseDate(LocalDateTime.now().minusHours(2));
    }

    @Test
    void login_withValidCredentials_shouldRedirectToHomePage() throws Exception{

    }
}
