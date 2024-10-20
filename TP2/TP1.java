//Código Principal feito por André Mendes, Caio Gomes e Daniel Salgado

import java.io.*;
import java.time.LocalDate;

import codigo.Arquivo;
import codigo.Tarefa;

class TP1 {

  public static void main(String args[]) {

    //excluindo todas as informações anteriores
    new File("dados/tarefas.db").delete();
    new File("dados/tarefas.hash_d.db").delete();
    new File("dados/tarefas.hash_c.db").delete();

    Arquivo<Tarefa> arqTarefas;
    Tarefa t1 = new Tarefa(-1, "Fazer TP1 de AEDS3",  LocalDate.of(2024, 9, 9), LocalDate.of(2024, 9, 14), "Feita", "Alta");
    Tarefa t2 = new Tarefa(-1, "Fazer Lista 2 de IA",  LocalDate.of(2024, 9, 10), LocalDate.of(2024, 9, 10), "Feita", "Média");
    Tarefa t3 = new Tarefa(-1, "Fazer lista de estatística",  LocalDate.of(2024, 9, 14), LocalDate.of(2024, 9, 16), "Em Progresso", "Alta");
    Tarefa t4 = new Tarefa(-1, "Fazer Sprint 2 LDDM", LocalDate.of(2024, 9, 18), LocalDate.of(2024, 9, 26), "Pendente", "Baixa");

    //criando os ids antes das ações
    int id1, id2, id3, id4;

    try {
      arqTarefas = new Arquivo<>("Tarefas", Tarefa.class.getConstructor());

      //cria 3 instâncias para funcionalidade do método create
      id1 = arqTarefas.create(t1);
      System.out.println("Nova tarefa criada com o ID: " + id1);

      id2 = arqTarefas.create(t2);
      System.out.println("Nova tarefa criada com o ID: " + id2);

      id3 = arqTarefas.create(t3);
      System.out.println("Nova tarefa criada com o ID: " + id3);
 
      //printando todos os cadastros
      for(int i = 1; i <= 3; i++){
        System.out.println("\nTarefa "+i+":\n" + arqTarefas.read(i));
      }

      //tenta deletar a tarefa 3, caso exista
      if (arqTarefas.delete(id3)){
        System.out.println("\nTarefa com o ID " + id3 + " deletada");
      }else{
        System.out.println("\nTarefa com o ID " + id3 + " não foi encontrada, não é possivel deletar os dados!\n");
      } 

      //atualizando os dados de uma tarefa existente
      t1.setNome("Fazer site de Desenvolvimento de Interfaces Web");
      t1.setDataCriacao(LocalDate.of(2024, 9, 20));
      t1.setDataConclusao(LocalDate.of(2024, 10, 20));
      t1.setStatus("Pendente");
      t1.setPrioridade("Média");
      if (arqTarefas.update(t1)){
        System.out.println("\nTarefa com o ID " + t1.getID() + " alterada!");
      }else{
        System.out.println("\nTarefa com o ID " + t1.getID() + " não foi encontrada, não é possível alterar os dados!\n");
      }

      //atualizando os dados de uma tarefa inexistente
      t3.setNome("Estudar para Grafos");
      t3.setDataCriacao(LocalDate.of(2024, 9, 3));
      t3.setDataConclusao(LocalDate.of(2024, 9, 10));
      t3.setStatus("Feita");
      t3.setPrioridade("Baixa");
      if (arqTarefas.update(t3)){
        System.out.println("\nTarefa com o ID " + t3.getID() + " alterada!");
      }else{
        System.out.println("\nTarefa com o ID " + t3.getID() + " não foi encontrada, não é possível alterar os dados!\n");
      }

      //cria a 4° instância
      id4 = arqTarefas.create(t4);
      System.out.println("Nova tarefa criada com o ID: " + id4);

      //printando todos os cadastros
      for(int i = 1; i <= 4; i++){
        System.out.println("\nTarefa "+i+":\n" + arqTarefas.read(i));
      }
      
      arqTarefas.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
