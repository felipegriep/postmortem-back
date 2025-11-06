-- ================================================================
-- SEED DE DADOS (PT-BR) – POSTMORTEM (25 incidentes variados)
-- Regras de Score de Completude (soma = 100):
--  - Timeline ≥ 4 eventos.......................... 20 pts
--  - Impacto preenchido............................ 10 pts
--  - 5 Porquês completos........................... 25 pts
--  - Causa raiz + fatores contribuintes............ 15 pts
--  - ≥1 ação corretiva e ≥1 preventiva c/ owner+due 20 pts
--  - Comunicação registrada (COMM)................. 10 pts
-- Fechamento (exemplo futuro): exigir score ≥ 70%.
-- Observação: este seed **garante** que incidentes CLOSED têm score ≥ 70.
-- ================================================================

USE POSTMORTEM;

-- ------------------------------------------------
-- 1) LIMPEZA DE BASE (mantém o schema)
-- ------------------------------------------------
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE POSTMORTEM_DOC;
TRUNCATE TABLE ACTION_ITEM;
TRUNCATE TABLE ROOT_CAUSE;
TRUNCATE TABLE INCIDENT_EVENT;
TRUNCATE TABLE INCIDENT;
TRUNCATE TABLE USER_ACCOUNT;
SET FOREIGN_KEY_CHECKS = 1;

-- ------------------------------------------------
-- 2) USUÁRIOS (NÃO criar usuário "Felipe Griep")
-- ------------------------------------------------
INSERT INTO USER_ACCOUNT (ID_USER_ACCOUNT, PROVIDER, EXTERNAL_ID, EMAIL, NAME, PICTURE_URL, ACTIVE, LAST_LOGIN_AT)
VALUES
    (1,'LOCAL',NULL,'ana.silva@example.com','Ana Silva',NULL,TRUE,'2025-10-15 09:12:00'),
    (2,'GOOGLE','google|u2a91','bruno.costa@example.com','Bruno Costa',NULL,TRUE,'2025-10-16 08:44:00'),
    (3,'GITHUB','github|m09x3','carla.souza@example.com','Carla Souza',NULL,TRUE,'2025-10-14 11:30:00'),
    (4,'LOCAL',NULL,'daniel.rocha@example.com','Daniel Rocha',NULL,TRUE,'2025-10-13 17:05:00'),
    (5,'LOCAL',NULL,'elisa.pereira@example.com','Elisa Pereira',NULL,TRUE,'2025-10-12 10:20:00'),
    (6,'GOOGLE','google|k77q2','fabio.lima@example.com','Fábio Lima',NULL,TRUE,'2025-10-11 15:10:00'),
    (7,'SAML','saml|44321','gabriel.almeida@example.com','Gabriel Almeida',NULL,TRUE,'2025-10-10 12:00:00'),
    (8,'LOCAL',NULL,'helena.martins@example.com','Helena Martins',NULL,TRUE,'2025-10-09 09:00:00'),
    (9,'GITHUB','github|x1y2z','igor.santos@example.com','Igor Santos',NULL,TRUE,'2025-10-18 19:40:00'),
    (10,'LOCAL',NULL,'julia.carvalho@example.com','Júlia Carvalho',NULL,TRUE,'2025-10-17 21:02:00'),
    (11,'LOCAL',NULL,'kaio.nunes@example.com','Kaio Nunes',NULL,TRUE,'2025-10-05 08:00:00'),
    (12,'LOCAL',NULL,'larissa.araujo@example.com','Larissa Araújo',NULL,TRUE,'2025-10-20 07:55:00'),
    (13,'LOCAL',NULL,'marcos.oliveira@example.com','Marcos Oliveira',NULL,TRUE,'2025-10-21 14:18:00'),
    (14,'GOOGLE','google|n8p6k','natalia.ramos@example.com','Natália Ramos',NULL,TRUE,'2025-10-08 13:33:00'),
    (15,'LOCAL',NULL,'otavio.moreira@example.com','Otávio Moreira',NULL,TRUE,'2025-10-22 10:10:00'),
    (16,'LOCAL',NULL,'pedro.barbosa@example.com','Pedro Barbosa',NULL,TRUE,'2025-10-03 16:44:00'),
    (17,'LOCAL',NULL,'queila.ferreira@example.com','Queila Ferreira',NULL,TRUE,'2025-10-28 09:59:00'),
    (18,'LOCAL',NULL,'rafael.teixeira@example.com','Rafael Teixeira',NULL,TRUE,'2025-10-29 18:22:00'),
    (19,'LOCAL',NULL,'sabrina.duarte@example.com','Sabrina Duarte',NULL,TRUE,'2025-10-25 07:45:00'),
    (20,'LOCAL',NULL,'tomas.monteiro@example.com','Tomás Monteiro',NULL,TRUE,'2025-10-26 22:10:00');

