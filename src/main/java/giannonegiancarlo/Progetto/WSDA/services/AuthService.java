package giannonegiancarlo.Progetto.WSDA.services;

import giannonegiancarlo.Progetto.WSDA.entities.Utente;
import giannonegiancarlo.Progetto.WSDA.exceptions.UnauthorizedException;
import giannonegiancarlo.Progetto.WSDA.payloads.UtenteLoginDTO;
import giannonegiancarlo.Progetto.WSDA.payloads.UtenteLoginRespDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UtentiService utentiService;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UtenteLoginRespDTO authenticateUserAndCreateSession(UtenteLoginDTO payload) {
        // 1. Cerco nel db tramite l'email l'utente
        Utente utente = this.utentiService.findByEmail(payload.email());

        // 2. Verifico se la password combacia con quella ricevuta nel payload
        if (bcrypt.matches(payload.password(), utente.getPassword())) {
            // 3. Se la password è corretta, creo il contesto di sicurezza per l'autenticazione
            UserDetails userDetails = new User(utente.getUsername(), utente.getPassword(), utente.getAuthorities());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );

            try {
                // Autenticazione e creazione della sessione
                authenticationManager.authenticate(authenticationToken);

                // 4. Recupero il ruolo dell'utente
                String role = utente.getRuolo().name(); // Ottieni il ruolo come stringa

                // 5. Restituisco la risposta con il ruolo (la sessione sarà gestita da Spring Security)
                return new UtenteLoginRespDTO(null, role); // Token non più necessario
            } catch (AuthenticationException e) {
                throw new UnauthorizedException("Credenziali non valide! Effettua di nuovo il login!");
            }
        } else {
            // 6. Se le credenziali non sono valide, lancio un'eccezione Unauthorized
            throw new UnauthorizedException("Credenziali non valide! Effettua di nuovo il login!");
        }
    }
}
