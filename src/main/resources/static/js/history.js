document.addEventListener("DOMContentLoaded", async () => {
    try {
        const res = await authFetch("/api/bookings");

        if (!res || res.status === 401 || res.status === 403) {
            localStorage.clear();
            window.location.href = "/";
            return;
        }

        const bookings = await res.json();
        const tableBody = document.getElementById("historyTableBody");
        const noData = document.getElementById("noData");

        tableBody.innerHTML = "";

        if (!bookings || bookings.length === 0) {
            noData.classList.remove("d-none");
            return;
        }

        bookings.forEach((b, index) => {
            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${index + 1}</td>
                <td>${b.passengerName}</td>
                <td>${b.contactInfo}</td>
                <td>${b.seatCount}</td>
                <td>${b.seatPreference}</td>
                <td>${b.bus ? b.bus.id : "N/A"}</td>
                <td>
                    <button class="btn btn-sm btn-outline-info"
                        onclick="viewTicket(${b.id})">
                        🎫 View Ticket
                    </button>
                </td>
            `;

            tableBody.appendChild(row);
        });

    } catch (err) {
        console.error("❌ Booking history error:", err);
    }
});

/**
 * ✅ Securely loads PDF ticket using JWT (NO 403, NO JSON error)
 */
async function viewTicket(bookingId) {
    try {
        const res = await authFetch(`/api/bookings/${bookingId}/ticket`);

        if (!res.ok) {
            alert("❌ Unable to load ticket");
            return;
        }

        // 🔥 PDF = BLOB (not JSON)
        const blob = await res.blob();
        const url = window.URL.createObjectURL(blob);

        // Open PDF in new tab
        window.open(url, "_blank");

        // Cleanup memory
        setTimeout(() => window.URL.revokeObjectURL(url), 5000);

    } catch (err) {
        console.error("❌ Ticket error:", err);
        alert("Something went wrong while loading the ticket");
    }
}
