cd react
npm install
npm run build
cd ..
mvn clean package -DskipTests
mvn spring-boot:run