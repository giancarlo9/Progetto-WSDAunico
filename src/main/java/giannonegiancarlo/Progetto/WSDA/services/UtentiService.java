package giannonegiancarlo.Progetto.WSDA.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import giannonegiancarlo.Progetto.WSDA.entities.Utente;
import giannonegiancarlo.Progetto.WSDA.exceptions.BadRequestException;
import giannonegiancarlo.Progetto.WSDA.exceptions.NotFoundException;
import giannonegiancarlo.Progetto.WSDA.payloads.NewUtenteDTO;
import giannonegiancarlo.Progetto.WSDA.repositories.UtentiDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class UtentiService {

    @Autowired
    private UtentiDAO utentiDAO;


    @Autowired
    private PasswordEncoder bcrypt;



    public UtentiService(UtentiDAO utentiDAO) {
        this.utentiDAO = utentiDAO;
    }

    public List<Utente> getUtentiList() {
        return utentiDAO.findAll();
    }



    public Utente saveUtente(NewUtenteDTO newUtenteDTO) throws BadRequestException {

        if (utentiDAO.existsByEmail(newUtenteDTO.email())) {
            throw new BadRequestException("L'email " + newUtenteDTO.email() + " è già in uso, quindi l'utente ha già un account!");
        }

        Utente utente = new Utente(newUtenteDTO.username(), newUtenteDTO.email(), bcrypt.encode(newUtenteDTO.password()),newUtenteDTO.ruolo());
        System.out.println(utente);
//        mailgunSender.sendRegistrationEmail(utente);
        return utentiDAO.save(utente);
    }


    public Utente findById(int id) {
        return utentiDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public Utente findByIdAndUpdate(int id, Utente updatedUtente) {
        Utente found = findById(id);
        found.setUsername(updatedUtente.getUsername());
        found.setEmail(updatedUtente.getEmail());
        found.setPassword(bcrypt.encode(updatedUtente.getPassword()));
        return utentiDAO.save(found);
    }
    //OK
    public void findByIdAndDelete(int utenteId) {
        utentiDAO.deleteById(utenteId);
    }


    public Page<Utente> getUtenti(int page, int size, String sortBy){
        if(size > 70) size = 70;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.utentiDAO.findAll(pageable);
    }

    public Utente findByEmail(String email) {
        return utentiDAO.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente con email " + email + " non trovato!"));
    }
}