-- ------------------------------------------------
-- 3) INCIDENTES (25 casos)
--    Notas:
--      - CLOSED: score >= 70
--      - Variar SEVERITY/STATUS/IMPACT/Timeline/COMM/RootCause/Ações
-- ------------------------------------------------
INSERT INTO INCIDENT (ID_INCIDENT, TITLE, SERVICE, SEVERITY, STATUS, STARTED_AT, ENDED_AT, IMPACT_SHORT, ID_USER_ACCOUNT_REPORTER)
VALUES
    (1,'Indisponibilidade Payments','Payments API','SEV-1','CLOSED','2025-09-12 13:05:00','2025-09-12 14:20:00','Checkout indisponível ~1h15; ~23k tentativas falhas.',1),
    (2,'Intermitência de Login','Auth Service','SEV-2','CLOSED','2025-09-20 09:10:00','2025-09-20 10:05:00','Login com ~8% falhas por 55min.',2),
    (3,'Crash App iOS','Mobile App','SEV-2','CLOSED','2025-10-01 07:40:00','2025-10-01 09:00:00',NULL,3), -- sem impacto
    (4,'Atraso no ETL Diário','Data Pipeline','SEV-3','CLOSED','2025-09-28 02:00:00','2025-09-28 05:30:00','KPIs matinais defasados por atraso de carga.',4),
    (5,'Alerta de Disco Kafka','Messaging/Kafka','SEV-3','IN_ANALYSIS','2025-10-11 16:30:00',NULL,'Disco > 90% no broker-2.',5),
    (6,'Cobrança em Duplicidade','Billing','SEV-2','CLOSED','2025-10-05 12:02:00','2025-10-05 13:15:00','214 transações impactadas por débito duplo.',6),
    (7,'Backlog de Notificações','Notification','SEV-4','OPEN','2025-10-18 19:20:00',NULL,'Atraso ~15min em push.',7),
    (8,'Exportação Lenta de Relatórios','Reporting','SEV-4','CLOSED','2025-09-22 10:00:00','2025-09-22 12:10:00','Export acima de 5min.',8),
    (9,'Queda de Relevância na Busca','Search','SEV-3','CLOSED','2025-10-07 08:30:00','2025-10-07 12:00:00','CTR caiu 12% no top-5.',9),
    (10,'Picos 502 no Gateway','API Gateway','SEV-1','CLOSED','2025-09-16 21:00:00','2025-09-16 21:40:00','Erros 502 ~7% por 40min.',10),
    (11,'Falha de Sincronismo de Estoque','Inventory','SEV-2','CLOSED','2025-10-03 04:10:00','2025-10-03 06:25:00','3 lojas desatualizadas por 2h15.',11),
    (12,'Timeout no Checkout','Checkout','SEV-1','CLOSED','2025-10-12 15:22:00','2025-10-12 16:00:00','Taxa de abandono +18%.',12),
    (13,'Erro ao Salvar Perfil','User Profile','SEV-3','OPEN','2025-10-27 14:15:00',NULL,NULL,13), -- sem impacto
    (14,'Falsos Positivos no Antifraude','Fraud Detector','SEV-2','CLOSED','2025-09-30 11:30:00','2025-09-30 13:10:00',NULL,14), -- sem impacto para score 80
    (15,'Dashboard com Dados Antigos','Analytics','SEV-4','CLOSED','2025-09-25 06:00:00','2025-09-25 08:15:00','Dados do dia anterior exibidos.',15),
    (16,'Falha em Job Noturno','ETL Job','SEV-3','CLOSED','2025-10-09 02:05:00','2025-10-09 03:40:00','Carga interrompida por erro de schema.',16),
    (17,'Instabilidade do Agente SRE','SRE Tooling','SEV-4','IN_ANALYSIS','2025-10-14 23:20:00',NULL,'Agente 1.9.4 crasha em Ubuntu 24.04.',17),
    (18,'Build do DevPortal Quebrado','DevPortal','SEV-4','CLOSED','2025-10-04 09:00:00','2025-10-04 10:05:00',NULL,18), -- sem impacto
    (19,'CI travado em Aprovação','CI/CD','SEV-3','CLOSED','2025-10-06 13:50:00','2025-10-06 14:45:00','Aprovação não processada no stage.',19),
    (20,'Failover no Cluster de DB','DB Cluster','SEV-1','CLOSED','2025-09-18 01:10:00','2025-09-18 02:00:00','12min read-only até estabilizar.',20),
    (21,'Tempestade de Evicções no Cache','Cache','SEV-2','CLOSED','2025-10-02 20:30:00','2025-10-02 21:25:00','TTL errado causou misses.',1),
    (22,'Alta Latência no Resizer','Image Resizer','SEV-3','OPEN','2025-10-24 18:00:00',NULL,'p95 1.8s ao redimensionar 1024px.',2),
    (23,'Excesso de Retries em Webhook','Webhook','SEV-3','CLOSED','2025-09-27 07:15:00','2025-09-27 09:00:00','Parceiro recebeu 5+ retries/evento.',3),
    (24,'Erro no Login do Admin','Admin Console','SEV-4','IN_ANALYSIS','2025-10-19 10:40:00',NULL,'Erro 400 após reset de senha.',4),
    (25,'Falha no OCR do KYC','KYC Service','SEV-2','CLOSED','2025-10-08 16:20:00','2025-10-08 17:35:00','OCR falha em CNH nova (~18%).',5);

-- ------------------------------------------------
-- 4) TIMELINE (garantir contagem de eventos e COMM onde for preciso)
-- ------------------------------------------------
-- #1 (score 100: >=4 + COMM)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (1,'2025-09-12 13:05:00','ALERT','PagerDuty alerta 5xx.',6),
                                                                                                 (1,'2025-09-12 13:10:00','DIAGNOSIS','Pool de conexões esgotado.',7),
                                                                                                 (1,'2025-09-12 13:18:00','COMMUNICATION','StatusPage: incidente identificado.',8),
                                                                                                 (1,'2025-09-12 13:25:00','MITIGATION','Tuning de pool + breaker.',6),
                                                                                                 (1,'2025-09-12 14:15:00','FIX','Rollback de driver e estabilização.',7);

-- #2 (score 90: sem COMM)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (2,'2025-09-20 09:10:00','ALERT','Queda de sucesso /login.',9),
                                                                                                 (2,'2025-09-20 09:20:00','DIAGNOSIS','Rate limit agressivo IdP.',10),
                                                                                                 (2,'2025-09-20 09:30:00','MITIGATION','Ajuste de limites.',9),
                                                                                                 (2,'2025-09-20 10:00:00','FIX','Burst allowance revisado.',10);

-- #3 (score 80: sem Impacto + sem COMM)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (3,'2025-10-01 07:40:00','ALERT','Crash iOS 18.1.',3),
                                                                                                 (3,'2025-10-01 07:55:00','DIAGNOSIS','SDK analytics incompatível.',12),
                                                                                                 (3,'2025-10-01 08:20:00','MITIGATION','Flag desativa analytics.',3),
                                                                                                 (3,'2025-10-01 08:50:00','FIX','Patch do SDK aplicado.',12);

-- #4 (score 70: sem ações + sem COMM)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (4,'2025-09-28 02:00:00','ALERT','SLA de ETL violado.',4),
                                                                                                 (4,'2025-09-28 02:40:00','DIAGNOSIS','Contenção por VACUUM.',15),
                                                                                                 (4,'2025-09-28 03:10:00','MITIGATION','Throttle em consultas.',16),
                                                                                                 (4,'2025-09-28 05:20:00','FIX','Reprogramar VACUUM.',15);

