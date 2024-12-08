package menus;

import java.util.ArrayList;
import java.util.Scanner;

import aeds3.ArvoreBMais;
import aeds3.ParIntInt;
import arquivos.ArquivoCategorias;
import arquivos.ArquivoTarefas;
import entidades.Categoria;
import entidades.Tarefa;

public class MenuCategorias {

  private static Scanner console = new Scanner(System.in);
  private ArquivoCategorias arqCategorias;
  private ArquivoTarefas arqTarefas;
  private ArvoreBMais<ParIntInt> relTarefasDaCategoria;

  public MenuCategorias() {
    try {
      arqCategorias = new ArquivoCategorias();
      arqTarefas = new ArquivoTarefas();
      relTarefasDaCategoria = new ArvoreBMais<>(ParIntInt.class.getConstructor(), 4, "dados/tarefas_categorias.btree.db");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // ---------------------
  // método de ler as categorias
  // ---------------------
  public Categoria leCategoria() throws Exception {

    // Lê os campos iniciais
    System.out.print("\nNome: ");
    String nome = console.nextLine();

    // Cria e retorna o categoria
    System.out.println();
    Categoria c = new Categoria(nome);
    return c;
  }

  // ---------------------
  // método para mostrar as categorias
  // ---------------------
  public void mostraCategoria(Categoria c) throws Exception {
    System.out.println("\nNome: " + c.getNome());
  }

  // ---------------------
  // método que mostra a interface do menu de categorias
  // ---------------------
  public void menu() {

    // Mostra o menu
    int opcao;
    do {
      System.out.println("\n\n\nTarefas AEDS3");
      System.out.println("------------");
      System.out.println("\n> Início > Categorias");
      System.out.println("\n1) Incluir Categoria");
      System.out.println("2) Buscar Categoria");
      System.out.println("3) Listar as Tarefas de uma Categoria");
      System.out.println("4) Alterar Categoria");
      System.out.println("5) Excluir Categoria");
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
          incluirCategoria();
          break;
        case 2:
          buscarCategoria();
          break;
        case 3:
          mostrarCategorias();
          break;
        case 4:
          alterarCategoria();
          break;
        case 5:
          excluirCategoria();
          break;
        case 0:
          break;
        default:
          System.out.println("Opção inválida");
      }
    } while (opcao != 0);

    // Fecha os arquivos
    try {
      arqCategorias.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // ---------------------
  // método de incluir novas categorias
  // ---------------------
  public void incluirCategoria() {

    // Lê uma nova categoria, testando se todos os campos foram preenchidos
    Categoria novaCategoria;
    try {
      boolean dadosCompletos = false;
      do {
        novaCategoria = leCategoria();
        if (novaCategoria.getNome().length() == 0)
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
      // Insere a nova categoria no banco de dados
      System.out.print("Confirma inclusão da categoria (S/N)? ");
      char resp = console.nextLine().charAt(0);
      if (resp == 'S' || resp == 's') {
        arqCategorias.create(novaCategoria);
      }
    } catch (Exception e) {
      System.out.println("Categoria não pode ser criada");
      e.printStackTrace();
      return;
    }

    System.out.println("\nCategoria armazenada!");

  }

  // ---------------------
  // método de busca da categoria selecionada
  // ---------------------
  public void buscarCategoria() {
    System.out.println("\n\n\nTarefas AEDS3");
    System.out.println("------------");
    System.out.println("\n> Início > Categorias > Busca");

    try {
      // Lista as categorias
      Categoria[] categorias = arqCategorias.readAll();
      int i;
      System.out.println("\nCategorias\n----------");
      for (i = 0; i < categorias.length; i++) {
        System.out.println(categorias[i].getID() + ": " + categorias[i].getNome());
      }

      // Lê a categoria
      System.out.print("\nID da categoria a exibir: ");
      int c;
      Categoria categoria;
      String sCategoria = console.nextLine();
      if (sCategoria.length() > 0) {
        c = Integer.parseInt(sCategoria);
        if (c > 0 && c <= categorias.length) {
          categoria = categorias[c - 1];
        } else {
          System.out.println("Categoria inválida!");
          return;
        }
      } else
        return;
      mostraCategoria(categoria);

    } catch (Exception e) {
      System.out.println("Erro no acesso ao arquivo");
      e.printStackTrace();
    }
  }

  // ---------------------
  // método de alterar a categoria selecionada
  // ---------------------
  public void alterarCategoria() {
    System.out.println("\n\n\nTarefas AEDS3");
    System.out.println("------------");
    System.out.println("\n> Início > Categorias > Alteração");

    try {
      // Lista as categorias
      Categoria[] categorias = arqCategorias.readAll();
      int i;
      System.out.println("\nCategorias\n----------");
      for (i = 0; i < categorias.length; i++) {
        System.out.println(categorias[i].getID() + ": " + categorias[i].getNome());
      }

      // Lê a categoria
      System.out.print("\nID da categoria a alterar: ");
      int c;
      Categoria categoria;
      String sCategoria = console.nextLine();
      if (sCategoria.length() > 0) {
        c = Integer.parseInt(sCategoria);
        if (c > 0 && c <= categorias.length) {
          categoria = categorias[c - 1];
        } else {
          System.out.println("Categoria inválida!");
          return;
        }
      } else
        return;
      mostraCategoria(categoria);

      System.out.println("\nDigite o novo nome da categoria");
      Categoria categoria2;
      try {
        categoria2 = leCategoria();
      } catch (Exception e) {
        System.out.println("Dados inválidos");
        return;
      }
      if (categoria2.getNome().length() > 0)
        categoria.setNome(categoria2.getNome());

      System.out.print("Confirma alteração da categoria (S/N)? ");
      char resp = console.nextLine().charAt(0);
      if (resp == 'S' || resp == 's') {
        if (arqCategorias.update(categoria))
          System.out.println("Categoria alterada!");
        else
          System.out.println("Erro na alteração da categoria!");
      } else
        System.out.println("Alteração cancelada!");
    } catch (Exception e) {
      System.out.println("Erro no acesso ao arquivo");
      e.printStackTrace();
    }
  }

  // ---------------------
  // método de excluir a categoria selecionada
  // ---------------------
  public void excluirCategoria() {
    System.out.println("\n\n\nTarefas AEDS3");
    System.out.println("------------");
    System.out.println("\n> Início > Categorias > Exclusão");

    try {
      // Lista as categorias
      Categoria[] categorias = arqCategorias.readAll();
      int i;
      System.out.println("\nCategorias\n----------");
      for (i = 0; i < categorias.length; i++) {
        System.out.println(categorias[i].getID() + ": " + categorias[i].getNome());
      }

      // Lê a categoria
      System.out.print("\nID da categoria a excluir: ");
      int c;
      Categoria categoria;
      String sCategoria = console.nextLine();
      if (sCategoria.length() > 0) {
        c = Integer.parseInt(sCategoria);
        if (c > 0 && c <= categorias.length) {
          categoria = categorias[c - 1];
        } else {
          System.out.println("Categoria inválida!");
          return;
        }
      } else
        return;

      // Mostra a categoria
      mostraCategoria(categoria);

      // Testa se a categoria tem Tarefas.
      ArrayList<ParIntInt> lista = relTarefasDaCategoria.read(new ParIntInt(categoria.getID(), -1));
      if (lista.size() > 0) {
        System.out.println(
            "Esta categoria tem Tarefas e não pode ser excluída.\nExclua ou altere os Tarefas antes de excluir a categoria.");
        return;
      }

      System.out.print("Confirma exclusão da categoria (S/N)? ");
      char resp = console.nextLine().charAt(0);
      if (resp == 'S' || resp == 's') {
        if (arqCategorias.delete(categoria.getID()))
          System.out.println("Categoria excluída!");
        else
          System.out.println("Erro na exclusão da categoria!");
      } else
        System.out.println("Exclusão cancelada!");
    } catch (Exception e) {
      System.out.println("Erro no acesso ao arquivo");
      e.printStackTrace();
    }
  }

 // ---------------------
  // método para mostrar as categorias
  // ---------------------
  public void mostrarCategorias() {
    System.out.println("\n\n\nTarefas AEDS3");
    System.out.println("------------");
    System.out.println("\n> Início > Categorias > Tarefas");

    try {
      // Lista as categorias
      Categoria[] categorias = arqCategorias.readAll();
      int i;
      System.out.println("\nCategorias\n----------");
      for (i = 0; i < categorias.length; i++) {
        System.out.println(categorias[i].getID() + ": " + categorias[i].getNome());
      }

      // Lê a categoria
      System.out.print("\nID da categoria a exibir: ");
      int c;
      Categoria categoria;
      String sCategoria = console.nextLine();
      if (sCategoria.length() > 0) {
        c = Integer.parseInt(sCategoria);
        if (c > 0 && c <= categorias.length) {
          categoria = categorias[c - 1];
        } else {
          System.out.println("Categoria inválida!");
          return;
        }
      } else
        return;

      mostraCategoria(categoria);

      // Recupera os Tarefas
      ArrayList<ParIntInt> lista = relTarefasDaCategoria.read(new ParIntInt(categoria.getID(), -1));
      if (lista.size() == 0) {
        System.out.println("Não há Tarefas nesta categoria.");
      } else {
        System.out.print("Tarefas: ");
        for (i = 0; i < lista.size(); i++) {
          Tarefa l = arqTarefas.read(lista.get(i).get2());
          System.out.print("\n- " + l.getNome());
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
