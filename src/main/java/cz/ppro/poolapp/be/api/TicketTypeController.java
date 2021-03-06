package cz.ppro.poolapp.be.api;

import cz.ppro.poolapp.be.model.TicketType;
import cz.ppro.poolapp.be.repository.TicketTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3006")
@RequestMapping("/ticketTypes")
@RestController
public class TicketTypeController {

    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    public TicketTypeController(TicketTypeRepository ticketTypeRepository){
        this.ticketTypeRepository=ticketTypeRepository;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<TicketType> getAll(){
        return ticketTypeRepository.findAll();
    }
}
