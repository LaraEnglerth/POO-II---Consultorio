// Mock API com LocalStorage
class API {
  constructor() {
    this.initializeData();
  }

  initializeData() {
    if (!localStorage.getItem('pacientes')) {
      localStorage.setItem('pacientes', JSON.stringify([
        { id: '1', nome_paciente: 'João Silva', idade: 35, fidelidade: 3 },
        { id: '2', nome_paciente: 'Maria Santos', idade: 28, fidelidade: 5 },
        { id: '3', nome_paciente: 'Pedro Costa', idade: 42, fidelidade: 2 },
      ]));
    }

    if (!localStorage.getItem('materiais')) {
      localStorage.setItem('materiais', JSON.stringify([
        { id: '1', nome_material: 'Anestésico', quantidade: 50, valor: 15.50, reutilizavel: 'N' },
        { id: '2', nome_material: 'Resina Composta', quantidade: 20, valor: 85.00, reutilizavel: 'N' },
        { id: '3', nome_material: 'Espelho Bucal', quantidade: 10, valor: 25.00, reutilizavel: 'S' },
        { id: '4', nome_material: 'Broca Diamantada', quantidade: 15, valor: 45.00, reutilizavel: 'S' },
      ]));
    }

    if (!localStorage.getItem('procedimentos')) {
      localStorage.setItem('procedimentos', JSON.stringify([
        {
          id: '1',
          nome_procedimento: 'Restauração Simples',
          assistente: 'N',
          duracao: 45,
          valorFinal: 250.00,
          pacienteId: '1',
          materiais: [
            { materialId: '1', quantidade: 2 },
            { materialId: '2', quantidade: 1 },
          ],
        },
        {
          id: '2',
          nome_procedimento: 'Limpeza Dental',
          assistente: 'S',
          duracao: 30,
          valorFinal: 150.00,
          pacienteId: '2',
          materiais: [{ materialId: '3', quantidade: 1 }],
        },
      ]));
    }
  }

  delay(ms = 300) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  // Pacientes
  async getPacientes() {
    await this.delay();
    return JSON.parse(localStorage.getItem('pacientes') || '[]');
  }

  async getPaciente(id) {
    await this.delay();
    const pacientes = JSON.parse(localStorage.getItem('pacientes') || '[]');
    return pacientes.find(p => p.id === id);
  }

  async createPaciente(data) {
    await this.delay();
    const pacientes = JSON.parse(localStorage.getItem('pacientes') || '[]');
    const newPaciente = {
      id: Date.now().toString(),
      ...data
    };
    pacientes.push(newPaciente);
    localStorage.setItem('pacientes', JSON.stringify(pacientes));
    return newPaciente;
  }

  async updatePaciente(id, data) {
    await this.delay();
    const pacientes = JSON.parse(localStorage.getItem('pacientes') || '[]');
    const index = pacientes.findIndex(p => p.id === id);
    if (index === -1) throw new Error('Paciente não encontrado');
    pacientes[index] = { ...pacientes[index], ...data };
    localStorage.setItem('pacientes', JSON.stringify(pacientes));
    return pacientes[index];
  }

  async deletePaciente(id) {
    await this.delay();
    const pacientes = JSON.parse(localStorage.getItem('pacientes') || '[]');
    const filtered = pacientes.filter(p => p.id !== id);
    localStorage.setItem('pacientes', JSON.stringify(filtered));
  }

  // Materiais
  async getMateriais() {
    await this.delay();
    return JSON.parse(localStorage.getItem('materiais') || '[]');
  }

  async getMaterial(id) {
    await this.delay();
    const materiais = JSON.parse(localStorage.getItem('materiais') || '[]');
    return materiais.find(m => m.id === id);
  }

  async createMaterial(data) {
    await this.delay();
    const materiais = JSON.parse(localStorage.getItem('materiais') || '[]');
    const newMaterial = {
      id: Date.now().toString(),
      ...data
    };
    materiais.push(newMaterial);
    localStorage.setItem('materiais', JSON.stringify(materiais));
    return newMaterial;
  }

  async updateMaterial(id, data) {
    await this.delay();
    const materiais = JSON.parse(localStorage.getItem('materiais') || '[]');
    const index = materiais.findIndex(m => m.id === id);
    if (index === -1) throw new Error('Material não encontrado');
    materiais[index] = { ...materiais[index], ...data };
    localStorage.setItem('materiais', JSON.stringify(materiais));
    return materiais[index];
  }

  async deleteMaterial(id) {
    await this.delay();
    const materiais = JSON.parse(localStorage.getItem('materiais') || '[]');
    const filtered = materiais.filter(m => m.id !== id);
    localStorage.setItem('materiais', JSON.stringify(filtered));
  }

  // Procedimentos
  async getProcedimentos() {
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
  }

  async getProcedimento(id) {
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
  }

  async createProcedimento(data) {
    await this.delay();
    const procedimentos = JSON.parse(localStorage.getItem('procedimentos') || '[]');
    const newProc = {
      id: Date.now().toString(),
      ...data
    };
    procedimentos.push(newProc);
    localStorage.setItem('procedimentos', JSON.stringify(procedimentos));
    return newProc;
  }

  async updateProcedimento(id, data) {
    await this.delay();
    const procedimentos = JSON.parse(localStorage.getItem('procedimentos') || '[]');
    const index = procedimentos.findIndex(p => p.id === id);
    if (index === -1) throw new Error('Procedimento não encontrado');
    procedimentos[index] = { ...procedimentos[index], ...data };
    localStorage.setItem('procedimentos', JSON.stringify(procedimentos));
    return procedimentos[index];
  }

  async deleteProcedimento(id) {
    await this.delay();
    const procedimentos = JSON.parse(localStorage.getItem('procedimentos') || '[]');
    const filtered = procedimentos.filter(p => p.id !== id);
    localStorage.setItem('procedimentos', JSON.stringify(filtered));
  }

  async getProcedimentosByPaciente(pacienteId) {
    await this.delay();
    const procedimentos = await this.getProcedimentos();
    return procedimentos.filter(p => p.pacienteId === pacienteId);
  }
}

const api = new API();
