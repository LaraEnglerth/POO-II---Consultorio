// Mock API com LocalStorage
const USE_MOCK = false;

const API_BASE_URL = "http://localhost:8080/api";

class API {
  constructor() {
  }

  delay(ms = 300) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  // Pacientes
    async getPacientes() {
    if (USE_MOCK) {
      await this.delay();
      return JSON.parse(localStorage.getItem('pacientes') || '[]');
    } else {
      const response = await fetch(`${API_BASE_URL}/pacientes`);

      if (!response.ok) {
        throw new Error("Erro ao buscar pacientes no backend");
      }

      const dados = await response.json();
      return dados;
    }
  }

  async getPaciente(id) {
    if (USE_MOCK) {
      await this.delay();
      const pacientes = JSON.parse(localStorage.getItem('pacientes') || '[]');
      return pacientes.find(p => p.id === id);
    } else {
      const response = await fetch(`${API_BASE_URL}/pacientes/${id}`);

      if (!response.ok) {
        throw new Error("Erro ao buscar paciente");
      }

      return response.json();
    }
  }

  async createPaciente(data) {
    if (USE_MOCK) {
      await this.delay();
      const pacientes = JSON.parse(localStorage.getItem('pacientes') || '[]');
      const newPaciente = {
        id: Date.now().toString(),
        ...data
      };
      pacientes.push(newPaciente);
      localStorage.setItem('pacientes', JSON.stringify(pacientes));
      return newPaciente;
    } else {
      // aqui garantimos que os nomes batem com o backend
      const payload = {
        nomePaciente: data.nomePaciente,
        idade: Number(data.idade),
        fidelidade: data.fidelidade ?? 0
      };

      const response = await fetch(`${API_BASE_URL}/pacientes`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const texto = await response.text();
        throw new Error("Erro ao criar paciente: " + texto);
      }

      return response.json();
    }
  }

  async updatePaciente(id, data) {
    if (USE_MOCK) {
      await this.delay();
      const pacientes = JSON.parse(localStorage.getItem('pacientes') || '[]');
      const index = pacientes.findIndex(p => p.id === id);
      if (index === -1) throw new Error('Paciente não encontrado');
      pacientes[index] = { ...pacientes[index], ...data };
      localStorage.setItem('pacientes', JSON.stringify(pacientes));
      return pacientes[index];
    } else {
      const payload = {
        nomePaciente: data.nomePaciente ?? data.nome_paciente ?? data.nome,
        idade: Number(data.idade),
        fidelidade: data.fidelidade ?? 0
      };

      const response = await fetch(`${API_BASE_URL}/pacientes/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const texto = await response.text();
        throw new Error("Erro ao atualizar paciente: " + texto);
      }

      return response.json();
    }
  }

  async deletePaciente(id) {
    if (USE_MOCK) {
      await this.delay();
      const pacientes = JSON.parse(localStorage.getItem('pacientes') || '[]');
      const filtered = pacientes.filter(p => p.id !== id);
      localStorage.setItem('pacientes', JSON.stringify(filtered));
    } else {
      const response = await fetch(`${API_BASE_URL}/pacientes/${id}`, {
        method: "DELETE"
      });

      if (!response.ok) {
        const texto = await response.text();
        throw new Error("Erro ao deletar paciente: " + texto);
      }
    }
  }

  // Materiais
  async getMateriais() {
    if (USE_MOCK) {
      await this.delay();
      return JSON.parse(localStorage.getItem('materiais') || '[]');
    } else {
      const response = await fetch(`${API_BASE_URL}/materiais`);
      if (!response.ok) {
        throw new Error('Erro ao buscar materiais no backend');
      }
      return response.json();
    }
  }

  async getMaterial(id) {
    if (USE_MOCK) {
      await this.delay();
      const materiais = JSON.parse(localStorage.getItem('materiais') || '[]');
      return materiais.find(m => m.id === id);
    } else {
      const response = await fetch(`${API_BASE_URL}/materiais/${id}`);
      if (!response.ok) {
        throw new Error('Erro ao buscar material');
      }
      return response.json();
    }
  }

  async createMaterial(data) {
    if (USE_MOCK) {
      await this.delay();
      const materiais = JSON.parse(localStorage.getItem('materiais') || '[]');
      const newMaterial = {
        id: Date.now().toString(),
        ...data
      };
      materiais.push(newMaterial);
      localStorage.setItem('materiais', JSON.stringify(materiais));
      return newMaterial;
    } else {
      const payload = {
        nomeMaterial: data.nomeMaterial ?? data.nome_material ?? data.nome,
        quantidade: Number(data.quantidade),
        valor: Number(data.valor),
        reutilizavel: data.reutilizavel === true || data.reutilizavel === 'S'
      };

      const response = await fetch(`${API_BASE_URL}/materiais`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const texto = await response.text();
        throw new Error('Erro ao criar material: ' + texto);
      }

      return response.json();
    }
  }

  async updateMaterial(id, data) {
    if (USE_MOCK) {
      await this.delay();
      const materiais = JSON.parse(localStorage.getItem('materiais') || '[]');
      const index = materiais.findIndex(m => m.id === id);
      if (index === -1) throw new Error('Material não encontrado');
      materiais[index] = { ...materiais[index], ...data };
      localStorage.setItem('materiais', JSON.stringify(materiais));
      return materiais[index];
    } else {
      const payload = {
        nomeMaterial: data.nomeMaterial ?? data.nome_material ?? data.nome,
        quantidade: Number(data.quantidade),
        valor: Number(data.valor),
        reutilizavel: data.reutilizavel === true || data.reutilizavel === 'S'
      };

      const response = await fetch(`${API_BASE_URL}/materiais/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const texto = await response.text();
        throw new Error('Erro ao atualizar material: ' + texto);
      }

      return response.json();
    }
  }

