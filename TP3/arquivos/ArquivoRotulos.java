/*

package arquivos;

import aeds3.Arquivo;
import entidades.Rotulo;


import java.util.ArrayList;
import java.util.Collections;

public class ArquivoRotulos extends Arquivo<Rotulo> {

    
  public ArquivoRotulos() throws Exception {
    super("rotulos", Rotulo.class.getConstructor());
  }

  @Override
  public int create(Rotulo obj) throws Exception {
    int id = super.create(obj);
    obj.setID(id);
    return id;
  }

  public Rotulo read(int id) throws Exception {
    return super.read(id);
  }

  @Override
  public boolean update(Rotulo novoRotulo) throws Exception {
    Rotulo rotuloAntigo = super.read(novoRotulo.getID());
    if (rotuloAntigo != null) {
      return super.update(novoRotulo);
    }
    return false;
  }

  @Override
  public boolean delete(int id) throws Exception {
    Rotulo rotulo = super.read(id);
    if (rotulo != null) {
      return super.delete(id);
    }
    return false;
  }

  public Rotulo[] readAll() throws Exception {
    arquivo.seek(TAM_CABECALHO);
    byte lapide;
    short tam;
    byte[] b;

    Rotulo r;
    ArrayList<Rotulo> rotulos = new ArrayList<>();
    while (arquivo.getFilePointer() < arquivo.length()) {
      lapide = arquivo.readByte();
      tam = arquivo.readShort();
      b = new byte[tam];
      arquivo.read(b);
      if (lapide != '*') {
        r = new Rotulo();
        r.fromByteArray(b);
        rotulos.add(r);
      }
    }
    Collections.sort(rotulos);
    Rotulo[] lista = rotulos.toArray(new Rotulo[0]);
    return lista;
  }
} 
*/

package arquivos;

import java.util.ArrayList;

import aeds3.Arquivo;
import entidades.Rotulo;

public class ArquivoRotulos extends Arquivo<Rotulo> {

  public ArquivoRotulos() throws Exception {
    super("Rotulos", Rotulo.class.getConstructor());
  }

  public Rotulo[] readTudo() throws Exception {
    arquivo.seek(TAM_CABECALHO);
    byte lapide;
    short tam;
    byte[] b;

    Rotulo c;
    ArrayList<Rotulo> Rotulos = new ArrayList<>();
    while (arquivo.getFilePointer() < arquivo.length()) {
      lapide = arquivo.readByte();
      tam = arquivo.readShort();
      b = new byte[tam];
      arquivo.read(b);
      if (lapide != '*') {
        c = new Rotulo(); // Cria uma nova instância a cada Rotulo
        c.fromByteArray(b);
        Rotulos.add(c);
      }
    }
    Rotulo[] lista = (Rotulo[]) Rotulos.toArray(new Rotulo[0]);
    return lista;
  }

  // TP3
  public int create(Rotulo rotulo) throws Exception {
    int id = super.create(rotulo);
    rotulo.setID(id);
    return id;
  }

}
