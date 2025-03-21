package giannonegiancarlo.Progetto.WSDA.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Veicolo {

    @Id
    private String targa;  // La targa è la chiave primaria del veicolo (@Id)

    private String marca;  // Marca del veicolo
    private String modello;  // Modello del veicolo
    private int anno;

    // Relazione con Cliente (molti veicoli possono appartenere a un cliente)
    @JsonIgnore
    //questo mi serve a non avere nella response della get dei veicoli anche la tabella  cliente, anche perchè richiama quella veicolo e si genererebbe un loop
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)  // Colonna che fa riferimento alla chiave primaria di Cliente
    private Cliente cliente;  // Il cliente a cui appartiene il veicolo



}
