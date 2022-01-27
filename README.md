# Transfer funds
## _Transfer money easy_

[![N|Solid](https://cldup.com/dTxpPi9lDf.thumb.png)](https://nodesource.com/products/nsolid)

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

## Start docker container

You can start the app using docker or gradle

### Using docker container

- To start the docker container, create the image compiling the Dockerfile
    ```sh
    docker build --build-arg JAR_FILE=build/libs/\*.jar -t transfer-funds .
    ```
- Run the next command
    ```sh
    docker run -p 8080:8080 transfer-funds
    ```

### Using gradle console
- To start the app using gradle run the next command
  ```./gradlew build && java -jar build/libs/funds-transfer-0.0.1-SNAPSHOT.jar```

## Endpoind list

- Get accounts
  ```http://localhost:8080/funds-transfer/v1/api/accounts```
- Get acount
  ```http://localhost:8080/funds-transfer/v1/api/accounts/{accounId}```
- Transfer funds
  ```http://localhost:8080/funds-transfer/v1/api/transfers/transfer_funds```

