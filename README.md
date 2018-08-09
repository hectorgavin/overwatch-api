# Overwatch API

Spring Boot application wrapping the unofficial `https://overwatch-api.net` API, with an optional in-memory persistence layer.

## Build & Test
`mvn clean package`

## Run
From the project root execute:

`java -jar overwatch-api/target/overwatch-api-1.0.0-SNAPSHOT.jar`

It will run in port `8080` by default.

## Swagger

`http://localhost:8080/swagger-ui.html`

## Configuration

*application.properties*

Set `overwatch-api.use-in-memory-db` to `true` or `false` depending on whether you want to use the in-memory db or not.

## Architecture

### overwatch-api

+ `com.dojo.overwatch.config`
    + Bean definitions
+ `com.dojo.overwatch.exception`
    + Service exceptions
+ `com.dojo.overwatch.mapper`
    + Mappers for the different domain objects
+ `com.dojo.overwatch.model`
    + Model objects for the different sub-modules (`persistence`, `unofficialApi`, and the service itself)
+ `com.dojo.overwatch.persistence`
    + Datasources
+ `com.dojo.overwatch.resource`
    + REST endpoints
+ `com.dojo.overwatch.service`
    + `OverwatchInMemoryDecoratorServiceImpl`
        + In-memory caching layer
    + `UnofficialOverwatchApiServiceImpl`
        + REST client for `https://overwatch-api.net` API
+ `com.dojo.overwatch.utils`
    + Util classes
    
### overwatch-common

+ `com.dojo.overwatch.common`
    + API's response objects
    
## Known issues

+ As there is no background process or TTL per `Hero`/`Ability` keeping the DB up-to-date, once the data is persisted, 
it never tries to fetch a newer version for the unofficial Overwatch API. 
    
## Future improvements

+ Keep caching layer in-sync with the unofficial Overwatch API.
+ Better error handling/tolerance when handling responses from an API
+ Call other Overwatch APIs in case of failure (possible Chain of Responsibility pattern)
+ Improve API error responses 
