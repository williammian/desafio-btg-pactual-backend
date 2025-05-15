# API de Processamento de Pedidos e Geração de Relatórios

Esta é uma API desenvolvida como parte do desafio BTG Pactual Backend. O projeto tem como objetivo processar pedidos de clientes e fornecer funcionalidades para consulta e geração de relatórios sobre esses pedidos. A aplicação é construída utilizando Java com o framework Spring Boot, persistindo os dados em MongoDB e utilizando RabbitMQ para mensageria assíncrona.

## Tecnologias Utilizadas

O projeto foi desenvolvido com as seguintes tecnologias e ferramentas:

*   **Java 21:** Linguagem de programação principal.
*   **Spring Boot 3.3.11 (via parent):** Framework para criação de aplicações Java robustas e independentes.
*   **Spring Data MongoDB:** Para integração e operações com o banco de dados NoSQL MongoDB.
*   **Spring AMQP:** Para integração com o RabbitMQ, facilitando a comunicação assíncrona.
*   **Maven:** Ferramenta para gerenciamento de dependências e build do projeto.
*   **MongoDB:** Banco de dados NoSQL orientado a documentos, utilizado para armazenar os dados dos pedidos.
*   **RabbitMQ:** Message broker para lidar com filas de mensagens, potencialmente para processamento assíncrono de pedidos ou notificações.
*   **Docker:** (A ser confirmado) Plataforma para desenvolvimento, deploy e execução de aplicações em contêineres.

## Pré-requisitos

Antes de executar o projeto, certifique-se de que você tem os seguintes softwares instalados e configurados em seu ambiente:

*   **Java Development Kit (JDK):** Versão 21 ou superior.
*   **Apache Maven:** Versão 3.6.x ou superior.
*   **MongoDB:** Instância do MongoDB em execução. As configurações de conexão estão detalhadas abaixo.
*   **RabbitMQ:** Instância do RabbitMQ em execução.
*   **Docker e Docker Compose (Opcional):** Se for utilizar a execução via contêineres (instruções a serem adicionadas caso arquivos Dockerfile/docker-compose.yml sejam encontrados).
*   **Git:** Para clonar o repositório.

## Configuração do Ambiente

### MongoDB

A aplicação espera que uma instância do MongoDB esteja acessível. As configurações de conexão são definidas no arquivo `orderms/src/main/resources/application.properties` e por padrão são:

*   **Host:** `localhost`
*   **Porta:** `27017`
*   **Database:** `btgpactualdb`
*   **Usuário de Autenticação:** `admin`
*   **Senha de Autenticação:** `123`
*   **Banco de Dados de Autenticação:** `admin`

Certifique-se de que o MongoDB esteja configurado com estas credenciais ou ajuste o arquivo `application.properties` conforme necessário.

### RabbitMQ

A aplicação utiliza RabbitMQ. As configurações padrão do Spring AMQP são geralmente suficientes se o RabbitMQ estiver rodando localmente na porta padrão (`5672`) sem credenciais específicas. Caso contrário, pode ser necessário adicionar propriedades de configuração para o RabbitMQ no arquivo `application.properties` (ex: `spring.rabbitmq.host`, `spring.rabbitmq.port`, `spring.rabbitmq.username`, `spring.rabbitmq.password`).

## Como Executar a Aplicação

