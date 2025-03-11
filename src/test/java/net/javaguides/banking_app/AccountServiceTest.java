package net.javaguides.banking_app;

import net.javaguides.banking_app.controller.AccountController;
import net.javaguides.banking_app.dto.AccountDto;
import net.javaguides.banking_app.dto.AccountUpdateDto;
import net.javaguides.banking_app.entity.Account;
import net.javaguides.banking_app.exception.ResourceNotFound;
import net.javaguides.banking_app.repository.AccountRepository;
import net.javaguides.banking_app.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.swing.text.html.Option;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("should create account with given data")
    void testCreateAccount() throws Exception {

        Account account = new Account();
        account.setHolderName("tester1");
        account.setBalance(100.00);

        Account resultAccount = new Account(
                1,
                "tester1",
                100.00,
                LocalDateTime.now()
        );

        AccountDto payload = new AccountDto(
                "tester1", 100.00
        );

        Mockito.doReturn(account).when(modelMapper).map(payload,Account.class);
        Mockito.when(accountRepository.save(account)).thenReturn(resultAccount);

        accountService.createAccount(payload);


        Mockito.verify(accountRepository).save(account);
    }


    @Test
    @DisplayName("should get account with given id")
    void testGetAccount() throws ResourceNotFound {

        final Long id = 1L;

        Account account = new Account();
        account.setHolderName("tester1");
        account.setBalance(1.00);


        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        Account account1 = accountService.getAccount(id);

        Mockito.verify(accountRepository).findById(id);
        assertThat(account1.getBalance()).isEqualTo(1.00);
        assertThat(account1.getHolderName()).isEqualTo("tester1");
    }


    @Test
    @DisplayName("should update account with given data")
    void testUpdateAccount() throws Exception {

        final Long id = 1L;

        Account account = new Account();
        account.setHolderName("tester1");
        account.setBalance(100.00);

        Account resultAccount = new Account(
                1,
                "tester1",
                100.00,
                LocalDateTime.now()
        );

        AccountDto payload = new AccountDto(
                "tester2",
                101.00
        );


        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(resultAccount));
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(resultAccount);

        accountService.updateAccount(payload,id);

        Mockito.verify(accountRepository, Mockito.atLeast(1)).save(resultAccount);
        Mockito.verify(accountRepository, Mockito.atLeast(1)).findById(id);

    }


    @Test
    @DisplayName("should delete successfully")
    public void verifyAccountDelete() throws Exception {

        final long Id = 2L;

        Account account = new Account(2,"john doe mock test", 100.0, LocalDateTime.now());

        Mockito.doReturn(Optional.of(account)).when(accountRepository).findById(Id);
        Mockito.doNothing().when(accountRepository).deleteById(Id);
        accountService.deleteAccount(Id);

        Mockito.verify(accountRepository).findById(Id);
        Mockito.verify(accountRepository).deleteById(Id);
    }


}
