package cz.ppro.poolapp.be.api;

import cz.ppro.poolapp.be.model.Role;
import cz.ppro.poolapp.be.repository.AccountRepository;
import cz.ppro.poolapp.be.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3006")
@RequestMapping("/roles")
@RestController
public class RoleController {
    private RoleRepository roleRepository;
    private AccountRepository accountRepository;

    @Autowired
    public RoleController(RoleRepository roleRepository, AccountRepository accountRepository){
        this.roleRepository=roleRepository;
        this.accountRepository=accountRepository;
    }
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Role> getAll(){
        return roleRepository.findAll();
    }



}
