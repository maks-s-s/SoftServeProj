package com.softserve.academy;

import com.softserve.academy.controller.SignController;
import com.softserve.academy.model.Customer;
import com.softserve.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class HomePageTest {

    @InjectMocks
    private SignController signController;

    MockMvc mockMvc;
    MockHttpSession session;
    Customer customer;

    @BeforeEach
    void set_up() {
        mockMvc = MockMvcBuilders.standaloneSetup(signController)
                .build();

        session = new MockHttpSession();

        customer = new Customer();
        customer.setId(1L);
        customer.setRole(Role.USER);
        customer.setName("Example");
        customer.setEmail("example@example.com");
        customer.setPhoneNumber("+380950000000");
        customer.setPassword("example");
    }


    @Test
    void homePage_shouldReturnHomePage_whenAuthenticated() throws Exception {
        session.setAttribute("customer", customer);
        mockMvc.perform(get("/home").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("homePage"));
    }

    @Test
    void homePage_shouldReturnSignIn_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/home").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void homePage_shouldReturnHomePageWithAttribute_whenAuthenticated() throws Exception {
        session.setAttribute("customer", customer);
        mockMvc.perform(get("/home").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("homePage"))
                .andExpect(model().attributeExists("customer"));
    }

}
