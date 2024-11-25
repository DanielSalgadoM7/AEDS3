//Trabalho Prático 3 feito pelos alunos André Mendes, Caio Gomes e Daniel Salgado

import java.io.File;
import java.time.LocalDate;
import java.util.Scanner;

import arquivos.ArquivoCategorias;
import arquivos.ArquivoTarefas;
import entidades.Categoria;
import entidades.Tarefa;
import menus.MenuCategorias;
import menus.MenuRotulos;
import menus.MenuTarefas;
import arquivos.ArquivoRotulos;
import entidades.Rotulo;

public class TP3 {

  private static Scanner console = new Scanner(System.in, "UTF-8");

  public static void main(String[] args) {

    try {

      int opcao;
      do {
        System.out.println("\n\n\nTarefas AEDS3");
        System.out.println("------------");
        System.out.println("\n> Início");
        System.out.println("\n1) Tarefas");
        System.out.println("\n2) Categorias");
        System.out.println("\n3) Rotulos");
        System.out.println("\n8) Limpar o Banco de Dados");
        System.out.println("\n0) Sair");

        System.out.print("\nOpção: ");
        try {
          opcao = Integer.valueOf(console.nextLine());
        } catch (NumberFormatException e) {
          opcao = -1;
        }

        switch (opcao) {
          case 1:
            (new MenuTarefas()).menu();
            break;
          case 2:
            (new MenuCategorias()).menu();
            break;
          case 3:
            (new MenuRotulos()).menu();
            break;
          case 8:
            limparDados();
            break;
          case 0:
            break;
          default:
            System.out.println("Opção inválida");
        }
      } while (opcao != 0);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void limparDados() {
    try {
      new File("dados/blocos.listainv.db").delete();
      new File("dados/categorias.db").delete();
      new File("dados/categorias.hash_c.db").delete();
      new File("dados/categorias.hash_d.db").delete();
      new File("dados/dicionario.listainv.db").delete();
      new File("dados/tarefas_categorias.btree.db").delete();
      new File("dados/tarefas_NomeId.hash_c.db").delete();
      new File("dados/tarefas_NomeId.hash_d.db").delete();
      new File("dados/tarefas.db").delete();
      new File("dados/tarefas.hash_d.db").delete();
      new File("dados/tarefas.hash_c.db").delete();
      new File("dados/rotulos.btree.db").delete();

      ArquivoTarefas arqTarefas = new ArquivoTarefas();
      ArquivoCategorias arqCategorias = new ArquivoCategorias();
      ArquivoRotulos arqRotulos = new ArquivoRotulos();

      // novas instâncias de categorias
      arqCategorias.create(new Categoria("Algoritmos e Estruturas de Dados III"));
      arqCategorias.create(new Categoria("Estatística e Probabilidade"));
      arqCategorias.create(new Categoria("Inteligência Artificial"));
      arqCategorias.create(new Categoria("Teoria dos Grafos e Computabilidade"));
      arqCategorias.create(new Categoria("Laboratório de Desenvolvimento de Dispositivos Móveis"));
      arqCategorias.create(new Categoria("Trabalho Interdisciplinar IV"));

      // novas instâncias de tarefas
      arqTarefas.create(new Tarefa("Lista de IA", LocalDate.of(2024, 10, 9), LocalDate.of(2024, 11, 9), 1, 3, 3));
      arqTarefas.create(new Tarefa("Sprint3", LocalDate.of(2024, 10, 9), LocalDate.of(2024, 11, 9), 2, 2, 6));
      arqTarefas.create(new Tarefa("Prova", LocalDate.of(2024, 10, 9), LocalDate.of(2024, 11, 9), 3, 1, 4));
      arqTarefas.create(new Tarefa("Lista", LocalDate.of(2024, 10, 9), LocalDate.of(2024, 11, 9), 1, 1, 2));

      arqTarefas.close();
      arqCategorias.close();

      System.out.println("Banco de dados preenchido com dados de exemplo.");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
