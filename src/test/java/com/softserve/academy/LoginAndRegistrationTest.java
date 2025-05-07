package com.softserve.academy;

import com.softserve.academy.controller.CustomerViewController;
import com.softserve.academy.controller.SignController;
import com.softserve.academy.model.Customer;
import com.softserve.academy.model.Role;
import com.softserve.academy.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class LoginAndRegistrationTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerViewController customerViewController;

    @InjectMocks
    private SignController signController;

    private MockMvc mockMvc;
    private MockHttpSession session;
    private Customer customer;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerViewController, signController)
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
    void signIn_shouldRedirectToHome_whenValidCredentials() throws Exception {
        String email = "example@example.com";
        String password = "example";

        when(customerService.findByEmail(email)).thenReturn(customer);

        mockMvc.perform(post("/customer/verify")
                        .param("email", email)
                        .param("password", password)
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"))
                .andExpect(model().attributeDoesNotExist("error"));
    }

    @Test
    void signIn_shouldRedirectToSignInPage_whenInvalidCredentials() throws Exception {
        String email = "example@example.com";
        String password = "1example";

        when(customerService.findByEmail(email)).thenReturn(customer);

        mockMvc.perform(post("/customer/verify")
                        .param("email", email)
                        .param("password", password)
                        .session(session))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    void signIn_shouldRedirectToHome_whenAuthenticated() throws Exception {
        session.setAttribute("customer", customer);

        mockMvc.perform(get("/").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void signIn_shouldReturnSignInPage_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("signIn"));
    }


    @Test
    void signUp_shouldReturnSignInPage_whenValidData() throws Exception {
        String name = "Example2";
        String email = "example2@example.com";
        String password = "example2";
        String phoneNumber = "+380000000000";

        when(customerService.existsByEmail(email)).thenReturn(false);
        when(customerService.existsByPhoneNumber(phoneNumber)).thenReturn(false);

        mockMvc.perform(post("/customer/add").session(session)
                        .param("name", name)
                        .param("email", email)
                        .param("password", password)
                        .param("phoneNumber", phoneNumber))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

    }

    @Test
    void signUp_shouldReturnSignUpPage_whenInValidEmail() throws Exception {
        String name = "Example2";
        String email = "example2@example.com";
        String password = "example2";
        String phoneNumber = "+380000000000";

        when(customerService.existsByEmail(email)).thenReturn(true);

        mockMvc.perform(post("/customer/add").session(session)
                        .param("name", name)
                        .param("email", email)
                        .param("password", password)
                        .param("phoneNumber", phoneNumber))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/SignUp"))
                .andExpect(flash().attributeExists("error"));;

    }

    @Test
    void signUp_shouldReturnSignUpPage_whenInValidPhoneNumber() throws Exception {
        String name = "Example2";
        String email = "example2@example.com";
        String password = "example2";
        String phoneNumber = "+380000000000";

        when(customerService.existsByPhoneNumber(phoneNumber)).thenReturn(true);

        mockMvc.perform(post("/customer/add").session(session)
                        .param("name", name)
                        .param("email", email)
                        .param("password", password)
                        .param("phoneNumber", phoneNumber))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/SignUp"))
                .andExpect(flash().attributeExists("error"));;

    }

    @Test
    void signUp_shouldReturnSignUpPage_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/SignUp").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("signUp"));
    }

    @Test
    void signUp_shouldRedirectToHome_whenAuthenticated() throws Exception {
        session.setAttribute("customer", customer);

        mockMvc.perform(get("/SignUp").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }
}
