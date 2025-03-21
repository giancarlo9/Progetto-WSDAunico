document.addEventListener("DOMContentLoaded", async () => {
  // Recupero il token dal localStorage
  const authToken = localStorage.getItem("authToken");

  if (!authToken) {
    alert("Devi effettuare il login prima di accedere ai dati!");
    window.location.href = "/login.html"; // Redirect alla pagina di login
    return;
  }

  try {
    // Chiamata API per ottenere i veicoli registrati
    const response = await fetch("http://localhost:3001/veicoli", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${authToken}`, // token per l'autenticazione
      },
    });

    if (!response.ok) {
      throw new Error("Errore nel recupero dei veicoli.");
    }

    // dati della risposta
    const veicoli = await response.json();
    const veicoliTableBody = document.querySelector("#veicoliTable tbody");

    // Inserisco i dati nella tabella
    veicoli.forEach((veicolo) => {
      const tr = document.createElement("tr");

      tr.innerHTML = `
          <td>${veicolo.targa.toUpperCase()}</td>
          <td>${veicolo.marca}</td>
          <td>${veicolo.modello}</td>
          <td>${veicolo.anno}</td>
        `;

      veicoliTableBody.appendChild(tr);
    });
  } catch (error) {
    console.error("Errore durante il recupero dei veicoli:", error);
    alert("C'è stato un errore nel recupero dei dati.");
  }
});
