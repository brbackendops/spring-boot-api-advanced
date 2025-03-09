package net.javaguides.banking_app.controller;


import net.javaguides.banking_app.dto.AccountDto;
import net.javaguides.banking_app.entity.Account;
import net.javaguides.banking_app.exception.ResourceNotFound;
import net.javaguides.banking_app.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/health")
    public ResponseEntity<Map<String,String>> getAccountApiHealth() {

        HashMap<String, String> response = new HashMap<>();
        response.put("message","welcome to Accounts API");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<Account>> getAllAccounts() throws Exception {
        List<Account> accounts = accountService.getAccounts();
        return ResponseEntity.status(HttpStatus.OK).body(accounts);
    }

    @PostMapping("/")
    public ResponseEntity<Map<String,String>> saveAccount(@RequestBody AccountDto accountDto) throws Exception {
        accountService.createAccount(accountDto);

        HashMap<String, String> response = new HashMap<>();
        response.put("message","account created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getSingleAccount(@PathVariable("id") long id) throws ResourceNotFound {
        Account account = accountService.getAccount(id);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String,String>> updateSingleAccount(@PathVariable("id") long id) throws ResourceNotFound {
        Account account = accountService.getAccount(id);

        HashMap<String, String> response = new HashMap<>();
        response.put("message","account updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> deleteSingleAccount(@PathVariable("id") long id) throws Exception {
        accountService.deleteAccount(id);

        HashMap<String, String> response = new HashMap<>();
        response.put("message","account deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
