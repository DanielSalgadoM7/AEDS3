package arquivos;

import aeds3.Arquivo;
import aeds3.ArvoreBMais;
import aeds3.HashExtensivel;
import aeds3.ListaInvertida;
import aeds3.ParIntInt;
import entidades.Tarefa;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;
import arquivos.ArquivoTarefas;

public class ArquivoTarefas extends Arquivo<Tarefa> {

  HashExtensivel<ParNomeId> indiceIndiretoNome;
  ArvoreBMais<ParIntInt> relTarefasDaCategoria;

  // Cria uma lista invertida
  ListaInvertida lista;

  public ArquivoTarefas() throws Exception {
    
    super("tarefas", Tarefa.class.getConstructor());
    indiceIndiretoNome = new HashExtensivel<>(
        ParNomeId.class.getConstructor(),
        4,
        "dados/tarefas_NomeId.hash_d.db",
        "dados/tarefas_NomeId.hash_c.db");
    relTarefasDaCategoria = new ArvoreBMais<>(ParIntInt.class.getConstructor(), 4, "dados/tarefas_categorias.btree.db");

    // Inicializa a lista invertida
    lista = new ListaInvertida();

  }

  // Métodos para o tratamento de palavras para adicionar na lista invertida

  // Lista de stop words em português
  private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
      "a", "o", "e", "de", "do", "da", "dos", "das", "em", "no", "na", "nos", "nas", "um", "uma", "uns", "umas",
      // Adicione mais palavras conforme necessário
      "por", "para", "com", "sem"));

  public static String removeStopWords(String text) {
    StringBuilder result = new StringBuilder();

    // Divide a string em palavras
    String[] words = text.split("\\s+");

    // Itera sobre as palavras e adiciona à saída se não for uma stop word
    for (String word : words) {
      word = deAccent(word.toLowerCase());
      if (!STOP_WORDS.contains(word)) {
        result.append(word).append(" ");
      }
    }

    // Remove o espaço extra no final
    return result.toString().trim();
  }

  // Metodo que tira os acentos
  public static String deAccent(String str) {
    String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    return pattern.matcher(nfdNormalizedString).replaceAll("");
  }

  //Método que remove os acentos e tira as letras maiúsculas e retorna um arraylist com as palavras chave
  public static ArrayList<String> tratamento(String t) {

    t = removeStopWords(t);
    String[] split = t.split(" ");
    ArrayList<String> resp = new ArrayList<>();
    for (String w : split) {
      resp.add(w);
    }
    return resp;

  }

  @Override
  public int create(Tarefa obj) throws Exception {
    int id = super.create(obj);
    obj.setID(id);
    indiceIndiretoNome.create(new ParNomeId(obj.getNome(), obj.getID()));
    relTarefasDaCategoria.create(new ParIntInt(obj.getIdCategoria(), obj.getID()));
    // lista de chaves que serão adicionadas a lista
    ArrayList<String> chaves = tratamento(obj.getNome());
    for (String s : chaves) {
      lista.create(s, obj.getID());
    }

    return id;
  }

  public Tarefa readNome(String nome) throws Exception {
    ParNomeId pni = indiceIndiretoNome.read(ParNomeId.hashNome(nome));
    if (pni == null)
      return null;
    int id = pni.getId();
    return super.read(id);
  }

  @Override
  public boolean delete(int id) throws Exception {
    Tarefa obj = super.read(id);
    if (obj != null)
      if (indiceIndiretoNome.delete(ParNomeId.hashNome(obj.getNome()))
          &&
          relTarefasDaCategoria.delete(new ParIntInt(obj.getIdCategoria(), obj.getID()))) {

        // Cria um arraylist de chaves e chama o metodo de exclusão da lista invertida
        ArrayList<String> chaves = tratamento(obj.getNome());
        for (String s : chaves) {
          lista.delete(s, id);
        }
        return super.delete(id);
      }
    return false;
  }

  @Override
  public boolean update(Tarefa novaTarefa) throws Exception {
    Tarefa tarefaAntiga = super.read(novaTarefa.getID());
    if (tarefaAntiga != null) {

      // Primeiro remove as chaves do titulo antigo para não ter chave duplicada
      ArrayList<String> chaves = tratamento(tarefaAntiga.getNome());
      for (String s : chaves) {
        lista.delete(s, tarefaAntiga.getID());
      }
      chaves.clear();

      // Adiciona as chaves novas
      chaves = tratamento(novaTarefa.getNome());
      for (String s : chaves) {
        lista.create(s, novaTarefa.getID());
      }
      // Atualiza a tarefa
      return super.update(novaTarefa);
    }
    return false;
  }

  
  //busca pela tarefa com base no nome
  public void busca(String s) throws Exception {
    // Valida a entrada
    if (s == null || s.trim().isEmpty()) {
        System.out.println("Nome da tarefa inválido.");
        return;
    }

    // Remove espaços extras e coloca o nome em formato adequado
    s = s.trim().toLowerCase();

    try {
        // Lê todas as tarefas
        Tarefa[] tarefas = readAll();

        // Verifica se há tarefas no sistema
        if (tarefas.length == 0) {
            System.out.println("Nenhuma tarefa registrada.");
            return;
        }

        boolean encontrou = false;
        System.out.println("Tarefas encontradas com o nome: " + s);

        // Itera por todas as tarefas e verifica o nome
        for (Tarefa tarefa : tarefas) {
            if (tarefa.getNome().toLowerCase().contains(s)) {
                System.out.println("-----------------------------");
                System.out.println("ID: " + tarefa.getID());
                System.out.println("Nome: " + tarefa.getNome());
                System.out.println("Categoria: " + tarefa.getIdCategoria());
                System.out.println("-----------------------------");
                encontrou = true;
            }
        }

        // Caso nenhuma tarefa tenha sido encontrada
        if (!encontrou) {
            System.out.println("Nenhuma tarefa encontrada com o nome: " + s);
        }

    } catch (Exception e) {
        System.out.println("Ocorreu um erro ao buscar a tarefa: " + e.getMessage());
        e.printStackTrace();
    }
}

  public Tarefa[] readAll() throws Exception {
    arquivo.seek(TAM_CABECALHO);
    byte lapide;
    short tam;
    byte[] b;

    Tarefa t;
    ArrayList<Tarefa> tarefas = new ArrayList<>();
    while (arquivo.getFilePointer() < arquivo.length()) {
      lapide = arquivo.readByte();
      tam = arquivo.readShort();
      b = new byte[tam];
      arquivo.read(b);
      if (lapide != '*') {
        t = new Tarefa(); // Cria uma nova instância a cada categoria
        t.fromByteArray(b);
        tarefas.add(t);
      }
    }
    Collections.sort(tarefas);
    Tarefa[] lista = (Tarefa[]) tarefas.toArray(new Tarefa[0]);
    return lista;
  }


}
