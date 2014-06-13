
DROP TABLE Item;
CREATE TABLE Item(
	upc 		INTEGER not null,
	taxable 	VARCHAR(3),
	sellPrice 	DECIMAL,
	type 		VARCHAR(20) check (type in ('book', 'score', 'cdvd')),
	PRIMARY KEY (upc))
	
DROP TABLE Book;
CREATE TABLE Book (
	upc 		INTEGER not null REFERENCES Item,
	title 		VARCHAR(20),
	publisher 	VARCHAR(20),
	PRIMARY KEY (book_upc));
	
DROP TABLE HasAuthor; 
CREATE TABLE HasAuthor (
	upc 		INTEGER not null REFERENCES Book,
	name 		VARCHAR(20) not null,
	PRIMARY KEY (upc, name));
	
DROP TABLE Score;
CREATE TABLE Score (
	upc 		INTEGER not null REFERENCES Item,
	title 		VARCHAR(20),
	publisher 	VARCHAR(20),
	PRIMARY KEY (upc));
	
DROP TABLE HasComposer;
CREATE TABLE HasComposer (
	upc			INTEGER not null REFERENCES Score,
	name 		VARCHAR(20) not null, 
	PRIMARY KEY (upc, name));

DROP TABLE CDVD1;
CREATE TABLE CDVD1(
	upc 		INTEGER not null REFERENCES Item,
	title 		VARCHAR(20),
	type 		VARCHAR(20) check (type in ('cd', 'dvd')),
	category	VARCHAR(20),
	year		DATE,
	PRIMARY KEY (upc));

	-- this has been broken to ensure BCNF 
DELETE TABLE CategoryCompany;
CREATE TABLE CategoryCompany(
	category	VARCHAR(20) not null,
	company 	VARCHAR(20),
	PRIMARY KEY (category));
	
DROP TABLE LeadingSinger;
CREATE TABLE LeadingSinger(
	upc 		INTEGER not null REFERENCES CDVD,
	name 		VARCHAR(20) not null, 
	PRIMARY KEY (upc, name));

DROP TABLE HasSong;
CREATE TABLE CDVD(
	upc 		INTEGER not null REFERENCES CDVD,
	title		VARCHAR(20) not null,
	PRIMARY KEY (upc, title));

DELETE TABLE Store;
CREATE TABLE Store(
	name		VARCHAR(40) not null,
	address		VARCHAR(40),
	type		VARCHAR(20) check (type in ('regular', 'warehouse')),
	PRIMARY KEY (name));
	
DELETE TABLE Stored;
CREATE TABLE Stored(
	name		VARCHAR(40) not null REFERENCES Store,
	upc 		INTEGER not null REFERENCES Item,
	stock		INTEGER,
	PRIMARY KEY (name, upc));

CREATE VIEW RegularStore 
	AS SELECT * 
	FROM Store 
	WHERE type = 'regular';

CREATE VIEW Warehouse
	AS SELECT * 
	FROM Store 
	WHERE type = 'warehouse';

DELETE TABLE Supplier;
CREATE TABLE Supplier(
	name 		VARCHAR(20) not null,
	address		VARCHAR(40),
	city		VARCHAR(20),
	status 		VARCHAR(20) check (status in ('active', 'inactive')),
	PRIMARY KEY (name));
	
DELETE TABLE Supplies;
CREATE TABLE Supplies(
	name 		VARCHAR(20) not null REFERENCES Supplier,
	upc 		INTEGER not null REFERENCES Item,
	supPrice	DECIMAL,
	PRIMARY KEY (name, upc));
	
DELETE TABLE Shipment1;
	sid			INTEGER not null,
	supName 	VARCHAR(20) REFERENCES Supplier(name),
	storeName	VARCHAR(20)	REFERENCES Store(name),
	date		DATE,
	upc			INTEGER not null REFERENCES Item,
	quantity	INTEGER,
	PRIMARY KEY (sid));

-- DELETE TABLE ShipItem;
-- CREATE TABLE ShipItem(
	-- sid 		INTEGER not null REFERENCES Shipment,
	-- category	VARCHAR(20),
	-- company 	VARCHAR(20),
	-- PRIMARY KEY (sid, upc));
	
DELETE TABLE Purchase;
CREATE TABLE Purchase(
	receiptId	INTEGER not null,
	date		DATETIME,
	cid			INTEGER REFERENCES Customer,
	name		VARCHAR(20) REFERENCES Store,
	cardNo		INTEGER,
	expire		DATE,
	deliveredDate DATE,
	PRIMARY KEY (receiptId));
	-- there are additional constraints here depends on name
	
DELETE TABLE PurchaseItem;
CREATE TABLE PurchaseItem(
	receiptId	INTEGER not null REFERENCES Purchase,
	upc			INTEGER not null REFERENCES Item,
	quantity	INTEGER,
	PRIMARY KEY (receiptId, upc));

DELETE TABLE Return;
CREATE TABLE Return(
	retid		INTEGER not null,
	date		DATETIME,
	receiptId	INTEGER REFERENCES Purchase,
	name		VARCHAR(20) REFERENCES Store,
	PRIMARY KEY (retid));
	
DELETE TABLE ReturnItem;
CREATE TABLE ReturnItem(
	retid		INTEGER not null REFERENCES Return,
	upc			INTEGER not null REFERENCES Item,
	quantity	INTEGER,
	PRIMARY KEY (retid, upc));
	
DELETE TABLE Customer;
CREATE TABLE Cutomer(
	cid			INTEGER not null,
	name		VARCHAR(20);
	address		VARCHAR(20);
	PRIMARY KEY (cid));
	
	