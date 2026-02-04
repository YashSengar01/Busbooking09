document.addEventListener("DOMContentLoaded", () => {
  loadBuses();

  const form = document.getElementById("bookingForm");
  form.addEventListener("submit", async function (e) {
    e.preventDefault();
    const formData = new FormData(form);
    const data = {
  passengerName: document.getElementById("passengerName").value,
  contactInfo: document.getElementById("contactInfo").value,
  seatCount: parseInt(document.getElementById("seatCount").value),
  seatPreference: document.getElementById("seatPreference").value,
  busId: parseInt(document.getElementById("busDropdown").value)
};


    const response = await fetch("/api/bookings", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + localStorage.getItem("token")
      },
      body: JSON.stringify(data)
    });

   if (response.ok) {
  const confirmation = await response.json();

  alert(
    `🎉 Booking Confirmed!\n\n` +
    `Booking ID: ${confirmation.bookingId}\n` +
    `Route: ${confirmation.fromLocation} → ${confirmation.toLocation}`
  );

  window.location.href =
    `/api/bookings/${confirmation.bookingId}/ticket`;
}
  });
});

function loadBuses() {
  fetch("/admin/api/buses", {
    headers: {
      "Authorization": "Bearer " + localStorage.getItem("token")
    }
  })
    .then(res => {
      if (!res.ok) throw new Error("Unauthorized");
      return res.json();
    })
    .then(buses => {
      const dropdown = document.getElementById("busDropdown");
      const tableBody = document.getElementById("busTableBody");

      dropdown.innerHTML = `<option disabled selected value="">Select a Bus</option>`;
      tableBody.innerHTML = "";

      buses.forEach(bus => {
        dropdown.innerHTML += `<option value="${bus.id}">
          ${bus.fromLocation} ➜ ${bus.toLocation}
        </option>`;

        const row = document.createElement("tr");
        row.innerHTML = `
          <td>${bus.fromLocation}</td>
          <td>${bus.toLocation}</td>
          <td>${bus.departureTime}</td>
          <td>${bus.arrivalTime}</td>
        `;
        tableBody.appendChild(row);
      });
    })
    .catch(err => console.error("Failed to load buses", err));
}
