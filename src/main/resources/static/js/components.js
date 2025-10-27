// Componentes reutilizáveis

function createSidebar(activePage) {
  return `
    <aside class="sidebar">
      <div class="sidebar-header">
        <h1 class="sidebar-title">
          Ortho<span class="sidebar-title-accent">Price</span>
        </h1>
      </div>
      <nav class="sidebar-nav">
        <a href="/index.html" class="sidebar-link ${activePage === 'dashboard' ? 'active' : ''}">
          ${icons.dashboard}
          <span>Dashboard</span>
        </a>
        <a href="/pacientes.html" class="sidebar-link ${activePage === 'pacientes' ? 'active' : ''}">
          ${icons.users}
          <span>Pacientes</span>
        </a>
        <a href="/procedimentos.html" class="sidebar-link ${activePage === 'procedimentos' ? 'active' : ''}">
          ${icons.clipboard}
          <span>Procedimentos</span>
        </a>
        <a href="/materiais.html" class="sidebar-link ${activePage === 'materiais' ? 'active' : ''}">
          ${icons.package}
          <span>Materiais</span>
        </a>
      </nav>
    </aside>
  `;
}

function createPageHeader(title, subtitle, actionButton = null) {
  return `
    <div class="page-header">
      <div>
        <h1 class="page-title">${title}</h1>
        ${subtitle ? `<p class="page-subtitle">${subtitle}</p>` : ''}
      </div>
      ${actionButton || ''}
    </div>
  `;
}

function createStatCard(title, value, iconName, description = '') {
  const iconSvg = icons[iconName] || '';
  return `
    <div class="card">
      <div class="stat-card">
        <div class="stat-info">
          <h3>${title}</h3>
          <div class="stat-value">${value}</div>
          ${description ? `<p class="page-subtitle" style="font-size: 0.75rem; margin-top: 0.25rem;">${description}</p>` : ''}
        </div>
        <div class="stat-icon">${iconSvg}</div>
      </div>
    </div>
  `;
}

function createBadge(text, variant = 'primary') {
  return `<span class="badge badge-${variant}">${text}</span>`;
}

function createActionButtons(id, onView, onEdit, onDelete) {
  return `
    <div class="table-actions">
      <button class="btn btn-ghost btn-sm" onclick="${onView}('${id}')">
        ${icons.eye}
      </button>
      <button class="btn btn-ghost btn-sm" onclick="${onEdit}('${id}')">
        ${icons.pencil}
      </button>
      <button class="btn btn-ghost btn-sm text-destructive" onclick="${onDelete}('${id}')">
        ${icons.trash}
      </button>
    </div>
  `;
}
