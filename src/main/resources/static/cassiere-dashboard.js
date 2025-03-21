document.addEventListener("DOMContentLoaded", () => {
  const welcomeMessage = document.getElementById("welcome-message");
  const logoutButton = document.getElementById("logoutButton");
  const container = document.getElementById("interventi-container");
  const priceListButton = document.getElementById("priceListButton");
  const priceList = document.getElementById("priceList");

  // Barra di ricerca per la targa
  const searchInput = document.createElement("input");
  searchInput.type = "text";
  searchInput.classList.add("form-control", "mb-3", "custom-input");
  searchInput.placeholder = "🔍 Cerca per targa...";
  searchInput.id = "search-bar";

  container.before(searchInput);

  let interventiData = [];

  // Recupero il ruolo dal localStorage
  const userRole = localStorage.getItem("userRole");
  welcomeMessage.textContent = userRole
    ? `Benvenuto/a nella dashboard, ${userRole}!`
    : "Benvenuto!";

  // Funzione di logout
  logoutButton.addEventListener("click", () => {
    localStorage.removeItem("authToken");
    localStorage.removeItem("userRole");
    window.location.href = "index.html";
  });

  // Funzione per mostrare o nascondere il listino prezzi
  priceListButton.addEventListener("click", () => {
    const isVisible = priceList.style.display === "block";
    if (isVisible) {
      priceList.style.display = "none";
      priceListButton.textContent = "Mostra listino prezzi";
    } else {
      priceList.style.display = "block";
      priceListButton.textContent = "Nascondi listino prezzi";
    }
  });

  // Recupero degli interventi dal server
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

      interventiData = await response.json();
      renderTable(interventiData);
    } catch (error) {
      console.error("Errore:", error);
    }
  }

  // Funzione per aggiornare lo stato dell'intervento
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

  // Funzione per creare la tabella
  function renderTable(interventi) {
    container.innerHTML = ""; // Pulisce il contenitore prima di aggiungere la tabella

    if (interventi.length === 0) {
      container.innerHTML = `<p class="text-center text-muted">Nessun intervento disponibile.</p>`;
      return;
    }

    const table = document.createElement("table");
    table.classList.add("table", "table-striped");

    table.innerHTML = `
      <thead class="table-dark">
        <tr>
          <th> <i class="bi bi-upc-scan"></i> Codice Servizio</th>
          <th> <i class="bi bi-tv-fill"></i> Targa</th>
          <th> <i class="bi bi-wrench-adjustable"></i> Lavorazioni Effettuate</th>
          <th><i class="bi bi-tools"></i> Ricambi</th>
          <th><i class="bi bi-stopwatch-fill"></i> Ore Lavorate</th>
          <th><i class="bi bi-cash-coin"></i> Costo Manodopera (€)</th>
          <th><i class="bi bi-cash-coin"></i> Totale (€)</th>
          <th> <i class="bi bi-card-checklist"></i> Stato</th>
        </tr>
      </thead>
      <tbody>
        ${interventi
          .map((intervento) => {
            return `
          <tr data-id="${intervento.codiceServizio}">
            <td>${intervento.codiceServizio}</td>
            <td>${intervento.veicolo?.targa || "N/A"}</td>
            <td>${intervento.lavorazioniEffettuate || "N/A"}</td>
            <td>${intervento.ricambi || "N/A"}</td>
            <td>${intervento.oreLavorate.toFixed(1)}</td>
            <td>${intervento.costoManodopera.toFixed(2)}</td>
            <td>${intervento.totale.toFixed(2)}</td>
            <td>
              <select class="editable custom-table-input" data-field="stato">
                <option value="${intervento.stato}" selected>${
              intervento.stato
            }</option>
                <option value="PAGATO">Pagato</option>
              </select>
            </td>
          </tr>
        `;
          })
          .join("")}
      </tbody>
    `;

    container.appendChild(table);

    // Aggiungo event listener ai campi editabili
    document.querySelectorAll(".editable").forEach((input) => {
      input.addEventListener("change", (event) => {
        const row = event.target.closest("tr");
        const codiceServizio = row.dataset.id;

        // Trovo l'intervento corrispondente
        const intervento = interventi.find(
          (i) => i.codiceServizio === codiceServizio
        );

        // Aggiorno lo stato (solo se viene impostato su "PAGATO")
        if (event.target.value === "PAGATO") {
          intervento.stato = "PAGATO";
          updateIntervento(intervento);
        } else {
          // Reset se prova a cambiare lo stato in qualcosa di non valido
          event.target.value = intervento.stato;
        }
      });
    });
  }

  // Funzione per filtrare la tabella per targa
  searchInput.addEventListener("input", (event) => {
    const searchTerm = event.target.value.toLowerCase();
    const filteredInterventi = interventiData.filter((intervento) =>
      intervento.veicolo?.targa.toLowerCase().includes(searchTerm)
    );

    renderTable(filteredInterventi);
  });

  // Avvia il caricamento degli interventi
  fetchInterventi();
});
