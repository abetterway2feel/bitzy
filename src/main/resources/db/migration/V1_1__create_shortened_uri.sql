CREATE TABLE shortened_uri (
    key varchar(6) NOT NULL PRIMARY KEY,
    uri varchar(2000) NOT NULL,
    partition int NOT NULL,
    created_at timestamp with time zone NOT NULL
)