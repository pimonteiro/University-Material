FROM bde2020/spark-base:2.4.5-hadoop2.7
COPY target/TP2-1.0-SNAPSHOT.jar /
ENTRYPOINT ["/spark/bin/spark-submit", "--total-executor-cores", "4", "--executor-memory","2g","--driver-memory","4g", "--class", "ex2.Ex2", "--master", "spark://spark-master:7077", "/TP2-1.0-SNAPSHOT.jar"]