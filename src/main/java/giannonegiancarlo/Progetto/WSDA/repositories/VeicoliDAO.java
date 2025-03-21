package giannonegiancarlo.Progetto.WSDA.repositories;

import giannonegiancarlo.Progetto.WSDA.entities.Veicolo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VeicoliDAO extends JpaRepository<Veicolo, String> {
//    boolean existsByTarga (String targa);
Optional<Veicolo> findByTarga(String targa);

}
