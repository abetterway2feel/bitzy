CREATE TABLE shortened_uri (
    key varchar(6) NOT NULL PRIMARY KEY,
    uri varchar(2000),
    createdAt timestamp,
    partition int
)