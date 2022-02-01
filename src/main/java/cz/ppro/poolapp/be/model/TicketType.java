package cz.ppro.poolapp.be.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name="ticket_type")
public class TicketType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ticket_type_id")
    private Long id;
    @Column(name = "ticket_type_name")
    @NotBlank
    private String name;
    @Column(name = "ticket_type_price")
    @NotNull
    private double price;
    @NotNull

    @OneToMany(mappedBy = "ticketType")
    @JsonIgnore
    private List<Ticket> tickets;



    public TicketType(@NotBlank String name, @NotNull double price, List<Ticket> tickets) {
        this.name = name;
        this.price = price;
        this.tickets = tickets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public List<TicketType> getTicketTypes() { return null;}


    public TicketType(){}


    public Long getId() {
        return id;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
