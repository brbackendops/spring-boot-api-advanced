package net.javaguides.banking_app;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import net.javaguides.banking_app.entity.Account;
import net.javaguides.banking_app.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
@TestPropertySource(properties = "spring.profiles.active=test")
public class AccountRepoTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void testSaveUser(){

//        entityManager.createQuery("DELETE FROM Account a WHERE a.id=:id")
//                        .setParameter("id", 1L)
//                        .executeUpdate();
//
//        entityManager.flush();
//        entityManager.clear();

        Account account = new Account();
        account.setHolderName("test");
        account.setBalance(0.00);

        accountRepository.save(account);

        assertNotNull(account.getId());
        assertEquals("test",account.getHolderName());
        assertEquals(0.00,account.getBalance());
    }

    @Test
    void testFindAccountById(){

        Long id = 1L;

        Account account = new Account();
        account.setHolderName("test");
        account.setBalance(0.00);

        accountRepository.save(account);

        Optional<Account> savedAccount = accountRepository.findById(id);
        assertNotNull(account);

        Account account1 = savedAccount.get();

        assertEquals(0.00,account1.getBalance());
        assertEquals("test",account1.getHolderName());
    }

    @Test
    void testUpdateAccountById(){

        Long id = 1L;

        Account account = new Account();
        account.setHolderName("test");
        account.setBalance(0.00);

        accountRepository.save(account);

        Optional<Account> savedAccount = accountRepository.findById(id);
        assertNotNull(account);

        Account account1 = savedAccount.get();

        account1.setBalance(1.00);
        account1.setHolderName("test1");

        accountRepository.save(account1);

        assertEquals("test1", account1.getHolderName());
        assertEquals(1.00, account1.getBalance());

    }

    @Test
    void testDeleteAccountById(){
        Long id = 1L;

        Account account = new Account();
        account.setHolderName("test");
        account.setBalance(0.00);

        accountRepository.save(account);

        accountRepository.deleteById(id);

        Optional<Account> savedAccount = accountRepository.findById(id);
        assertEquals(true,savedAccount.isEmpty());

    }
}
