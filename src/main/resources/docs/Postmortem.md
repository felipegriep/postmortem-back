# Postmortem — Incidente {{incident.id}}{{#incident.title}} — {{incident.title}}{{/incident.title}}

**Severidade:** {{incident.severity}}  
**Status:** {{incident.status}}  
**Período:** {{incident.startedAt_fmt}} — {{incident.endedAt_fmt}}{{#dur}} (Duração: {{dur}}){{/dur}}  
**MTTA:** {{metrics.mtta}} min · **MTTR:** {{metrics.mttr}} min  
**Score de Completude:** {{metrics.score}}/100

---

## Resumo / Impacto
{{#incident.impact}}{{incident.impact}}{{/incident.impact}}{{^incident.impact}}—{{/incident.impact}}

---

## Timeline (eventos principais)
| Quando | Tipo | Ator | Detalhes |
|---|---|---|---|
{{#events}}
| {{when}} | {{type}} | {{actor}} | {{details}} |
{{/events}}
{{^events}}_Sem eventos cadastrados._{{/events}}

> **Nota:** MTTA = ALERT→DIAGNOSIS · MTTR = ALERT→FIX

---

## Análise (5 Porquês, Causa e Fatores)
**5 Porquês**
1. {{why1}}
2. {{why2}}
3. {{why3}}
4. {{why4}}
5. {{why5}}

**Causa Raiz**  
{{#rootCause}}{{rootCause}}{{/rootCause}}{{^rootCause}}—{{/rootCause}}

**Fatores Contribuintes**  
{{#factors}}{{factors}}{{/factors}}{{^factors}}—{{/factors}}

**Lições Aprendidas**  
{{#lessons}}{{lessons}}{{/lessons}}{{^lessons}}—{{/lessons}}

---

## Ações (corretivas e preventivas)
{{#actions}}
- **[{{type}}]** {{desc}}  
  Owner: {{owner}} · Due: {{due}} · Status: {{status}}{{#badge}} · {{badge}}{{/badge}}{{#evidence}} · [evidência]({{evidence}}){{/evidence}}
  {{/actions}}
  {{^actions}}_Nenhuma ação cadastrada._{{/actions}}

> Cobertura mínima para score: ≥1 CORRECTIVE + ≥1 PREVENTIVE com owner + due.

---

## Anexos / Referências
- Documento gerado em: {{generatedAt_fmt}} ({{tz}})
- ID do incidente: {{incident.id}}
  {{#refs}}
- {{.}}
  {{/refs}}