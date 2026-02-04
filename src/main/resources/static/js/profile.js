document.addEventListener("DOMContentLoaded", () => {
    loadProfile();

    document.getElementById("updateProfileForm").addEventListener("submit", updateProfile);
    document.getElementById("changePasswordForm").addEventListener("submit", changePassword);

    document.getElementById("profileImage").addEventListener("change", previewImage);
});

// 🔹 Load profile
function loadProfile() {
    fetch("/api/auth/profile", {
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
    .then(res => res.json())
    .then(user => {
        document.getElementById("name").value = user.name || "";
        document.getElementById("email").value = user.email || "";
        if (user.imageUrl) {
            document.getElementById("profilePreview").src = user.imageUrl;
        }
    })
    .catch(() => alert("Failed to load profile"));
}

// 🔹 Update profile
function updateProfile(e) {
    e.preventDefault();

    fetch("/api/auth/profile", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        body: JSON.stringify({
            name: document.getElementById("name").value,
            email: document.getElementById("email").value
        })
    })
    .then(res => {
        if (!res.ok) throw new Error();
        alert("✅ Profile updated");
    })
    .catch(() => alert("❌ Update failed"));
}

// 🔹 Change password
function changePassword(e) {
    e.preventDefault();

    fetch("/api/auth/change-password", {
        method: "PUT", // ✅ FIXED
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        body: JSON.stringify({
            oldPassword: document.getElementById("oldPassword").value,
            newPassword: document.getElementById("newPassword").value
        })
    })
    .then(res => {
        if (!res.ok) throw new Error();
        alert("🔐 Password changed successfully");
        e.target.reset();
    })
    .catch(() => alert("❌ Password change failed"));
}

// 🔹 Show / Hide password
function togglePassword(id) {
    const input = document.getElementById(id);
    input.type = input.type === "password" ? "text" : "password";
}

// 🔹 Image preview
function previewImage(e) {
    const reader = new FileReader();
    reader.onload = () => {
        document.getElementById("profilePreview").src = reader.result;
    };
    reader.readAsDataURL(e.target.files[0]);
}

// 🔹 Upload profile image
function uploadProfileImage() {
    const fileInput = document.getElementById("profileImage");
    if (!fileInput.files.length) return alert("Select an image");

    const formData = new FormData();
    formData.append("image", fileInput.files[0]);

    fetch("/api/auth/profile/image", {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        body: formData
    })
    .then(res => {
        if (!res.ok) throw new Error();
        alert("🖼️ Image uploaded");
    })
    .catch(() => alert("❌ Image upload failed"));
}
