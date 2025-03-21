document.addEventListener("DOMContentLoaded", async () => {
  // Recupero il token dal localStorage
  const authToken = localStorage.getItem("authToken");

  if (!authToken) {
    alert("Devi effettuare il login prima di accedere ai dati!");
    window.location.href = "/login.html"; // Redirigo alla pagina di login
    return;
  }

  try {
    // richiesta GET per ottenere i dati dei clienti
    const response = await fetch("http://localhost:3001/clienti", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${authToken}`, // token per l'autenticazione
      },
    });

    if (!response.ok) {
      throw new Error("Errore nel recupero dei dati.");
    }

    // dati dalla risposta JSON
    const clienti = await response.json();
    const clientTableBody = document.querySelector("#clientTable tbody");

    // Per ogni cliente, creo una riga della tabella
    clienti.forEach((cliente) => {
      const tr = document.createElement("tr");

      // Aggiungo celle con i dati del cliente
      tr.innerHTML = `
          <td>${cliente.id}</td>
          <td>${cliente.nome}</td>
          <td>${cliente.cognome}</td>
          <td>${cliente.indirizzo}</td>
          <td>${cliente.telefono}</td>
          <td>${cliente.email}</td>
          <td>
            ${
              cliente.veicoli.length > 0
                ? cliente.veicoli
                    .map((veicolo) => `${veicolo.marca} ${veicolo.modello}`)
                    .join(", ")
                : "Nessun veicolo"
            }
          </td>
        `;

      clientTableBody.appendChild(tr); // Aggiungo la riga alla tabella
    });
  } catch (error) {
    console.error("Errore durante il recupero dei clienti:", error);
    alert("C'è stato un errore nel recupero dei dati.");
  }
});
