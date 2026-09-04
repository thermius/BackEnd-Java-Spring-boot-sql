## ☕ BackEnd Java Springboot PostgreSQL
Backend desenvolvido em Java + Spring Boot + PostgreSQL para cadastro e consulta de clientes, com armazenamento protegido de dados sensíveis.

O projeto foi desenvolvido com foco no estudo de backend, persistência de dados, APIs REST e criptografia aplicada ao armazenamento.

**Funcionalidades**

* Cadastro de clientes através de API REST
* Consulta de clientes através do CPF
* Persistência dos dados utilizando PostgreSQL
* Criptografia de dados sensíveis com AES-GCM
* Geração de HMAC-SHA256 para identificação determinística de documentos
* Integração com PostgreSQL através do Spring Data JPA
* Conversão dos dados armazenados para objetos Java durante a consulta

## 🌐 API

## 🌐 API
### Cadastro
POST /cadastrar

Recebe os dados do cliente através de JSON e realiza o processamento antes do armazenamento.

Fluxo simplificado:

JSON
 ↓
Controller
 ↓
Processamento dos dados
 ↓
🔒 AES-GCM / HMAC-SHA256
 ↓
Spring Data JPA
 ↓
PostgreSQL

### Consulta
GET /consultar/{cpf}

O CPF informado é utilizado para gerar o HMAC correspondente. O resultado é utilizado para localizar o registro no banco sem a necessidade de descriptografar todos os registros.

Fluxo:

CPF
 ↓
HMAC-SHA256
 ↓
Consulta PostgreSQL
 ↓
Registro encontrado
 ↓
🔓 Descriptografia AES-GCM
 ↓
Objeto Java
 ↓
JSON

⚠️ **Nota:** Para fins exclusivamente demonstrativos, o projeto exibe alguns dados sensíveis durante a execução para permitir a visualização e validação do funcionamento da API. Essa prática é utilizada apenas no contexto educacional deste projeto e seria inadequada em um ambiente de produção. Todos os dados são fictícios e gerados com IA:

```bash
=== DADOS RECEBIDOS VIA POST ===
Nome:                   Maria Oliveira
CPF:                    98765432100
Senha:                  MinhaSenhaForte!2026
Número Cartão:          5500000000000004
CVV:                    888
Data Vencimento:        2031-09-04
Data Cadastro:          2026-09-04T14:23:52.763793708
====================================

=== CONVERSÃO PARA BYTES (HEXADECIMAL) ===
Nome Hex:            Maria Oliveira
CPF Hex:             39 38 37 36 35 34 33 32 31 30 30
Senha Hex:           4d 69 6e 68 61 53 65 6e 68 61 46 6f 72 74 65 21 32 30 32 36
Cartão Hex:          35 35 30 30 30 30 30 30 30 30 30 30 30 30 30 34
CVV Hex:             38 38 38
Data Vencimento Hex: 32 30 33 31 2d 30 39 2d 30 34
Data Cadastro Hex:   32 30 32 36 2d 30 39 2d 30 34 54 31 34 3a 32 33 3a 35 32 2e 37 36 33 39 39 34 31 35 35
===========================================

=== DADOS RECEBIDOS VIA POST ===
Nome:                   Carlos Eduardo
CPF:                    45678912300
Senha:                  Acesso@Sistema#99
Número Cartão:          6363680000000000
CVV:                    951
Data Vencimento:        2031-09-04
Data Cadastro:          2026-09-04T14:24:24.076321971
====================================

=== CONVERSÃO PARA BYTES (HEXADECIMAL) ===
Nome Hex:            Carlos Eduardo
CPF Hex:             34 35 36 37 38 39 31 32 33 30 30
Senha Hex:           41 63 65 73 73 6f 40 53 69 73 74 65 6d 61 23 39 39
Cartão Hex:          36 33 36 33 36 38 30 30 30 30 30 30 30 30 30 30
CVV Hex:             39 35 31
Data Vencimento Hex: 32 30 33 31 2d 30 39 2d 30 34
Data Cadastro Hex:   32 30 32 36 2d 30 39 2d 30 34 54 31 34 3a 32 34 3a 32 34 2e 30 37 36 34 38 30 31 32 39
===========================================

```




## 🔒 Proteção dos dados
AES-GCM

Os dados que precisam ser recuperados posteriormente são armazenados utilizando AES-GCM.

Cada operação de criptografia utiliza um IV aleatório, fazendo com que duas criptografias do mesmo valor produzam resultados diferentes.
Algebricamente temos:


**AES(K, CPF, IV₁) ≠ AES(K, CPF, IV₂)** 

## 🔒 HMAC-SHA256

Para permitir consultas determinísticas, o CPF também possui uma representação através de HMAC-SHA256.

HMAC(K, CPF)

