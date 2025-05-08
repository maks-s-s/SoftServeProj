package com.softserve.academy;

import com.softserve.academy.controller.PurchaseRestController;
import com.softserve.academy.controller.SignController;
import com.softserve.academy.model.*;
import com.softserve.academy.service.CustomerService;
import com.softserve.academy.service.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PurchaseHistoryTest {

    @Mock
    PurchaseService purchaseService;
    @Mock
    private PurchaseRestController purchaseRestController;
    @Mock
    private CustomerService customerService;
    @InjectMocks
    private SignController signController;



    MockMvc mockMvc;
    MockHttpSession session;
    Customer customer;
    List<Customer> customers;
    List<Purchase> purchasesList;
    List<Product> products;
    Set<Store> stores;
    Category phonesCategory;
    Purchase purchase;
    Product product;
    Store store;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(signController, purchaseRestController)
                .build();

        session = new MockHttpSession();

        customer = new Customer();
        customer.setId(1L);
        customer.setRole(Role.USER);
        customer.setName("Example");
        customer.setEmail("example@example.com");
        customer.setPhoneNumber("+380950000000");
        customer.setPassword("example");

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

        purchasesList = new ArrayList<>();
        for (long i = 1; i <= 5; i++) {
            Purchase purchase = new Purchase();
            purchase.setId(i);
            purchase.setCustomer(customer);
            purchase.setProduct(product);
            purchase.setQuantity(1);
            purchase.setTotalPrice(BigDecimal.valueOf(50 * i));
            purchase.setPurchaseDate(LocalDateTime.now().minusHours(i));
            purchasesList.add(purchase);
        }
    }

    @Test
    void purchaseHistory_shouldReturnSignIn_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/purchaseHistory").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void purchaseHistory_shouldReturnPurchaseHistory_whenAuthenticated() throws Exception {
        Pageable pageable = PageRequest.of(0, 4);
        Page<Purchase> purchases = new PageImpl<>(purchasesList, pageable, purchasesList.size());

        when(purchaseRestController.getPurchasesByCustomerId(anyLong(), anyInt(), anyInt(), anyBoolean(), anyBoolean()))
                .thenReturn(purchases);

        session.setAttribute("customer", customer);

        mockMvc.perform(get("/purchaseHistory").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("purchaseHistoryPage"));
    }

    @Test
    void purchaseHistory_shouldDisplayCorrectPurchasesForFirstPage() throws Exception {
        session.setAttribute("customer", customer);

        Pageable pageable = PageRequest.of(0, 4);
        Page<Purchase> purchasesPage = new PageImpl<>(purchasesList.subList(0, 4), pageable, purchasesList.size());

        when(purchaseRestController.getPurchasesByCustomerId(anyLong(), eq(4), eq(0), anyBoolean(), anyBoolean()))
                .thenReturn(purchasesPage);

        mockMvc.perform(get("/purchaseHistory?page=0&size=4").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("purchases"))
                .andExpect(model().attribute("purchases", instanceOf(Page.class)))
                .andExpect(model().attribute("purchases", hasProperty("content", hasSize(4))))
                .andExpect(view().name("purchaseHistoryPage"));
    }

    @Test
    void purchaseHistory_shouldDisplayCorrectPurchasesForSecondPage() throws Exception {
        session.setAttribute("customer", customer);

        Pageable pageable = PageRequest.of(1, 4);
        Page<Purchase> purchasesPage = new PageImpl<>(purchasesList.subList(4, 5), pageable, purchasesList.size());

        when(purchaseRestController.getPurchasesByCustomerId(anyLong(), eq(4), eq(1), anyBoolean(), anyBoolean()))
                .thenReturn(purchasesPage);

        mockMvc.perform(get("/purchaseHistory?page=1&size=4").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("purchases"))
                .andExpect(model().attribute("purchases", instanceOf(Page.class)))
                .andExpect(model().attribute("purchases", hasProperty("content", hasSize(1))))
                .andExpect(view().name("purchaseHistoryPage"));
    }

    @Test
    void purchaseHistoryBackToHomeButton_shouldReturnHomePage() throws Exception {
        session.setAttribute("customer", customer);

        mockMvc.perform(get("/home").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("homePage"));
    }

    @Test
    void purchaseHistory_shouldReturnSortedPurchase_whenSortByTotalPrice() throws Exception {
        purchasesList.sort((p1, p2) -> p2.getTotalPrice().compareTo(p1.getTotalPrice()));

        Pageable pageable = PageRequest.of(0, 4, Sort.by(Sort.Order.desc("totalPrice")));
        Page<Purchase> purchasesPage = new PageImpl<>(purchasesList.subList(0, 4), pageable, purchasesList.size());

        when(purchaseRestController.getPurchasesByCustomerId(anyLong(), eq(4), eq(0), eq(false), eq(true)))
                .thenReturn(purchasesPage);

        session.setAttribute("customer", customer);

        mockMvc.perform(get("/purchaseHistory?byDate=false&byPrice=true").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("purchases", instanceOf(Page.class)))
                .andExpect(model().attribute("purchases", hasProperty("content", hasSize(4))))
                .andExpect(model().attribute("purchases", hasProperty("content", contains(
                        allOf(hasProperty("totalPrice", is(BigDecimal.valueOf(250)))),
                        allOf(hasProperty("totalPrice", is(BigDecimal.valueOf(200)))),
                        allOf(hasProperty("totalPrice", is(BigDecimal.valueOf(150)))),
                        allOf(hasProperty("totalPrice", is(BigDecimal.valueOf(100))))
                ))))
                .andExpect(view().name("purchaseHistoryPage"));
    }

    @Test
    void purchaseHistory_shouldReturnMaxPage_whenPageBiggerThanMax() throws Exception {
        session.setAttribute("customer", customer);

        Pageable pageable = PageRequest.of(1, 4);
        Page<Purchase> purchasesPage = new PageImpl<>(purchasesList.subList(4, 5), pageable, purchasesList.size());

        when(purchaseRestController.getPurchasesByCustomerId(anyLong(), anyInt(), anyInt(), anyBoolean(), anyBoolean()))
                .thenReturn(purchasesPage);

        mockMvc.perform(get("/purchaseHistory?page=100&size=4").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("purchases"))
                .andExpect(model().attribute("purchases", instanceOf(Page.class)))
                .andExpect(model().attribute("purchases", hasProperty("content", hasSize(1))))
                .andExpect(view().name("purchaseHistoryPage"));
    }

    @Test
    void purchaseHistory_shouldReturn403_withoutAdminAccess() throws Exception {
        String otherCustomer = "otherExample@example.com";

        session.setAttribute("customer", customer);

        mockMvc.perform(get("/purchaseHistory").session(session)
                .param("otherCustomer", otherCustomer))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error-403"));

    }

    @Test
    void purchaseHistory_shouldReturnPurchaseHistory_withAdminAccess() throws Exception {
        String otherCustomer = "otherExample@example.com";

        customer.setRole(Role.ADMIN);
        session.setAttribute("customer", customer);

        Pageable pageable = PageRequest.of(0, 4);
        Page<Purchase> purchases = new PageImpl<>(purchasesList, pageable, purchasesList.size());

        when(purchaseRestController.getPurchasesByCustomerId(anyLong(), anyInt(), anyInt(), anyBoolean(), anyBoolean()))
                .thenReturn(purchases);
        when(customerService.existsByEmail(otherCustomer))
                .thenReturn(true);
        when(customerService.findByEmail(otherCustomer))
                .thenReturn(customer);

        mockMvc.perform(get("/purchaseHistory").session(session)
                        .param("otherCustomer", otherCustomer))
                .andExpect(status().isOk())
                .andExpect(view().name("purchaseHistoryPage"));
    }
}
