Trabalho Prático 4 - AEDs3

Grupo: André Mendes, Caio Gomes e Daniel Salgado

---

O trabalho prático 4 de AEDs 3 envolve a criação de backups para salvamentos de dados, incluindo a possibilidade de realizar a recuperação dos dados presentes em cada backup.

A maior parte das classes criadas, juntamente de seus métodos, foram feitos em sala, juntamente do professor:

- Classe TP4(main) - cria as instâncias de tarefas e chama os menus criados com cada CRUD por meio de um switch.
- Classe de Tarefas - cria a estrutura de tarefas que será executada no código principal, neste caso, denominado de TP4.
- Classe de Arquivo - possui os 4 métodos do CRUD, além do método de criação do arquivo que receberá os dados e um método de getPosicao que tem como objetivo guardar a posição do último elemento visitado e verificar se tem espaço nas posições existentes. Além disso, a classe Arquivo se extendeu para mais duas classes para implementar os relacionamentos entre os produtos registrados.
- Menus: Os Menus são divididos em 3, Tarefas, Categorias e Backup, este último adicionado para o TP4. Cada menu cria a interface responsável pela manipulação do usuário no código.

---

Para este TP foram adicionados apenas 3 novas classes, todas relacionadas ao backup:
- Menu de Backup: classe que cria a interface para o usuário realizar as operações de backup, tanto para realizar o backup ou restaurar um backup já realizado. Esta classe também adiciona todos os backups em um txt de versões, para que o usuário e o arquivo vejam quais os backups disponíveis.
- LZW: classe para codificar e decodificar os bytes de cada arquivo lido na pasta dados.
- BitSequence: classe para armazenar e manipular números inteiros compactados em um formato de vetor de bits, que serão usados pelo LZW.

---

A experiência em realizar a última alteração no trabalho foi tranquila em sua maioria. Com os códigos fornecidos pelo professor, e a dinâmica dos menus implementadas de forma fácil de conduzir foram essenciais para fazer o uso do LZW. Houveram dificuldades na parte de descompressão dos arquivos, pois os dados estavam saindo meio confusos, porém com o conserto da leitura e a descompactação sendo feita corretamente, a mecânica dos backups fluiu tranquila com uma boa taxa de compressão. 
- Há uma rotina de compactação usando o algoritmo LZW para fazer backup dos arquivos? Sim.
- Há uma rotina de descompactação usando o algoritmo LZW para recuperação dos arquivos? Sim.
- O usuário pode escolher a versão a recuperar? Sim, o usuário pode escolher a versão, mas assim que escolhida, ela será apagada dos registros de backup, caso o usuário recupere outro backup sem salvar esse backup atual, essa versão do backup que foi recuperada será perdida.
- Qual foi a taxa de compressão alcançada por esse backup? (Compare o tamanho dos arquivos compactados com os arquivos originais). A taxa de compressão foi de 50%.
- O trabalho está funcionando corretamente? Sim.
- O trabalho está completo? Não, o trabalho não contém as mudanças realizadas no TP3, por isso, não contém a identificação por rótulos.
- O trabalho é original e não a cópia de um trabalho de um colega? Sim.
