-- Create database
Create schema if not exists ChatDB

-- Create user
drop user chat;
create user chat identified by "chat";

-- Grant privilegies to user
GRANT ALL PRIVILEGES ON ChatDB.* TO 'chat'@'%'
WITH GRANT OPTION;