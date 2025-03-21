package giannonegiancarlo.Progetto.WSDA.controllers;

import giannonegiancarlo.Progetto.WSDA.exceptions.BadRequestException;
import giannonegiancarlo.Progetto.WSDA.payloads.NewUtenteDTO;
import giannonegiancarlo.Progetto.WSDA.payloads.NewUtenteRespDTO;
import giannonegiancarlo.Progetto.WSDA.payloads.UtenteLoginDTO;
import giannonegiancarlo.Progetto.WSDA.payloads.UtenteLoginRespDTO;
import giannonegiancarlo.Progetto.WSDA.services.AuthService;
import giannonegiancarlo.Progetto.WSDA.services.UtentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UtentiService utentiService;

    //    POST http://localhost:3001/auth/login
    @PostMapping("/login")
    public UtenteLoginRespDTO login(@RequestBody UtenteLoginDTO payload){
        return this.authService.authenticateUserAndGenerateToken(payload); // Restituisce UtenteLoginRespDTO
    }

    //    POST http://localhost:3001/auth/register
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUtenteRespDTO saveUser(@RequestBody @Validated NewUtenteDTO body, BindingResult validation){
        // @Validated valida il payload in base ai validatori utilizzati nella classe NewUtenteDTO
        // BindingResult validation ci serve per valutare il risultato di questa validazione
        if(validation.hasErrors()) { // Se ci sono stati errori di validazione dovrei triggerare un 400 Bad Request
            throw new BadRequestException(validation.getAllErrors()); // Invio la lista degli errori all'Error Handler opportuno
        }
        // Altrimenti se non ci sono stati errori posso salvare tranquillamente lo user
        return new NewUtenteRespDTO(this.utentiService.saveUtente(body).getId());
    }

}
