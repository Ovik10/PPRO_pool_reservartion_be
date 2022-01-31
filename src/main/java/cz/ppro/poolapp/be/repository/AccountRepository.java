package cz.ppro.poolapp.be.repository;

import cz.ppro.poolapp.be.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByLogin(String login);

    Boolean existsByLogin(String login);

    @Query(value = "SELECT * FROM Account JOIN Role ON Account.role_role_id=Role.role_id" +
            " WHERE Role.role_name = 'Trainer'"
            , nativeQuery = true)
    List<Account> findAllTrainers();

    List<Account> findAllByRoleName(String roleName);
}
