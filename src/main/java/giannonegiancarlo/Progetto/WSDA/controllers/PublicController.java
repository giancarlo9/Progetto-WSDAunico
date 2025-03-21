package giannonegiancarlo.Progetto.WSDA.controllers;

import giannonegiancarlo.Progetto.WSDA.enums.Stato;
import giannonegiancarlo.Progetto.WSDA.exceptions.BadRequestException;
import giannonegiancarlo.Progetto.WSDA.payloads.NewInterventoRequestDTO;
import giannonegiancarlo.Progetto.WSDA.services.InterventiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private InterventiService interventiService;

    // Endpoint per ottenere lo stato dell'intervento passando il codice del servizio e la targa

//    http://localhost:3001/public/intervento/stato
    @PostMapping("/intervento/stato")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Stato> getStatoIntervento(@RequestBody @Validated NewInterventoRequestDTO body, BindingResult validation) {
        // Verifica se ci sono errori di validazione
        if (validation.hasErrors()) {
            System.out.println(validation.getAllErrors());
            throw new BadRequestException(validation.getAllErrors());
        }

        // Chiamata al servizio per ottenere lo stato dell'intervento
        Stato stato = Stato.valueOf(interventiService.getStatoRiparazione(body.codiceServizio(), body.targa()));

        // Restituisco lo stato come risposta
        return ResponseEntity.ok(stato);
    }


}
