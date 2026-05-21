# Serviço Auditor de Falhas (DLQ)

Este serviço é responsável por consumir mensagens de uma Dead Letter Queue (DLQ) do Amazon SQS, auditar os erros e persistir os detalhes em um banco de dados para análise posterior.

## Justificativa da Arquitetura

Para este serviço, foi escolhida uma **Arquitetura em Camadas (Layered Architecture)**, uma abordagem clássica e eficaz para a maioria das aplicações de back-end, especialmente para serviços com um escopo bem definido como este.

A estrutura do projeto foi organizada da seguinte forma:

- **`application`**: Esta camada contém a lógica de aplicação e os casos de uso do sistema.
    - **`dto`**: Contém os Data Transfer Objects (DTOs), que são usados para transportar dados entre as camadas, especialmente para desserializar as mensagens da fila.
    - **`ErrorAuditService`**: É o coração da lógica de aplicação. Ele orquestra o processo de auditoria: recebe o payload da mensagem, aplica as regras de negócio para determinar a severidade do erro e coordena a persistência dos dados.

- **`domain`**: A camada de domínio representa as regras de negócio e as entidades principais do sistema.
    - **`ErrorAuditRecord`**: A entidade que modela o registro de auditoria de erro que será salvo no banco de dados.
    - **`ErrorStatus`** e **`Severity`**: Enums que representam estados e classificações importantes para o domínio do problema.

- **`infrastructure`**: Esta camada é responsável pelos detalhes técnicos de implementação, como a comunicação com sistemas externos (banco de dados, filas).
    - **`persistence`**: Contém o `ErrorAuditRepository`, uma interface do Spring Data JPA que abstrai o acesso ao banco de dados.
    - **`sqs`**: Contém o `DlqListener`, o componente que escuta a fila SQS e atua como ponto de entrada para o serviço.

### Por que a Arquitetura em Camadas?

1.  **Simplicidade e Clareza**: Para um serviço de "apoio" como este, cuja principal responsabilidade é consumir, processar e persistir dados, a arquitetura em camadas oferece uma estrutura clara e fácil de entender. As responsabilidades de cada camada são bem definidas, o que facilita a manutenção e a evolução do código.

2.  **Separação de Responsabilidades (SoC)**: A divisão em camadas (aplicação, domínio, infraestrutura) garante uma boa separação de responsabilidades. A lógica de negócio (`domain` e `application`) não se mistura com os detalhes de infraestrutura (`infrastructure`). Por exemplo, o `ErrorAuditService` não sabe como as mensagens são recebidas (SQS) ou como os dados são persistidos (JPA/H2), ele apenas delega essas tarefas para as implementações na camada de infraestrutura.

3.  **Testabilidade**: A separação de responsabilidades facilita a escrita de testes unitários e de integração. É possível testar a lógica de negócio no `ErrorAuditService` de forma isolada, "mockando" o repositório e outras dependências externas.

4.  **Flexibilidade e Manutenibilidade**: Embora a Arquitetura Hexagonal pudesse ser uma alternativa, para a escala e complexidade deste serviço, ela poderia ser um exagero. A arquitetura em camadas já nos dá uma boa flexibilidade. Se no futuro precisarmos mudar o banco de dados de H2 para Postgres, ou a fila de SQS para RabbitMQ, as alterações ficariam contidas na camada de `infrastructure`, com impacto mínimo ou nulo nas camadas de `application` e `domain`.

Em resumo, a Arquitetura em Camadas foi escolhida por ser uma solução pragmática que equilibra simplicidade, organização e boas práticas de design de software, sendo perfeitamente adequada para a natureza e os requisitos deste serviço de auditoria de falhas.
