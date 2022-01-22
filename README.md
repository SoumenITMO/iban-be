# iban-be
this is an IBAN validator backend part based on REST API

# Steps How to build the application

####### First of all make sure that the system has follow prerequirments :

1. JDK 17
2. Already Installed maven

### Before Building application, please finalize the seeding file location,
#### It is located in "*application.properties*" file, it is needed for running test cases too

>- seeding.datafile=C://init-iban.csv

## After this all please run the follow command to build the application :

>- mvn package -X

#### After this there will be a "*.jar*" file in project target folder and to run it 
#### please follow the following command : 

>-  java -jar validator-1.0.0.jar


