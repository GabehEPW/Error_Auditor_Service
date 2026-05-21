Implementando o Error Auditor Service (DLQ)

Objetivo
Este servico consome mensagens da DLQ do SQS e persiste um registro de auditoria no banco de dados. O registro inclui o payload bruto, a fila de origem e a severidade calculada a partir da quantidade total de itens do pedido.

Arquitetura escolhida: Layered Architecture
Escolhi arquitetura em camadas porque este servico tem um fluxo simples, com regras de negocio claras e integracao com tecnologias externas (SQS e banco). A separacao entre camadas deixa explicito o que e regra de negocio e o que e adaptador de tecnologia, reduz o acoplamento e facilita testes.

Organizacao do projeto
- domain: entidade e enums de negocio. Aqui ficam as regras e o modelo de auditoria (ErrorAuditRecord, ErrorStatus, Severity).
- application: casos de uso/servicos de negocio. O ErrorAuditService concentra a regra de severidade e a criacao do registro.
- infrastructure: adaptadores de tecnologia. O DlqListener integra com SQS e o repository integra com JPA.

Justificativa detalhada
- A regra de severidade e independente de SQS e banco. Por isso fica em application, protegendo o dominio de detalhes de infraestrutura.
- O listener e apenas um adaptador: recebe a mensagem, captura o erro se existir no header e delega ao servico. Isso evita que logica de negocio fique dentro do adapter.
- O JPA repository fica isolado para que o dominio nao dependa do framework.
- Essa estrutura permite trocar SQS por outra fila ou JPA por outra persistencia com baixo impacto nas regras.

Contrato do registro salvo
{
	"errorId": "uuid-gerado-pelo-servico",
	"queueName": "T0XN_seu_nome_original",
	"payload": "{ ... conteudo bruto da mensagem ... }",
	"timestamp": "2026-05-07T14:30:00Z",
	"status": "PENDING_ANALYSIS",
	"severity": "HIGH|MEDIUM|LOW"
}

Regra de severidade
- total de itens > 100: HIGH
- total de itens entre 50 e 100 (inclusive): MEDIUM
- total de itens < 50: LOW

Configuracao
- Banco: H2 em memoria (application.yml). Console em http://localhost:8080/h2 com JDBC URL jdbc:h2:mem:error_auditor.
- AWS: use variaveis de ambiente para nao expor credenciais.
	- AWS_ACCESS_KEY_ID
	- AWS_SECRET_ACCESS_KEY
	- APP_SQS_QUEUE_NAME (fila original)
	- APP_SQS_DLQ_NAME (fila DLQ)

Como executar
1) Configure as variaveis de ambiente acima.
2) Rode o projeto com Maven: mvn spring-boot:run

Evidencias para entrega
- Print do terminal mostrando o consumo da mensagem.
- Print do banco mostrando o registro persistido.
