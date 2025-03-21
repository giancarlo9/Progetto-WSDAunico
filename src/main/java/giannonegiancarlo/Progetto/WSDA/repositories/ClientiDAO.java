package giannonegiancarlo.Progetto.WSDA.repositories;

import giannonegiancarlo.Progetto.WSDA.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientiDAO extends JpaRepository<Cliente, Long> {
    boolean existsByEmail(String email);
    Optional<Cliente> findByEmail(String email);
}

