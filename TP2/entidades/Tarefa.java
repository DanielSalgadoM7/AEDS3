//Estrutura de Tarefas feita por André Mendes, Caio Gomes e Daniel Salgado

package entidades;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.time.LocalDate;

import aeds3.Registro;

public class Tarefa implements Registro {

  //variáveis e construtores
  private int ID;
  private String nome;
  private LocalDate dataCriacao;
  private LocalDate dataConclusao;
  private int status;
  private int prioridade;
  private int idCategoria;

  public Tarefa() {
    this(-1,"", LocalDate.now(), LocalDate.now(), -1, -1, -1);
  }

  public Tarefa(String n, LocalDate dcr, LocalDate dco, int s, int p, int idC) {
    this(-1, n, dcr, dco, s, p, idC);
  }

  public Tarefa(int id, String n, LocalDate dcr, LocalDate dco, int s, int p, int idC) {
    this.ID = id;
    this.nome = n;
    this.dataCriacao = dcr;
    this.dataConclusao = dco;
    this.status = s;
    this.prioridade = p;
    this.idCategoria = idC;
  }

  //getters e setters
  public int getID() {
    return this.ID;
  }

  public void setID(int i) {
    this.ID = i;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getNome() {
    return nome;
  }

  public void setDataCriacao(LocalDate dataCriacao) {
    this.dataCriacao = dataCriacao;
  }

  public LocalDate getDataCriacao() {
    return dataCriacao;
  }

  public void setDataConclusao(LocalDate dataConclusao) {
    this.dataConclusao = dataConclusao;
  }

  public LocalDate getDataConclusao() {
    return dataConclusao;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getStatus() {
    return status;
  }

  public void setPrioridade(int prioridade) {
    this.prioridade = prioridade;
  }

  public int getPrioridade() {
    return prioridade;
  }

  public int getIdCategoria() {
    return idCategoria;
  }

  public void setIdCategoria(int idCategoria) {
    this.idCategoria = idCategoria;
  }

  //converter pra sequencia de bytes
  public byte[] toByteArray() throws Exception {
    ByteArrayOutputStream ba_out = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(ba_out);
    dos.writeInt(this.ID);
    dos.writeUTF(this.nome);
    dos.writeInt((int) this.dataCriacao.toEpochDay());
    dos.writeInt((int) this.dataConclusao.toEpochDay());
    dos.writeInt(this.status);
    dos.writeInt(this.prioridade);
    dos.writeInt(this.idCategoria);
    return ba_out.toByteArray();
  }

  //converte da sequencia de bytes pro original
  public void fromByteArray(byte[] ba) throws Exception {
    ByteArrayInputStream ba_in = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(ba_in);
    this.ID = dis.readInt();
    this.nome = dis.readUTF();
    this.dataCriacao = LocalDate.ofEpochDay(dis.readInt());
    this.dataConclusao = LocalDate.ofEpochDay(dis.readInt());
    this.status = dis.readInt();
    this.prioridade = dis.readInt();
    this.idCategoria = dis.readInt();
  }

  public String toString() {
    return "ID: " + this.ID +
        "\nTarefa: " + this.nome +
        "\nData de criação da tarefa: " + this.dataCriacao +
        "\nData de conclusão da tarefa: " + this.dataConclusao +
        "\nStatus da tarefa: " + this.status +
        "\nPrioridade da tarefa: " + this.prioridade +
        "\nCategoria: " + this.idCategoria;
  }

  public String getStatusString (){
    switch(status) {
        case 1 : 
          return "Pendente";
        case 2 : 
          return "Em andamento";
        case 3:
          return "Concluída";
        default: 
          return "erro";
    } 
  } 

  public String getPrioridadeString () {
    switch(prioridade){
      case 1: 
        return "Baixa";
      case 2: 
        return "Média";
      case 3: 
        return "Alta";
      default: 
        return "Prioridade inválida";
    } 
  } 

  public int compareTo(Object b) {
    return this.getID() - ((Tarefa) b).getID();
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

}
