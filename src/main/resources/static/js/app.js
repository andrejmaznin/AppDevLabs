document.addEventListener('DOMContentLoaded', () => {
    loadMissions();

    document.getElementById('searchInput').addEventListener('input', debounce(applySearch, 300));
    document.getElementById('importInput').addEventListener('change', importMission);
    document.getElementById('clearBtn').addEventListener('click', clearMissions);
});

async function loadMissions(search = '') {
    const response = await fetch(`/api/missions${search ? '?search=' + encodeURIComponent(search) : ''}`);
    const missions = await response.json();
    renderTable(missions);
}

function renderTable(missions) {
    const tbody = document.getElementById('missionTableBody');
    tbody.innerHTML = '';

    missions.forEach(m => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${m.missionId}</td>
            <td>${m.date || '—'}</td>
            <td>${m.location || '—'}</td>
            <td>${m.outcome || '—'}</td>
            <td>${m.damageCost ? m.damageCost.toLocaleString() : 0}</td>
            <td>
                <button class="btn btn-primary btn-small" onclick="showBasicReport('${m.missionId}')">Отчет</button>
                <button class="btn btn-secondary btn-small" onclick="showTreeReport('${m.missionId}')">Дерево</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function applySearch() {
    const query = document.getElementById('searchInput').value;
    loadMissions(query);
}

async function importMission(event) {
    const file = event.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append('file', file);

    try {
        const response = await fetch('/api/missions/import', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            alert('Миссия успешно импортирована!');
            loadMissions();
        } else {
            const error = await response.text();
            alert('Ошибка импорта: ' + error);
        }
    } catch (e) {
        alert('Ошибка при соединении с сервером');
    }
    event.target.value = ''; // Reset input
}

async function clearMissions() {
    if (!confirm('Вы уверены, что хотите удалить все миссии?')) return;

    await fetch('/api/missions', { method: 'DELETE' });
    loadMissions();
}

async function showBasicReport(id) {
    const response = await fetch(`/api/missions/${id}/report/basic`);
    const r = await response.json();

    const content = document.getElementById('basicReportContent');
    content.innerHTML = `
        <h2>Отчет по миссии: ${r.missionId}</h2>
        <div class="report-grid">
            <div class="report-column">
                <div class="report-section">
                    <h3>Общая информация</h3>
                    <div class="report-item"><label>Дата:</label> <span>${r.date || '—'}</span></div>
                    <div class="report-item"><label>Локация:</label> <span>${r.location || '—'}</span></div>
                    <div class="report-item"><label>Результат:</label> <span>${r.outcome || '—'}</span></div>
                    <div class="report-item"><label>Ущерб:</label> <span>${r.formattedDamage || '0'}</span></div>
                </div>

                <div class="report-section">
                    <h3>Проклятие</h3>
                    <div class="report-item"><label>Название:</label> <span>${r.curseName || '—'}</span></div>
                    <div class="report-item"><label>Уровень:</label> <span>${r.curseThreatLevel || '—'}</span></div>
                </div>

                <div class="report-section">
                    <h3>Условия</h3>
                    <div class="report-item"><label>Окружение:</label> <span>${r.environmentSummary || '—'}</span></div>
                    <div class="report-item"><label>Гражданские:</label> <span>${r.civilianImpactSummary || '—'}</span></div>
                    <div class="report-item"><label>Враг:</label> <span>${r.enemyActivitySummary || '—'}</span></div>
                </div>
            </div>
            
            <div class="report-column">
                <div class="report-section">
                    <h3>Маги и Техники</h3>
                    <p><strong>Маги:</strong> ${(r.sorcerersDetails && r.sorcerersDetails.join(', ')) || 'нет'}</p>
                    <p><strong>Техники:</strong> ${(r.techniquesDetails && r.techniquesDetails.join(', ')) || 'нет'}</p>
                </div>

                <div class="report-section">
                    <h3>Хронология</h3>
                    <ul class="timeline-list">
                        ${(r.timelineDetails && r.timelineDetails.map(t => `<li>${t}</li>`).join('')) || '<li>нет данных</li>'}
                    </ul>
                </div>

                <div class="report-section">
                    <h3>Комментарий</h3>
                    <p>${r.comment || '—'}</p>
                </div>
            </div>
        </div>
    `;

    document.getElementById('basicReportModal').style.display = 'block';
}

async function showTreeReport(id) {
    const response = await fetch(`/api/missions/${id}/report/tree`);
    const data = await response.json();

    const content = document.getElementById('treeReportContent');
    content.innerHTML = '';
    renderTreeNode(data.rootNode, content);

    document.getElementById('treeReportModal').style.display = 'block';
}

function renderTreeNode(node, container) {
    const div = document.createElement('div');
    div.className = 'tree-node';
    
    const labelSpan = document.createElement('span');
    labelSpan.className = 'tree-label';
    labelSpan.textContent = node.label;
    div.appendChild(labelSpan);

    if (node.children && node.children.length > 0) {
        const childrenDiv = document.createElement('div');
        node.children.forEach(child => renderTreeNode(child, childrenDiv));
        div.appendChild(childrenDiv);
    }

    container.appendChild(div);
}

function closeModal(id) {
    document.getElementById(id).style.display = 'none';
}

function debounce(func, wait) {
    let timeout;
    return function() {
        const context = this, args = arguments;
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(context, args), wait);
    };
}

// Close modals on outside click
window.onclick = function(event) {
    if (event.target.className === 'modal') {
        event.target.style.display = 'none';
    }
}
