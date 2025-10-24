// Show all maps with marker
function initLeafletMaps() {
    setTimeout(() => {
        document.querySelectorAll(".map").forEach(div => {
            if (div.dataset.initialized === "true") return;

            const lat = parseFloat(div.dataset.lat);
            const lng = parseFloat(div.dataset.lng);
            if (isNaN(lat) || isNaN(lng)) return;

            const map = L.map(div).setView([lat, lng], 15);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                maxZoom: 19
            }).addTo(map);
            L.marker([lat, lng]).addTo(map);
            map.invalidateSize();

            div.dataset.initialized = "true";
        });
    }, 500);
}