# <p align=center>Sample Cleaner CLI</p>

<div align="center">

[![Build Maven Package](https://github.com/qbicsoftware/sample-cleaner-cli/actions/workflows/build_package.yml/badge.svg)](https://github.com/qbicsoftware/sample-cleaner-cli/actions/workflows/build_package.yml)
[![Run Maven Tests](https://github.com/qbicsoftware/sample-cleaner-cli/actions/workflows/run_tests.yml/badge.svg)](https://github.com/qbicsoftware/sample-cleaner-cli/actions/workflows/run_tests.yml)
[![CodeQL](https://github.com/qbicsoftware/sample-cleaner-cli/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/qbicsoftware/sample-cleaner-cli/actions/workflows/codeql-analysis.yml)
[![release](https://img.shields.io/github/v/release/qbicsoftware/sample-cleaner-cli?include_prereleases)](https://github.com/qbicsoftware/sample-cleaner-cli/releases)

![license](https://img.shields.io/github/license/qbicsoftware/sample-cleaner-cli)
![language](https://img.shields.io/badge/language-java-blue.svg)
![framework](https://img.shields.io/badge/framework-spring-blue.svg)

</div>

<div align="center">
A Spring Boot CLI to remove sample tracking information for deleted samples.
</div>

When deleting samples from our openBis instance the associated sample tracking information still persits in our database.
To remove this (outdated) tracking information the CLI collects all currently registered samples and removes the tracking information for the samples that are not
stored in our database anymore.

## How to run

First compile the project and build an executable java archive:

```
mvn clean package
```

The JAR file will be created in the ``/target`` folder, for example:

```
|-target
|---sample-cleaner-cli-1.0.0.jar
|---...
```

Just change into the folder and run the CLI app with:

```
java -jar sample-cleaner-cli-1.0.0.jar -sampleList file.tsv
```

More information about the parameters can be found in section **How to use**

### Configuration

#### Environment Variables
The env variables contain information about the salt and the secret. Both of them are used to encrypt and decrypt user information.

| environment variable       | description               |
|----------------------------|---------------------------|
| `USER_DB_DIALECT`          | The database dialect      |
| `USER_DB_DRIVER`           | The database driver       |
| `USER_DB_URL`              | The database host address |
| `USER_DB_USER_NAME`        | The database user name    |
| `USER_DB_USER_PW`          | The database password     |

The application properties file could look like the following:
```properties
databases.users.database.dialect=${:org.hibernate.dialect.MariaDBDialect}
databases.users.database.driver=${USER_DB_DRIVER:com.mysql.cj.jdbc.Driver}
databases.users.database.url=${USER_DB_URL:localhost}
databases.users.user.name=${USER_DB_USER_NAME:myusername}
databases.users.user.password=${USER_DB_USER_PW:astrongpassphrase!}
```

## How to use

### --sampleList

Provide a list with all valid samples of type`Q-TEST-SAMPLE`. The file is in TSV format and should contain the sample ids in the first column.

```
Code	
QSTTS022AH	
QSTTS023AP	
QSTTS030A8	
QSTTS020A1	
```




## Licence
This work is licensed under the [MIT license](https://mit-license.org/).

**Note**: This work uses the [Spring Framework](https://github.com/spring-projects) and derivatives from the Spring framework family, which are licensed under [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0).
