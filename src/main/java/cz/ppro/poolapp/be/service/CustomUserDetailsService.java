package cz.ppro.poolapp.be.service;

import cz.ppro.poolapp.be.model.Account;
import cz.ppro.poolapp.be.model.CustomUserDetails;
import cz.ppro.poolapp.be.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private AccountRepository accountRepository;
    @Autowired
    public CustomUserDetailsService(AccountRepository accountRepository){
        this.accountRepository=accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findByLogin(login);
        optionalAccount
                .orElseThrow(()->new UsernameNotFoundException("Username not found"));
        return optionalAccount
                .map(account ->
                        new CustomUserDetails(account)
                ).get();
    }
}
