# Como Limpar o Histórico de Commits

## Opção 1: Criar um Único Commit Limpo (Recomendado)

```bash
# 1. Criar um backup (segurança)
git branch backup-commits

# 2. Fazer soft reset para o primeiro commit
git reset --soft $(git rev-list --max-parents=0 HEAD)

# 3. Criar um único commit limpo
git add .
git commit -m "feat: Sistema de notificações assíncronas com Kafka

- Implementa microserviço Producer (porta 8081)
- Implementa microserviço Consumer (porta 8082)
- Integração com Apache Kafka para mensageria
- Envio de emails via Gmail SMTP
- Docker Compose para Kafka e Zookeeper"

# 4. Forçar push (CUIDADO: sobrescreve histórico remoto)
git push -f origin main
```

## Opção 2: Squash dos Últimos N Commits

```bash
# Ver histórico
git log --oneline

# Squash dos últimos 5 commits (ajuste o número)
git reset --soft HEAD~5

# Criar novo commit
git commit -m "feat: Sistema completo de notificações com Kafka"

# Forçar push
git push -f origin main
```

## Opção 3: Rebase Interativo (Mais Controle)

```bash
# Rebase dos últimos 10 commits (ajuste o número)
git rebase -i HEAD~10

# No editor que abrir:
# - Deixe 'pick' no primeiro commit
# - Troque 'pick' por 's' (squash) nos demais
# - Salve e feche

# Edite a mensagem do commit final
# Forçar push
git push -f origin main
```

## Opção 4: Começar do Zero (Mais Drástico)

```bash
# 1. Remover pasta .git
rmdir /s .git

# 2. Inicializar novo repositório
git init

# 3. Adicionar tudo
git add .

# 4. Primeiro commit limpo
git commit -m "feat: Sistema de notificações assíncronas com Kafka e SMTP"

# 5. Conectar ao repositório remoto
git remote add origin <URL_DO_SEU_REPO>

# 6. Forçar push
git push -f origin main
```

## ⚠️ AVISOS IMPORTANTES

1. **Backup**: Sempre faça backup antes de mexer no histórico
2. **Force Push**: Use `-f` com cuidado, sobrescreve o histórico remoto
3. **Colaboradores**: Se outras pessoas usam o repo, avise antes
4. **Branches**: Considere criar uma nova branch limpa

## 📝 Sugestões de Mensagens de Commit

```
feat: Sistema de notificações assíncronas com Kafka

Implementa arquitetura de microserviços para envio de emails:
- Producer: API REST que publica mensagens no Kafka
- Consumer: Serviço que consome mensagens e envia emails
- Tecnologias: Spring Boot, Kafka, Gmail SMTP, Docker
```

## 🔍 Verificar Resultado

```bash
# Ver histórico limpo
git log --oneline

# Ver status
git status
```
