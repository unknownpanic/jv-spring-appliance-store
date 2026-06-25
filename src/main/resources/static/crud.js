function showLoading(containerId) {
  document.getElementById(containerId).innerHTML = `<p class="no-data">${t('common.loading')}</p>`;
}

function openModal() {
  document.getElementById('modal').classList.remove('hidden');
  const errBox = document.getElementById('modalErrorBox');
  if (errBox) errBox.classList.add('hidden');
}

function closeModal() {
  document.getElementById('modal').classList.add('hidden');
}

function renderPagination(page, totalPages) {
  const container = document.getElementById('pagination');
  if (!container) return;
  if (totalPages <= 1) { container.innerHTML = ''; return; }
  container.innerHTML = `
    <button class="btn-outline" onclick="loadItems(${page - 1})" ${page === 0 ? 'disabled' : ''}>‹ ${t('common.back')}</button>
    <span>${t('common.page')} ${page + 1} / ${totalPages}</span>
    <button class="btn-outline" onclick="loadItems(${page + 1})" ${page >= totalPages - 1 ? 'disabled' : ''}>${t('common.next') || 'Next'} ›</button>
  `;
}

let _confirmCallback = null;

function showConfirm(msg, callback) {
  _confirmCallback = callback;
  document.getElementById('confirmMsg').textContent = msg;
  document.getElementById('confirmModal').classList.remove('hidden');
}

document.addEventListener('DOMContentLoaded', () => {
  const modalClose = document.getElementById('modalClose');
  const modalCancel = document.getElementById('modalCancel');
  if (modalClose) modalClose.addEventListener('click', closeModal);
  if (modalCancel) modalCancel.addEventListener('click', closeModal);

  const confirmYes = document.getElementById('confirmYes');
  const confirmNo = document.getElementById('confirmNo');
  if (confirmYes) confirmYes.addEventListener('click', async () => {
    document.getElementById('confirmModal').classList.add('hidden');
    if (_confirmCallback) {
      try { await _confirmCallback(); }
      catch (err) { showError(err.message); }
      _confirmCallback = null;
    }
  });
  if (confirmNo) confirmNo.addEventListener('click', () => {
    document.getElementById('confirmModal').classList.add('hidden');
    _confirmCallback = null;
  });

  document.querySelectorAll('.modal-overlay').forEach(overlay => {
    overlay.addEventListener('click', (e) => {
      if (e.target === overlay) overlay.classList.add('hidden');
    });
  });
});

function escStr(s) {
  return String(s).replace(/'/g, "\\'").replace(/"/g, '&quot;');
}

TRANSLATIONS.en.common.next = 'Next';
TRANSLATIONS.en.common.page = 'Page';
TRANSLATIONS.uk.common.next = 'Наступна';
TRANSLATIONS.uk.common.page = 'Сторінка';
