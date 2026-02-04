document.addEventListener("DOMContentLoaded", async () => {
    try {
        const res = await authFetch("/api/bookings");

        if (!res || res.status === 401 || res.status === 403) {
            localStorage.clear();
            window.location.href = "/login";
            return;
        }

        const data = await res.json();

        document.getElementById("bookingCount").textContent =
            `${data.bookings ?? 0} Total`;

        document.getElementById("busCount").textContent =
            `${data.buses ?? 0} Active`;

        document.getElementById("passengerCount").textContent =
            `${data.passengers ?? 0} Registered`;

    } catch (err) {
        console.error("❌ Dashboard error:", err);
    }
});
