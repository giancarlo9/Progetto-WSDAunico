package giannonegiancarlo.Progetto.WSDA.controllers;

import giannonegiancarlo.Progetto.WSDA.entities.Intervento;
import giannonegiancarlo.Progetto.WSDA.exceptions.BadRequestException;
import giannonegiancarlo.Progetto.WSDA.payloads.NewInterventoDTO;
import giannonegiancarlo.Progetto.WSDA.payloads.NewInterventoRespDTO;
import giannonegiancarlo.Progetto.WSDA.services.InterventiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interventi")
public class InterventiController {
    @Autowired
    private InterventiService interventiService;


    //    1. POST http://localhost:3001/interventi (+ body)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewInterventoRespDTO saveIntervento(@RequestBody @Validated NewInterventoDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            System.out.println(validation.getAllErrors());
            throw new BadRequestException(validation.getAllErrors());
        }
        System.out.println(body);
        return new NewInterventoRespDTO(this.interventiService.saveIntervento(body).getCodiceServizio());
    }




    //    2. GET http://localhost:3001/interventi
    @GetMapping
//    @PreAuthorize("hasAuthority('MECCANICO')")
    @PreAuthorize("hasAnyAuthority('MECCANICO', 'MAGAZZINIERE' , 'CASSIERE')")
    public List<Intervento> getAllIntervento(){
        return this.interventiService.getInterventiList();
    }



    //    3 Vedere se integrarla al posto della get normale
    //    Paginazione e ordinamento http://localhost:3001/interventi/page
    @GetMapping("/page")
    public Page<Intervento> getAllInterventi(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "1") int size,
                                           @RequestParam(defaultValue = "id") String sortBy) {
        return this.interventiService.getInterventi(page, size, sortBy);
    }

    // 4. PUT http://localhost:3001/interventi/{{interventoId}} (+ body)
    @PutMapping("/{interventoId}")
    public Intervento findByIdAndUpdate(@PathVariable String interventoId, @RequestBody Intervento body){
        return this.interventiService.findByIdAndUpdate(interventoId, body);
    }



    // 5. DELETE http://localhost:3001/interventi/{interventoId}
    @DeleteMapping("/{interventoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInterventoById(@PathVariable String interventoId) {
        this.interventiService.findByIdAndDelete(interventoId);
    }





}
