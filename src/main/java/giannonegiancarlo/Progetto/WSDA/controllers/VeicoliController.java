package giannonegiancarlo.Progetto.WSDA.controllers;

import giannonegiancarlo.Progetto.WSDA.entities.Veicolo;
import giannonegiancarlo.Progetto.WSDA.exceptions.BadRequestException;
import giannonegiancarlo.Progetto.WSDA.payloads.NewVeicoloDTO;
import giannonegiancarlo.Progetto.WSDA.payloads.NewVeicoloRespDTO;
import giannonegiancarlo.Progetto.WSDA.services.VeicoliService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veicoli")
public class VeicoliController {
    @Autowired
    private VeicoliService veicoliService;


    //    1. POST http://localhost:3001/veicoli (+ body)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewVeicoloRespDTO saveVeicolo(@RequestBody @Validated NewVeicoloDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            System.out.println(validation.getAllErrors());
            throw new BadRequestException(validation.getAllErrors());
        }
        System.out.println(body);
        return new NewVeicoloRespDTO(this.veicoliService.saveVeicolo(body).getTarga());
    }


    // 2. GET http://localhost:3001/veicoli/{{veicoloId}}
    @GetMapping("/{veicoloId}")
    private Veicolo findVeicoloById(@PathVariable String veicoloId){
        return this.veicoliService.findById(veicoloId);
    }

    //    3. GET http://localhost:3001/veicoli
    @GetMapping
    @PreAuthorize("hasAuthority('ACCETTAZIONE')")
    public List<Veicolo> getAllVeicoli(){
        return this.veicoliService.getVeicoliList();
    }



    // 4. PUT http://localhost:3001/veicoli/{{veicoloId}} (+ body)
    @PutMapping("/{veicoloId}")
    private Veicolo findByIdAndUpdate(@PathVariable String veicoloId, @RequestBody Veicolo body){
        return this.veicoliService.findByIdAndUpdate(veicoloId, body);
    }



    // 5. DELETE http://localhost:3001/veicoli/{veicoloId}
    @DeleteMapping("/{veicoloId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVeicoloById(@PathVariable String veicoloId) {
        this.veicoliService.findByIdAndDelete(veicoloId);
    }




}

