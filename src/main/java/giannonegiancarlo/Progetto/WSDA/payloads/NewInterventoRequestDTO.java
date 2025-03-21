package giannonegiancarlo.Progetto.WSDA.payloads;

import jakarta.validation.constraints.NotNull;

public record NewInterventoRequestDTO(
        @NotNull(message = "Il codice servizio è obbligatorio")
        String codiceServizio,

        @NotNull(message = "La targa è obbligatoria")
        String targa
) {
}
