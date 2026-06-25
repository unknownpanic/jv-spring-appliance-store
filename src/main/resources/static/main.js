initNav();
applyLang();

const user = getUser();

const titleEl = document.getElementById('welcomeTitle');
if (titleEl) {
  const greeting = getLang() === 'uk' ? 'Ласкаво просимо' : 'Welcome';
  titleEl.textContent = user ? `${greeting}, ${user.email}!` : `${greeting}! 👋`;
}

const cards = [
  { href: 'appliances.html', icon: '🔌', key: 'nav.appliances', descUk: 'Перегляд та пошук побутової техніки', descEn: 'Browse and search appliances', roles: 'all' },
  { href: 'orders.html',     icon: '🛒', key: 'nav.orders',     descUk: 'Створення та управління замовленнями', descEn: 'Create and manage your orders', roles: 'auth' },
  { href: 'clients.html',    icon: '👥', key: 'nav.clients',    descUk: 'Управління клієнтами магазину', descEn: 'Manage store clients', roles: 'employee' },
  { href: 'employees.html',  icon: '👤', key: 'nav.employees',  descUk: 'Управління працівниками', descEn: 'Manage store employees', roles: 'employee' },
  { href: 'manufacturers.html', icon: '🏭', key: 'nav.manufacturers', descUk: 'Управління виробниками техніки', descEn: 'Manage appliance manufacturers', roles: 'employee' },
];

function isAllowed(roles) {
  if (roles === 'all') return true;
  if (!user) return false;
  if (roles === 'auth') return true;
  if (roles === 'employee') return user.isEmployee;
  return false;
}

const grid = document.getElementById('menuCards');
if (grid) {
  const lang = getLang();
  cards.filter(c => isAllowed(c.roles)).forEach(card => {
    const a = document.createElement('a');
    a.href = card.href;
    a.className = 'menu-card';
    a.innerHTML = `
      <div class="menu-card-icon">${card.icon}</div>
      <h3>${t(card.key)}</h3>
      <p>${lang === 'uk' ? card.descUk : card.descEn}</p>
      <span class="menu-card-arrow">→</span>
    `;
    grid.appendChild(a);
  });
}
