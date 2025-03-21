package giannonegiancarlo.Progetto.WSDA.services;


import giannonegiancarlo.Progetto.WSDA.entities.Cliente;
import giannonegiancarlo.Progetto.WSDA.entities.Veicolo;
import giannonegiancarlo.Progetto.WSDA.exceptions.NotFoundException;
import giannonegiancarlo.Progetto.WSDA.payloads.NewVeicoloDTO;
import giannonegiancarlo.Progetto.WSDA.repositories.ClientiDAO;
import giannonegiancarlo.Progetto.WSDA.repositories.VeicoliDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeicoliService {

    @Autowired
    private VeicoliDAO veicoliDAO;


    @Autowired
    private ClientiDAO clientiDAO;

    @Autowired
    private InterventiService interventiService; // Servizio per la creazione dell'intervento

    public VeicoliService(VeicoliDAO veicoliDAO) {
        this.veicoliDAO = veicoliDAO;
    }

    public List<Veicolo> getVeicoliList() {
        return veicoliDAO.findAll();
    }

    public Veicolo saveVeicolo(NewVeicoloDTO newVeicoloDTO) {


        Cliente cliente = clientiDAO.findById(newVeicoloDTO.clienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente non trovato con ID: " + newVeicoloDTO.clienteId()));
        Veicolo veicolo = new Veicolo(
                newVeicoloDTO.targa(),
                newVeicoloDTO.marca(),
                newVeicoloDTO.modello(),
                newVeicoloDTO.anno(),
                cliente
        );

        Veicolo savedVeicolo = veicoliDAO.save(veicolo);

        // Dopo aver creato il veicolo, creo automaticamente un intervento associato
        interventiService.createEmptyIntervento(savedVeicolo);

        return savedVeicolo;
    }


    public Veicolo findById(String id) {
        return veicoliDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public Veicolo findByIdAndUpdate(String id, Veicolo updatedVeicolo) {
        Veicolo found = findById(id);
        found.setTarga(updatedVeicolo.getTarga());
        found.setMarca(updatedVeicolo.getMarca());
        found.setModello(updatedVeicolo.getModello());
        found.setAnno(updatedVeicolo.getAnno());
        return veicoliDAO.save(found);
    }

    public void findByIdAndDelete(String veicoloId) {
        veicoliDAO.deleteById(veicoloId);
    }


    


}
