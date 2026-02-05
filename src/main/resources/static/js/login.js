document.getElementById("loginForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();
    const messageBox = document.getElementById("message");

    try {
        const res = await fetch("/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password })
        });

        if (!res.ok) throw new Error("Login failed");

        const data = await res.json();

        // ✅ Save JWT
        localStorage.setItem("token", data.token);

        // ✅ Decode role
        const payload = JSON.parse(atob(data.token.split(".")[1]));
        const role = payload.roles?.[0] || null;
        localStorage.setItem("role", role);

        // ✅ Redirect
        if (role === "ROLE_ADMIN") {
            window.location.href = "/admin/Admindashboard";
        } else {
            window.location.href = "/users/dashboard";
        }

    } catch (err) {
        messageBox.textContent = "Invalid username or password";
        messageBox.style.color = "red";
    }

     const toggle = document.getElementById("togglePass");
    const pass = document.getElementById("password");
    if (toggle && pass) {
      toggle.addEventListener("click", () => {
        const isHidden = pass.type === "password";
        pass.type = isHidden ? "text" : "password";
        toggle.textContent = isHidden ? "🙈" : "👁️";
      });
    }
});