Siga os passos abaixo para executar a aplicação localmente:

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/williammian/desafio-btg-pactual-backend.git
    cd desafio-btg-pactual-backend
    ```

2.  **Navegue até o diretório do módulo principal:**
    ```bash
    cd orderms
    ```

3.  **Compile o projeto com Maven:**
    Este comando irá baixar as dependências e compilar o código-fonte.
    ```bash
    mvn clean install
    ```

4.  **Execute a aplicação:**
    Após a compilação bem-sucedida, um arquivo `.jar` será gerado no diretório `target`.
    ```bash
    java -jar target/orderms-0.0.1-SNAPSHOT.jar
    ```
    Alternativamente, você pode executar a aplicação diretamente usando o plugin do Maven para Spring Boot:
    ```bash
    mvn spring-boot:run
    ```

A aplicação será iniciada e, por padrão, estará acessível na porta `8080` (porta padrão do Spring Boot, a menos que especificada de outra forma no `application.properties`).




## Estrutura do Projeto

O projeto está organizado dentro do módulo `orderms`. A estrutura principal de diretórios segue o padrão Maven para projetos Java:

*   `orderms/src/main/java`: Contém o código-fonte da aplicação, organizado nos seguintes pacotes principais:
    *   `br.com.wm.btgpactual.orderms.config`: Classes de configuração para a aplicação Spring Boot, como a configuração do RabbitMQ (ex: `RabbitMQConfig.java`).
    *   `br.com.wm.btgpactual.orderms.controller`: Controladores REST que expõem os endpoints da API. O `OrderController.java` é o principal controlador para as operações de pedido.
    *   `br.com.wm.btgpactual.orderms.controller.dto`: Objetos de Transferência de Dados (DTOs) utilizados para a comunicação entre o cliente e a API, tanto para requisições quanto para respostas (ex: `OrderResponse.java`, `PaginationResponse.java`, `ApiResponse.java`).
    *   `br.com.wm.btgpactual.orderms.entity`: Entidades JPA que mapeiam os objetos do domínio para o banco de dados MongoDB (ex: `OrderEntity.java`, `OrderItem.java`).
    *   `br.com.wm.btgpactual.orderms.listener`: Listeners para filas do RabbitMQ. O `OrderCreatedListener.java` é responsável por processar mensagens da fila de criação de pedidos.
    *   `br.com.wm.btgpactual.orderms.repository`: Interfaces de repositório (Spring Data MongoDB) para interagir com o banco de dados (ex: `OrderRepository.java`).
    *   `br.com.wm.btgpactual.orderms.service`: Classes de serviço que contêm a lógica de negócio da aplicação (ex: `OrderService.java`).
    *   `br.com.wm.btgpactual.orderms.OrdermsApplication.java`: Classe principal que inicializa a aplicação Spring Boot.
*   `orderms/src/main/resources`: Contém arquivos de configuração, como o `application.properties` (configurações do Spring Boot, banco de dados, etc.) e `static`/`templates` (se houver conteúdo web).
*   `orderms/src/test/java`: Contém os testes unitários e de integração da aplicação.
*   `orderms/pom.xml`: Arquivo de configuração do Maven, definindo as dependências do projeto, plugins e informações de build.

## Endpoints da API

A API expõe os seguintes endpoints principais através do `OrderController` para gerenciar pedidos:

### Listar Pedidos por Cliente

Este endpoint permite a consulta paginada de todos os pedidos associados a um determinado código de cliente.

*   **Método HTTP:** `GET`
*   **Caminho:** `/customers/{customerId}/orders`
*   **Parâmetros de Caminho:**
    *   `customerId` (Long): O identificador único do cliente.
*   **Parâmetros de Query (Opcionais):**
    *   `page` (Integer, default: `0`): O número da página desejada para a paginação dos resultados.
    *   `pageSize` (Integer, default: `10`): O número de pedidos a serem retornados por página.
*   **Resposta de Sucesso (200 OK):** Um objeto JSON contendo uma lista paginada de pedidos do cliente. A estrutura da resposta inclui os dados da paginação (número da página, tamanho da página, total de elementos, total de páginas) e uma lista dos pedidos (`OrderResponse`).
    ```json
    {
      "data": [
        {
          "orderId": 123,
          "customerCode": 1,
          "total": 150.75,
          "items": [
            {"product": "Produto A", "quantity": 2, "price": 50.25},
            {"product": "Produto B", "quantity": 1, "price": 50.25}
          ]
        }
        // ... outros pedidos
      ],
      "pagination": {
        "page": 0,
        "pageSize": 10,
        "totalElements": 1,
        "totalPages": 1
      }
    }
    ```
*   **Exemplo de Requisição (usando cURL):**
    ```bash
    curl -X GET "http://localhost:8080/customers/1/orders?page=0&pageSize=5"
    ```

### Calcular Valor Total do Pedido

Este endpoint permite calcular o valor total de um pedido específico, identificado pelo seu código.

*   **Método HTTP:** `GET`
*   **Caminho:** `/orders/{orderId}/total`
*   **Parâmetros de Caminho:**
    *   `orderId` (Long): O identificador único do pedido.
*   **Resposta de Sucesso (200 OK):** Um objeto JSON simples contendo o valor total do pedido.
    ```json
    {
      "total": 250.50
    }
    ```
*   **Exemplo de Requisição (usando cURL):**
    ```bash
    curl -X GET "http://localhost:8080/orders/101/total"
    ```

## Fluxo de Processamento de Pedidos e Integração com RabbitMQ

A criação de um pedido na API dispara um evento que é publicado em uma fila do RabbitMQ. O `OrderCreatedListener` é responsável por consumir mensagens desta fila. Ao receber uma mensagem de pedido criado, o listener processa essa informação, que no contexto atual do código, parece estar focada em salvar o pedido no banco de dados MongoDB através do `OrderService`.

Este desacoplamento via RabbitMQ permite que a criação do pedido seja uma operação rápida para o cliente que chama a API, enquanto o processamento subsequente (como persistência, notificações, ou outras lógicas de negócio) pode ocorrer de forma assíncrona, melhorando a resiliência e escalabilidade do sistema.

A configuração da infraestrutura do RabbitMQ (filas, exchanges, bindings) é gerenciada pela classe `RabbitMQConfig.java` dentro do pacote `config`. Ela define a fila `btg-pactual-order-created` e a exchange correspondente.

## Geração de Relatórios

O escopo atual do código analisado foca principalmente no processamento e consulta de pedidos individuais ou por cliente. Detalhes específicos sobre a "geração de relatórios" mais complexos (como relatórios agregados, consolidados ou com filtros avançados) não estão explicitamente detalhados nos controllers ou services revisados. A funcionalidade de listar pedidos por cliente com paginação pode ser considerada uma forma básica de relatório. Se houver funcionalidades de relatório mais avançadas, elas podem estar em outros módulos ou necessitariam de uma análise mais aprofundada do código ou documentação adicional.

## Executando com Docker

Até o momento da análise, não foram encontrados arquivos `Dockerfile` ou `docker-compose.yml` na estrutura do projeto. Se estes arquivos forem adicionados posteriormente, as instruções para build e execução da aplicação via contêineres Docker deverão ser incluídas nesta seção.

## Licença

Este projeto está licenciado sob a licença MIT.

