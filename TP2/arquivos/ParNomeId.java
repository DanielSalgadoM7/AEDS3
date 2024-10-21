package arquivos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParNomeId implements aeds3.RegistroHashExtensivel<ParNomeId> {

  private String nome;
  private int id;
  private short TAMANHO = 17;

  public ParNomeId() {
    this.nome = "0000000000000";
    this.id = -1;
  }

  public ParNomeId(String nome, int i) {
    try {
      this.nome = nome;
      this.id = i;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getNome() {
    return nome;
  }

  public int getId() {
    return id;
  }

  public short size() {
    return this.TAMANHO;
  }

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.write(this.nome.getBytes());
    dos.writeInt(this.id);
    return baos.toByteArray();
  }

  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    byte[] b = new byte[13];
    dis.read(b);
    this.nome = new String(b);
    this.id = dis.readInt();
  }

  @Override
  public int hashCode() {
    return ParNomeId.hashNome(this.nome);
  }

  public static int hashNome(String nome) {
    return Math.abs(nome.hashCode());
  }

}
