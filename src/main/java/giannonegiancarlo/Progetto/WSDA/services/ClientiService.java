package giannonegiancarlo.Progetto.WSDA.services;

import giannonegiancarlo.Progetto.WSDA.entities.Cliente;
import giannonegiancarlo.Progetto.WSDA.exceptions.NotFoundException;
import giannonegiancarlo.Progetto.WSDA.payloads.NewClienteDTO;
import giannonegiancarlo.Progetto.WSDA.repositories.ClientiDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientiService {

    @Autowired
    private ClientiDAO clientiDAO;

    public ClientiService(ClientiDAO clientiDAO) {
        this.clientiDAO = clientiDAO;
    }


    public List<Cliente> getClientiList() {return clientiDAO.findAll();}

    public Cliente saveCliente(NewClienteDTO newClienteDTO) {
        // Potrei ottene l'oggetto di autenticazione qualora avessi una piattaforma in cui ogni utente non debba vedere ciò che salva un altro utente
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        // Estraggo l'ID dell'utente e ottengo l'utente dal DAO
//        int utenteId = extractUtenteIdFromAuthentication(authentication);
//        Utente utente = utentiDAO.findById(utenteId)
//                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato con ID: " + utenteId));

        // Creo un nuovo cliente e qualora fosse un'applicazione da gestire per più officine separate, potrei
        //assegnare l'utente al cliente/intervento/veicolo creato, ma qua non ha senso assegnare l'utente
        //perchè sarà un sistema unico
        Cliente cliente = new Cliente(
                newClienteDTO.nome(),
                newClienteDTO.cognome(),
                newClienteDTO.indirizzo(),
                newClienteDTO.telefono(),
                newClienteDTO.email()
//                utente
        );
        System.out.println(cliente);
        System.out.println(newClienteDTO);
        return clientiDAO.save(cliente);
    }

//    // Metodo per estrarre l'ID dell'utente dalla classe Utente
//    private int extractUtenteIdFromAuthentication(Authentication authentication) {
//        // Ottengo l'utente autenticato dall'oggetto di autenticazione
//        Utente utente = (Utente) authentication.getPrincipal();
//        return utente.getId();
//    }


    public Cliente findById(Long id) {
        return clientiDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente non trovato con ID: " + id));
    }

    public Cliente findByIdAndUpdate(Long id, Cliente updatedCliente) {
        Cliente found = findById(id);
        found.setNome(updatedCliente.getNome());
        found.setCognome(updatedCliente.getCognome());
        found.setIndirizzo(updatedCliente.getIndirizzo());
        found.setTelefono(updatedCliente.getTelefono());
        found.setEmail(updatedCliente.getEmail());
        found.setVeicoli(updatedCliente.getVeicoli());
        return clientiDAO.save(found);
    }

    public void findByIdAndDelete(Long clienteId) {
        clientiDAO.deleteById(clienteId);
    }


    public Optional<Cliente> getClientiByEmail(String email) {
        return clientiDAO.findByEmail(email);
    }




}
