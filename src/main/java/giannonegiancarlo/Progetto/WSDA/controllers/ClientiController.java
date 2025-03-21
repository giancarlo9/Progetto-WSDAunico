package giannonegiancarlo.Progetto.WSDA.controllers;

import giannonegiancarlo.Progetto.WSDA.entities.Cliente;
import giannonegiancarlo.Progetto.WSDA.exceptions.BadRequestException;
import giannonegiancarlo.Progetto.WSDA.payloads.NewClienteDTO;
import giannonegiancarlo.Progetto.WSDA.payloads.NewClienteRespDTO;
import giannonegiancarlo.Progetto.WSDA.services.ClientiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clienti")
public class ClientiController {
    @Autowired
    private ClientiService clientiService;


    //    1. POST http://localhost:3001/clienti (+ body)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewClienteRespDTO saveCliente(@RequestBody @Validated NewClienteDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            System.out.println(validation.getAllErrors());
            throw new BadRequestException(validation.getAllErrors());
        }
        System.out.println(body);
        return new NewClienteRespDTO(this.clientiService.saveCliente(body).getId());
    }


    // 2. GET http://localhost:3001/clienti/{{clienteId}}
    @GetMapping("/{clienteId}")
    private Cliente findClienteById(@PathVariable Long clienteId){
        return this.clientiService.findById(clienteId);
    }

    //    3. GET http://localhost:3001/clienti
    @GetMapping
    @PreAuthorize("hasAuthority('ACCETTAZIONE')")
    public List<Cliente> getAllClienti(){
        return this.clientiService.getClientiList();
    }


    // 4. PUT http://localhost:3001/clienti/{{clienteId}} (+ body)
    @PutMapping("/{clienteId}")
    private Cliente findByIdAndUpdate(@PathVariable Long clienteId, @RequestBody Cliente body){
        return this.clientiService.findByIdAndUpdate(clienteId, body);
    }


    // 5. DELETE http://localhost:3001/clienti/{clienteId}
    @DeleteMapping("/{clienteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClienteById(@PathVariable Long clienteId) {
        this.clientiService.findByIdAndDelete(clienteId);
    }


}
