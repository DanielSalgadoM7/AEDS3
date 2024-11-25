package arquivos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ParNomeId implements aeds3.RegistroHashExtensivel<ParNomeId> {

  private String nome;
  private int id;
  private final short TAMANHO = 17;

  public ParNomeId() {
    this.nome = "0000000000000";
    this.id = -1;
  }

  public ParNomeId(String nome, int id) {
    this.nome = nome;
    this.id = id;
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

  // Converte para array de bytes
  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);

    // Converte o nome para UTF-8 e limita a 13 bytes
    byte[] nomeBytes = this.nome.getBytes(StandardCharsets.UTF_8);
    if (nomeBytes.length > 13) {
        dos.write(nomeBytes, 0, 13);  // Se o nome for maior que 13, truncar
    } else {
        dos.write(nomeBytes);  // Escreve o nome
        dos.write(new byte[13 - nomeBytes.length]);  // Preenche com espaços
    }
    
    dos.writeInt(this.id);  // Escreve o ID (4 bytes)

    return baos.toByteArray();
  }

  // Converte de array de bytes para o objeto
  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);

    byte[] b = new byte[13];
    dis.readFully(b);  // Lê os 13 bytes do nome
    this.nome = new String(b, StandardCharsets.UTF_8).trim();  // Remove espaços extras
    
    this.id = dis.readInt();  // Lê o ID (4 bytes)
  }

  @Override
  public int hashCode() {
    return ParNomeId.hashNome(this.nome);
  }

  // Hash simplificado com base no nome
  public static int hashNome(String nome) {
    return nome.hashCode();  // Usa o hashCode padrão da string
  }
}