  async deleteMaterial(id) {
    if (USE_MOCK) {
      await this.delay();
      const materiais = JSON.parse(localStorage.getItem('materiais') || '[]');
      const filtered = materiais.filter(m => m.id !== id);
      localStorage.setItem('materiais', JSON.stringify(filtered));
    } else {
      const response = await fetch(`${API_BASE_URL}/materiais/${id}`, {
        method: 'DELETE'
      });

      if (!response.ok) {
        const texto = await response.text();
        throw new Error('Erro ao deletar material: ' + texto);
      }
    }
  }

  // Procedimentos
  async getProcedimentos() {
    if (USE_MOCK) {
      await this.delay();
      const procedimentos = JSON.parse(localStorage.getItem('procedimentos') || '[]');
      const materiais = JSON.parse(localStorage.getItem('materiais') || '[]');
      return procedimentos.map(p => ({
        ...p,
        materiais: p.materiais.map(mp => ({
          ...mp,
          material: materiais.find(m => m.id === mp.materialId)
        }))
      }));
    } else {
      const response = await fetch(`${API_BASE_URL}/procedimentos`);
      if (!response.ok) {
        throw new Error('Erro ao buscar procedimentos no backend');
      }
      // aqui assumo que o backend já devolve o procedimento com seus materiais (ou pelo menos o básico)
      return response.json();
    }
  }

  async getProcedimento(id) {
    if (USE_MOCK) {
      await this.delay();
      const procedimentos = JSON.parse(localStorage.getItem('procedimentos') || '[]');
      const materiais = JSON.parse(localStorage.getItem('materiais') || '[]');
      const proc = procedimentos.find(p => p.id === id);
      if (!proc) return null;
      return {
        ...proc,
        materiais: proc.materiais.map(mp => ({
          ...mp,
          material: materiais.find(m => m.id === mp.materialId)
        }))
      };
    } else {
      const response = await fetch(`${API_BASE_URL}/procedimentos/${id}`);
      if (!response.ok) {
        throw new Error('Erro ao buscar procedimento');
      }
      return response.json();
    }
  }

