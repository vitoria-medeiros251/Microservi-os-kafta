# Limpar Commits no PowerShell (Windows)

## ✅ Opção Mais Simples - Criar Commit Único

```powershell
# 1. Navegar até a pasta do projeto
cd "C:\Users\VitoriaLeticiadaSilv\Downloads\microserviços kafta"

# 2. Fazer backup (segurança)
git branch backup-commits

# 3. Ver quantos commits existem
git log --oneline

# 4. Resetar para o primeiro commit (mantém as alterações)
git reset --soft (git rev-list --max-parents=0 HEAD)

# 5. Adicionar tudo
git add .

# 6. Criar commit limpo
git commit -m "feat: Sistema de notificações assíncronas com Kafka e Gmail SMTP"

# 7. Forçar push (sobrescreve histórico remoto)
git push -f origin main
```

## 🔄 Alternativa - Squash dos Últimos N Commits

```powershell
# Ver histórico
git log --oneline

# Juntar os últimos 5 commits (ajuste o número)
git reset --soft HEAD~5

# Criar novo commit
git commit -m "feat: Sistema completo de notificações com Kafka"

# Forçar push
git push -f origin main
```

## 🆕 Opção Começar do Zero

```powershell
# 1. Navegar até a pasta
cd "C:\Users\VitoriaLeticiadaSilv\Downloads\microserviços kafta"

# 2. Remover .git (CUIDADO!)
Remove-Item -Recurse -Force .git

# 3. Inicializar novo repositório
git init

# 4. Adicionar tudo
git add .

# 5. Primeiro commit
git commit -m "feat: Sistema de notificações assíncronas com Kafka

- Microserviço Producer (porta 8081)
- Microserviço Consumer (porta 8082)
- Integração com Apache Kafka
- Envio de emails via Gmail SMTP
- Docker Compose para infraestrutura"

# 6. Conectar ao repositório remoto (substitua pela sua URL)
git remote add origin https://github.com/seu-usuario/seu-repo.git

# 7. Forçar push
git push -f origin main
```

## 📝 Passo a Passo Recomendado

### 1. Abrir PowerShell
- Pressione `Win + X`
- Escolha "Windows PowerShell" ou "Terminal"

### 2. Navegar até o projeto
```powershell
cd "C:\Users\VitoriaLeticiadaSilv\Downloads\microserviços kafta"
```

### 3. Verificar status atual
```powershell
git status
git log --oneline
```

### 4. Fazer backup
```powershell
git branch backup-commits
```

### 5. Resetar commits
```powershell
git reset --soft (git rev-list --max-parents=0 HEAD)
```

### 6. Criar commit limpo
```powershell
git add .
git commit -m "feat: Sistema de notificações assíncronas com Kafka e Gmail SMTP"
```

### 7. Enviar para o GitHub
```powershell
git push -f origin main
```

## ⚠️ IMPORTANTE

- ✅ Funciona no PowerShell do Windows
- ✅ Funciona no Git Bash (se preferir)
- ⚠️ O `-f` sobrescreve o histórico remoto
- 💾 Sempre faça backup antes
- 👥 Se tiver colaboradores, avise antes

## 🔍 Verificar Resultado

```powershell
# Ver histórico limpo
git log --oneline

# Ver status
git status

# Ver branches
git branch
```

## 🆘 Se Algo Der Errado

```powershell
# Voltar para o backup
git reset --hard backup-commits

# Ou restaurar do remoto
git fetch origin
git reset --hard origin/main
```
