package giannonegiancarlo.Progetto.WSDA.repositories;

import giannonegiancarlo.Progetto.WSDA.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtentiDAO extends JpaRepository<Utente, Integer> {
    boolean existsByEmail(String email);
    Optional<Utente> findByEmail(String email);

}
