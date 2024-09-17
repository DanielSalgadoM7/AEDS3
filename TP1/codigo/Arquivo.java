package codigo;


import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class Arquivo<T extends Registro> {

  protected RandomAccessFile arquivo;
  protected String nomeEntidade = "";
  protected Constructor<T> construtor;
  final protected int TAM_CABECALHO = 4;
  protected HashExtensivel<ParIDEndereco> indiceDireto;
  //lista de endereços excluídos
  protected ArrayList<Long> Registros = new ArrayList<>();

  public Arquivo(String na, Constructor<T> c) throws Exception {
    this.nomeEntidade = na;
    this.construtor = c;
    arquivo = new RandomAccessFile("dados/" + na + ".db", "rw");
    if (arquivo.length() < TAM_CABECALHO) {
      arquivo.seek(0);
      arquivo.writeInt(0);
    }
    indiceDireto = new HashExtensivel<>(ParIDEndereco.class.getConstructor(),
        3,
        "dados/" + na + ".hash_d.db",
        "dados/" + na + ".hash_c.db");
  }

  public int create(T obj) throws Exception {
    arquivo.seek(0);
    int ultimoID = arquivo.readInt();
    ultimoID++;
    arquivo.seek(0);
    arquivo.writeInt(ultimoID);
    obj.setID(ultimoID);
    byte[] ba = obj.toByteArray();
    short tam = (short) ba.length;
    arquivo.seek(getPosicao(tam)); //reutiliza lápides ou escreve no final do arquivo
    long endereco = arquivo.getFilePointer();
    arquivo.writeByte(' '); //lápide
    arquivo.writeShort(tam);
    arquivo.write(ba);
    indiceDireto.create(new ParIDEndereco(obj.getID(), endereco));
    return obj.getID();
  }

  public T read(int id) throws Exception {
    T obj = construtor.newInstance();
    short tam;
    byte[] ba;

    ParIDEndereco pie = indiceDireto.read(id);
    long endereco = pie != null ? pie.getEndereco() : -1;
    if (endereco != -1) {
      arquivo.seek(endereco + 1); //pula o lápide também
      tam = arquivo.readShort();
      ba = new byte[tam];
      arquivo.read(ba);
      obj.fromByteArray(ba);
      return obj;
    }
    return null;
  }

  public boolean delete(int id) throws Exception {
    ParIDEndereco pie = indiceDireto.read(id);
    long endereco = pie != null ? pie.getEndereco() : -1;
    if (endereco != -1) {
      Registros.add(endereco); //adiciona o endereço na lista
      arquivo.seek(endereco);
      arquivo.writeByte('*'); //marca como excluído
      indiceDireto.delete(id);
      return true;
    } else
      return false;
  }

  public boolean update(T novoObj) throws Exception {
    T obj = construtor.newInstance();
    short tam, tam2;
    byte[] ba, ba2;
    ParIDEndereco pie = indiceDireto.read(novoObj.getID());
    long endereco = pie != null ? pie.getEndereco() : -1;

    if (endereco != -1) {
      arquivo.seek(endereco + 1); //pula o campo lápide
      tam = arquivo.readShort();
      ba = new byte[tam];
      arquivo.read(ba);
      obj.fromByteArray(ba);
      ba2 = novoObj.toByteArray();
      tam2 = (short) ba2.length;
      if (tam2 <= tam) { //se o tamanho for igual ou menor, o registro fica na mesma posição
        arquivo.seek(endereco + 1 + 2);
        arquivo.write(ba2);
      } else {
        arquivo.seek(endereco);
        Registros.add(endereco); //adiciona o endereço na lista
        arquivo.writeByte('*');
        arquivo.seek(getPosicao(tam2)); //procura um espaço adequado com base na comparação feita
        long endereco2 = arquivo.getFilePointer();
        arquivo.writeByte(' ');
        arquivo.writeShort(tam2);
        arquivo.write(ba2);
        indiceDireto.update(new ParIDEndereco(novoObj.getID(), endereco2));
      }
      return true;
    }
    return false;
  }

  public long getPosicao(int tam) throws Exception {
    long posicao = arquivo.length();
    int menorReg = -1; //tamanho do menor registro encontrado
    for (int i = 0; i < Registros.size(); i++) {

      arquivo.seek(Registros.get(i) + 1); //pula o lápide
      int tamR = arquivo.readShort(); //lê o tamanho do registro atual

      if (menorReg == -1) {
        //compara os dois tamanhos e se der certo, pega a posição do primeiro registro adequado e atualiza o tamanho do menor registro
        if (tamR >= tam) {
          posicao = Registros.get(i);
          menorReg = tamR; 
        }
      } else if (tamR >= tam && tamR < menorReg) { // Se o registro atual for menor que o anterior
        posicao = Registros.get(i);
        menorReg = tamR;
      }
    }


    return posicao;
  }

  public void close() throws Exception {
    arquivo.close();
    indiceDireto.close();
  }

}
