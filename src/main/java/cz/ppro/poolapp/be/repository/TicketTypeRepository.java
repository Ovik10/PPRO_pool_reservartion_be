package cz.ppro.poolapp.be.repository;

import cz.ppro.poolapp.be.model.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {

    }
