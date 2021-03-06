CREATE TABLE country(
	id INTEGER GENERATED BY DEFAULT AS IDENTITY,
	name VARCHAR(50) NOT NULL,
	UNIQUE (name)
);

CREATE TABLE  city(
	id  INTEGER GENERATED BY DEFAULT AS IDENTITY,
	name VARCHAR(40) NOT NULL,
	country_id INTEGER NOT NULL,
	status INTEGER
);

CREATE TABLE  news(
	id INTEGER GENERATED BY DEFAULT AS IDENTITY,
	text VARCHAR(10000),
	timestamp TIMESTAMP,
	like_number INTEGER,
	status INTEGER
);

CREATE TABLE  news_image(
	id INTEGER GENERATED BY DEFAULT AS IDENTITY,
	name VARCHAR(50),
	news_id INTEGER,
	image BLOB,
	comment VARCHAR(500)
);

CREATE TABLE role(
	id INTEGER GENERATED BY DEFAULT AS IDENTITY,
	name VARCHAR(50),
	UNIQUE (id)
);

CREATE TABLE user(
	id INTEGER GENERATED BY DEFAULT AS IDENTITY,
	email VARCHAR(50) NOT NULL,
	password_cipher VARCHAR(60) NOT NULL, 
	surname VARCHAR(40),
	name VARCHAR(40),
	role_id INTEGER,
	is_blocked BOOLEAN,
	UNIQUE (email)
);

CREATE TABLE user_additional_info(
	user_id INTEGER,
	gender BOOLEAN,
	birthday DATE,
	country_id INTEGER,
	city_id INTEGER,
	address VARCHAR(60),
	phone VARCHAR(20),
	status INTEGER,
	about_yourself VARCHAR(500)
);

CREATE TABLE user_observer(
	id INTEGER GENERATED BY DEFAULT AS IDENTITY,
	user_observer_id INTEGER,
	user_observed_id INTEGER
);

CREATE TABLE user_image(
	id INTEGER GENERATED BY DEFAULT AS IDENTITY,
	name VARCHAR(100),
	user_id INTEGER,
	image BLOB,
	status INTEGER,
	comment VARCHAR(500),
	album_name VARCHAR(100)
);

CREATE TABLE twit(
	id INTEGER GENERATED BY DEFAULT AS IDENTITY,
	owner_id INTEGER NOT NULL,
	text VARCHAR(500),
	timestamp TIMESTAMP,
	like_number INTEGER,
	status INTEGER,
	image BLOB
);