## Arquitetura escolhida e justificativa

Escolhi **Layered Architecture (Arquitetura em Camadas)** porque este servico e pequeno, tem fluxo linear e regras de negocio claras, mas ainda assim integra com tecnologias externas (SQS e banco). Esse modelo deixa as responsabilidades bem separadas, reduz acoplamento e facilita manutencao e testes. Como o servico e de apoio (auditoria) e nao tem UI nem fluxos complexos, uma arquitetura mais simples e direta atende melhor que opcoes mais pesadas.

### Por que essa arquitetura faz sentido aqui
- **Regra de negocio isolada:** a triagem de severidade e logica do dominio e nao deve depender de SQS ou JPA. Assim ela fica em uma camada de aplicacao/servico, preservando independencia.
- **Adaptadores separados:** o listener da SQS so recebe a mensagem, registra o erro e delega ao caso de uso. Isso evita que a regra de negocio fique misturada com detalhes de mensageria.
- **Persistencia desacoplada:** a camada de infraestrutura concentra o repository JPA. Se futuramente eu trocar Postgres por outro banco ou outro ORM, a regra de negocio continua intacta.
- **Teste mais simples:** fica facil testar a regra de severidade sem precisar subir fila ou banco, pois ela esta isolada.

### Organizacao das pastas
- **domain**: modelo central da auditoria (entidade e enums). Representa o “contrato” do registro de erro.
- **application**: casos de uso e regras (criacao do registro e calculo de severidade).
- **infrastructure**: adaptadores de tecnologia (SQS listener e repository).

### Beneficios práticos desta escolha
- **Manutencao facil:** alteracoes no listener ou no banco nao afetam a regra de negocio.
- **Evolucao segura:** novas fontes de erro (ex: Kafka) podem ser adicionadas como outro adaptador.
- **Clareza de responsabilidade:** cada camada tem um papel unico e bem definido.
