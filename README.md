# Sistema Odontológico

## Descrição
O **OrthoPrice** é uma aplicação desenvolvida para gerenciar um consultório odontológico. Ele permite o cadastro, edição, visualização e exclusão de pacientes, materiais e procedimentos, além de calcular preços de serviços odontológicos.

## Tecnologias Utilizadas
- **Java 17**
- **Spring Boot**
- **Maven**
- **HTML5, CSS3 e JavaScript**
- **H2 Database** (banco de dados em memória para desenvolvimento e testes)

## Estrutura do Projeto
A estrutura do projeto segue o padrão do Spring Boot:

```
src/
  main/
    java/
      com/odonto/sistema_odontologico/
        controllers/       # Controladores REST
        dto/               # Objetos de Transferência de Dados
        models/            # Modelos de Dados
        repositories/      # Interfaces de Repositórios
        services/          # Lógica de Negócio
    resources/
      static/              # Arquivos estáticos (HTML, CSS, JS)
      application.properties # Configurações da aplicação
  test/                    # Testes automatizados
```

## Instalação e Configuração
Siga os passos abaixo para configurar e executar o projeto localmente:

### Pré-requisitos
- **Java 17**
- **Maven**
- Um editor de código como o **VS Code** ou **IntelliJ IDEA**

### Passos
1. Clone o repositório:
   ```bash
   git clone https://github.com/LaraEnglerth/POO-II---Consultorio.git
   ```
2. Navegue até o diretório do projeto:
   ```bash
   cd POO-II---Consultorio
   ```
3. Execute a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Acesse a aplicação no navegador em: [http://localhost:8080](http://localhost:8080)

## Uso
A aplicação possui as seguintes funcionalidades principais:

- **Pacientes**:
  - Cadastro, edição, visualização e exclusão de pacientes.
- **Materiais**:
  - Gerenciamento de materiais odontológicos.
- **Procedimentos**:
  - Cadastro e cálculo de preços de procedimentos odontológicos.

## Autores
- [**Lara Englerth**] (https://github.com/LaraEnglerth)
- [**José Renato Cardoso de Campos**] (https://github.com/JoseRenatoCardoso)
- [**Nathaniel Pereira Utida**](https://github.com/NanatUtida)

