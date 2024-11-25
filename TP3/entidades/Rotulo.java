package entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import aeds3.Registro;

public class Rotulo implements Registro {

  // Atributos
  private int ID;
  private String rotulo;

  // Construtores
  public Rotulo() {
    this(-1, "");
  }

  public Rotulo(String rotulo) {
    this(-1, rotulo);
  }

  public Rotulo(int ID, String rotulo) {
    this.ID = ID;
    this.rotulo = rotulo;
  }

  // Getters e Setters
  public int getID() {
    return ID;
  }

  public void setID(int ID) {
    this.ID = ID;
  }

  public String getRotulo() {
    return rotulo;
  }

  public void setRotulo(String rotulo) {
    this.rotulo = rotulo;
  }

  // Converte para sequência de bytes
  public byte[] toByteArray() throws Exception {
    ByteArrayOutputStream ba_out = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(ba_out);
    dos.writeInt(this.ID);
    dos.writeUTF(this.rotulo);
    return ba_out.toByteArray();
  }

  // Reconstrói a partir de sequência de bytes
  public void fromByteArray(byte[] ba) throws Exception {
    ByteArrayInputStream ba_in = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(ba_in);
    this.ID = dis.readInt();
    this.rotulo = dis.readUTF();
  }


  @Override
  public String toString() {
    return "ID: " + this.ID + "\nRótulo: " + this.rotulo;
  }

  @Override
  public int compareTo(Object b) {
    return this.ID - ((Rotulo) b).getID();
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
