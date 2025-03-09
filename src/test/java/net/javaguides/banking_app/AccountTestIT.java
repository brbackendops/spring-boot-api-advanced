package net.javaguides.banking_app;

import net.javaguides.banking_app.controller.AccountController;
import net.javaguides.banking_app.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest(AccountController.class)
public class AccountTestIT {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }
}
