run:
	mvn exec:java

lint:
  mvn rewrite:run

package:
  mvn package
