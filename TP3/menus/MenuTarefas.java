package menus;

// -------------------------------------------------------
/*A classe MenuTarefas oferece ao usuário a oportunidade de verificar as tarefas, passando pelo processo de leitura dos dados
 fazendo com que o usuário consiga buscar, incluir, alterar e excluir novas tarefas */
// -------------------------------------------------------
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import aeds3.ListaInvertida;
import arquivos.ArquivoCategorias;
import arquivos.ArquivoRotulos;
import arquivos.ArquivoTarefas;
import entidades.Categoria;
import entidades.Rotulo;
import entidades.Tarefa;

public class MenuTarefas {

  Scanner console = new Scanner(System.in, "UTF-8");
  private ArquivoTarefas arqTarefas;
  private ArquivoCategorias arqCategorias;
  private ArquivoRotulos arqRotulos;
  private ListaInvertida lista;
  // MenuRotulos menuRotulos = new MenuRotulos();

  // ------------------------------------

  // ------------------------------------

  public MenuTarefas() {
    try {
      arqTarefas = new ArquivoTarefas();
      arqCategorias = new ArquivoCategorias();
      arqRotulos = new ArquivoRotulos();
      lista = new ListaInvertida();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // ---------------------
  // método de ler as tarefas
  // ---------------------
  public Tarefa leTarefa() throws Exception {

    // Lê os campos iniciais
    System.out.print("Nome: ");
    String nome = console.nextLine();

    LocalDate dataCriacao, dataConclusao;
    System.out.print("Data Criação - Modelo dd/MM/yyyy: ");
    String dataCri = console.nextLine();
    System.out.print("Data Conclusão - Modelo dd/MM/yyyy: ");
    String dataConc = console.nextLine();

    // conversor data de criação
    if (dataCri.length() > 0) {
      try {
        // Definindo o formato esperado para a data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Convertendo a string para LocalDate
        dataCriacao = LocalDate.parse(dataCri, formatter);

      } catch (DateTimeParseException e) {
        // Se a string não for uma data válida
        dataCriacao = null; // ou outra lógica para datas inválidas
      }
    } else {
      dataCriacao = null; // ou atribua uma data padrão
    }

    // conversor data de conclusão
    if (dataConc.length() > 0) {
      try {
        // Definindo o formato esperado para a data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Convertendo a string para LocalDate
        dataConclusao = LocalDate.parse(dataConc, formatter);

      } catch (DateTimeParseException e) {
        // Se a string não for uma data válida
        dataConclusao = null; // ou outra lógica para datas inválidas
      }
    } else {
      dataConclusao = null; // ou atribua uma data padrão
    }

    System.out.print("Status (1 - Pendente, 2 - Em andamento, 3 - Concluída): ");
    int status = Integer.parseInt(console.nextLine());

    System.out.print("Prioridade (1 - Baixa, 2 - Média, 3 - Alta): ");
    int prioridade = Integer.parseInt(console.nextLine());

    // Lista as categorias
    Categoria[] categorias = arqCategorias.readAll();
    int i;
    System.out.println("Categorias");
    for (i = 0; i < categorias.length; i++) {
      System.out.println((i + 1) + ": " + categorias[i].getNome());
    }

    // Lê a categoria
    System.out.print("Categoria: ");
    int c, categoria;
    String sCategoria = console.nextLine();
    if (sCategoria.length() > 0) {
      c = Integer.parseInt(sCategoria);
      if (c > 0 && c <= categorias.length)
        categoria = categorias[c - 1].getID();
      else
        categoria = -1;
    } else
      categoria = -1;

    // Cria e retorna a Tarefa
    System.out.println();
    Tarefa l = new Tarefa(nome, dataCriacao, dataConclusao, status, prioridade, categoria);
    return l;
  }

  // ---------------------
  // método de mostrar as tarefas
  // ---------------------
  public void mostrarTarefas(Tarefa l) throws Exception {
    Categoria c = arqCategorias.read(l.getIdCategoria());
    String nomeCategoria = c == null ? "Categoria inválida" : c.getNome();
    System.out.println(
        "\nNome: " + l.getNome() +
            "\nCategoria: " + nomeCategoria +
            "\nData de Criação: " + l.getDataCriacao() +
            "\nData de Conclusão: " + l.getDataConclusao() +
            "\nStatus: " + l.getStatusString() +
            "\nPrioridade: " + l.getPrioridadeString());
  }

  // ---------------------
  // método que mostra a interface do menu de tarefas
  // ---------------------
  public void menu() throws Exception {

    // Mostra o menu
    int opcao;
    do {
      System.out.println("\n\n\nTarefas AEDS3");
      System.out.println("------------");
      System.out.println("\n> Início > Tarefas");
      System.out.println("\n1) Incluir Tarefa");
      System.out.println("2) Buscar Tarefa");
      System.out.println("3) Listar todas as Tarefas");
      System.out.println("4) Alterar Tarefa");
      System.out.println("5) Excluir Tarefa");
      System.out.println("\n0) Retornar ao menu anterior");

      System.out.print("\nOpção: ");
      try {
        opcao = Integer.valueOf(console.nextLine());
      } catch (NumberFormatException e) {
        opcao = -1;
      }

      // Seleciona a operação
      switch (opcao) {
        case 1:
          incluirTarefa();
          break;
        case 2:
          buscarTarefa();
          break;
        case 3:
          listarTarefas();
          break;
        case 4:
          alterarTarefa();
          break;
        case 5:
          excluirTarefa();
          break;
        case 0:
          break;
        default:
          System.out.println("Opção inválida");
      }
    } while (opcao != 0);

    // Fecha os arquivos
    try {
      arqTarefas.close();
      arqCategorias.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  // ---------------------
  // método de incluir novas tarefas
  // ---------------------
  public void incluirTarefa() {

    // Lê uma nova Tarefa, testando se todos os campos foram preenchidos
    Tarefa novaTarefa;
    try {
      boolean dadosCompletos = false;
      do {
        novaTarefa = leTarefa();
        if (novaTarefa.getNome().length() == 0 || novaTarefa.getNome().length() == 0
            || novaTarefa.getIdCategoria() < 0)
          System.out.println("Dados incompletos. Preencha todos os campos.");
        else
          dadosCompletos = true;
      } while (!dadosCompletos);

    } catch (Exception e) {
      System.out.println("Dados inválidos");
      e.printStackTrace();
      return;
    }

    try {
      // Testa se já existe um Tarefa com esse nome
      if (arqTarefas.readNome(novaTarefa.getNome()) != null) {
        System.out.println("Já existe uma tarefa com esse nome.");
        return;
      }

      // Insere o novo Tarefa no banco de dados
      System.out.print("Confirma inclusão da Tarefa (S/N)? ");
      char resp = console.nextLine().charAt(0);
      if (resp == 'S' || resp == 's') {
        arqTarefas.create(novaTarefa);
      }
    } catch (Exception e) {
      System.out.println("Tarefa não pode ser criada");
      e.printStackTrace();
      return;
    }

    System.out.println("\nTarefa armazenada!");
    try {

      // Mostra a lista invertida após a inclusão
      System.out.println("-----------------------------");
      lista.print();
      System.out.println("-----------------------------");
    } catch (Exception e) {
      System.out.println("Não foi possivel mostar a Lista invertida");
      e.printStackTrace();
      return;
    }

  }

  // ---------------------
  // método de busca da tarefa selecionada
  // ---------------------
  public void buscarTarefa() {
    System.out.println("\n\n\nTarefas AEDS3");
    System.out.println("------------");
    System.out.println("\n> Início > Tarefas > Busca");

    try {
      // Lista as tarefas
      Tarefa[] tarefas = arqTarefas.readAll();
      int i;
      System.out.println("\nTarefas\n----------");
      for (i = 0; i < tarefas.length; i++) {
        System.out.println((i + 1) + ": " + tarefas[i].getNome());
      }

      // Lê o nome da tarefa
      System.out.print("\nNome da tarefa a exibir: ");
      String nomeTarefa = console.nextLine();

      // Verifica se o nome foi digitado
      if (nomeTarefa.length() == 0) {
        System.out.println("Nenhum nome de tarefa fornecido!");
        return;
      }

      // Busca a tarefa pelo nome
      Tarefa tarefa = arqTarefas.readNome(nomeTarefa);
      if (tarefa == null) {
        System.out.println("Tarefa não encontrada!");
        return;
      }

      // Mostra a tarefa encontrada
      mostrarTarefas(tarefa);

    } catch (Exception e) {
      System.out.println("Erro no acesso ao arquivo");
      e.printStackTrace();
    }
  }

  // ---------------------
  // método de listar todas as tarefas
  // ---------------------
  public void listarTarefas() {
    System.out.println("\n\n\nTarefas AEDS3");
    System.out.println("------------");
    System.out.println("\n> Início > Tarefas > Listar todas");

    try {
      // Lista todas as tarefas
      Tarefa[] tarefas = arqTarefas.readAll(); // Carrega todas as tarefas
      if (tarefas.length == 0) {
        System.out.println("Nenhuma tarefa cadastrada.");
        return;
      }

      int i;
      System.out.println("\nTarefas\n----------");
      for (i = 0; i < tarefas.length; i++) {
        System.out.println((i + 1) + ": " + tarefas[i].getNome());
      }

      System.out.println("\nDetalhes das Tarefas:\n");

      // Exibe detalhes de cada tarefa
      for (Tarefa tarefa : tarefas) {
        mostrarTarefas(tarefa); // Chama o método que mostra detalhes da tarefa
      }

    } catch (Exception e) {
      System.out.println("Erro ao listar tarefas.");
      e.printStackTrace();
    }
  }

  // ---------------------
  // método de alterar a tarefa selecionada
  // ---------------------
  public void alterarTarefa() {
    System.out.println("\n\n\nTarefas AEDS3");
    System.out.println("------------");
    System.out.println("\n> Início > Tarefas > Alteração");

    try {
      Tarefa[] tarefas = arqTarefas.readAll();
      int i;
      System.out.println("\nTarefas\n----------");
      for (i = 0; i < tarefas.length; i++) {
        System.out.println((i + 1) + ": " + tarefas[i].getNome());
      }

      System.out.print("\nNome: ");
      String nome = console.nextLine();

      Tarefa Tarefa = arqTarefas.readNome(nome);
      if (Tarefa == null) {
        System.out.println("Tarefa não encontrada.");
        return;
      }
      mostrarTarefas(Tarefa);

      System.out.println("\nDigite os novos dados.\nDeixe em branco os que não desejar alterar.");
      Tarefa Tarefa2;
      try {
        Tarefa2 = leTarefa();
      } catch (Exception e) {
        System.out.println("Dados inválidos");
        return;
      }

      // comparação do novo e antigo
      if (Tarefa2.getNome().length() > 0)
        Tarefa.setNome(Tarefa2.getNome());
      if (Tarefa2.getIdCategoria() > 0)
        Tarefa.setIdCategoria(Tarefa2.getIdCategoria());
      if (Tarefa2.getDataCriacao() != null)
        Tarefa.setDataCriacao(Tarefa2.getDataCriacao());
      if (Tarefa2.getDataConclusao() != null)
        Tarefa.setDataConclusao(Tarefa2.getDataConclusao());
      if (Tarefa2.getStatus() > 0)
        Tarefa.setStatus(Tarefa2.getStatus());
      if (Tarefa2.getPrioridade() > 0)
        Tarefa.setPrioridade(Tarefa2.getPrioridade());

      System.out.print("Confirma alteração da tarefa (S/N)? ");
      char resp = console.nextLine().charAt(0);
      if (resp == 'S' || resp == 's') {
        if (arqTarefas.update(Tarefa)) {
          System.out.println("Tarefa alterada!");
          lista.print();
        } else
          System.out.println("Erro na alteração da tarefa!");
      } else
        System.out.println("Alteração cancelada!");
    } catch (Exception e) {
      System.out.println("Erro no acesso ao arquivo");
      e.printStackTrace();
    }
  }

  // ---------------------
  // método para mostrar as tarefas
  // ---------------------
  public void mostraTarefa(Tarefa t) throws Exception {
    System.out.println("\nNome: " + t.getNome());
  }

  // ---------------------
  // método de excluir a tarefa selecionada
  // ---------------------
  public void excluirTarefa() {
    System.out.println("\n\n\nTarefas AEDS3");
    System.out.println("------------");
    System.out.println("\n> Início > Tarefas > Exclusão");

    try {
      // Lista as tarefas
      Tarefa[] tarefas = arqTarefas.readAll();
      int i;
      System.out.println("\nTarefas\n----------");
      for (i = 0; i < tarefas.length; i++) {
        System.out.println((i + 1) + ": " + tarefas[i].getNome());
      }

      // Lê o nome da tarefa
      System.out.print("\nNome da tarefa a excluir: ");
      String nomeTarefa = console.nextLine();

      // Busca a tarefa pelo nome
      Tarefa tarefa = arqTarefas.readNome(nomeTarefa);
      if (tarefa == null) {
        System.out.println("Tarefa não encontrada!");
        return;
      }

      // Mostra a tarefa encontrada
      mostraTarefa(tarefa);

      // Confirma a exclusão
      System.out.print("Confirma exclusão da tarefa (S/N)? ");
      char resp = console.nextLine().charAt(0);
      if (resp == 'S' || resp == 's') {
        if (arqTarefas.delete(tarefa.getID())) {
          System.out.println("Tarefa excluída!");
        } else {
          System.out.println("Erro na exclusão da tarefa!");
        }
      } else {
        System.out.println("Exclusão cancelada!");
      }
    } catch (Exception e) {
      System.out.println("Erro no acesso ao arquivo");
      e.printStackTrace();
    }
  }

  // METODOS ROTULOS

  // ---------------------
  // método de ler rotulos
  // ---------------------
  public Rotulo leRotulo(ArquivoRotulos arqRotulos) throws Exception {
    // Lê o nome do rótulo
    System.out.print("Nome do Rótulo: ");
    String nomeRotulo = console.nextLine().trim();

    if (nomeRotulo.isEmpty()) {
      System.out.println("Nome do rótulo não pode ser vazio!");
      return null;
    }

    // Verifica se o rótulo já existe
    Rotulo novoRotulo = new Rotulo(nomeRotulo);
    int idRotulo = arqRotulos.create(novoRotulo);
    novoRotulo.setID(idRotulo);

    System.out.println("Rótulo \"" + nomeRotulo + "\" criado com sucesso.");
    return novoRotulo;
  }

  // ---------------------
  // método de incluir novos rotulos a tarefas
  // ---------------------
  public void incluirRotulo(ArquivoRotulos arqRotulos, ArquivoTarefas arqTarefas) throws Exception {
    // Lê ou cria o rótulo
    Rotulo rotulo = leRotulo(arqRotulos);
    if (rotulo == null) {
      return; // Não foi possível criar o rótulo
    }

    // Exibe as tarefas existentes
    Tarefa[] tarefas = arqTarefas.readAll();
    if (tarefas.length == 0) {
      System.out.println("Nenhuma tarefa cadastrada para associar ao rótulo.");
      return;
    }

    System.out.println("Tarefas disponíveis:");
    for (int i = 0; i < tarefas.length; i++) {
      System.out.println((i + 1) + ": " + tarefas[i].getNome() + " (ID: " + tarefas[i].getID() + ")");
    }

    // Lê as tarefas que devem ser associadas ao rótulo
    System.out.print("Informe os números das tarefas a serem associadas ao rótulo (separados por vírgula): ");
    String[] idsTarefasSelecionadas = console.nextLine().split(",");

    // Associa o rótulo às tarefas selecionadas
    for (String idStr : idsTarefasSelecionadas) {
      try {
        int indice = Integer.parseInt(idStr.trim()) - 1;
        if (indice >= 0 && indice < tarefas.length) {
          arqTarefas.incluirRotulo(tarefas[indice].getID(), rotulo.getID());
          System.out.println(
              "Rótulo \"" + rotulo.getRotulo() + "\" associado à tarefa \"" + tarefas[indice].getNome() + "\".");
        } else {
          System.out.println("Número inválido: " + (indice + 1));
        }
      } catch (NumberFormatException e) {
        System.out.println("Formato inválido: " + idStr.trim());
      }
    }
  }

  // ---------------------
  // método de busca da tarefa por rotulo
  // ---------------------
  public void buscarTarefasPorRotulo(ArquivoRotulos arqRotulos, ArquivoTarefas arqTarefas) {
    System.out.println("\n\n\nTarefas AEDS3");
    System.out.println("------------");
    System.out.println("\n> Início > Tarefas > Busca por Rótulo");

    try {
      // Lista os rótulos disponíveis
      Rotulo[] rotulos = arqRotulos.readTudo();
      if (rotulos.length == 0) {
        System.out.println("Nenhum rótulo cadastrado!");
        return;
      }

      System.out.println("\nRótulos disponíveis\n-------------------");
      for (int i = 0; i < rotulos.length; i++) {
        System.out.println((i + 1) + ": " + rotulos[i].getRotulo());
      }

      // Lê o nome do rótulo para busca
      System.out.print("\nDigite o nome do rótulo: ");
      String nomeRotulo = console.nextLine().trim();

      // Verifica se o nome foi fornecido
      if (nomeRotulo.isEmpty()) {
        System.out.println("Nenhum rótulo fornecido!");
        return;
      }

      // Busca o rótulo pelo nome
      Rotulo rotulo = null;
      for (Rotulo r : rotulos) {
        if (r.getRotulo().equalsIgnoreCase(nomeRotulo)) {
          rotulo = r;
          break;
        }
      }

      // Verifica se o rótulo foi encontrado
      if (rotulo == null) {
        System.out.println("Rótulo não encontrado!");
        return;
      }

      // Busca as tarefas associadas ao rótulo
      List<Integer> tarefasIDs = arqTarefas.buscarTarefasPorRotulo(rotulo.getID());
      if (tarefasIDs.isEmpty()) {
        System.out.println("Nenhuma tarefa associada ao rótulo \"" + nomeRotulo + "\".");
        return;
      }

      // Exibe as tarefas encontradas
      System.out.println("\nTarefas associadas ao rótulo \"" + nomeRotulo + "\":\n");
      for (int id : tarefasIDs) {
        Tarefa tarefa = arqTarefas.read(id);
        if (tarefa != null) {
          mostrarTarefas(tarefa);
        }
      }

    } catch (Exception e) {
      System.out.println("Erro no acesso ao arquivo");
      e.printStackTrace();
    }
  }

  // ---------------------
  // método de listar todos os rotulos
  // ---------------------
  public void listarRotulos(ArquivoRotulos arqRotulos) {
    System.out.println("\n\n\nRótulos");
    System.out.println("--------------");
    System.out.println("\n> Início > Rótulos > Listar todos");

    try {
      // Lista todos os rótulos usando a Árvore B+
      Rotulo[] rotulos = arqRotulos.readTudo(); // Carrega todos os rótulos
      if (rotulos.length == 0) {
        System.out.println("Nenhum rótulo cadastrado.");
        return;
      }

      int i;
      System.out.println("\nRótulos Cadastrados\n-------------------");
      for (i = 0; i < rotulos.length; i++) {
        System.out.println((i + 1) + ": " + rotulos[i].getRotulo() + " (ID: " + rotulos[i].getID() + ")");
      }

      System.out.println("\nDetalhes dos Rótulos:\n");

      // Exibe detalhes de cada rótulo
      for (Rotulo rotulo : rotulos) {
        mostrarRotulo(rotulo); // Método para exibir detalhes do rótulo
      }

    } catch (Exception e) {
      System.out.println("Erro ao listar rótulos.");
      e.printStackTrace();
    }
  }

  // ---------------------
  // método de mostrar os rotulos
  // ---------------------
  private void mostrarRotulo(Rotulo rotulo) {
    System.out.println("------------------------------");
    System.out.println("ID: " + rotulo.getID());
    System.out.println("Nome do Rótulo: " + rotulo.getRotulo());
    System.out.println("------------------------------");
  }

  // ---------------------
  // método de alterar o rotulo
  // ---------------------
  public void alterarRotulo(ArquivoRotulos arqRotulos) {
    System.out.println("\n\n\nRótulos AEDS3");
    System.out.println("--------------");
    System.out.println("\n> Início > Rótulos > Alteração");

    try {
      // Lista os rótulos existentes
      Rotulo[] rotulos = arqRotulos.readTudo();
      if (rotulos.length == 0) {
        System.out.println("Nenhum rótulo cadastrado.");
        return;
      }

      System.out.println("\nRótulos Cadastrados\n-------------------");
      for (int i = 0; i < rotulos.length; i++) {
        System.out.println((i + 1) + ": " + rotulos[i].getRotulo() + " (ID: " + rotulos[i].getID() + ")");
      }

      // Lê o nome do rótulo a ser alterado
      System.out.print("\nNome do Rótulo: ");
      String nomeRotulo = console.nextLine().trim();

      // Busca o rótulo pelo nome
      Rotulo rotulo = null;
      for (Rotulo r : rotulos) {
        if (r.getRotulo().equalsIgnoreCase(nomeRotulo)) {
          rotulo = r;
          break;
        }
      }

      if (rotulo == null) {
        System.out.println("Rótulo não encontrado.");
        return;
      }

      // Exibe os detalhes do rótulo
      mostrarRotulo(rotulo);

      // Solicita os novos dados
      System.out.println("\nDigite os novos dados.\nDeixe em branco os que não desejar alterar.");
      System.out.print("Novo Nome do Rótulo: ");
      String novoNomeRotulo = console.nextLine().trim();

      // Atualiza os dados do rótulo
      if (!novoNomeRotulo.isEmpty()) {
        rotulo.setRotulo(novoNomeRotulo);
      }

      // Confirma a alteração
      System.out.print("Confirma alteração do rótulo (S/N)? ");
      char resp = console.nextLine().charAt(0);
      if (resp == 'S' || resp == 's') {
        if (arqRotulos.update(rotulo)) {
          System.out.println("Rótulo alterado com sucesso!");
        } else {
          System.out.println("Erro ao alterar o rótulo!");
        }
      } else {
        System.out.println("Alteração cancelada.");
      }

    } catch (Exception e) {
      System.out.println("Erro no acesso ao arquivo.");
      e.printStackTrace();
    }
  }

  // ---------------------
  // método de excluir rotulo
  // ---------------------
  public void excluirRotulo(ArquivoRotulos arqRotulos, ArquivoTarefas arqTarefas) {
    System.out.println("\n\n\nRótulos AEDS3");
    System.out.println("--------------");
    System.out.println("\n> Início > Rótulos > Exclusão");

    try {
      // Lista os rótulos existentes
      Rotulo[] rotulos = arqRotulos.readTudo();
      if (rotulos.length == 0) {
        System.out.println("Nenhum rótulo cadastrado.");
        return;
      }

      System.out.println("\nRótulos Cadastrados\n-------------------");
      for (int i = 0; i < rotulos.length; i++) {
        System.out.println((i + 1) + ": " + rotulos[i].getRotulo() + " (ID: " + rotulos[i].getID() + ")");
      }

      // Lê o nome do rótulo a ser excluído
      System.out.print("\nNome do Rótulo a excluir: ");
      String nomeRotulo = console.nextLine().trim();

      // Busca o rótulo pelo nome
      Rotulo rotulo = null;
      for (Rotulo r : rotulos) {
        if (r.getRotulo().equalsIgnoreCase(nomeRotulo)) {
          rotulo = r;
          break;
        }
      }

      if (rotulo == null) {
        System.out.println("Rótulo não encontrado.");
        return;
      }

      // Exibe os detalhes do rótulo
      mostrarRotulo(rotulo);

      // Confirma a exclusão
      System.out.print("Confirma exclusão do rótulo (S/N)? ");
      char resp = console.nextLine().charAt(0);
      if (resp == 'S' || resp == 's') {
        // Remove associações do rótulo com tarefas
        List<Integer> tarefasAssociadas = arqTarefas.buscarTarefasPorRotulo(rotulo.getID());
        for (int idTarefa : tarefasAssociadas) {
          arqTarefas.removerRotulo(idTarefa, rotulo.getID());
        }

        // Exclui o rótulo do arquivo
        if (arqRotulos.delete(rotulo.getID())) {
          System.out.println("Rótulo excluído com sucesso!");
        } else {
          System.out.println("Erro ao excluir o rótulo.");
        }
      } else {
        System.out.println("Exclusão cancelada.");
      }

    } catch (Exception e) {
      System.out.println("Erro no acesso ao arquivo.");
      e.printStackTrace();
    }
  }

}
