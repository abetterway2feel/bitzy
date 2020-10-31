# Bitzy URI Shortener

The Bitzy uri shortener API has two functions:
1. - Take a uri and shorten it to a 'bitzy' string of six charachters
2. - Given a previously 'bitzy', redirect the user to the saved uri

## Running the application:

### Docker

The easiest way to get started with the application is to run 
`docker-compose up`

### Maven

To start the application with maven you need to have a local postgres running. 
This match the databases set up in `src/main/resources/application-dev.yaml`;

Now run `./mvnw spring-boot:run`

## Testing the application

### Manually

There are two endpoints available:

* POST / with the uri in json body
  
`curl -H "Content-Type: application/json" -d '{ "uri": "<uri you want to shorten>" }' http://localhost:8080/`

* GET /{:key}

`curl http://localhost:8080/<key>`

If you now paste the same bitzy uri into your browser you will be redirected to the uri
that was associated to it. 


### Automated tests

Unit and integration tests can be ran for the project by running 

`mvn test`

## Architecture

### Storing the data 
Currently the data is sharded accross two database. For each uri a key is generated. 
The currently selected strategy for this is to take a SHA1 hash of the uri in base64 
and select 6 random characters from this string, minus and special characters (i.e. 
'+' or '/'). 

The algorithm for picking a key is as follows:
```
1. Generate SHA1 hash of uri in base64 encoding
2. The key is then selected by taking the first 6 characters from this string, excluding 
any special characters (i.e. '+' or '/')
3. partion number = hashCode(key) modulus 10 (i.e. number of partions)
4. db number = partions modulus 2 (i.e. number of databases)
5. If there is a colision then the next 6 characters are takend from the hash until a unique key is found or the has is exhausted.
```
If required, the collision logic could be extended further to generate different keys, eg say add or remove a slash the trailing slash inthe uri so that a new hash is required. However, given there are 6^62 permutations

In order to add a new database only the data for partions 3,6,9 will need to be moved. If the data saving did not use a partion then all keys would need to be rehashed. 