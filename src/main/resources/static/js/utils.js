// Funções utilitárias

// Toast notifications
const toast = {
  container: null,
  
  init() {
    if (!this.container) {
      this.container = document.createElement('div');
      this.container.className = 'toast-container';
      document.body.appendChild(this.container);
    }
  },
  
  show(message, type = 'info') {
    this.init();
    
    const toastEl = document.createElement('div');
    toastEl.className = `toast toast-${type}`;
    toastEl.textContent = message;
    
    this.container.appendChild(toastEl);
    
    setTimeout(() => {
      toastEl.remove();
    }, 3000);
  },
  
  success(message) {
    this.show(message, 'success');
  },
  
  error(message) {
    this.show(message, 'error');
  },
  
  info(message) {
    this.show(message, 'info');
  }
};

// Modal de confirmação
function showConfirmDialog(title, message, onConfirm) {
  const overlay = document.createElement('div');
  overlay.className = 'modal-overlay';
  
  const modal = document.createElement('div');
  modal.className = 'modal';
  
  modal.innerHTML = `
    <div class="modal-header">
      <h2 class="modal-title">${title}</h2>
    </div>
    <div class="modal-content">
      <p>${message}</p>
    </div>
    <div class="modal-footer">
      <button class="btn btn-outline" id="cancelBtn">Cancelar</button>
      <button class="btn btn-destructive" id="confirmBtn">Confirmar</button>
    </div>
  `;
  
  overlay.appendChild(modal);
  document.body.appendChild(overlay);
  
  document.getElementById('cancelBtn').onclick = () => overlay.remove();
  document.getElementById('confirmBtn').onclick = () => {
    onConfirm();
    overlay.remove();
  };
  
  overlay.onclick = (e) => {
    if (e.target === overlay) overlay.remove();
  };
}

// Formatação de moeda
function formatCurrency(value) {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  }).format(value);
}

// Get query params
function getQueryParam(name) {
  const urlParams = new URLSearchParams(window.location.search);
  return urlParams.get(name);
}

// Set active nav link
function setActiveNavLink() {
  const currentPath = window.location.pathname;
  const links = document.querySelectorAll('.sidebar-link');
  
  links.forEach(link => {
    const href = link.getAttribute('href');
    if (currentPath === href || (href !== '/' && currentPath.startsWith(href))) {
      link.classList.add('active');
    } else {
      link.classList.remove('active');
    }
  });
}

// Loading state
function showLoading(element) {
  element.innerHTML = '<div class="loading"><div class="spinner"></div></div>';
}

// Table utilities
class DataTable {
  constructor(data, columns, options = {}) {
    this.data = data;
    this.columns = columns;
    this.searchKey = options.searchKey;
    this.onRowClick = options.onRowClick;
    this.pageSize = options.pageSize || 10;
    this.currentPage = 1;
    this.searchTerm = '';
    this.sortKey = null;
    this.sortOrder = 'asc';
  }
  
  get filteredData() {
    let filtered = [...this.data];
    
    if (this.searchTerm && this.searchKey) {
      filtered = filtered.filter(item =>
        String(item[this.searchKey]).toLowerCase().includes(this.searchTerm.toLowerCase())
      );
    }
    
    if (this.sortKey) {
      filtered.sort((a, b) => {
        const aVal = String(a[this.sortKey]);
        const bVal = String(b[this.sortKey]);
        if (this.sortOrder === 'asc') {
          return aVal.localeCompare(bVal);
        }
        return bVal.localeCompare(aVal);
      });
    }
    
    return filtered;
  }
  
  get paginatedData() {
    const start = (this.currentPage - 1) * this.pageSize;
    return this.filteredData.slice(start, start + this.pageSize);
  }
  
  get totalPages() {
    return Math.ceil(this.filteredData.length / this.pageSize);
  }
  
  setSearch(term) {
    this.searchTerm = term;
    this.currentPage = 1;
  }
  
