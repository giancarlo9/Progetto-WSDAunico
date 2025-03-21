package giannonegiancarlo.Progetto.WSDA.repositories;

import giannonegiancarlo.Progetto.WSDA.entities.Intervento;
import giannonegiancarlo.Progetto.WSDA.enums.Stato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterventiDAO extends JpaRepository<Intervento, String> {
    boolean existsByStato (Stato stato);
    Optional<InterventiDAO> findByStato(Stato stato);
    Optional<Intervento> findByVeicolo_TargaAndCodiceServizio(String targa, String codiceServizio);

}
