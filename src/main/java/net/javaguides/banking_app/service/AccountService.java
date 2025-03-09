package net.javaguides.banking_app.service;

import net.javaguides.banking_app.dto.AccountDto;
import net.javaguides.banking_app.entity.Account;
import net.javaguides.banking_app.exception.ResourceNotFound;
import net.javaguides.banking_app.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;


    public Account getAccount(long id) throws ResourceNotFound {
        Optional<Account> account = accountRepository.findById(id);
        if(account.isPresent()) {
            return account.get();
        }

        throw  new ResourceNotFound("account not found");
    }

    public List<Account> getAccounts() throws Exception {
        try {
            return accountRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createAccount(AccountDto accountDto) throws Exception {
        try {
            Account account = modelMapper.map(accountDto, Account.class);
            accountRepository.save(account);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateAccount(AccountDto accountDto , long accountId) throws Exception {
        try {

            Account account = getAccount(accountId);

            if (accountDto.getBalance() != null) {
                account.setBalance(accountDto.getBalance());
            }

            if (accountDto.getHolderName() != null) {
                account.setHolderName(accountDto.getHolderName());
            }

            accountRepository.save(account);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAccount(long accountId) throws ResourceNotFound,Exception {
        try {
            Account account = getAccount(accountId);
            if (account == null) {
                throw new ResourceNotFound("account not found");
            }
            accountRepository.deleteById(accountId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
