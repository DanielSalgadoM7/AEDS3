Trabalho Prático 4 - AEDs3

Grupo: André Mendes, Caio Gomes e Daniel Salgado

---

O trabalho prático 2 de AEDs 3 envolve a manipulação de dados em um registro de tarefas, envolvendo operações aprimoradas do CRUD. Neste caso, adicionamos a categoria das tarefas, podendo verificar as atividades de cada categoria separadamente.

A maior parte das classes criadas, juntamente de seus métodos, foram feitos em sala, juntamente do professor:

- Classe TP2(main) - cria as instâncias de tarefas e chama os menus criados com cada CRUD por meio de um switch.
- Classe de Tarefas - cria a estrutura de tarefas que será executada no código principal, neste caso, denominado de TP2.
- Classe de Arquivo - possui os 4 métodos do CRUD, além do método de criação do arquivo que receberá os dados e um método de getPosicao que tem como objetivo guardar a posição do último elemento visitado e verificar se tem espaço nas posições existentes. Além disso, a classe Arquivo se extendeu para mais duas classes para implementar os relacionamentos entre os produtos registrados.

---

Novas classes:

- ArvoreBMais - implementada pelo professor Kutova
- ListaInvertida - implementada pelo professor Kutova
- ParIntInt - implementada pelo professor Kutova
- RegistroArvoreBMais - implementada pelo professor Kutova
- ParNomeId - classe que registra a ligação entre um nome e seu id através do registro do hash extensível
- ArquivoTarefas - CRUD dos arquivos das tarefas, gerenciando melhor a manipulação, busca e organização dessas tarefas. 
- ArquivoCategorias - CRUD dos arquivos das categorias gerenciando melhor a manipulação, busca e organização dessas categorias.
- Categoria - cria a estrutura de categorias que será executada no código principal, neste caso, denominado de TP2.
- MenuTarefas - sistema visual para o usuário poder realizar as ações do CRUD nas tarefas.
- MenuCategorias - sistema visual para o usuário poder realizar as ações do CRUD nas categorias.

---

A experiência geral de realizar o TP foi tranquila pois o professor ajudou bastante nos códigos auxiliares que eram uma parte muito importante para a implementação de todos os requisitos. A operação mais desafiadora foi realizar o código para mostrar os arquivos salvos. As categorias foram salvas corretamente, mas a parte de tarefas, que também seria uma opção legal, demonstrou ser mais complicada para fazer.
- O CRUD (com índice direto) de categorias foi implementado? Sim, o CRUD foi implementado.
- Há um índice indireto de nomes para as categorias? Sim, existe o índice indireto de nomes para categorias.
- O atributo de ID de categoria, como chave estrangeira, foi criado na classe Tarefa? Sim, o atributo ID como chave estrangeira foi criado na classe Tarefa.
- Há uma árvore B+ que registre o relacionamento 1:N entre tarefas e categorias? Sim, existe uma árvore B+ que registra o relacionamento 1:N entre tarefas e categorias.
- É possível listar as tarefas de uma categoria? Sim, é possível verificar todas as tarefas de uma categoria.
- A remoção de categorias checa se há alguma tarefa vinculada a ela? Sim, existe a checagem para verificar se a categoria está vazia antes de realizar a remoção.
- A inclusão da categoria em uma tarefa se limita às categorias existentes? Sim, não foi possível criar novas categorias no momento da criação de novas tarefas.
- O trabalho está funcionando corretamente? Não, os métodos buscarTarefas, alterarTarefas e excluirTarefas estão dando erro ao realizar as operações em tarefas com o nome já alterado anteriormente.
- O trabalho está completo? Baseado nos pedidos feitos na aba "O que Fazer?", o trabalho está completo.
- O trabalho é original e não a cópia de um trabalho de outro grupo? Sim, o trabalho é original.
