package giannonegiancarlo.Progetto.WSDA.payloads;

import giannonegiancarlo.Progetto.WSDA.enums.Stato;
import jakarta.validation.constraints.NotNull;

public record NewClienteDTO(

        @NotNull(message = "Il nome è obbligatorio")
        String nome,

        @NotNull(message = "Il cognome è obbligatorio")
        String cognome,

        @NotNull(message = "L'indirizzo è obbligatorio")
        String indirizzo,

        @NotNull(message = "Il numero di telefono è obbligatorio")
        String telefono,

        @NotNull(message = "L'indirizzo email è obbligatorio")
        String email
) {
}
