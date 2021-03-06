-- Clean previous tables:
DROP TABLE ReturnItem;
DROP TABLE Return;
DROP TABLE PurchaseItem;
DROP TABLE Purchase;
DROP TABLE Customer;
DROP TABLE HasSong;
DROP TABLE LeadSinger;
DROP TABLE Item;

-- Create tables:
CREATE TABLE Item (
upc INTEGER PRIMARY KEY,
title VARCHAR(40) NOT NULL,
type CHAR(3) NOT NULL CHECK (type IN('CD', 'DVD')),
category CHAR(12) NOT NULL CHECK (category IN('rock', 'pop', 'rap', 'country', 'classical', 'new age', 'instrumental')),
company VARCHAR(40),
year SMALLINT CHECK (year > 1500 AND year < 2500),
price FLOAT CHECK (price >= 0),
stock INTEGER CHECK (stock >= 0) );

CREATE TABLE LeadSinger (
upc INTEGER,
name VARCHAR(40),
PRIMARY KEY (upc, name),
FOREIGN KEY (upc) REFERENCES Item );

CREATE TABLE HasSong (
upc INTEGER,
title VARCHAR(40),
PRIMARY KEY (upc, title),
FOREIGN KEY (upc) REFERENCES Item );

CREATE TABLE Customer (
cid INTEGER PRIMARY KEY,
password CHAR(32) NOT NULL,
name VARCHAR(40) NOT NULL,
address VARCHAR(40),
phone VARCHAR(20),
UNIQUE (name, password));

CREATE TABLE Purchase(
receiptId INTEGER PRIMARY KEY,
cid INTEGER,
purchaseDate DATE,
card# VARCHAR(20),
expiryDate DATE,
expectedDate DATE,
deliveredDate DATE,
FOREIGN KEY (cid) REFERENCES Customer );

CREATE TABLE PurchaseItem (
receiptId INTEGER,
upc INTEGER,
quantity INTEGER CHECK (quantity > 0),
PRIMARY KEY (receiptId, upc),
FOREIGN KEY (receiptId) REFERENCES Purchase
ON DELETE CASCADE,
FOREIGN KEY (upc) REFERENCES Item );

CREATE TABLE Return (
retid INTEGER PRIMARY KEY,
receiptId INTEGER NOT NULL,
returnDate DATE,
FOREIGN KEY (receiptId) REFERENCES Purchase);

CREATE TABLE ReturnItem (
retid INTEGER,
upc INTEGER,
quantity INTEGER CHECK (quantity > 0),
PRIMARY KEY (retid, upc),
FOREIGN KEY (retid) REFERENCES Return,
FOREIGN KEY (upc) REFERENCES Item);

--Sequences to be able to generate customer, return ids, and purchase ids:
DROP SEQUENCE seq_upc;
DROP SEQUENCE seq_cid;
DROP SEQUENCE seq_receiptId;
DROP SEQUENCE seq_retid;

CREATE SEQUENCE seq_upc MINVALUE 1 START WITH 1 INCREMENT BY 1 CACHE 10;
CREATE SEQUENCE seq_cid MINVALUE 1 START WITH 1 INCREMENT BY 1 CACHE 10;
CREATE SEQUENCE seq_receiptId MINVALUE 1 START WITH 1 INCREMENT BY 1 CACHE 10;
CREATE SEQUENCE seq_retid MINVALUE 1 START WITH 1 INCREMENT BY 1 CACHE 10;
