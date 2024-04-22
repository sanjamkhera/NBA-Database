.PHONY := build

InteractWithDatabase.class: InteractWithDatabase.java 
	javac InteractWithDatabase.java

build: InteractWithDatabase.class 

run: build
	java -cp .:mssql-jdbc-11.2.0.jre18.jar InteractWithDatabase
