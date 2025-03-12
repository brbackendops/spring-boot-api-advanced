package net.javaguides.banking_app;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import net.javaguides.banking_app.dto.AccountDto;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = "spring.profiles.active=test")
public class AccountIntegrationTest extends BankingAppApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }


//    @Rule
//    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldThrowInternalServerError() throws Exception {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/account/",String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    public void checkHealthMethodForAccount() throws Exception {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/api/accounts/health",String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().toString())
                .isNotNull()
                .isNotEqualTo("text/plain;charset=UTF-8");

        assertThat(response.getHeaders().getContentType().toString())
                .isNotNull()
                .isEqualTo("application/json");

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        String message = documentContext.read("$.message");

        assertThat(message).isNotNull();
    }

    @Test
    @DisplayName("should throw NOT FOUND if user does not exists with given id")
    public void verifyGetByIdMethodForAccount() throws ResourceNotFound {

        final Long Id = 1L;

        Mockito.when(accountRepository.findById(Id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccount(Id)).isInstanceOf(ResourceNotFound.class);
    }

    @Test
    @DisplayName("should return user with given id")
    public void shouldGetByIdMethodForAccount() throws Exception {

        final Long Id = 1L;

        Account account = new Account(1,"john doe", 120.0, LocalDateTime.now());

        Mockito.when(accountRepository.findById(Id)).thenReturn(Optional.of(account));

        Account result = accountService.getAccount(Id);


        assertThat(result).isNotNull();
        assertThat(result.getHolderName()).isEqualTo("john doe");
        assertThat(result.getBalance()).isEqualTo(120.0);
    }


    @Test
    public void checkCreateMethodForAccount() throws Exception {
        AccountDto accountDto = new AccountDto("john doe test",000.0);
        Account account = new Account(1,"john doe test", 000.0, LocalDateTime.now());


        Mockito.doReturn(account).when(modelMapper).map(accountDto,Account.class);
        Mockito.doReturn(account).when(accountRepository).save(Mockito.any(Account.class));
//        Mockito.doNothing().when(accountService).createAccount(accountDto);

        accountService.createAccount(accountDto);

        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/accounts/", accountDto , String.class);

        DocumentContext document = JsonPath.parse(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String msg = document.read("$.message");
        assertThat(msg).isEqualTo("account created successfully");
    }

    @Test
    @DisplayName("should update successfully")
    public void verifyAccountUpdate() throws Exception {

        final long Id = 1L;

        AccountDto accountDto = new AccountDto("john doe test",000.0);
        Account account = new Account(1,"john doe test", 000.0, LocalDateTime.now());

        Mockito.doReturn(Optional.of(account)).when(accountRepository).findById(Id);
        accountService.updateAccount(accountDto,Id);


        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/accounts/" + Id ,
                HttpMethod.PUT,
                new HttpEntity<>(accountDto),
                String.class
        );
        DocumentContext document = JsonPath.parse(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("should delete successfully")
    public void verifyAccountDelete() throws Exception {

        final long Id = 2L;

        Account account = new Account(2,"john doe test", 000.0, LocalDateTime.now());

        Mockito.doReturn(Optional.of(account)).when(accountRepository).findById(Id);
        Mockito.doNothing().when(accountRepository).deleteById(Id);
        accountService.deleteAccount(Id);

        Mockito.verify(accountRepository).findById(Id);
        Mockito.verify(accountRepository).deleteById(Id);
    }
}