  setSort(key) {
    if (this.sortKey === key) {
      this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortKey = key;
      this.sortOrder = 'asc';
    }
  }
  
  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
    }
  }
  
  prevPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }
  
  render(container) {
    let html = '';
    
    if (this.searchKey) {
      html += `
        <div class="search-box">
          <svg class="search-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
          </svg>
          <input type="text" class="form-input search-input" placeholder="Buscar..." id="searchInput">
        </div>
      `;
    }
    
    html += '<div class="table-wrapper"><table class="table"><thead><tr>';
    
    this.columns.forEach(col => {
      const sortIcon = this.sortKey === col.key ? (this.sortOrder === 'asc' ? ' ↑' : ' ↓') : '';
      html += `<th data-key="${col.key}" ${col.sortable ? 'class="sortable"' : ''}>${col.label}${sortIcon}</th>`;
    });
    
    html += '</tr></thead><tbody>';
    
    if (this.paginatedData.length === 0) {
      html += `<tr><td colspan="${this.columns.length}" class="table-empty">Nenhum registro encontrado</td></tr>`;
    } else {
      this.paginatedData.forEach(item => {
        html += `<tr data-id="${item.id}">`;
        this.columns.forEach(col => {
          const value = col.render ? col.render(item) : item[col.key];
          html += `<td>${value}</td>`;
        });
        html += '</tr>';
      });
    }
    
    html += '</tbody></table></div>';
    
    if (this.totalPages > 1) {
      html += `
        <div class="pagination">
          <div class="pagination-info">Página ${this.currentPage} de ${this.totalPages}</div>
          <div class="pagination-buttons">
            <button class="btn btn-outline btn-sm" id="prevPageBtn" ${this.currentPage === 1 ? 'disabled' : ''}>
              <svg width="16" height="16" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path>
              </svg>
            </button>
            <button class="btn btn-outline btn-sm" id="nextPageBtn" ${this.currentPage === this.totalPages ? 'disabled' : ''}>
              <svg width="16" height="16" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
              </svg>
            </button>
          </div>
        </div>
      `;
    }
    
    container.innerHTML = html;
    
    // Event listeners
    if (this.searchKey) {
      const searchInput = document.getElementById('searchInput');
      searchInput.addEventListener('input', (e) => {
        this.setSearch(e.target.value);
        this.render(container);
      });
    }
    
    const sortableHeaders = container.querySelectorAll('th.sortable');
    sortableHeaders.forEach(th => {
      th.addEventListener('click', () => {
        this.setSort(th.dataset.key);
        this.render(container);
      });
    });
    
    if (this.onRowClick) {
      const rows = container.querySelectorAll('tbody tr[data-id]');
      rows.forEach(row => {
        row.style.cursor = 'pointer';
        row.addEventListener('click', () => {
          const item = this.data.find(d => d.id === row.dataset.id);
          if (item) this.onRowClick(item);
        });
      });
    }
    
    const prevBtn = document.getElementById('prevPageBtn');
    const nextBtn = document.getElementById('nextPageBtn');
    if (prevBtn) {
      prevBtn.addEventListener('click', () => {
        this.prevPage();
        this.render(container);
      });
    }
    if (nextBtn) {
      nextBtn.addEventListener('click', () => {
        this.nextPage();
        this.render(container);
      });
    }
  }
}

// Icons (SVG strings)
const icons = {
  users: '<svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path></svg>',
  clipboard: '<svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"></path></svg>',
  package: '<svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"></path></svg>',
  dashboard: '<svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"></path></svg>',
  plus: '<svg width="16" height="16" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path></svg>',
  eye: '<svg width="16" height="16" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"></path></svg>',
  pencil: '<svg width="16" height="16" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path></svg>',
  trash: '<svg width="16" height="16" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path></svg>',
  arrowLeft: '<svg width="16" height="16" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path></svg>',
  calculator: '<svg width="16" height="16" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 7h6m0 10v-3m-3 3h.01M9 17h.01M9 14h.01M12 14h.01M15 11h.01M12 11h.01M9 11h.01M7 21h10a2 2 0 002-2V5a2 2 0 00-2-2H7a2 2 0 00-2 2v14a2 2 0 002 2z"></path></svg>',
  x: '<svg width="16" height="16" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>',
  user: '<svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path></svg>'
};
