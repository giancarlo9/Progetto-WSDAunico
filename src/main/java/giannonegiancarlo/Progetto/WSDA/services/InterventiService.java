package giannonegiancarlo.Progetto.WSDA.services;

import giannonegiancarlo.Progetto.WSDA.entities.Intervento;
import giannonegiancarlo.Progetto.WSDA.entities.Veicolo;
import giannonegiancarlo.Progetto.WSDA.enums.Stato;
import giannonegiancarlo.Progetto.WSDA.exceptions.NotFoundException;
import giannonegiancarlo.Progetto.WSDA.payloads.NewInterventoDTO;
import giannonegiancarlo.Progetto.WSDA.repositories.InterventiDAO;
import giannonegiancarlo.Progetto.WSDA.repositories.VeicoliDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@Service
public class InterventiService {

    @Autowired
    private InterventiDAO interventiDAO;

    @Autowired
    private VeicoliDAO veicoliDAO; // Aggiunto il repository per i veicoli

    public InterventiService(InterventiDAO interventiDAO, VeicoliDAO veicoliDAO) {
        this.interventiDAO = interventiDAO;
        this.veicoliDAO = veicoliDAO;
    }

    public List<Intervento> getInterventiList() {
        return interventiDAO.findAll();
    }

    public Intervento saveIntervento(NewInterventoDTO newInterventoDTO) {
        // Controllo che il veicolo esista
        Veicolo veicolo = veicoliDAO.findById(newInterventoDTO.veicoloId())
                .orElseThrow(() -> new NotFoundException("Veicolo non trovato con targa: " + newInterventoDTO.veicoloId()));

        // Creazione dell'intervento con il veicolo corretto
        Intervento intervento = new Intervento(
                newInterventoDTO.codiceServizio(), // Il codice servizio deve essere assegnato qui!
                veicolo,
                newInterventoDTO.lavorazioniEffettuate(),
                newInterventoDTO.stato(),
                newInterventoDTO.costoManodopera(),
                newInterventoDTO.ricambi(),
                newInterventoDTO.totale(),
                newInterventoDTO.oreLavorate()
        );

        // Stampa di debug per verificare che codiceServizio sia corretto
        System.out.println("Salvando intervento con codice servizio: " + intervento.getCodiceServizio());

        return interventiDAO.save(intervento);
    }

    public Intervento findById(String id) {
        return interventiDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Intervento non trovato con codice: " + id));
    }


    public Intervento findByIdAndUpdate(String id, Intervento updatedIntervento) {
        Intervento found = findById(id);

        if (updatedIntervento.getVeicolo() != null && updatedIntervento.getVeicolo().getTarga() != null) {
            Optional<Veicolo> veicoloOpt = veicoliDAO.findByTarga(updatedIntervento.getVeicolo().getTarga());
            veicoloOpt.ifPresent(found::setVeicolo);
        }

        found.setLavorazioniEffettuate(updatedIntervento.getLavorazioniEffettuate());
        found.setStato(updatedIntervento.getStato());
        found.setCostoManodopera(updatedIntervento.getCostoManodopera());
        found.setRicambi(updatedIntervento.getRicambi());
        found.setOreLavorate(updatedIntervento.getOreLavorate());

        return interventiDAO.save(found);
    }



    public void findByIdAndDelete(String interventoId) {
        interventiDAO.deleteById(interventoId);
    }

    public Page<Intervento> getInterventi(int page, int size, String sortBy) {
        if (size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.interventiDAO.findAll(pageable);
    }

    public String getStatoRiparazione(String codiceServizio, String targa) {
        // Cerca l'intervento che corrisponde al codice servizio e alla targa del veicolo
        Intervento intervento = interventiDAO.findByVeicolo_TargaAndCodiceServizio(targa, codiceServizio)
                .orElseThrow(() -> new IllegalArgumentException("Intervento non trovato"));

        // Restituisci lo stato della riparazione
        return intervento.getStato().name();  // assuming Stato is an Enum
    }


    public void createEmptyIntervento(Veicolo veicolo) {
        Intervento nuovoIntervento = new Intervento();
        nuovoIntervento.setCodiceServizio(generateCodiceServizio());
        nuovoIntervento.setVeicolo(veicolo);
        nuovoIntervento.setStato(Stato.ACCETTATO); // Imposta uno stato iniziale
        nuovoIntervento.setLavorazioniEffettuate("");
        nuovoIntervento.setCostoManodopera(0.0);
        nuovoIntervento.setRicambi("");
        nuovoIntervento.setTotale(0.0);
        nuovoIntervento.setOreLavorate(0.0);

        interventiDAO.save(nuovoIntervento);
    }

    private String generateCodiceServizio() {
        return "SRV-" + System.currentTimeMillis(); // Genera un codice univoco
    }
}