-- #5 (score 60: timeline<4 (3 eventos), COMM presente, sem ações)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (5,'2025-10-11 16:30:00','ALERT','Disco > 90% no broker-2.',5),
                                                                                                 (5,'2025-10-11 17:00:00','DIAGNOSIS','Logs crescendo acima do esperado.',6),
                                                                                                 (5,'2025-10-11 17:10:00','COMMUNICATION','Aviso interno: mitigação em curso.',7);

-- #6 (100)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (6,'2025-10-05 12:02:00','ALERT','Reclamações de débito duplo.',14),
                                                                                                 (6,'2025-10-05 12:12:00','DIAGNOSIS','Idempotência quebrada.',13),
                                                                                                 (6,'2025-10-05 12:20:00','COMMUNICATION','StatusPage: financeiro em apuração.',14),
                                                                                                 (6,'2025-10-05 12:40:00','MITIGATION','Pausa no reprocessamento.',13),
                                                                                                 (6,'2025-10-05 13:10:00','FIX','Chave idempotente corrigida.',14);

-- #7 (90: sem COMM, mas >=4 eventos; ainda OPEN)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (7,'2025-10-18 19:20:00','ALERT','Backlog > 50k.',7),
                                                                                                 (7,'2025-10-18 19:35:00','DIAGNOSIS','Quota limitada no provedor.',8),
                                                                                                 (7,'2025-10-18 20:00:00','MITIGATION','Rebalancear produtores.',7),
                                                                                                 (7,'2025-10-18 21:10:00','FIX','Scaling adicional aplicado.',8);

-- #8 (100)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (8,'2025-09-22 10:00:00','ALERT','p95 export > 5min.',8),
                                                                                                 (8,'2025-09-22 10:20:00','DIAGNOSIS','JOIN sem índice.',9),
                                                                                                 (8,'2025-09-22 10:30:00','COMMUNICATION','Aviso interno sobre lentidão.',10),
                                                                                                 (8,'2025-09-22 11:10:00','MITIGATION','Particionamento e LIMIT progressivo.',8),
                                                                                                 (8,'2025-09-22 12:00:00','FIX','Índice composto criado.',9);

-- #9 (75: 5 porquês INCOMPLETOS; COMM presente)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (9,'2025-10-07 08:30:00','ALERT','Queda em CTR.',9),
                                                                                                 (9,'2025-10-07 09:05:00','DIAGNOSIS','Mudança recente no ranking.',10),
                                                                                                 (9,'2025-10-07 09:20:00','COMMUNICATION','Aviso interno a marketing.',11),
                                                                                                 (9,'2025-10-07 11:20:00','FIX','Ajuste de pesos aplicado.',10);

-- #10 (90: sem COMM)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (10,'2025-09-16 21:00:00','ALERT','Picos 502.',10),
                                                                                                 (10,'2025-09-16 21:10:00','DIAGNOSIS','CPU throttling.',12),
                                                                                                 (10,'2025-09-16 21:30:00','MITIGATION','Scale-out + pin de região.',12),
                                                                                                 (10,'2025-09-16 21:40:00','FIX','Warm-up de instâncias.',13);

-- #11 (100)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (11,'2025-10-03 04:10:00','ALERT','Falha de sync ERP.',11),
                                                                                                 (11,'2025-10-03 04:40:00','DIAGNOSIS','Refresh token ausente.',12),
                                                                                                 (11,'2025-10-03 05:10:00','COMMUNICATION','Aviso a operações.',13),
                                                                                                 (11,'2025-10-03 06:10:00','FIX','Reprocessamento concluído.',11);

-- #12 (100)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (12,'2025-10-12 15:22:00','ALERT','Timeout em /confirm.',12),
                                                                                                 (12,'2025-10-12 15:30:00','DIAGNOSIS','Leak de conexões.',14),
                                                                                                 (12,'2025-10-12 15:40:00','COMMUNICATION','Comunicado público.',15),
                                                                                                 (12,'2025-10-12 15:50:00','MITIGATION','Reinício controlado.',12),
                                                                                                 (12,'2025-10-12 16:00:00','FIX','Patch aplicado.',14);

-- #13 (50: timeline<4, sem impacto, sem COMM, sem ações)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (13,'2025-10-27 14:15:00','ALERT','Erro 409 ao salvar.',13),
                                                                                                 (13,'2025-10-27 14:30:00','DIAGNOSIS','Conflito de versão.',14);

-- #14 (80: sem Impacto + sem COMM)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (14,'2025-09-30 11:30:00','ALERT','Aumento de falsos positivos.',14),
                                                                                                 (14,'2025-09-30 11:45:00','DIAGNOSIS','Threshold sensível a novo padrão.',16),
                                                                                                 (14,'2025-09-30 12:20:00','MITIGATION','Ajuste temporário.',14),
                                                                                                 (14,'2025-09-30 13:00:00','FIX','Re-treino do modelo.',16);

-- #15 (70: sem ações + sem COMM)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (15,'2025-09-25 06:00:00','ALERT','Dados desatualizados.',15),
                                                                                                 (15,'2025-09-25 06:40:00','DIAGNOSIS','Cache não invalidado.',18),
                                                                                                 (15,'2025-09-25 07:20:00','MITIGATION','Purge no CDN.',15),
                                                                                                 (15,'2025-09-25 08:10:00','FIX','Invalidação pós-ETL.',18);

-- #16 (90: sem COMM)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (16,'2025-10-09 02:05:00','ALERT','Falha no step de load.',16),
                                                                                                 (16,'2025-10-09 02:20:00','DIAGNOSIS','Campo novo sem default.',17),
                                                                                                 (16,'2025-10-09 03:00:00','MITIGATION','Hotfix no schema.',16),
                                                                                                 (16,'2025-10-09 03:30:00','FIX','Migration definitiva.',17);

-- #17 (40: >=4 eventos incluindo COMM, mas sem whys/ações/causa)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (17,'2025-10-14 23:20:00','ALERT','Crashes do agente.',17),
                                                                                                 (17,'2025-10-14 23:40:00','DIAGNOSIS','Kernel 6.8 com regressão.',18),
                                                                                                 (17,'2025-10-15 00:05:00','COMMUNICATION','Aviso interno: investigando.',19),
                                                                                                 (17,'2025-10-15 01:00:00','MITIGATION','Rollback parcial.',17);

