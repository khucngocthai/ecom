package com.nashtech.ecommerce.controller;

import com.nashtech.ecommerce.auth.ApplicationUser;
import com.nashtech.ecommerce.domain.Account;
import com.nashtech.ecommerce.domain.Customer;
import com.nashtech.ecommerce.exception.AlreadyExistsException;
import com.nashtech.ecommerce.jwt.JwtUtils;
import com.nashtech.ecommerce.payload.request.LoginRequest;
import com.nashtech.ecommerce.payload.request.SignupRequest;
import com.nashtech.ecommerce.payload.response.JwtResponse;
import com.nashtech.ecommerce.payload.response.MessageResponse;
import com.nashtech.ecommerce.repository.AccountRepository;
import com.nashtech.ecommerce.repository.CustomerRepository;
import com.nashtech.ecommerce.service.AccountService;
import com.nashtech.ecommerce.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    public static final String ACCOUNT_NOT_FOUND = "Account not found!";
    public static final String PHONE_ALREADY_EXISTS = "Phone number already exists!";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists!";

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        ApplicationUser userDetails = (ApplicationUser) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(
                userDetails.getId(),
                jwt,
                userDetails.getUsername(),
                roles
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        // Create new customer's account
        if (accountRepository.findAccountByUsername(signUpRequest.getUsername()).isPresent())
            throw new AlreadyExistsException("Username already exists!");
        //check phone number existence
        if (customerRepository.existsCustomerByPhone(signUpRequest.getPhone()))
            throw new AlreadyExistsException("Phone number already in use!");
        //check email existence
        if (customerRepository.existsCustomerByEmail(signUpRequest.getEmail()))
            throw new AlreadyExistsException("Email already in use!");
        //TODO: prevent saving into db without relying on FE
        //create new account
        Account account = new Account();
        account.setUsername(signUpRequest.getUsername());
        account.setPassword(encoder.encode(signUpRequest.getPassword()));
        account.setJoinDate();
        accountRepository.save(account);
        //create new customer
        Customer customer = new Customer();
        customer.setAccount(account);
        customer.setFirstName(signUpRequest.getFirstName());
        customer.setLastName(signUpRequest.getLastName());
        customer.setPhone(signUpRequest.getPhone());
        customer.setEmail(signUpRequest.getEmail());
        customerRepository.save(customer);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
