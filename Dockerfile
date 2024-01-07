FROM eclipse-temurin:17
ADD build/libs/jopiter-uber.jar jopiter-uber.jar
EXPOSE 5000
CMD java -jar jopiter-uber.jar