-- #18 (80: sem Impacto + sem COMM)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (18,'2025-10-04 09:00:00','ALERT','Build falha no lint.',18),
                                                                                                 (18,'2025-10-04 09:20:00','DIAGNOSIS','Regra nova incompatível.',19),
                                                                                                 (18,'2025-10-04 09:40:00','MITIGATION','Pin versão anterior.',18),
                                                                                                 (18,'2025-10-04 10:00:00','FIX','Ajuste no config.',19);

-- #19 (90: sem COMM)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (19,'2025-10-06 13:50:00','ALERT','Stage aguardando aprovação.',19),
                                                                                                 (19,'2025-10-06 14:10:00','DIAGNOSIS','Webhook não recebido.',20),
                                                                                                 (19,'2025-10-06 14:30:00','MITIGATION','Reenvio manual.',19),
                                                                                                 (19,'2025-10-06 14:45:00','FIX','Correção do mapeamento.',20);

-- #20 (100)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (20,'2025-09-18 01:10:00','ALERT','Primário indisponível.',20),
                                                                                                 (20,'2025-09-18 01:15:00','DIAGNOSIS','Falha de host na AZ-1.',1),
                                                                                                 (20,'2025-09-18 01:25:00','COMMUNICATION','Aviso público: failover.',2),
                                                                                                 (20,'2025-09-18 01:40:00','MITIGATION','Rota para réplica.',20),
                                                                                                 (20,'2025-09-18 02:00:00','FIX','Cluster estabilizado.',1);

-- #21 (90: sem COMM)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (21,'2025-10-02 20:30:00','ALERT','Miss rate subiu 40%.',1),
                                                                                                 (21,'2025-10-02 20:45:00','DIAGNOSIS','TTL 5s por engano.',2),
                                                                                                 (21,'2025-10-02 21:00:00','MITIGATION','TTL para 300s.',1),
                                                                                                 (21,'2025-10-02 21:20:00','FIX','Deploy com config correta.',2);

-- #22 (60: timeline<4 (3 eventos), COMM presente, sem ações)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (22,'2025-10-24 18:00:00','ALERT','p95 1.8s.',2),
                                                                                                 (22,'2025-10-24 18:30:00','DIAGNOSIS','Auto-scaling subdimensionado.',3),
                                                                                                 (22,'2025-10-24 18:40:00','COMMUNICATION','Aviso interno a squads.',4);

-- #23 (75: COMM presente, whys incompletos)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (23,'2025-09-27 07:15:00','ALERT','Retries excessivos.',3),
                                                                                                 (23,'2025-09-27 07:40:00','DIAGNOSIS','Parceiro retorna 429.',4),
                                                                                                 (23,'2025-09-27 08:00:00','COMMUNICATION','Comunicado ao parceiro.',5),
                                                                                                 (23,'2025-09-27 08:40:00','FIX','Backoff exponencial.',3);

-- #24 (50: timeline<4, COMM ausente, sem ações)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (24,'2025-10-19 10:40:00','ALERT','Erro 400 após reset.',4),
                                                                                                 (24,'2025-10-19 11:10:00','DIAGNOSIS','Payload incompatível.',5);

-- #25 (90: sem COMM)
INSERT INTO INCIDENT_EVENT (ID_INCIDENT, EVENT_AT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_ACTOR) VALUES
                                                                                                 (25,'2025-10-08 16:20:00','ALERT','Falhas de OCR.',5),
                                                                                                 (25,'2025-10-08 16:35:00','DIAGNOSIS','Biblioteca desatualizada.',6),
                                                                                                 (25,'2025-10-08 16:55:00','MITIGATION','Fallback para leitura manual.',5),
                                                                                                 (25,'2025-10-08 17:30:00','FIX','Upgrade de lib e template.',6);

-- ------------------------------------------------
-- 5) ROOT CAUSE (5 Porquês completos ou parciais)
--     Regra para 15 pts: ROOT_CAUSE_TEXT **e** CONTRIBUTING_FACTORS não nulos
-- ------------------------------------------------
-- 1 (completo)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, WHY3, WHY4, WHY5, ROOT_CAUSE_TEXT, CONTRIBUTING_FACTORS, LESSONS_LEARNED) VALUES
    (1,'Pool esgotou.','Fuga de conexões.','Driver novo não retorna em erro.','Upgrade sem teste de falha.','Checklist incompleto.','Upgrade de driver sem testes de falha esgotou o pool.','Checklist incompleto; observabilidade limitada.','Adicionar testes de caos e métricas.');
-- 2 (completo)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, WHY3, WHY4, WHY5, ROOT_CAUSE_TEXT, CONTRIBUTING_FACTORS, LESSONS_LEARNED) VALUES
    (2,'Rate limit no IdP.','Mudança unilateral de burst.','Cliente sem cache.','Requisito não capturado.','Falta de fallback local.','Limite do IdP sem cache local causou falhas.','Dependência sem SLO; ausência de cache.','Definir SLOs e cache.');
-- 3 (completo)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, WHY3, WHY4, WHY5, ROOT_CAUSE_TEXT, CONTRIBUTING_FACTORS, LESSONS_LEARNED) VALUES
    (3,'SDK incompatível.','API alterada.','CI sem matriz iOS 18.1.','Matriz desatualizada.','Sem owner mobile.','Upgrade de iOS sem matriz de testes quebrou o app.','Cobertura de CI limitada; comunicação fraca.','Atualizar matriz e smoke tests.');
-- 4 (completo)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, WHY3, WHY4, WHY5, ROOT_CAUSE_TEXT, CONTRIBUTING_FACTORS, LESSONS_LEARNED) VALUES
    (4,'Contenção no warehouse.','VACUUM em pico.','Janela inadequada.','Cron não revisado.','Falta de revisão de DBA.','VACUUM em horário impróprio gerou atraso.','Governança fraca de cron; falta de testes.','Revisar cron via change management.');
-- 5 (completo)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, WHY3, WHY4, WHY5, ROOT_CAUSE_TEXT, CONTRIBUTING_FACTORS, LESSONS_LEARNED) VALUES
    (5,'Logs excessivos.','Nível de debug habilitado.','Rotação ineficiente.','Alertas não calibrados.','Monitoria pouco granular.','Logs em excesso consumiram disco.','Alertas mal calibrados; retenção inadequada.','Ajustar retenção e rotação.');
