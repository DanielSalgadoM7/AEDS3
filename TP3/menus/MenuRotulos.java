package menus;

import java.util.List;
import java.util.Scanner;

import aeds3.ListaInvertida;
import arquivos.ArquivoCategorias;
import arquivos.ArquivoTarefas;
import arquivos.ArquivoRotulos;
import entidades.Categoria;
import entidades.Rotulo;
import entidades.Tarefa;

public class MenuRotulos // MenuRotulos
{

  Scanner console = new Scanner(System.in, "UTF-8");
  private ArquivoRotulos arqRotulos;
  private ArquivoTarefas arqTarefas;
  private ArquivoCategorias arqCategorias;
  private ListaInvertida lista;

  public MenuRotulos() {
    try {
      arqRotulos = new ArquivoRotulos();
      arqTarefas = new ArquivoTarefas();
      arqCategorias = new ArquivoCategorias();
      lista = new ListaInvertida();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

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
  // método de mostrar os rotulos
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
  // método que mostra a interface do menu de rotulos
  // ---------------------
  public void menu() throws Exception {

    // Mostra o menu
    int opcao;
    do {
      System.out.println("\n\n\nRotulos AEDS3");
      System.out.println("------------");
      System.out.println("\n> Início > Rotulos");
      System.out.println("\n1) Incluir Rotulo");
      System.out.println("2) Buscar Tarefa por Rotulo");
      System.out.println("3) Listar todos os Rotulos");
      System.out.println("4) Alterar Rotulo");
      System.out.println("5) Excluir Rotulo");
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
        incluirRotulo(arqRotulos, arqTarefas);
        break;
    case 2:
        buscarTarefasPorRotulo(arqRotulos, arqTarefas);
        break;
    case 3:
        listarRotulos(arqRotulos);
        break;
    case 4:
        alterarRotulo(arqRotulos);
        break;
    case 5:
        excluirRotulo(arqRotulos, arqTarefas);
        break;
    case 0:
        break;
    default:
        System.out.println("Opção inválida");
}

    } while (opcao != 0);

    // Fecha os arquivos
    try {
      arqRotulos.close();
      arqCategorias.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

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
  // método de excluir o rotulo
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
