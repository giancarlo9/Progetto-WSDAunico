package giannonegiancarlo.Progetto.WSDA.services;

import giannonegiancarlo.Progetto.WSDA.entities.Utente;
import giannonegiancarlo.Progetto.WSDA.exceptions.UnauthorizedException;
import giannonegiancarlo.Progetto.WSDA.payloads.UtenteLoginDTO;
import giannonegiancarlo.Progetto.WSDA.payloads.UtenteLoginRespDTO;
import giannonegiancarlo.Progetto.WSDA.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    @Autowired
    private UtentiService utentiService;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private PasswordEncoder bcrypt;

    public UtenteLoginRespDTO authenticateUserAndGenerateToken(UtenteLoginDTO payload){
        // 1. Controllo le credenziali
        // 1.1 Cerco nel db tramite l'email l'utente
        Utente utente = this.utentiService.findByEmail(payload.email());

        // 1.2 Verifico se la password combacia con quella ricevuta nel payload
        if (bcrypt.matches(payload.password(), utente.getPassword())) {
            // 2. Se è tutto OK, genero un token
            String token = jwtTools.createToken(utente);

            // 3. Recupero il ruolo dell'utente
            String role = utente.getRuolo().name(); // Ottieni il ruolo come stringa

            // 4. Restituisco la risposta con il token e il ruolo
            return new UtenteLoginRespDTO(token, role);
        } else {
            // 3. Se le credenziali non sono valide, lancio un'eccezione Unauthorized
            throw new UnauthorizedException("Credenziali non valide! Effettua di nuovo il login!");
        }
    }
}