-- 6 (completo)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, WHY3, WHY4, WHY5, ROOT_CAUSE_TEXT, CONTRIBUTING_FACTORS, LESSONS_LEARNED) VALUES
    (6,'Reprocessamento duplicou.','Chave idempotente incorreta.','Falha de normalização.','Teste de idempotência ausente.','Pressa no hotfix anterior.','Chave idempotente mal definida permitiu duplicidade.','Review apressado; monitoria de duplicidade ausente.','Criar testes e guard rails.');
-- 7 (completo)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, WHY3, WHY4, WHY5, ROOT_CAUSE_TEXT, CONTRIBUTING_FACTORS, LESSONS_LEARNED) VALUES
    (7,'Quota insuficiente.','Uso acima do contratado.','Ausência de autoscaling por horário.','Falta de acordo com provedor.','Planejamento de capacidade deficiente.','Quota baixa gerou backlog.','Planejamento de capacidade fraco.','Negociar quota e ajustar autoscaling.');
-- 8 (completo)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, WHY3, WHY4, WHY5, ROOT_CAUSE_TEXT, CONTRIBUTING_FACTORS, LESSONS_LEARNED) VALUES
    (8,'JOIN não indexado.','Crescimento sem revisão.','Falta de ownership do relatório.','Shadow IT criou consulta.','Ausência de catálogo.','Consulta sem índice causou lentidão.','Catálogo ausente; ownership indefinido.','Criar catálogo e revisar relatórios.');
-- 9 (INCOMPLETO)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, ROOT_CAUSE_TEXT, CONTRIBUTING_FACTORS, LESSONS_LEARNED) VALUES
    (9,'Mudança de ranking afetou CTR.','Pesos desbalanceados.','Ajuste de pesos causou queda de CTR.','Governança fraca de experiments.','Implantar avaliação offline.');
-- 10 (completo)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, WHY3, WHY4, WHY5, ROOT_CAUSE_TEXT, CONTRIBUTING_FACTORS, LESSONS_LEARNED) VALUES
    (10,'Instância saturada.','Throttling de CPU.','Burst sem warm-up.','Autoscaling agressivo.','Config padrão não ajustada.','Escalonamento agressivo gerou 502.','Healthcheck curto; pre-warm ausente.','Adicionar warm-up.');
-- 11 (completo)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, WHY3, WHY4, WHY5, ROOT_CAUSE_TEXT, CONTRIBUTING_FACTORS, LESSONS_LEARNED) VALUES
    (11,'Token expirou.','Refresh ausente.','SDK legado.','Dívida técnica.','Priorizações conflitantes.','SDK legado sem refresh quebrou sync.','Contrato frágil; dívida técnica.','Substituir SDK.');
-- 12 (completo)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, WHY3, WHY4, WHY5, ROOT_CAUSE_TEXT, CONTRIBUTING_FACTORS, LESSONS_LEARNED) VALUES
    (12,'Leak de conexões.','Pool não fechava em exceção.','Código sem finally.','Testes de carga insuficientes.','Pressão por release.','Falha de fechamento gerou timeout.','Observabilidade insuficiente; pressa.','Adicionar try-with-resources.');
-- 13 (INCOMPLETO)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1) VALUES (13,'Conflito de versão no perfil.');
-- 14 (completo)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, WHY3, WHY4, WHY5, ROOT_CAUSE_TEXT, CONTRIBUTING_FACTORS, LESSONS_LEARNED) VALUES
    (14,'Threshold desajustado.','Dataset desatualizado.','Coleta incompleta.','Falta integração com parceiro.','Escopo reduzido.','Threshold alto frente a novos padrões.','Pipeline incompleto; pouca validação.','Estabelecer MLOps.');
-- 15 (completo)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, WHY3, WHY4, WHY5, ROOT_CAUSE_TEXT, CONTRIBUTING_FACTORS, LESSONS_LEARNED) VALUES
    (15,'Cache não invalidou.','Sem trigger pós-ETL.','Requisito omitido.','Falha entre times.','RACI indefinido.','Sem invalidação pós-ETL causou dados antigos.','RACI indefinido; docs incompletas.','Criar trigger e alinhar RACI.');
-- 16 (completo)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, WHY3, WHY4, WHY5, ROOT_CAUSE_TEXT, CONTRIBUTING_FACTORS, LESSONS_LEARNED) VALUES
    (16,'Campo novo sem default.','Change apressada.','Janela curta.','Pressão de negócio.','Sem change advisory.','Migration sem default quebrou job.','Processo frágil; testes insuficientes.','Impor default e rollback.');
-- 17 (INCOMPLETO)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2) VALUES (17,'Regressão no kernel.','Agente incompatível.');
-- 18 (completo)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, WHY3, WHY4, WHY5, ROOT_CAUSE_TEXT, CONTRIBUTING_FACTORS, LESSONS_LEARNED) VALUES
    (18,'Regras novas do lint.','Código legado incompatível.','Sem migração.','Sprint cheia.','Sem deprecação gradual.','Política de lint mudou sem migração.','Governança de tooling falha.','Introduzir períodos de compatibilidade.');
-- 19 (completo)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, WHY3, WHY4, WHY5, ROOT_CAUSE_TEXT, CONTRIBUTING_FACTORS, LESSONS_LEARNED) VALUES
    (19,'Aprovação não processada.','Webhook bloqueado.','WAF com regra genérica.','Sem whitelisting.','Falta de monitoria.','WAF bloqueou webhook de aprovação.','Regras amplas; pouca observabilidade.','Adicionar allowlist e métricas.');
-- 20 (completo)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, WHY3, WHY4, WHY5, ROOT_CAUSE_TEXT, CONTRIBUTING_FACTORS, LESSONS_LEARNED) VALUES
    (20,'Host falhou.','Falha de hardware.','Detecção lenta.','Timeout alto.','Config conservadora.','Timeout alto prolongou failover.','Config conservadora; DR raro.','Reduzir timeouts e treinar DR.');
-- 21 (completo)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, WHY3, WHY4, WHY5, ROOT_CAUSE_TEXT, CONTRIBUTING_FACTORS, LESSONS_LEARNED) VALUES
    (21,'TTL muito baixo.','Config errada.','Template compartilhado.','Reuso sem revisão.','Sem owner de config.','TTL incorreto causou storm de evictions.','Template sem owner.','Definir ownership e revisão.');
