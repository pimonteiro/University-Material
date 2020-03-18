FROM bde2020/hadoop-base
COPY target/imdb-1.0-SNAPSHOT.jar /
ENTRYPOINT ["hadoop", "jar", "/imdb-1.0-SNAPSHOT.jar", "FilmsPerGenre"]