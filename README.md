# Sistema de Notificações Assíncronas com Kafka

Sistema de microserviços para envio de notificações por email utilizando mensageria assíncrona com Apache Kafka.

## 📋 Visão Geral

Este projeto implementa uma arquitetura de microserviços baseada em eventos, composta por dois serviços independentes que se comunicam através do Apache Kafka:

- **notification-producer**: Microserviço produtor que recebe requisições HTTP e publica mensagens no Kafka
- **notification-consumer**: Microserviço consumidor que escuta mensagens do Kafka e envia emails

## 🏗️ Arquitetura

```
[Cliente HTTP] → [Producer API] → [Kafka Topic] → [Consumer Service] → [Email SMTP]
                     :8081          notification-topic      :8082
```

## 🔧 Tecnologias Utilizadas

### Ambos os Microserviços
- **Java 25**: Linguagem de programação
- **Spring Boot 3.5.9**: Framework para desenvolvimento de aplicações Java
- **Spring Kafka**: Integração do Spring com Apache Kafka
- **Lombok**: Redução de código boilerplate
- **Maven**: Gerenciamento de dependências e build

### Notification Producer
- **Spring Web**: Criação de APIs REST
- **Spring Data JPA**: Persistência de dados
- **H2 Database**: Banco de dados em memória
- **Jackson**: Serialização/deserialização JSON

### Notification Consumer
- **Spring Mail**: Envio de emails via SMTP
- **Gmail SMTP**: Servidor de email (smtp.gmail.com:587)
- **STARTTLS**: Criptografia de conexão

### Infraestrutura
- **Apache Kafka**: Sistema de mensageria distribuída
- **Zookeeper**: Coordenação do cluster Kafka
- **Docker Compose**: Orquestração dos containers

## 📦 Microserviço Producer

### Responsabilidades
- Expor API REST para receber requisições de notificação
- Serializar dados em formato JSON
- Publicar mensagens no tópico Kafka

### Funcionamento do Producer

O **KafkaProducerService** é responsável por enviar mensagens para o Kafka:

```java
@Service
public class KafkaProducerService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Value("${app.kafka.topic.notification}")
    private String topicName; // notification-topic
    
    public void publishMessageEmail(String userEmail, String userName) {
        var notificationDto = new NotificationDTO();
        notificationDto.setEmailTo(userEmail);
        notificationDto.setSubject("Cadastro realizado com sucesso");
        notificationDto.setText(userName + ", seja bem vindo(a)!");
        
        // Serializa o objeto para JSON
        ObjectMapper mapper = new ObjectMapper();
        String jsonMessage = mapper.writeValueAsString(notificationDto);
        
        // Envia para o tópico Kafka
        kafkaTemplate.send(topicName, jsonMessage);
    }
}
```

### Configurações do Producer
- **Porta**: 8081
- **Kafka Bootstrap**: localhost:9092
- **Tópico**: notification-topic
- **Serializer**: StringSerializer
- **Acks**: all (garantia de entrega)
- **Retries**: 3 tentativas

## 📨 Microserviço Consumer

### Responsabilidades
- Escutar mensagens do tópico Kafka
- Deserializar mensagens JSON
- Processar e enviar emails via SMTP

### Funcionamento do Consumer

O **KafkaConsumerService** escuta o tópico e processa as mensagens:

```java
@Service
public class KafkaConsumerService {
    @Autowired
    private EmailService emailService;
    
    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void consumeNotification(String message) {
        logger.info("=== NOVA MENSAGEM RECEBIDA ===");
        
        // Deserializa a mensagem JSON
        NotificationDTO notification = objectMapper.readValue(message, NotificationDTO.class);
        
        // Envia o email
        emailService.sendEmail(
            notification.getEmailTo(), 
            notification.getSubject(), 
            notification.getText()
        );
        
        logger.info("Email processado com sucesso");
    }
}
```

### Configurações do Consumer
- **Porta**: 8082
- **Kafka Bootstrap**: localhost:9092
- **Group ID**: notification-group
- **Deserializer**: StringDeserializer
- **Auto Offset Reset**: earliest (lê desde o início)

### Configurações de Email (Gmail SMTP)
- **Host**: smtp.gmail.com
- **Porta**: 587
- **Autenticação**: Habilitada
- **STARTTLS**: Habilitado (criptografia TLS)
- **Credenciais**: Configuradas no application.properties
- **Timeout**: 5000ms para conexão

## 🔄 Fluxo de Mensagens Kafka

### 1. Producer Envia Mensagem
```
Cliente → POST /api/notification
         ↓
Producer cria NotificationDTO
         ↓
Serializa para JSON
         ↓
KafkaTemplate.send("notification-topic", json)
         ↓
Mensagem armazenada no Kafka
```

### 2. Consumer Recebe Mensagem
```
Kafka Topic "notification-topic"
         ↓
@KafkaListener detecta nova mensagem
         ↓
Deserializa JSON para NotificationDTO
         ↓
EmailService envia email via SMTP
         ↓
Log de confirmação
```

### Conceitos Importantes

**Tópico (Topic)**: Canal de comunicação nomeado onde as mensagens são publicadas
- Nome: `notification-topic`
- Armazena mensagens de forma durável

**Producer**: Aplicação que publica mensagens no tópico
- Serializa dados em String (JSON)
- Envia para o broker Kafka

**Consumer**: Aplicação que consome mensagens do tópico
- Pertence ao grupo `notification-group`
- Deserializa String (JSON) em objetos
- Processa mensagens de forma assíncrona

**Group ID**: Identificador do grupo de consumidores
- Permite balanceamento de carga
- Garante que cada mensagem seja processada uma vez por grupo

## 🚀 Como Executar

### 1. Iniciar Kafka e Zookeeper
```bash
cd "notification- producer (3)"
docker-compose up -d
```

### 2. Iniciar Producer
```bash
cd "notification- producer (3)/notification-producer"
mvnw spring-boot:run
```

### 3. Iniciar Consumer
```bash
cd "notification- consumer/notification-consumer"
mvnw spring-boot:run
```

### 4. Testar o Sistema
```bash
curl -X POST http://localhost:8081/api/notification \
  -H "Content-Type: application/json" \
  -d '{"email":"usuario@example.com","name":"João Silva"}'
```

## 📊 Monitoramento

- **Producer**: http://localhost:8081
- **Consumer**: http://localhost:8082
- **H2 Console**: http://localhost:8081/h2-console
- **Logs**: Verifique os logs do consumer para acompanhar o processamento

## 🔐 Segurança

### Gmail SMTP
- Utiliza autenticação via senha de aplicativo do Gmail
- Conexão segura com STARTTLS na porta 587
- Credenciais configuradas no application.properties do consumer
- **Importante**: Em produção, use variáveis de ambiente para credenciais

### Kafka
- Sem autenticação (apenas para desenvolvimento)
- Em produção, configure SASL/SSL

## 📝 Estrutura do DTO

```java
public class NotificationDTO {
    private String emailTo;    // Destinatário
    private String subject;    // Assunto
    private String text;       // Corpo do email
}
```

## 🎯 Benefícios da Arquitetura

- **Desacoplamento**: Producer e Consumer são independentes
- **Escalabilidade**: Múltiplos consumers podem processar mensagens
- **Resiliência**: Mensagens persistidas no Kafka mesmo se consumer estiver offline
- **Assíncrono**: Producer não espera o envio do email
- **Rastreabilidade**: Logs detalhados em cada etapa
