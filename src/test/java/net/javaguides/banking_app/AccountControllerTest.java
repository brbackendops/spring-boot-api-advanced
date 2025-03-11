package net.javaguides.banking_app;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.banking_app.controller.AccountController;
import net.javaguides.banking_app.dto.AccountDto;
import net.javaguides.banking_app.entity.Account;
import net.javaguides.banking_app.service.AccountService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ObjectInputStream;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.junit.jupiter.api.Assertions.*;


@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private  AccountService accountService;

    @MockBean
    private ModelMapper modelMapper;

//    private static final AccountService accountService = Mockito.mock(AccountService.class);
//
//    @TestConfiguration
//    static class TestConfig {
//        @Bean
//        public AccountService accountService() {
//            return accountService;
//        }
//    }

    @Test
    @DisplayName("GET /health controller test")
    void testHealthController() throws Exception {
        mockMvc.perform(get("/api/accounts/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("welcome to Accounts API"));
    }

    @Test
    @DisplayName("GET /{id} controller test")
    void testGetAccountController() throws Exception {

        final Long id = 1L;

        Account account = new Account(
          1,
          "tester",
          100.000,
          LocalDateTime.now()
        );

        Mockito.when(accountService.getAccount(id)).thenReturn(account);

        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST / controller test")
    void testCreateAccountController() throws Exception {


        AccountDto payload = new AccountDto(
                "tester",
                100.000
        );

        Account account = new Account(
                1,
                "tester",
                100.00,
                LocalDateTime.now()
        );

        Mockito.doReturn(account).when(modelMapper).map(payload,Account.class);
        Mockito.doNothing().when(accountService).createAccount(payload);

        mockMvc.perform(
                    post("/api/accounts/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsBytes(payload))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("account created successfully"));

        Mockito.verify(accountService).createAccount(Mockito.any(AccountDto.class));
    }

    @Test
    @DisplayName("PUT /{id} controller test")
    void testUpdateAccountController() throws Exception {

        final Long id = 1L;

        AccountDto payload = new AccountDto(
                "tester",
                100.000
        );

        Account account = new Account(
                1,
                "tester",
                100.00,
                LocalDateTime.now()
        );

        Mockito.doReturn(account).when(modelMapper).map(payload,Account.class);

        Mockito.doReturn(account).when(accountService).getAccount(id);
        Mockito.doNothing().when(accountService).updateAccount(payload,id);

        mockMvc.perform(
                        put("/api/accounts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsBytes(payload))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("account updated successfully"));

        Mockito.verify(accountService).updateAccount(Mockito.any(AccountDto.class),Mockito.eq(id));
    }

    @Test
    @DisplayName("DELETE /{id} controller test")
    void testDeleteAccountController() throws Exception {

        final Long id = 1L;

        AccountDto payload = new AccountDto(
                "tester",
                100.000
        );

        Account account = new Account(
                1,
                "tester",
                100.00,
                LocalDateTime.now()
        );


        Mockito.doReturn(account).when(accountService).getAccount(id);
        Mockito.doNothing().when(accountService).updateAccount(payload,id);

        mockMvc.perform(
                        delete("/api/accounts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(System.out::println)
                .andExpect(jsonPath("$.message").value("account deleted successfully"));

        Mockito.verify(accountService).deleteAccount(id);
    }


}
