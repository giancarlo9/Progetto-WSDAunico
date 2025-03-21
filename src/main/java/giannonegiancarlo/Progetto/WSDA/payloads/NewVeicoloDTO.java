package giannonegiancarlo.Progetto.WSDA.payloads;

import jakarta.validation.constraints.NotNull;

public record NewVeicoloDTO(
        @NotNull(message = "La targa è obbligatoria")
        String targa,

        @NotNull(message = "La marca è obbligatoria")
        String marca,

        @NotNull(message = "Il modello è obbligatorio")
        String modello,

        @NotNull(message = "L'anno è obbligatorio")
        int anno,

        @NotNull(message = "L'ID cliente è obbligatorio")
        Long clienteId
) {
}