  async createProcedimento(data) {
    if (USE_MOCK) {
      await this.delay();
      const procedimentos = JSON.parse(localStorage.getItem('procedimentos') || '[]');
      const newProc = {
        id: Date.now().toString(),
        ...data
      };
      procedimentos.push(newProc);
      localStorage.setItem('procedimentos', JSON.stringify(procedimentos));
      return newProc;
    } else {

      const materiaisArr = Array.isArray(data.materiais)
        ? data.materiais.map(m => ({
            materialId: Number(m.materialId),
            quantidade: Number(m.quantidade)
          }))
        : [];

      const payload = {
        nomeProcedimento: data.nome_procedimento ?? data.nomeProcedimento ?? data.nome,
        assistente: data.assistente === 'S' || data.assistente === true,
        duracao: Number(data.duracao),
        valorFinal: Number(data.valorFinal),
        pacienteId: data.pacienteId ? Number(data.pacienteId) : null,
        materiais: materiaisArr,
        materiaisIds: materiaisArr.map(m => m.materialId)
      };

      alert("Payload procedimento: " + JSON.stringify(payload));

      const response = await fetch(`${API_BASE_URL}/procedimentos`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const texto = await response.text();
        throw new Error('Erro ao criar procedimento: ' + texto);
      }
      return response.json();
    }
  }

  async updateProcedimento(id, data) {
    if (USE_MOCK) {
      await this.delay();
      const procedimentos = JSON.parse(localStorage.getItem('procedimentos') || '[]');
      const index = procedimentos.findIndex(p => p.id === id);
      if (index === -1) throw new Error('Procedimento não encontrado');
      procedimentos[index] = { ...procedimentos[index], ...data };
      localStorage.setItem('procedimentos', JSON.stringify(procedimentos));
      return procedimentos[index];
    } else {

      const materiaisArr = Array.isArray(data.materiais)
        ? data.materiais.map(m => ({
            materialId: Number(m.materialId),
            quantidade: Number(m.quantidade)
          }))
        : [];

      const payload = {
        nomeProcedimento: data.nome_procedimento ?? data.nomeProcedimento ?? data.nome,
        assistente: data.assistente === 'S' || data.assistente === true,
        duracao: Number(data.duracao),
        valorFinal: Number(data.valorFinal),
        pacienteId: data.pacienteId ? Number(data.pacienteId) : null,
        materiais: materiaisArr,
        materiaisIds: materiaisArr.map(m => m.materialId)
      };

      const response = await fetch(`${API_BASE_URL}/procedimentos/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const texto = await response.text();
        throw new Error('Erro ao atualizar procedimento: ' + texto);
      }
      return response.json();
    }
  }

  async deleteProcedimento(id) {
    if (USE_MOCK) {
      await this.delay();
      const procedimentos = JSON.parse(localStorage.getItem('procedimentos') || '[]');
      const filtered = procedimentos.filter(p => p.id !== id);
      localStorage.setItem('procedimentos', JSON.stringify(filtered));
    } else {
      const response = await fetch(`${API_BASE_URL}/procedimentos/${id}`, {
        method: 'DELETE'
      });
      if (!response.ok) {
        const texto = await response.text();
        throw new Error('Erro ao deletar procedimento: ' + texto);
      }
    }
  }

  async getProcedimentosByPaciente(pacienteId) {
    if (USE_MOCK) {
      await this.delay();
      const procedimentos = await this.getProcedimentos();
      return procedimentos.filter(p => p.pacienteId === pacienteId);
    } else {
      // se você tiver esse endpoint no backend, use:
      // const response = await fetch(`${API_BASE_URL}/procedimentos/paciente/${pacienteId}`);
      // ...
      // se não tiver, por enquanto busca todos e filtra no front:
      const procedimentos = await this.getProcedimentos();
      return procedimentos.filter(p => p.pacienteId === pacienteId);
    }
  }

}

const api = new API();
