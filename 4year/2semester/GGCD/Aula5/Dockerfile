FROM bde2020/hadoop-base
COPY target/Aula5-1.0-SNAPSHOT.jar /
ENTRYPOINT ["hadoop", "jar", "/Aula5-1.0-SNAPSHOT.jar", "LoadMovies","LoadActorBasic","LoadPrincipals","LoadAllMoviesActor","LoadScores","Top3Movies"]