-- 22 (completo)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, WHY3, WHY4, WHY5, ROOT_CAUSE_TEXT, CONTRIBUTING_FACTORS, LESSONS_LEARNED) VALUES
    (22,'Auto-scaling baixo.','Perfil de tráfego variou.','Sem buffers mínimos.','Alarme tardio.','Observabilidade insuficiente.','Subdimensionamento elevou latência.','Alarmes tardios; pre-scale ausente.','Configurar min replicas.');
-- 23 (INCOMPLETO – whys parciais)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, ROOT_CAUSE_TEXT, LESSONS_LEARNED) VALUES
    (23,'Parceiro retornou 429.','Limites não comunicados previamente.','Falta de backoff causou saturação.','Implementar backoff e acordos de limite.');
-- 24 (INCOMPLETO)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1) VALUES (24,'Payload do reset incompatível.');
-- 25 (completo)
INSERT INTO ROOT_CAUSE (ID_INCIDENT, WHY1, WHY2, WHY3, WHY4, WHY5, ROOT_CAUSE_TEXT, CONTRIBUTING_FACTORS, LESSONS_LEARNED) VALUES
    (25,'Layout novo da CNH.','Biblioteca desatualizada.','Sem monitor de documentos.','Falta parceria com órgão.','Escopo limitado do KYC.','OCR sem suporte ao novo layout.','Integração fraca com fontes oficiais.','Monitorar mudanças e atualizar modelos.');

-- ------------------------------------------------
-- 6) AÇÕES (corretivas/preventivas) – obedecer regra de owner+due
-- ------------------------------------------------
-- #1 (ganha 20 pts)
INSERT INTO ACTION_ITEM (ID_INCIDENT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_OWNER, DUE_DATE, STATUS, EVIDENCE_LINK, COMPLETED_AT) VALUES
                                                                                                                                   (1,'CORRECTIVE','Testes de caos no pool.',6,'2025-09-20 18:00:00','DONE','https://ex/pr/1001','2025-09-18 10:00:00'),
                                                                                                                                   (1,'PREVENTIVE','Checklist de upgrade de drivers.',7,'2025-09-25 18:00:00','DONE','https://ex/doc/checklist','2025-09-22 09:30:00');
-- #2 (ganha 20 pts)
INSERT INTO ACTION_ITEM (ID_INCIDENT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_OWNER, DUE_DATE, STATUS, EVIDENCE_LINK, COMPLETED_AT) VALUES
                                                                                                                                   (2,'CORRECTIVE','Cache local de tokens.',9,'2025-09-25 12:00:00','DONE','https://ex/pr/2001','2025-09-24 16:00:00'),
                                                                                                                                   (2,'PREVENTIVE','Negociar SLO com IdP.',10,'2025-09-28 12:00:00','DONE','https://ex/ticket/IDP-SLO','2025-09-27 11:00:00');
-- #3 (ganha 20 pts)
INSERT INTO ACTION_ITEM (ID_INCIDENT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_OWNER, DUE_DATE, STATUS, EVIDENCE_LINK, COMPLETED_AT) VALUES
                                                                                                                                   (3,'CORRECTIVE','Remover SDK problemático.',12,'2025-10-03 18:00:00','DONE','https://ex/pr/3001','2025-10-02 14:00:00'),
                                                                                                                                   (3,'PREVENTIVE','Ampliar matriz CI para iOS 18.1.',3,'2025-10-05 18:00:00','DONE','https://ex/ci/matrix','2025-10-04 10:30:00');
-- #4 (NENHUMA ação para manter -20)
-- #5 (apenas 1 ação corretiva, não ganha 20)
INSERT INTO ACTION_ITEM (ID_INCIDENT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_OWNER, DUE_DATE, STATUS) VALUES
    (5,'CORRECTIVE','Rotacionar logs e arquivar.',5,'2025-10-12 12:00:00','DOING');
-- #6 (ganha 20 pts)
INSERT INTO ACTION_ITEM (ID_INCIDENT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_OWNER, DUE_DATE, STATUS, EVIDENCE_LINK, COMPLETED_AT) VALUES
                                                                                                                                   (6,'CORRECTIVE','Corrigir idempotência + reprocessar.',14,'2025-10-07 12:00:00','DONE','https://ex/pr/6001','2025-10-06 09:00:00'),
                                                                                                                                   (6,'PREVENTIVE','Testes automáticos de duplicidade.',13,'2025-10-10 18:00:00','DONE','https://ex/pr/6002','2025-10-09 16:45:00');
-- #7 (2 ações → ganha 20, ainda OPEN)
INSERT INTO ACTION_ITEM (ID_INCIDENT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_OWNER, DUE_DATE, STATUS, EVIDENCE_LINK, COMPLETED_AT) VALUES
                                                                                                                                   (7,'CORRECTIVE','Reprocessar backlog com prioridade.',7,'2025-10-21 18:00:00','DONE','https://ex/run/7001','2025-10-20 09:00:00'),
                                                                                                                                   (7,'PREVENTIVE','Aumentar quotas com provedor.',8,'2025-10-22 09:00:00','DONE','https://ex/ticket/push-quota','2025-10-21 15:00:00');
-- #8 (ganha 20 pts)
INSERT INTO ACTION_ITEM (ID_INCIDENT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_OWNER, DUE_DATE, STATUS, EVIDENCE_LINK, COMPLETED_AT) VALUES
                                                                                                                                   (8,'CORRECTIVE','Criar índice composto.',9,'2025-09-24 18:00:00','DONE','https://ex/pr/8001','2025-09-23 17:00:00'),
                                                                                                                                   (8,'PREVENTIVE','Catálogo de relatórios críticos.',8,'2025-09-30 18:00:00','DONE','https://ex/doc/catalogo','2025-09-29 11:00:00');
-- #9 (ganha 20)
INSERT INTO ACTION_ITEM (ID_INCIDENT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_OWNER, DUE_DATE, STATUS, EVIDENCE_LINK, COMPLETED_AT) VALUES
                                                                                                                                   (9,'CORRECTIVE','Ajustar pesos do ranking.',10,'2025-10-09 18:00:00','DONE','https://ex/pr/9001','2025-10-08 13:00:00'),
                                                                                                                                   (9,'PREVENTIVE','Avaliação offline periódica.',11,'2025-10-12 18:00:00','DONE','https://ex/doc/exp-eval','2025-10-11 16:00:00');
