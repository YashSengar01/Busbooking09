document.addEventListener("DOMContentLoaded", () => {
  fetchBuses();

  const addBusForm = document.getElementById('addBusForm');
  if (addBusForm) {
    addBusForm.addEventListener('submit', async function (e) {
      e.preventDefault();

      const formData = new FormData(this);
      const data = Object.fromEntries(formData.entries());

      const response = await fetch('/admin/api/buses', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + localStorage.getItem('token')
        },
        body: JSON.stringify(data)
      });

      if (response.ok) {
        alert('✅ Bus added successfully!');
        this.reset();
        fetchBuses();
      } else {
        alert('❌ Failed to add bus');
      }
    });
  }
});

// Fetch and display all buses
function fetchBuses() {
   const token = localStorage.getItem('token');
  fetch("/admin/api/buses", {
    headers: {
      'Authorization': 'Bearer ' + localStorage.getItem('token')
    }
  })
    .then(res => {
      if (!res.ok) {
        throw new Error(`HTTP error! status: ${res.status}`);
      }
      return res.json();
    })
    .then(buses => {
      const tableBody = document.getElementById("busTableBody");
      tableBody.innerHTML = "";

      buses.forEach(bus => {
        const row = document.createElement("tr");
        row.innerHTML = `
          <td>${bus.fromLocation}</td>
          <td>${bus.toLocation}</td>
          <td>${bus.departureTime}</td>
          <td>${bus.arrivalTime}</td>
          <td>${bus.price}</td>
          <td>${bus.totalSeats}</td>
          <td><button class="btn btn-danger btn-sm" onclick="deleteBus(${bus.id})">Delete</button></td>
        `;
        tableBody.appendChild(row);
      });
    })
    .catch(err => console.error("Failed to fetch buses:", err));
}

// Delete a bus
async function deleteBus(id) {
  if (confirm("Are you sure you want to delete this bus?")) {
    const response = await fetch(`/admin/api/buses/${id}`, {
      method: 'DELETE',
      headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('token')
      }
    });

    if (response.ok) {
      alert("🗑️ Bus deleted.");
      fetchBuses();
    } else {
      alert("❌ Failed to delete bus.");
    }
  }
}

// Filter buses based on search input
function searchBuses() {
  const from = document.getElementById("fromLocation").value.toLowerCase();
  const to = document.getElementById("toLocation").value.toLowerCase();
  const rows = document.querySelectorAll("#busTableBody tr");

  rows.forEach(row => {
    const fromText = row.children[0].textContent.toLowerCase();
    const toText = row.children[1].textContent.toLowerCase();

    row.style.display = (from && !fromText.includes(from)) || (to && !toText.includes(to)) ? "none" : "";
  });
}

