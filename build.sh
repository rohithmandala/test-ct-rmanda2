#/usr/bin
mvn clean
mvn compile
mvn war:war
cp ./target/clinicalTrials.war ~/warbuild/dev

