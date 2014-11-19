-- Drop Tables
drop table messagechat;
drop table clienthistory;
drop table client;
drop table history;

-- Create Table client
Create table client(
  "username" varchar(50) NOT NULL primary key,
  "name" varchar(50) not null,
  "lastname" varchar(50) not null,
  "mail" varchar(50) not null,
  "password" varchar(20) not null
);

-- Create Table record
Create table history(
  "record_id" integer NOT NULL primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  "date" timestamp,
  "log" long varchar
);

-- Create Table record_client
Create table clienthistory(
  "record_id" integer NOT NULL,
  "username" varchar(50) NOT NULL
);

ALTER TABLE clienthistory
ADD FOREIGN KEY ("record_id")
REFERENCES history("record_id");

ALTER TABLE clienthistory
ADD FOREIGN KEY ("username") 
REFERENCES client("username");

ALTER TABLE clienthistory
ADD PRIMARY KEY ("record_id", "username");

-- Create Table message
Create table messagechat(
  "id" integer NOT NULL primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  "record_id" integer not null,
  "username" varchar(50) not null,
  "messagecontent" long varchar,
  "date" timestamp not null
);

ALTER TABLE messagechat
ADD FOREIGN KEY ("record_id")
REFERENCES history("record_id");

ALTER TABLE messagechat
ADD FOREIGN KEY ("username") 
REFERENCES client("username");

