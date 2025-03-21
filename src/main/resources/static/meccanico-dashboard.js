document.addEventListener("DOMContentLoaded", () => {
  const welcomeMessage = document.getElementById("welcome-message");
  const logoutButton = document.getElementById("logoutButton");
  const container = document.getElementById("interventi-container");

  const userRole = localStorage.getItem("userRole");
  welcomeMessage.textContent = userRole
    ? `Benvenuto/a nella dashboard, ${userRole}!`
    : "Benvenuto!";

  logoutButton.addEventListener("click", () => {
    localStorage.removeItem("authToken");
    localStorage.removeItem("userRole");
    window.location.href = "index.html";
  });

  // Lista dei ricambi predefiniti (devono corrispondere a quelli della mappa backend)

  const ricambiDisponibili = [
    "Pastiglie freno",
    "Olio motore",
    "Catena trasmissione",
    "Corona e pignone",
    "Filtro aria sportivo",
    "Filtro olio",
    "Batteria moto",
    "Candele iridio",
    "Dischi freno wave",
    "Ammortizzatori regolabili",
    "Pompa freno radiale",
    "Radiatore olio",
    "Regolatore di tensione",
    "Gomme sportive",
    "Cupolino",
  ];
  

  async function creaNuovoIntervento(veicoloId) {
    const nuovoIntervento = {
      codiceServizio: `SERV${Math.floor(Math.random() * 1000000)}`, // Generazione codice univoco fittizio
      veicoloId: veicoloId,
      lavorazioniEffettuate: "",
      stato: "IN_LAVORAZIONE",
      costoManodopera: "",
      ricambi: "",
      totale: "",
      oreLavorate: "",
    };

    try {
      const response = await fetch("http://localhost:3001/interventi", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("authToken")}`,
        },
        body: JSON.stringify(nuovoIntervento),
      });

      if (!response.ok)
        throw new Error("Errore nella creazione dell'intervento");

      console.log("Nuovo intervento creato con successo!");
      fetchInterventi(); // Ricarica la tabella dopo la creazione
    } catch (error) {
      console.error("Errore:", error);
    }
  }

  async function fetchInterventi() {
    try {
      const response = await fetch("http://localhost:3001/interventi", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("authToken")}`,
        },
      });

      if (!response.ok) throw new Error("Errore nel recupero degli interventi");

      let interventi = await response.json();
      interventi = interventi.filter(
        (i) => i.stato === "IN_LAVORAZIONE" || i.stato === "ACCETTATO"
      );

      renderTable(interventi);
    } catch (error) {
      console.error("Errore:", error);
    }
  }

  async function updateIntervento(intervento) {
    try {
      const response = await fetch(
        `http://localhost:3001/interventi/${intervento.codiceServizio}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("authToken")}`,
          },
          body: JSON.stringify(intervento),
        }
      );

      if (!response.ok) throw new Error("Errore aggiornamento intervento");

      console.log("Intervento aggiornato con successo!");
    } catch (error) {
      console.error("Errore:", error);
    }
  }

  function renderTable(interventi) {
    container.innerHTML = "";

    if (interventi.length === 0) {
      container.innerHTML = `<p class="text-center text-muted">Nessun intervento disponibile.</p>`;
      return;
    }

    const table = document.createElement("table");
    table.classList.add("table", "table-striped");

    table.innerHTML = `
      <thead class="table-dark">
        <tr>
         <th><i class="bi bi-plus-circle-fill"></i> Aggiungi intervento al veicolo</th>
          <th> <i class="bi bi-upc-scan"></i> Codice Servizio</th>
          <th> <i class="bi bi-tv-fill"></i> Targa Veicolo</th>
          <th> <i class="bi bi-car-front-fill"></i> Modello</th>
          <th><i class="bi bi-wrench-adjustable"></i> Lavorazioni Effettuate</th>
          <th> <i class="bi bi-tools"></i> Ricambi</th>
          <th><i class="bi bi-stopwatch-fill"></i> Ore Lavorate</th>
          <th> <i class="bi bi-card-checklist"></i> Stato</th>
        </tr>
      </thead>
      <tbody>
        ${interventi
          .map((intervento) => {
            return `
          <tr data-id="${intervento.codiceServizio}">
            <td>
              <button class="add-button btn-sm add-intervento" data-veicolo="${
                intervento.veicolo.targa
              }">
                +
              </button>
            </td>
            <td>${intervento.codiceServizio}</td>
            <td>${intervento.veicolo ? intervento.veicolo.targa : "N/A"}</td>
            <td>${intervento.veicolo ? intervento.veicolo.modello : "N/A"}</td>
            <td>
              <input type="text" class="editable custom-table-input" data-field="lavorazioniEffettuate"
                value="${intervento.lavorazioniEffettuate || ""}">
            </td>

            <td>
              <select class="editable-ricambi custom-table-input" data-field="ricambi">
                ${ricambiDisponibili
                  .map(
                    (ricambio) => `
                    <option value="${ricambio}"
                      ${
                        intervento.ricambi &&
                        intervento.ricambi.includes(ricambio)
                          ? "selected"
                          : ""
                      }>
                      ${ricambio}
                    </option>
                  `
                  )
                  .join("")}
              </select>
            </td>

            <td>
              <input type="number" step="0.1" class="editable custom-table-input" data-field="oreLavorate"
                value="${intervento.oreLavorate.toFixed(1)}">
            </td>

            <td>
              <select class="editable custom-table-input" data-field="stato">
                <option value="ACCETTATO" ${
                  intervento.stato === "ACCETTATO" ? "selected" : ""
                }>Accettato</option>
                <option value="IN_LAVORAZIONE" ${
                  intervento.stato === "IN_LAVORAZIONE" ? "selected" : ""
                }>In Lavorazione</option>
                <option value="COMPLETATO" ${
                  intervento.stato === "COMPLETATO" ? "selected" : ""
                }>Completato</option>
              </select>
            </td>
          </tr>
        `;
          })
          .join("")}
      </tbody>
    `;

    container.appendChild(table);

    document.querySelectorAll(".editable").forEach((input) => {
      input.addEventListener("change", (event) => {
        const row = event.target.closest("tr");
        const codiceServizio = row.dataset.id;

        const intervento = interventi.find(
          (i) => i.codiceServizio === codiceServizio
        );
        const field = event.target.dataset.field;

        intervento[field] =
          event.target.type === "number"
            ? parseFloat(event.target.value)
            : event.target.value;

        updateIntervento(intervento);
      });
    });

    document.querySelectorAll(".editable-ricambi").forEach((select) => {
      select.addEventListener("change", (event) => {
        const row = event.target.closest("tr");
        const codiceServizio = row.dataset.id;

        const intervento = interventi.find(
          (i) => i.codiceServizio === codiceServizio
        );
        const selectedOptions = Array.from(event.target.selectedOptions).map(
          (opt) => opt.value
        );

        intervento.ricambi = selectedOptions.join(", "); // Salvo i ricambi come stringa separata da virgole

        updateIntervento(intervento);
      });
    });

    document.querySelectorAll(".add-intervento").forEach((button) => {
      button.addEventListener("click", (event) => {
        const veicoloId = event.target.dataset.veicolo;
        creaNuovoIntervento(veicoloId);
      });
    });
  }

  fetchInterventi();
});
