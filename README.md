# SBST vs LLM – Comparação de Geração de Testes

Projeto desenvolvido para comparar:
- SBST (EvoSuite)
- LLM (IA generativa)
- Baseline manual

## Como executar

### Rodar testes
mvn test

### Rodar mutation testing
mvn org.pitest:pitest-maven:mutationCoverage

Relatório gerado em:
target/pit-reports/index.html

## Estrutura
- src/ → código fonte
- evosuite/ → testes gerados por SBST
- llm/ → testes gerados por LLM
