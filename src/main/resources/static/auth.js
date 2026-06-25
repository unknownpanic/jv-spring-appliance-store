const API_BASE = 'http://localhost:8080';

function getToken() { return localStorage.getItem('jwt_token'); }
function saveToken(token) { localStorage.setItem('jwt_token', token); }
function removeToken() { localStorage.removeItem('jwt_token'); }

function decodeJwt(token) {
  if (!token) return null;
  try {
    const base64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
    return JSON.parse(atob(base64));
  } catch { return null; }
}

function getUser() {
  const decoded = decodeJwt(getToken());
  if (!decoded) return null;
  const roles = decoded.roles || (decoded.role ? [decoded.role] : []);
  return {
    email: decoded.sub || decoded.email,
    roles,
    isEmployee: roles.some(r => r === 'ROLE_EMPLOYEE' || r === 'EMPLOYEE'),
    isClient: roles.some(r => r === 'ROLE_CLIENT' || r === 'CLIENT')
  };
}

function isEmployee() { return getUser()?.isEmployee || false; }
function isLoggedIn() { return !!getToken(); }

function logout() {
  removeToken();
  window.location.href = 'login.html';
}

function requireAuth() {
  if (!isLoggedIn()) window.location.href = 'login.html';
}

function requireEmployee() {
  if (!isLoggedIn()) { window.location.href = 'login.html'; return; }
  if (!isEmployee()) window.location.href = '/error/403.html';
}

async function apiFetch(path, options = {}) {
  const token = getToken();
  const headers = {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...(options.headers || {})
  };

  const res = await fetch(API_BASE + path, { ...options, headers });

  if (res.status === 204) return null;

  const text = await res.text();
  let data;
  try {
    data = JSON.parse(text);
  } catch {
    data = { message: text || `HTTP ${res.status}` };
  }

  if (!res.ok) {
    if (res.status === 403) {
      window.location.href = '/error/403.html';
      return;
    }
    if (res.status === 404 && !path.startsWith('/api/')) {
      window.location.href = '/error/404.html';
      return;
    }

    const msg = data.message || data.error || `HTTP ${res.status}`;
    const err = new Error(msg);
    err.status = res.status;
    throw err;
  }

  return data;
}

function initNav() {
  initLang();
  const user = getUser();

  document.querySelectorAll('.auth-only').forEach(el => el.style.display = user ? '' : 'none');
  document.querySelectorAll('.guest-only').forEach(el => el.style.display = user ? 'none' : '');
  document.querySelectorAll('.employee-only').forEach(el => el.style.display = user?.isEmployee ? '' : 'none');

  const emailEl = document.getElementById('userEmail');
  const roleEl = document.getElementById('userRole');
  if (emailEl && user) {
    if (!user.isEmployee) {
      emailEl.innerHTML = `<a href="profile.html" style="text-decoration: underline; cursor: pointer;">${user.email}</a>`;
    } else {
      emailEl.textContent = user.email;
    }
  }
  if (roleEl && user) {
    roleEl.textContent = user.isEmployee ? 'Employee' : 'Client';
    roleEl.className = 'badge ' + (user.isEmployee ? 'badge-blue' : 'badge-green');
  }

  const burger = document.getElementById('burger');
  const navLinks = document.getElementById('navLinks');
  if (burger && navLinks) {
    burger.addEventListener('click', () => navLinks.classList.toggle('open'));
  }

  applyLang();
}

function showError(msg) {
  const box = document.getElementById('errorBox');
  if (box) { box.textContent = msg; box.classList.remove('hidden'); }
}

function hideError() {
  const box = document.getElementById('errorBox');
  if (box) box.classList.add('hidden');
}
