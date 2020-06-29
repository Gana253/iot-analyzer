echo Building - workspace Please wait...
cd ../
docker-compose -f src/main/docker/cassandra.yml up -d
echo  Starting cassandra instance. Going to sleep for 30 seconds before starting the service!!!
sleep 30
echo cassandra instance is up. Starting the application!!!
mvn spring-boot:run
echo Build completed...