Para a mesma entrada e a mesma chave secreta, o resultado é determinístico.

Dessa forma, a aplicação consegue verificar a existência de um CPF através do valor do HMAC sem precisar descriptografar os registros armazenados.

A aplicação é responsável pelo processamento criptográfico. O PostgreSQL recebe e armazena os valores já processados pela aplicação.


🔐 **Nota:** As chaves criptográficas não são armazenadas no código-fonte nem no banco de dados.
A aplicação utiliza variáveis de ambiente para carregar as chaves:
  ```bash
    AES_SECRET_KEY
  HMAC_SECRET_KEY
  ```


## 🖥️ Tecnologias
Java
Spring Boot
Spring Data JPA
Hibernate
PostgreSQL
SQL
REST API
HTTP
JSON
AES-GCM
HMAC-SHA256
Git / GitHub


## 🗄️ Banco de dados

O banco de dados tem a seguinte estrutura:

```bash
dados_de_clientes-# \d clientes_tb;
                                            Table "public.clientes_tb"
         Column         |          Type          | Collation | Nullable |                 Default                 
------------------------+------------------------+-----------+----------+-----------------------------------------
 id                     | bigint                 |    dados_de_clientes-# \d clientes_tb;
                                            Table "public.clientes_tb"
         Column         |          Type          | Collation | Nullable |                 Default                 
------------------------+------------------------+-----------+----------+-----------------------------------------
 id                     | bigint                 |           | not null | nextval('clientes_tb_id_seq'::regclass)
 nome                   | character varying(255) |           | not null | 
 numero_cartao          | bytea                  |           | not null | 
 cvv                    | bytea                  |           | not null | 
 cpf                    | bytea                  |           | not null | 
 data_vencimento_cartao | bytea                  |           | not null | 
 data_cadastro          | bytea                  |           | not null | 
 senha                  | bytea                  |           | not null | 
 hash_cpf               | bytea                  |           | not null | 
Indexes:
    "clientes_tb_pkey" PRIMARY KEY, btree (id)
    "unico" UNIQUE CONSTRAINT, btree (hash_cpf)
```


Onde o único campo visível é o nome do cliente:


```bash
dados_de_clientes=# select * from clientes_tb;
 id  |      nome      |                                       numero_cartao                                        |                               cvv                                |                                       cpf            
                            |                             data_vencimento_cartao                             |                                                    data_cadastro                                                     |        
                                        senha                                                 |                              hash_cpf                              
-----+----------------+--------------------------------------------------------------------------------------------+------------------------------------------------------------------+------------------------------------------------------
----------------------------+--------------------------------------------------------------------------------+----------------------------------------------------------------------------------------------------------------------+--------
----------------------------------------------------------------------------------------------+--------------------------------------------------------------------
 152 | João Silva     | \xbdc00cb60bcb89b9c536228543006ffa54c90394dca6d9018a441e764bda7566743ecd4b9d7044c9f6d7a596 | \xc820094247805c776e595531eb84311bd8eb76ab8cc103ee19b4c8d66047d5 | \xe03ee1b08b614970d6a16b54b2638f0f1a3ed2dd6be4d802d4d
b615eda7059923267e9f79250e1 | \xce6e93938b7b9fac388fb0424b0dce0695f19fd1c7287a4c3c38be27af01994c8ad30d24bcd3 | \x144755dd76a3ccb83f87dc60b59b6c749f7e7d4d0e92684b507d757044ae697a6838fb141b9cec43decc23a7b864938a7e145594016fcaad4d | \x8b5ba
6e19a8ea98996a563153bc7b218ea56507c0ab27cdaed0587a6dde8b4eb1379f672762295dd037f9e3ed8c69fcc56 | \xb9c0f56326abc6ec762404f9d5b7eb44b40e24b5f10a959d0d9861600e69ea90
 352 | Cliente Teste  | \x0cb5a7c066df5e7e9f16b4db30cdeeb44051e8f3c58913cfb0bf048bc0b924154703ceed1ed9bf4ae7a185c7 | \x0e0739173c61a4d02d028767c30594e22ee7ba254b9a5f10d4644d9eb24f71 | \x4a0250eb368906350374e0a889dc575e1fcfe411bccf547618d
049eeb9f2f8c39256c8cc24aa48 | \x5a75de7b23caf08ac43a3f6416fd76e005b2e4d152bcab9a181a5de94e1c741727e333462c3f | \x2d8df981e49a2f5ea3beb28c3672551d29124f90e26466ebfc2df8e29a8add7b07e4c08482149e9427aa146ac88618c3629c5a60009ae15510 | \x2d511
a6b2a24a83ddeffdc569d7bdf09b66d029b5b4393fe7b89d7e4d40115c1734a94f5229fb779ced4               | \x95364e5fe7b2e041abcaf43b78b54cc87b3ccc5e306d6e857ea861fc6bb5d2df
 403 | Maria Oliveira | \x16aecbcb47c80db3d1de61ee73caaa20ce2704f0e7d80c62c3d33799c66f8d03ce0fbb44e5fc244b9cfabda0 | \x2a56a7b57b5e68e1fb5451fe8c9f8ac923cfe90e7d0b3c10b510f870ce4429 | \x3520f9a938ae955b6ceb1d0941080682ee3f7e77a64c59b7e8c
bbeba4de73a655c43c93e8fa333 | \x27bccfd44a93ce3ebef8168736ed6d8e79edade25c37c145a9eeee63748467f315fe994dfca2 | \x7aa8522c174a56d46b860aa23095ec3288314d15961fe4b901e438b231ea4b73e3d4170e7e737764a979fed773fa8b6165394a59a1b1829b30 | \x0b8e6
96a7901dcf5439564d6f8701bfc5b93a603d830f0cf6354541b369c387dbb0e0ecc0cff46b7ac1b51df8072ca98   | \x992f58089ce70925521b12a5880d9b5f5fd49f7c8e34dddd8ff508e214f7ca87
 404 | Carlos Eduardo | \x9112a01d4be17d0ac0f7b585f89460fe8ff255a8b9cdb7ed83073ce55054642d7565b85377323eb75bed0b2e | \xdfdcb34e7b174d640f0f16b864c7d47ab8ec9c4415a49f9e00ef100ee0ae2f | \x72e969df88a8519b261746aa51a9bb1b71403e968bea7457ed5
6ac7d047b7122d9bc87231c49e6 | \x5382f60a53d476d99fe9333bd5de33a2ca0a2138b370d88c92a33c5e0110c4146c699201d150 | \x2e35a44bd63172d7ddfd11988b622854b0bdd2566b013f8c568d4aab3911050b8f8274763cc67a5409f2e048f60f7f780b71a873cd81f3546e | \x3651f
0586e3e99b058357b3a3764843c86ef56f8c0f1ad23df5d676795f313f2b088c7b6bf109f2c6d70ebfbcc         | \xcfb1bd162090736883b803910b770e0b3000e4cca4bf2388a8a5587b1696a6e6
(4 rows)

```


