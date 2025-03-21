package giannonegiancarlo.Progetto.WSDA.payloads;

import giannonegiancarlo.Progetto.WSDA.enums.Stato;
import jakarta.validation.constraints.NotNull;


public record NewInterventoDTO(
        @NotNull(message = "Il codice servizio è obbligatorio")
        String codiceServizio,

        @NotNull(message = "Il veicolo associato è obbligatorio")
        String veicoloId,

        @NotNull(message = "Le lavorazioni effettuate al veicolo sono obbligatorie")
        String lavorazioniEffettuate,

        @NotNull(message = "Lo stato è obbligatorio")
        Stato stato,


        @NotNull(message = "Il costo della manodopera è obbligatorio")
        double costoManodopera,

        @NotNull(message = "Il campo ricambi è obbligatorio")
        String ricambi,

//        @NotNull(message = "Il costo totale è obbligatorio")
        double totale,

        @NotNull(message = "Le ore lavorate sono obbligatorie")
        double oreLavorate
) {
}
