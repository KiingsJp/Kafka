Comandos terminal:

--Ligar Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

--Ligar Kafka
bin/kafka-server-start.sh config/server.properties

--Listar topicos
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
bin/kafka-topics.sh --describe --bootstrap-server localhost:9092

--Ler mensagens
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic NOME_DO_TOPICO --from-beginning
