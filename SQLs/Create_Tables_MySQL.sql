-- Drop Tables
drop table if exists ChatDB.messagechat;
drop table if exists ChatDB.clienthistory;
drop table if exists ChatDB.client;
drop table if exists ChatDB.history;

-- Create Table client
Create table if not exists ChatDB.client(
  username varchar(50) NOT NULL,
  name varchar(50) not null,
  lastname varchar(50) not null,
  mail varchar(50) not null,
  password varchar(20) not null,
  primary key (username),
  INDEX (username)
) ENGINE=INNODB;

-- Create Table record
Create table if not exists ChatDB.history(
  record_id integer NOT NULL AUTO_INCREMENT,
  date TIMESTAMP,
  log TEXT,
  primary key (record_id),
  INDEX (record_id)
) ENGINE=INNODB;

-- Create Table record_client
Create table if not exists ChatDB.clienthistory(
  record_id integer NOT NULL,
  username varchar(50) NOT NULL,
  primary key (record_id, username),
  FOREIGN KEY(record_id) REFERENCES history(record_id),
  FOREIGN KEY(username) REFERENCES client(username),
  INDEX (record_id)
) ENGINE=INNODB;

-- Create Table message
Create table if not exists ChatDB.messagechat(
  id integer NOT NULL AUTO_INCREMENT,
  record_id integer not null,
  username varchar(50) not null,
  messagecontent TEXT,
  date TIMESTAMP not null,
  FOREIGN KEY(record_id) REFERENCES history(record_id),
  FOREIGN KEY(username) REFERENCES client(username),
  INDEX (id)
) ENGINE=INNODB;