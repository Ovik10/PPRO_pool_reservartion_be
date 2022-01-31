package cz.ppro.poolapp.be.api;

import cz.ppro.poolapp.be.exception.ResourceNotFoundException;
import cz.ppro.poolapp.be.model.Account;
import cz.ppro.poolapp.be.model.Role;
import cz.ppro.poolapp.be.repository.AccountRepository;
import cz.ppro.poolapp.be.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RequestMapping("/accounts")
@RestController
public class AccountController {

    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    @Autowired
    public AccountController(AccountRepository accountRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder){
        this.accountRepository=accountRepository;
        this.roleRepository=roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Secured({ "ROLE_Admin", "ROLE_Trainer", "ROLE_Employee" })
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Account> getAll(){
        return accountRepository.findAll();
    }

    @Secured({ "ROLE_Admin", "ROLE_Trainer", "ROLE_Employee" })
    @RequestMapping(value = "/all/trainer", method = RequestMethod.GET)
    public List<Account> getTrainers(){
        return accountRepository.findAllTrainers();
    }

    @Secured({ "ROLE_Admin", "ROLE_Trainer", "ROLE_Employee" })
    @RequestMapping(value = "/all/clients", method = RequestMethod.GET)
    public List<Account> getClients(){
        return accountRepository.findAllByRoleName("Client");
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public Account getById(@PathVariable(value = "id") Long id){
        return accountRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Account", "id", id));
    }

    //pro vytvoreni uzivatelskeho uctu
    @RequestMapping(value = "/createClient", method = RequestMethod.POST)
    public @ResponseBody
    Account create(@Valid @NonNull @RequestBody Account account){

        String password = passwordEncoder.encode(account.getPassword());
        account.setPassword(password);
        Role userRole = roleRepository.findByName("Client");
        account.setRole(userRole);
        if(accountRepository.findByLogin(account.getLogin()).isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Uživatelské jméno je již obsazeno");
        }
        return accountRepository.save(account);

    }
    //pro vytvoreni uctu adminem -  pro trenery, zamestnance
    @RequestMapping(value = "/create/admin/{roleName}", method = RequestMethod.POST)
    public @ResponseBody
    Account createAdmin(@Valid @NonNull @RequestBody Account account, @PathVariable(value = "roleName") String roleName){
        String password = passwordEncoder.encode(account.getPassword());
        account.setPassword(password);
        Role userRole = roleRepository.findByName(roleName);
        account.setRole(userRole);

        if(accountRepository.findByLogin(account.getLogin()).isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Uživatelské jméno je již obsazeno");
        }
        return accountRepository.save(account);

    }


    @RequestMapping(value = "/update/{id}/{auId}", method = RequestMethod.PUT)
    public Account update(@PathVariable(value = "id") Long id, @PathVariable(value = "auId") Long auId,
                          @Valid @RequestBody Account accountDetails){
        Account account = accountRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Account", "id", id));
        Account user = accountRepository.findById(auId)
                .orElseThrow(()-> new ResourceNotFoundException("Account", "id", auId));
        if((user.getId()==account.getId()) || ((user.getRole().getName().equals("Admin")) || (user.getRole().getName().equals("Trainer")) || user.getRole().getName().equals("Employee"))){
        account.setEmail(accountDetails.getEmail());
        account.setPhoneNumber(accountDetails.getPhoneNumber());
        account.setFirstName(accountDetails.getFirstName());
        account.setLastName(accountDetails.getLastName());
        Account updatedAccount = accountRepository.save(account);
        return updatedAccount;
        }
        else{
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Neoprávněný přístup");
        }
    }

}