## 💻 Teste da API via CURL

Os testes demonstram o funcionamento da API, incluindo o cadastro de clientes e a consulta dos dados armazenados e posteriormente descriptografados pela aplicação.

**Teste método GET:**
```bash
thermius@arch: curl -i -X GET http://localhost:8080/consultar/98765432100
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Fri, 04 Sep 2026 17:30:03 GMT

{"cpf":"98765432100","cvv":"888","dataCadastro":"2026-09-04T14:23:52.763994155","dataVencimento":"2031-09-04","nome":"Maria Oliveira","numeroCartao":"5500000000000004","senha":"MinhaSenhaForte!2026"}

thermius@arch: curl -i -X GET http://localhost:8080/consultar/45678912300
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Fri, 04 Sep 2026 17:30:11 GMT

{"cpf":"45678912300","cvv":"951","dataCadastro":"2026-09-04T14:24:24.076480129","dataVencimento":"2031-09-04","nome":"Carlos Eduardo","numeroCartao":"6363680000000000","senha":"Acesso@Sistema#99"}
thermius@arch: 

```

**Teste método POST:**
```bash

thermius@arch: curl -i -X POST http://localhost:8080/cadastrar \        
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Ana Beatriz Santos",
    "cpf": "11122233344",
    "senha": "Senha#MuitoDificil12",
    "numeroCartao": "4000123456789010",
    "cvv": "101"
  }'
HTTP/1.1 201 
Content-Type: text/plain;charset=UTF-8
Content-Length: 39
Date: Fri, 04 Sep 2026 17:32:24 GMT

Cliente cadastrado com sucesso! ID: 405

thermius@arch: curl -i -X POST http://localhost:8080/cadastrar \        
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Roberto Almeida",
    "cpf": "55566677788",
    "senha": "AmexUser2026!",
    "numeroCartao": "378282246310005",
    "cvv": "1234"
  }'
HTTP/1.1 201 
Content-Type: text/plain;charset=UTF-8
Content-Length: 39
Date: Fri, 04 Sep 2026 17:32:41 GMT

Cliente cadastrado com sucesso! ID: 406
thermius@arch:

```
## 👨‍💻 Autor
**Thermius**
Projeto desenvolvido para fins educacionais e de estudo em desenvolvimento backend, segurança de aplicações e criptografia.


## 📄 Licença

Todos os direitos reservados.

Este projeto é disponibilizado exclusivamente para fins de portfólio e demonstração técnica. O código-fonte não pode ser copiado, redistribuído, modificado ou utilizado, integral ou parcialmente, sem autorização prévia e explícita do autor.
