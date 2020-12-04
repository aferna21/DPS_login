 
CREATE DATABASE test;
CREATE USER 'user'@'localhost' IDENTIFIED BY 'studytonight';

CREATE TABLE test.register (
    id int NOT NULL AUTO_INCREMENT,
    email varchar(50) NOT NULL UNIQUE, 
    pass varchar(256) NOT NULL,
    PRIMARY KEY (id)
); 

GRANT ALL PRIVILEGES ON test.* TO 'user'@'localhost';

