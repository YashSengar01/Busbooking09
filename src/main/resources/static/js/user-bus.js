document.addEventListener("DOMContentLoaded", () => {
  loadBuses();

  document.getElementById("fromLocation").addEventListener("input", filterBuses);
  document.getElementById("toLocation").addEventListener("input", filterBuses);
});

let allBuses = [];

function loadBuses() {
  fetch("/admin/api/buses", {
    headers: {
      "Authorization": "Bearer " + localStorage.getItem("token")
    }
  })
    .then(res => {
      if (!res.ok) {
        throw new Error("Unauthorized");
      }
      return res.json();
    })
    .then(buses => {
      allBuses = buses;
      renderTable(buses);
    })
    .catch(err => {
      console.error("Failed to load buses:", err);
      document.getElementById("busTableBody").innerHTML = `
        <tr>
          <td colspan="6" class="text-center text-danger">
            Failed to load buses
          </td>
        </tr>
      `;
    });
}

function filterBuses() {
  const from = document.getElementById("fromLocation").value.toLowerCase();
  const to = document.getElementById("toLocation").value.toLowerCase();

  const filtered = allBuses.filter(bus => {
    const fromMatch = !from || bus.fromLocation.toLowerCase().includes(from);
    const toMatch = !to || bus.toLocation.toLowerCase().includes(to);
    return fromMatch && toMatch;
  });

  renderTable(filtered);
}

function renderTable(buses) {
  const tableBody = document.getElementById("busTableBody");
  tableBody.innerHTML = "";

  if (buses.length === 0) {
    tableBody.innerHTML = `
      <tr>
        <td colspan="6" class="text-center text-muted">
          No buses found
        </td>
      </tr>
    `;
    return;
  }

  buses.forEach(bus => {
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${bus.fromLocation}</td>
      <td>${bus.toLocation}</td>
      <td>${bus.departureTime}</td>
      <td>${bus.arrivalTime}</td>
      <td>₹${bus.price}</td>
      <td>${bus.totalSeats}</td>
    `;
    tableBody.appendChild(row);
  });
}
