document.addEventListener("DOMContentLoaded", () => {
  const welcomeMessage = document.getElementById("welcome-message");
  const logoutButton = document.getElementById("logoutButton");
  const container = document.querySelector(".container-fluid"); // Dove inseriremo la tabella

  // Recupero il ruolo dal localStorage
  const userRole = localStorage.getItem("userRole");

  if (userRole) {
    welcomeMessage.textContent = `Benvenuto/a nella dashboard, ${userRole}!`;
  } else {
    welcomeMessage.textContent = "Benvenuto!";
  }

  // Funzione di logout
  logoutButton.addEventListener("click", () => {
    localStorage.removeItem("authToken");
    localStorage.removeItem("userRole");
    window.location.href = "index.html"; // Redirect alla homepage o alla pagina di login
  });

  // Funzione per recuperare gli interventi
  async function fetchInterventi() {
    try {
      const response = await fetch("http://localhost:3001/interventi", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("authToken")}`, // Autenticazione
        },
      });

      if (!response.ok) {
        throw new Error("Errore nel recupero degli interventi");
      }

      let interventi = await response.json();

      renderTable(interventi);
    } catch (error) {
      console.error("Errore:", error);
    }
  }

  // Funzione per creare la tabella
  function renderTable(interventi) {
    const tableContainer = document.createElement("div"); // Contenitore per la tabella
    tableContainer.classList.add("container", "mt-4");

    if (interventi.length === 0) {
      tableContainer.innerHTML = `<p class="text-center text-muted">Nessun intervento disponibile.</p>`;
    } else {
      const table = document.createElement("table");
      table.classList.add("table", "table-striped");

      // intestazione della tabella
      table.innerHTML = `
        <thead class="table-dark">
          <tr>
            <th> <i class="bi bi-upc-scan"></i> Codice Servizio</th>
            <th><i class="bi bi-card-checklist"></i> Stato</th>
            <th><i class="bi bi-tools"> </i> Ricambi</th>
          </tr>
        </thead>
        <tbody>
          ${interventi
            .map(
              (intervento) => `
            <tr>
              <td>${intervento.codiceServizio}</td>
              <td>${intervento.stato}</td>
              <td>${intervento.ricambi || "N/A"}</td>
            </tr>
          `
            )
            .join("")}
        </tbody>
      `;

      tableContainer.appendChild(table);
    }

    // riepilogo dei ricambi (con conteggio delle occorrenze)
    const ricambiCount = {};

    interventi.forEach((intervento) => {
      if (intervento.ricambi) {
        const ricambiList = intervento.ricambi.split(","); // Split dei ricambi separati da virgola
        ricambiList.forEach((ricambio) => {
          ricambio = ricambio.trim(); // Rimuovo spazi extra
          if (ricambiCount[ricambio]) {
            ricambiCount[ricambio]++;
          } else {
            ricambiCount[ricambio] = 1;
          }
        });
      }
    });

    // Creazione del testo del riepilogo con "2x Tergicristallo" e una riga per ogni ricambio
    const riepilogoRicambi = document.createElement("p");
    riepilogoRicambi.classList.add("mt-3", "text-center");

    const ricambiLista = Object.keys(ricambiCount)
      .map((ricambio) => {
        const count = ricambiCount[ricambio];
        return `<i class="bi bi-cursor-fill"></i> ${count}x ${ricambio}`;
      })
      .join("<br/>");

    riepilogoRicambi.innerHTML = `Ricambi da ordinare al fornitore:<br/>${
      ricambiLista || "Nessun ricambio richiesto"
    }`;

    // Pulisco il contenitore prima di aggiungere la tabella
    const interventiContainer = document.getElementById("interventi-container");
    interventiContainer.innerHTML = "";
    interventiContainer.appendChild(tableContainer);
    interventiContainer.appendChild(riepilogoRicambi); // Aggiungo il riepilogo sotto la tabella
    // Sposto il pulsante Logout sotto la tabella e il riepilogo
    const logoutButton = document.getElementById("logoutButton");
    interventiContainer.appendChild(logoutButton);
  }

  // Recupero gli interventi all'avvio
  fetchInterventi();
});
