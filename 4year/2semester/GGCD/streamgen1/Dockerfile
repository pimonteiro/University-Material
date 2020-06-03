FROM bde2020/spark-base
COPY target/streamgen-1.0-SNAPSHOT.jar /
ENTRYPOINT ["/spark/bin/spark-submit", "--total-executor-cores", "4", "--executor-memory", "4g", "--driver-memory","4g", "--class", "Ex1", \
            "--master", "spark://spark-master:7077","/streamgen-1.0-SNAPSHOT.jar"]