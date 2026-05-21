# Servico Auditor de Falhas (DLQ)

## Objetivo
Servico independente que consome mensagens da fila DLQ do SQS e persiste o registro de auditoria no banco de dados para analise posterior.

## Arquitetura escolhida e justificativa
Escolhi uma arquitetura em camadas com adaptadores (inspirada em Ports and Adapters), separando responsabilidades para manter o servico pequeno, claro e facil de testar.

- **domain**: regras de negocio puras (entidades e enums). Nao depende de Spring nem de infraestrutura.
- **application**: casos de uso e orquestracao da regra de negocio (triagem de severidade e persistencia).
- **infrastructure**: adaptadores para tecnologias externas (SQS e JPA/H2), mantendo o dominio limpo.

Essa divisao evita acoplamento direto entre regra de negocio e tecnologia (SQS/DB), facilita manutencao, e deixa claro onde cada mudanca deve ser feita. Para um servico de apoio como este, que so consome fila e salva auditoria, essa organizacao reduz complexidade sem perder clareza.

## Requisitos atendidos
- Escuta ativa da fila DLQ com `@SqsListener`.
- Captura do erro associado a mensagem e persistencia no banco.
- Mensagem removida da DLQ apenas apos salvar no banco (retorno bem sucedido do listener).
- Regra de triagem de severidade:
  - total > 100: HIGH
  - total entre 50 e 100: MEDIUM
  - total < 50: LOW
- Registro persistido com os campos exigidos:
  - `errorId`, `queueName`, `payload`, `timestamp`, `status`, `severity`.

## Estrutura de pastas
```
src/main/java/br/com/gabrielwandscheer/errorauditorservice/
  application/
  domain/
  infrastructure/
```

## Configuracao
Use variaveis de ambiente. **Nunca** coloque chaves reais no codigo ou no Git.

Variaveis esperadas:
- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`
- `AWS_DEFAULT_REGION` (ex: us-east-1)
- `APP_SQS_DLQ_NAME` (ex: T0XN_SEU_NOME-DLQ.fifo)
- `APP_SQS_QUEUE_NAME` (ex: T0XN_SEU_NOME.fifo)

## Como executar
1. Configure as variaveis de ambiente.
2. Execute o projeto:
   - `mvnw.cmd spring-boot:run`

## Evidencias para entrega
- Print do terminal recebendo a mensagem.
- Print do banco (H2) com o registro persistido.

## Observacoes de seguranca
Se voce ja expos chaves AWS, **rotacione** imediatamente e remova de qualquer arquivo.
