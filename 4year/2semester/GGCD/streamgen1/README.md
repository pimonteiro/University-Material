Gerador de dados para teste de processamento de streams.

Modo de usar:

    $ java -jar target/streamgen-1.0-SNAPSHOT.jar ./title.ratings.tsv.gz 120
    
em que o primeiro parametro é a localização do ficheiro do IMDb
e o segundo é o numero de eventos gerados por minuto.

Para testar, ligar a localhost:12345, por exemplo com:

    $ nc localhost 12345