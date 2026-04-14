// BFF base URL — in Kubernetes this will be the ingress URL
const API_BASE = 'http://localhost:8086/api';

// Get stored token
function getToken() {
    return localStorage.getItem('token');
}

// Check if user is logged in
function isLoggedIn() {
    return !!getToken();
}

// Update navbar auth button
function updateNavAuth() {
    const authLink = document.getElementById('auth-link');
    if (!authLink) return;
    if (isLoggedIn()) {
        authLink.textContent = 'Logout';
        authLink.href = '#';
        authLink.onclick = () => {
            localStorage.removeItem('token');
            localStorage.removeItem('userId');
            window.location.href = '/index.html';
        };
    } else {
        authLink.textContent = 'Login / Signup';
        authLink.href = '/login.html';
    }
}

// Generic API call helper
async function apiCall(method, path, body = null, requireAuth = false) {
    const headers = { 'Content-Type': 'application/json' };
    if (requireAuth && getToken()) {
        headers['Authorization'] = `Bearer ${getToken()}`;
    }
    const response = await fetch(API_BASE + path, {
        method,
        headers,
        body: body ? JSON.stringify(body) : null
    });
    return response;
}

// Format salary nicely
function formatSalary(amount, currency) {
    return `${currency} ${Number(amount).toLocaleString()}`;
}

// Show alert message
function showAlert(containerId, message, type = 'info') {
    const container = document.getElementById(containerId);
    if (!container) return;
    container.innerHTML = `<div class="alert alert-${type}">${message}</div>`;
    setTimeout(() => container.innerHTML = '', 4000);
}

// Badge color by level
function levelBadge(level) {
    const l = (level || '').toLowerCase();
    if (l === 'senior') return 'badge-senior';
    if (l === 'junior') return 'badge-junior';
    return 'badge-mid';
}

// Run on every page
document.addEventListener('DOMContentLoaded', updateNavAuth);