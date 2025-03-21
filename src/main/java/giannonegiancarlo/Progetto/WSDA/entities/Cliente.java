package giannonegiancarlo.Progetto.WSDA.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cognome;
    private String indirizzo;
    private String telefono;
    private String email;

    // Relazione uno-a-molti con veicoli
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Veicolo> veicoli;  // Lista di veicoli associati al cliente

    // Costruttore senza ID (usato quando creiamo un nuovo cliente)
    public Cliente(String nome, String cognome, String indirizzo, String telefono, String email) {
        this.nome = nome;
        this.cognome = cognome;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.email = email;
    }


}

