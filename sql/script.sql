create database pconline_db;
use pconline_db;
create table product (
	 id   int AUTO_INCREMENT,
	 name varchar(128),
	 type varchar(128),
	 content varchar(20000),
	 abstractcontent varchar(512),
	 url varchar(512),
	 imageurl varchar(128),
	 updatedtime varchar(64),
	 primary key(id)
	);