package cz.ppro.poolapp.be.api;

import cz.ppro.poolapp.be.exception.ResourceNotFoundException;
import cz.ppro.poolapp.be.model.Account;
import cz.ppro.poolapp.be.model.Ticket;
import cz.ppro.poolapp.be.model.TicketType;
import cz.ppro.poolapp.be.repository.AccountRepository;
import cz.ppro.poolapp.be.repository.TicketRepository;
import cz.ppro.poolapp.be.repository.TicketTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RequestMapping("/tickets")
@RestController
public class TicketController {
    private AccountRepository accountRepository;
    private TicketRepository ticketRepository;
    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    public TicketController(AccountRepository accountRepository, TicketRepository ticketRepository, TicketTypeRepository ticketTypeRepository) {
        this.accountRepository = accountRepository;
        this.ticketRepository = ticketRepository;
        this.ticketTypeRepository=ticketTypeRepository;
    }
    @Secured({ "ROLE_Client", "ROLE_Employee", "ROLE_Admin" })
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Ticket> getAll(){
        List<Ticket> tickets = ticketRepository.findAll();
        return tickets;
    }

    @Secured({ "ROLE_Client", "ROLE_Employee", "ROLE_Admin" })
    @RequestMapping(value ="/account/{id}/all", method = RequestMethod.GET)
    public List<Ticket> getAllByAccount(@PathVariable(value = "id") Long accountId){
        List<Ticket> tickets = ticketRepository.findAllByAccount_Id(accountId);
        return tickets;
    }

    @Secured({ "ROLE_Client", "ROLE_Employee", "ROLE_Admin" })
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public Ticket getById(@PathVariable(value = "id") Long id){

        return ticketRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Ticket", "id", id));
    }
    @RequestMapping(value = "/create/{accountId}/{ticketTypeId}", method = RequestMethod.POST)
    public @ResponseBody
    Ticket create(@Valid @NonNull @RequestBody Ticket ticket, @PathVariable(value = "accountId") Long accountId, @PathVariable(value = "ticketTypeId") Long ticketTypeId){
        Account client = accountRepository.findById(accountId).orElseThrow(()->new ResourceNotFoundException("Account", "id", accountId));
        TicketType ticketType = ticketTypeRepository.findById(ticketTypeId).orElseThrow(()->new ResourceNotFoundException("TicketType", "id", ticketTypeId));
        ticket.setTicketType(ticketType);
        ticket.setAccount(client);
        return ticketRepository.save(ticket);

    }
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.DELETE)
    public void remove(@PathVariable(value = "id") Long id){
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Ticket", "id", id));
        Account account = ticket.getAccount();
        account.getTickets().remove(ticket);
        accountRepository.save(account);
        ticketRepository.delete(ticket);
    }
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public Ticket update(@PathVariable(value = "id") Long id,
                          @Valid @RequestBody Ticket ticketDetails){
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Ticket", "id", id));
        ticket.setAccount(ticketDetails.getAccount());
        Account account = ticket.getAccount();
        Account newAcc = ticketDetails.getAccount();
        ticket.setBeginDate(ticketDetails.getBeginDate());
        ticket.setEndDate(ticketDetails.getEndDate());
        ticket.setTicketType(ticketDetails.getTicketType());
        ticket.setSport(ticketDetails.getSport());
        Ticket updatedTicket = ticketRepository.save(ticket);
        if(account.getId()!=newAcc.getId()){
            account.getTickets().remove(ticket);
            newAcc.getTickets().add(ticket);
            accountRepository.save(account);
            accountRepository.save(newAcc);
        }
        return updatedTicket;
    }


}