-- #10 (ganha 20)
INSERT INTO ACTION_ITEM (ID_INCIDENT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_OWNER, DUE_DATE, STATUS, EVIDENCE_LINK, COMPLETED_AT) VALUES
                                                                                                                                   (10,'CORRECTIVE','Warm-up nas políticas de autoscaling.',12,'2025-09-18 18:00:00','DONE','https://ex/pr/10001','2025-09-17 19:30:00'),
                                                                                                                                   (10,'PREVENTIVE','Pre-warming no gateway.',13,'2025-09-25 18:00:00','DONE','https://ex/doc/prewarm','2025-09-22 10:15:00');
-- #11 (ganha 20)
INSERT INTO ACTION_ITEM (ID_INCIDENT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_OWNER, DUE_DATE, STATUS, EVIDENCE_LINK, COMPLETED_AT) VALUES
                                                                                                                                   (11,'CORRECTIVE','Atualizar SDK do ERP.',11,'2025-10-05 18:00:00','DONE','https://ex/pr/11001','2025-10-04 15:00:00'),
                                                                                                                                   (11,'PREVENTIVE','Monitor de expiração de token.',12,'2025-10-08 18:00:00','DONE','https://ex/pr/11002','2025-10-07 17:45:00');
-- #12 (ganha 20)
INSERT INTO ACTION_ITEM (ID_INCIDENT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_OWNER, DUE_DATE, STATUS, EVIDENCE_LINK, COMPLETED_AT) VALUES
                                                                                                                                   (12,'CORRECTIVE','Fechamento correto de conexões.',12,'2025-10-14 18:00:00','DONE','https://ex/pr/12001','2025-10-13 16:10:00'),
                                                                                                                                   (12,'PREVENTIVE','Aumentar cobertura de carga.',14,'2025-10-20 18:00:00','DONE','https://ex/doc/carga','2025-10-19 09:30:00');
-- #13 (nenhuma ação)
-- #14 (ganha 20)
INSERT INTO ACTION_ITEM (ID_INCIDENT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_OWNER, DUE_DATE, STATUS, EVIDENCE_LINK, COMPLETED_AT) VALUES
                                                                                                                                   (14,'CORRECTIVE','Re-treinar com novos dados.',16,'2025-10-03 18:00:00','DONE','https://ex/pr/14001','2025-10-02 13:00:00'),
                                                                                                                                   (14,'PREVENTIVE','Implantar MLOps.',14,'2025-10-15 18:00:00','DONE','https://ex/doc/mlops','2025-10-14 17:00:00');
-- #15 (sem ações)
-- #16 (ganha 20)
INSERT INTO ACTION_ITEM (ID_INCIDENT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_OWNER, DUE_DATE, STATUS, EVIDENCE_LINK, COMPLETED_AT) VALUES
                                                                                                                                   (16,'CORRECTIVE','Adicionar default ao campo.',16,'2025-10-10 18:00:00','DONE','https://ex/pr/16001','2025-10-09 12:30:00'),
                                                                                                                                   (16,'PREVENTIVE','Checklist de DDLs com rollback.',17,'2025-10-18 18:00:00','DONE','https://ex/doc/ddl','2025-10-16 09:10:00');
-- #17 (nenhuma ação)
-- #18 (ganha 20)
INSERT INTO ACTION_ITEM (ID_INCIDENT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_OWNER, DUE_DATE, STATUS, EVIDENCE_LINK, COMPLETED_AT) VALUES
                                                                                                                                   (18,'CORRECTIVE','Ajustar config do ESLint.',18,'2025-10-06 18:00:00','DONE','https://ex/pr/18001','2025-10-05 11:40:00'),
                                                                                                                                   (18,'PREVENTIVE','Migração gradual de regras.',19,'2025-10-12 18:00:00','DONE','https://ex/doc/lint-mig','2025-10-11 09:00:00');
-- #19 (ganha 20)
INSERT INTO ACTION_ITEM (ID_INCIDENT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_OWNER, DUE_DATE, STATUS, EVIDENCE_LINK, COMPLETED_AT) VALUES
                                                                                                                                   (19,'CORRECTIVE','Whitelist do webhook no WAF.',19,'2025-10-08 18:00:00','DONE','https://ex/pr/19001','2025-10-07 13:10:00'),
                                                                                                                                   (19,'PREVENTIVE','Métricas de integrações externas.',20,'2025-10-15 18:00:00','DONE','https://ex/doc/metrics','2025-10-14 10:00:00');
-- #20 (ganha 20)
INSERT INTO ACTION_ITEM (ID_INCIDENT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_OWNER, DUE_DATE, STATUS, EVIDENCE_LINK, COMPLETED_AT) VALUES
                                                                                                                                   (20,'CORRECTIVE','Reduzir timeouts de detecção.',20,'2025-09-25 18:00:00','DONE','https://ex/pr/20001','2025-09-21 15:00:00'),
                                                                                                                                   (20,'PREVENTIVE','Treino trimestral de DR.',1,'2025-10-15 18:00:00','DONE','https://ex/doc/dr','2025-10-10 10:45:00');
-- #21 (ganha 20)
INSERT INTO ACTION_ITEM (ID_INCIDENT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_OWNER, DUE_DATE, STATUS, EVIDENCE_LINK, COMPLETED_AT) VALUES
                                                                                                                                   (21,'CORRECTIVE','Ajustar TTL e políticas.',1,'2025-10-04 18:00:00','DONE','https://ex/pr/21001','2025-10-03 12:00:00'),
                                                                                                                                   (21,'PREVENTIVE','Definir ownership de templates.',2,'2025-10-12 18:00:00','DONE','https://ex/doc/templates','2025-10-11 17:20:00');
