package giannonegiancarlo.Progetto.WSDA.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import giannonegiancarlo.Progetto.WSDA.enums.Stato;
import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Intervento {

    @Id
    private String codiceServizio;  // Chiave primaria, il codice servizio è univoco


//    @JsonIgnore //questo mi serve a non avere nella response della get degli interventi anche la tabella veicolo, anche perchè richiama quella cliente che richiama quella veicolo e si genererebbe un loop
    @ManyToOne
    @JoinColumn(name = "targa", nullable = false)  // Chiave esterna, la targa del veicolo
    private Veicolo veicolo;  // Il veicolo associato all'intervento

    private String lavorazioniEffettuate;

    @Enumerated(EnumType.STRING)
    private Stato stato;  // Stato dell'intervento (accettato, in lavorazione, completato, pagato)

    private double costoManodopera;  // Costo per la manodopera
    private String ricambi;  // ricambi usati durante l'intervento
    private double totale;  // Costo totale dell'intervento (manodopera + ricambi)

    private double oreLavorate;  // Ore lavorate per l'intervento


    private static final Map<String, Double> PREZZI_RICAMBI = Map.ofEntries(
            Map.entry("Pastiglie freno", 40.0),
            Map.entry("Olio motore", 25.0),
            Map.entry("Catena trasmissione", 90.0),
            Map.entry("Corona e pignone", 110.0),
            Map.entry("Filtro aria sportivo", 35.0),
            Map.entry("Filtro olio", 18.0),
            Map.entry("Batteria moto", 80.0),
            Map.entry("Candele iridio", 50.0),
            Map.entry("Dischi freno wave", 120.0),
            Map.entry("Ammortizzatori regolabili", 150.0),
            Map.entry("Pompa freno radiale", 200.0),
            Map.entry("Radiatore olio", 130.0),
            Map.entry("Regolatore di tensione", 90.0),
            Map.entry("Gomme sportive", 250.0),
            Map.entry("Cupolino", 70.0)
    );



    // Metodo per calcolare il costo della manodopera
    public void calcolaCostoManodopera() {
        this.costoManodopera = this.oreLavorate * 10; // 10€ per ora lavorata
    }

    // Metodo per calcolare il costo dei ricambi
    public double calcolaCostoRicambi() {
        if (ricambi == null || ricambi.trim().isEmpty()) {
            return 0.0;
        }

        return Arrays.stream(ricambi.split(","))
                .map(String::trim)
                .mapToDouble(nome -> PREZZI_RICAMBI.getOrDefault(nome, 0.0))
                .sum();
    }

    // Metodo per calcolare il totale
    public void calcolaTotale() {
        calcolaCostoManodopera();
        this.totale = this.costoManodopera + calcolaCostoRicambi();
    }

    // Metodo per aggiornare il totale prima di salvare l'entità
    @PrePersist
    @PreUpdate
    public void aggiornaTotale() {
        calcolaTotale();
    }

}
