# Projeto Final - Arquitetura de Objetos

Backend de uma aplicação de acompanhamento de hábitos, metas, planos e progresso do usuário. O sistema permite que cada usuário cadastre seus próprios dados, acompanhe streaks, ganhe XP, evolua de nível, mantenha um mascote e consulte ranking e relatórios.

## Link do deploy

> Link deploy

Exemplo:

```text
https://deploy
```

## Organização do projeto

O projeto foi organizado por feature. Cada pasta principal representa uma parte do domínio da aplicação, como `User`, `Habito`, `Meta`, `Plano`, `Mascote`, `Notificacao`, `Ranking` e `Relatorio`.

Dentro de cada feature, separamos as responsabilidades em:

- **Classe de domínio**: representa a entidade ou o modelo principal da feature, com seus atributos e regras mais próximas do estado do objeto.
- **Controller**: recebe as requisições HTTP, define as rotas da API e chama o service correspondente.
- **Service**: concentra as regras de negócio da feature, como validações, controle de propriedade dos dados, limites, XP, streak e exclusão lógica.
- **Repository**: faz o acesso aos dados usando Spring Data JPA ou consultas específicas da feature.
- **DTOs**: ficam dentro da pasta `dto` de cada feature e definem os objetos usados para entrada e saída da API, evitando expor diretamente as entidades.

Exemplo de organização:

```text
Habito/
  Habito.java
  HabitoController.java
  HabitoService.java
  HabitoRepository.java
  dto/
    CriarHabitoRequestDTO.java
    HabitoResponseDTO.java
```

Essa separação deixa o projeto mais organizado, facilita a manutenção e ajuda a localizar rapidamente onde cada tipo de regra deve ser alterado.

## As 8 classes principais do sistema

### 1. User

A classe `User` representa o usuário do sistema. Ela guarda dados como nome, email, senha com hash, XP, nível, streak, limites de hábitos, metas e planos, além do status `ativo`.

O usuário é a classe central do projeto. Hábitos, metas, planos, mascotes, notificações, ranking e relatórios dependem dele. A autenticação também usa o email do usuário como login via Basic Auth.

### 2. Habito

A classe `Habito` representa um hábito criado por um usuário. Cada hábito pertence a um único usuário e possui título, descrição, registro diário, streak interno e status `ativo`.

Quando o usuário marca o registro diário de um hábito, o sistema atualiza o progresso, pode aumentar o streak global do usuário e adiciona XP. Caso o hábito não seja cumprido dentro da rotina diária, o streak pode ser resetado e uma notificação pode ser gerada.

### 3. Meta

A classe `Meta` representa um objetivo do usuário. Cada meta pertence a um usuário e possui título, descrição, data limite, status de conclusão e status `ativo`.

Ao concluir uma meta, o usuário recebe XP e pode evoluir o streak global. As metas também podem ser usadas dentro de planos, conectando objetivos maiores com hábitos que ajudam a alcançá-los.

### 4. Plano

A classe `Plano` representa uma organização maior criada pelo usuário. Um plano pertence a um usuário, pode ter vários hábitos associados e pode estar ligado a uma meta.

Essa classe conecta as outras partes do acompanhamento: o usuário pode criar um plano para organizar hábitos ativos e, opcionalmente, associar esse plano a uma meta do mesmo usuário. Assim, o plano funciona como uma estrutura para acompanhar uma rotina ou objetivo mais completo.

### 5. Mascote

A classe `Mascote` representa o mascote do usuário. Cada usuário pode ter um mascote ativo, com HP, status de check, status `ativo` e informações usadas para verificar o progresso diário.

O mascote está ligado diretamente ao progresso do usuário. Quando o usuário mantém o streak, o mascote continua saudável. Quando o progresso não evolui na rotina diária, o HP pode ser reduzido, respeitando o mínimo de zero.

### 6. Notificacao

A classe `Notificacao` representa um aviso para o usuário. Ela pertence a um usuário e também está ligada a um hábito específico.

As notificações são usadas principalmente para avisar quando um hábito não foi cumprido. Elas possuem data de referência, status de visualização e status `ativo`. O sistema evita duplicar notificações ativas para o mesmo usuário, hábito e data.

### 7. Ranking

A classe `Ranking` representa a listagem de usuários ordenados pelo desempenho. Ela não é uma entidade persistida como `User` ou `Habito`; ela monta uma visão dos usuários com base em XP, streak e id.

O ranking depende dos dados dos usuários. A ordenação considera XP em ordem decrescente, depois streak em ordem decrescente e, por fim, id em ordem crescente. O usuário administrador não deve ser considerado no ranking.

### 8. Relatorio

A classe `Relatorio` representa uma consolidação dos dados do usuário. Ela junta hábitos, metas e planos ativos, além da data de geração.

O relatório depende diretamente do usuário logado e das features `Habito`, `Meta` e `Plano`. Ele serve para mostrar uma visão geral do progresso do usuário, indicando totais e quantidades concluídas ou pendentes.

## Como as classes se conectam

O `User` é o centro do sistema. Cada usuário possui seus próprios hábitos, metas, planos, mascote e notificações. Por isso, as regras de propriedade dos dados garantem que um usuário autenticado só consiga acessar os dados que pertencem a ele.

Os `Habitos` e as `Metas` alimentam o progresso do usuário, pois podem aumentar XP, streak e nível. O `Plano` conecta hábitos e metas para organizar objetivos maiores. O `Mascote` reage ao progresso diário do usuário, principalmente ao streak. A `Notificacao` avisa sobre hábitos não cumpridos. O `Ranking` compara usuários pelo desempenho, enquanto o `Relatorio` mostra um resumo individual do usuário logado.

Em resumo:

```text
User
  -> Habito
  -> Meta
  -> Plano
       -> Habito
       -> Meta
  -> Mascote
  -> Notificacao
       -> Habito
  -> Ranking
  -> Relatorio
       -> Habito
       -> Meta
       -> Plano
```

## Regras principais

- Apenas a criação de usuário é pública. As demais rotas exigem autenticação com Basic Auth.
- O email do usuário é usado como login.
- A senha é salva com hash usando BCrypt.
- Cada usuário só pode acessar seus próprios dados.
- Hábitos, metas, planos, mascotes e notificações usam exclusão lógica com `ativo = false`.
- A criação de usuário gera automaticamente um mascote inicial.
- Hábitos e metas respeitam limites definidos pelo nível do usuário.
- Concluir hábitos e metas adiciona XP e pode atualizar streak.
- O nível e os limites do usuário evoluem conforme o XP.
- O ranking não considera o usuário administrador.
- O relatório consolida apenas dados ativos do próprio usuário.

## Documentação da API

Com a aplicação em execução, a documentação pode ser acessada pelas rotas:

```text
/docs
/docs/api-docs
```

## Testes

Foram adicionados testes automatizados para ajudar no desenvolvimento e na validação das principais regras do sistema, incluindo autenticação, propriedade dos dados, exclusão lógica, limites, XP, streak, ranking e relatório.

Os testes foram feitos com apoio de IA. Como os testes não eram uma entrega obrigatória cobrada diretamente no projeto, entendemos que não havia problema em usar IA nessa parte. A ideia foi usar a ferramenta como apoio ao desenvolvimento, para reduzir erros manuais e facilitar a verificação das regras de negócio.