-- #22 (nenhuma ação)
-- #23 (ganha 20)
INSERT INTO ACTION_ITEM (ID_INCIDENT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_OWNER, DUE_DATE, STATUS, EVIDENCE_LINK, COMPLETED_AT) VALUES
                                                                                                                                   (23,'CORRECTIVE','Backoff exponencial no produtor.',3,'2025-09-29 18:00:00','DONE','https://ex/pr/23001','2025-09-28 09:00:00'),
                                                                                                                                   (23,'PREVENTIVE','Acordo de limites com parceiro.',4,'2025-10-05 18:00:00','DONE','https://ex/doc/limits','2025-10-03 16:30:00');
-- #24 (nenhuma ação)
-- #25 (ganha 20)
INSERT INTO ACTION_ITEM (ID_INCIDENT, TYPE, DESCRIPTION, ID_USER_ACCOUNT_OWNER, DUE_DATE, STATUS, EVIDENCE_LINK, COMPLETED_AT) VALUES
                                                                                                                                   (25,'CORRECTIVE','Atualizar biblioteca de OCR.',5,'2025-10-10 18:00:00','DONE','https://ex/pr/25001','2025-10-09 14:00:00'),
                                                                                                                                   (25,'PREVENTIVE','Monitorar layouts oficiais.',6,'2025-10-18 18:00:00','DONE','https://ex/doc/layouts','2025-10-16 09:45:00');

-- ------------------------------------------------
-- 7) POSTMORTEM_DOC (scores CONSISTENTES com as regras)
--     Cálculo esperado por caso:
--       TL(20) IMP(10) WHY(25) CAUSA(15) AÇÃO(20) COMM(10)
-- ------------------------------------------------
INSERT INTO POSTMORTEM_DOC (ID_INCIDENT, MD_CONTENT, GENERATED_AT, COMPLETENESS_SCORE, VERSION) VALUES
                                                                                                    -- 1: 100 (CLOSED)
                                                                                                    (1,'# PM 1\nCompleto.','2025-09-13 10:00:00',100,1),
                                                                                                    -- 2: 90 (sem COMM) (CLOSED)
                                                                                                    (2,'# PM 2\nSem comunicação pública.','2025-09-20 12:00:00',90,1),
                                                                                                    -- 3: 80 (sem Impacto e sem COMM) (CLOSED)
                                                                                                    (3,'# PM 3\nSem impacto + sem comunicação.','2025-10-01 12:00:00',80,1),
                                                                                                    -- 4: 70 (sem ações e sem COMM) (CLOSED)
                                                                                                    (4,'# PM 4\nSem ações/COMM.','2025-09-28 12:00:00',70,1),
                                                                                                    -- 5: 60 (IN_ANALYSIS)
                                                                                                    (5,'# PM 5\nLinha do tempo curta; sem ações.','2025-10-11 18:00:00',60,1),
                                                                                                    -- 6: 100 (CLOSED)
                                                                                                    (6,'# PM 6\nCompleto.','2025-10-05 15:00:00',100,1),
                                                                                                    -- 7: 90 (sem COMM) (OPEN)
                                                                                                    (7,'# PM 7\nSem comunicação externa.','2025-10-18 22:00:00',90,1),
                                                                                                    -- 8: 100 (CLOSED)
                                                                                                    (8,'# PM 8\nCompleto.','2025-09-22 13:00:00',100,1),
                                                                                                    -- 9: 75 (whys incompletos) (CLOSED)
                                                                                                    (9,'# PM 9\n5 Porquês incompletos.','2025-10-07 13:00:00',75,1),
                                                                                                    -- 10: 90 (sem COMM) (CLOSED)
                                                                                                    (10,'# PM 10\nSem comunicação.','2025-09-16 22:00:00',90,1),
                                                                                                    -- 11: 100 (CLOSED)
                                                                                                    (11,'# PM 11\nCompleto.','2025-10-03 07:00:00',100,1),
                                                                                                    -- 12: 100 (CLOSED)
                                                                                                    (12,'# PM 12\nCompleto.','2025-10-12 17:00:00',100,1),
                                                                                                    -- 13: 0 (OPEN)
                                                                                                    (13,'# PM 13\nInicial, sem insumos.','2025-10-27 16:00:00',0,1),
                                                                                                    -- 14: 80 (sem Impacto e sem COMM) (CLOSED)
                                                                                                    (14,'# PM 14\nSem impacto/COMM.','2025-09-30 14:00:00',80,1),
                                                                                                    -- 15: 70 (sem ações e sem COMM) (CLOSED)
                                                                                                    (15,'# PM 15\nSem ações/COMM.','2025-09-25 09:00:00',70,1),
                                                                                                    -- 16: 90 (sem COMM) (CLOSED)
                                                                                                    (16,'# PM 16\nSem comunicação pública.','2025-10-09 04:10:00',90,1),
                                                                                                    -- 17: 40 (IN_ANALYSIS)
                                                                                                    (17,'# PM 17\nAguardando análise e ações.','2025-10-15 02:00:00',40,1),
                                                                                                    -- 18: 80 (sem Impacto e sem COMM) (CLOSED)
                                                                                                    (18,'# PM 18\nSem impacto/COMM.','2025-10-04 11:00:00',80,1),
                                                                                                    -- 19: 90 (sem COMM) (CLOSED)
                                                                                                    (19,'# PM 19\nSem comunicação externa.','2025-10-06 15:00:00',90,1),
                                                                                                    -- 20: 100 (CLOSED)
                                                                                                    (20,'# PM 20\nCompleto.','2025-09-18 03:00:00',100,1),
                                                                                                    -- 21: 90 (sem COMM) (CLOSED)
                                                                                                    (21,'# PM 21\nSem comunicação externa.','2025-10-02 22:00:00',90,1),
                                                                                                    -- 22: 60 (OPEN)
                                                                                                    (22,'# PM 22\nLinha do tempo curta; sem ações.','2025-10-24 20:00:00',60,1),
                                                                                                    -- 23: 75 (CLOSED)
                                                                                                    (23,'# PM 23\n5 Porquês incompletos.','2025-09-27 10:00:00',75,1),
                                                                                                    -- 24: 10 (IN_ANALYSIS)
                                                                                                    (24,'# PM 24\nEm análise inicial.','2025-10-19 12:00:00',10,1),
                                                                                                    -- 25: 90 (sem COMM) (CLOSED)
                                                                                                    (25,'# PM 25\nSem comunicação externa.','2025-10-08 18:00:00',90,1);

-- FIM DO SEED E2E (PT-BR)