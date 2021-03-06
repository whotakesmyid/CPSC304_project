-- <- use this syntax to create comments in .sql files :)
-- should be done now, how it looks like in data/table form,
-- go look at google drive

INSERT INTO Item
VALUES (123456789, 'Thriller', 'CD', 'pop', 'That company', 1982, 13.5, 9);

INSERT INTO Item
VALUES (123456792, 'Their Greatest Hits', 'CD', 'rock', 'Big Company', 1976, 30,	39);

INSERT INTO Item
VALUES (123456795, 'Back in Black', 'CD', 'rock', 'Record Company',	1980, 40.21, 0);
	
INSERT INTO Item
VALUES (123456798, 'Saturday Night Fever', 'DVD', 'pop', 'Produce Company', 1977, 35, 299);

INSERT INTO Item
VALUES (123456801, 'Rumours', 'DVD', 'rock', 'Music Company', 1977, 19.3, 49);

INSERT INTO Item
VALUES (123456804, 'The Dark Side of the Moon', 'CD', 'rock', NULL, 1973, 10.23, 29);

INSERT INTO Item
VALUES (123456807, 'Sing that Song', 'DVD', 'pop', 'Yeah Company', 1992, 0, 29);

INSERT INTO LeadSinger
VALUES (123456789, 'Michael Jackson');

INSERT INTO LeadSinger
VALUES (123456792, 'Eagles');

INSERT INTO LeadSinger
VALUES (123456795, 'AC/DC');

INSERT INTO LeadSinger
VALUES (123456798, 'Bee Gees ');

INSERT INTO LeadSinger
VALUES (123456801, 'Fleetwood Mac');

INSERT INTO LeadSinger
VALUES (123456804, 'Pink Floyd');

INSERT INTO LeadSinger
VALUES (123456807, 'Whitney Houston ');

INSERT INTO HasSong
VALUES (123456789, 'Thriller');

INSERT INTO HasSong
VALUES (123456789, 'Beat It');

INSERT INTO HasSong
VALUES (123456789, 'Billie Jean');

INSERT INTO HasSong
VALUES (123456792, 'Tequila Sunrise');

INSERT INTO HasSong
VALUES (123456792, 'Desperado');

INSERT INTO HasSong
VALUES (123456795, 'Hells Bells');

INSERT INTO HasSong
VALUES (123456795, 'You Shook Me All Night Long');

INSERT INTO HasSong
VALUES (123456798, 'Back In Black');

INSERT INTO HasSong
VALUES (123456798, 'Stayin Alive');

INSERT INTO HasSong
VALUES (123456798, 'Night Fever');

INSERT INTO HasSong
VALUES (123456801, 'How Deep Is Your Love');

INSERT INTO HasSong
VALUES (123456801, 'Go Your Own Way');

INSERT INTO HasSong
VALUES (123456801, 'You Make Loving Fun');

INSERT INTO HasSong
VALUES (123456804, 'Dreams');

INSERT INTO HasSong
VALUES (123456804, 'Time');

INSERT INTO HasSong
VALUES (123456804, 'Eclipse');

INSERT INTO HasSong
VALUES (123456807, 'I Will Always Love You');

INSERT INTO HasSong
VALUES (123456807, 'Run To You');

INSERT INTO Customer
VALUES (100000, ORA_HASH('pass'), 'Maria', '123 My Way Vancouver', '867-436-9423');

INSERT INTO Customer
VALUES (105000, ORA_HASH('ipad'), 'Charles', '321 This Street, Vancouver', NULL);

INSERT INTO Customer
VALUES (110000, ORA_HASH('gaming'), 'Jennie', '456 Other Place, Burnaby', '905-607-0475');

INSERT INTO Customer
VALUES (115000, ORA_HASH('latenight'), 'Rodrigo', '294 That Road, Victoria', '250-893-5749');

INSERT INTO Customer
VALUES (120000, ORA_HASH('jennie'), 'George', '2 Bleh, CityName', '712-455-8804');

INSERT INTO Customer
VALUES (125000, ORA_HASH('charlies'), 'Hunter', NULL, '603-528-2483');

INSERT INTO Customer
VALUES (130000, ORA_HASH('rod'), 'Chris', '2835 Yorkie Lane, Waycross, GA 31501', '203-870-2025');

INSERT INTO Customer
VALUES (135000, ORA_HASH('database'), 'Cornell', '2826 Abner Road', '304-652-4682');

INSERT INTO Customer
VALUES (140000, ORA_HASH('sequel'), 'Greta', '2369 Quayside Dr', '416-875-0823');

INSERT INTO Customer
VALUES (145000, ORA_HASH('java'), 'Justine', '1998 Park Ct. Cadomin, AB T0E 0E0', '647-201-9210');

INSERT INTO Customer
VALUES (150000, ORA_HASH('weekend'), 'Greg', '3382 49th Avenue Nanisivik', '604-866-7902');

INSERT INTO Purchase
VALUES (3029000, 100000, NULL, NULL, NULL, NULL, NULL);

INSERT INTO Purchase
VALUES (3030000, 105000, NULL, NULL, NULL, NULL, NULL);

INSERT INTO Purchase
VALUES (3031000, 110000, NULL, NULL, NULL, NULL, NULL);

INSERT INTO Purchase
VALUES (3032000, 115000, NULL, NULL, NULL, NULL, NULL);

INSERT INTO Purchase
VALUES (3033000, 120000, NULL, NULL, NULL, NULL, NULL);

INSERT INTO Purchase
VALUES (3034000, 125000, NULL, NULL, NULL, NULL, NULL);

INSERT INTO Purchase
VALUES (3035000, 130000, NULL, NULL, NULL, NULL, NULL);

INSERT INTO Purchase
VALUES (3036000, 135000, NULL, NULL, NULL, NULL, NULL);

INSERT INTO Purchase
VALUES (3037000, 140000, NULL, NULL, NULL, NULL, NULL);

INSERT INTO Purchase
VALUES (3038000, 145000, NULL, NULL, NULL, NULL, NULL);

INSERT INTO Purchase
VALUES (3039000, 150000, NULL, NULL, NULL, NULL, NULL);

INSERT INTO Purchase
VALUES (3040000, 100000, '2013-01-18', 4532867091339, '2018-10-01', '2013-01-28', '2013-01-30');

INSERT INTO Purchase
VALUES (3041000, 105000, '2013-01-18', 4532867091340, '2018-10-01', '2013-01-28', '2013-01-27');

INSERT INTO Purchase
VALUES (3042000, 110000, '2013-01-18', 4532867091341, '2018-10-01', '2013-01-28', '2013-01-28');

INSERT INTO Purchase
VALUES (3043000, 115000, '2011-01-03', 4532867091342, '2018-10-01', NULL, '2013-01-28');

INSERT INTO Purchase
VALUES (3044000, 120000, '2011-01-03', NULL, '2018-10-01', '2011-01-17', '2011-01-17');

INSERT INTO Purchase
VALUES (3045000, 125000, '2011-01-03', 4532867091344, '2018-10-01', '2011-01-17', NULL);

INSERT INTO Purchase
VALUES (3046000, 130000, '2011-01-03', 4532867091345, '2018-10-01', '2011-01-17', '2011-01-21');

INSERT INTO Purchase
VALUES (3047000, 135000, NULL, 4532867091346, '2018-10-01', '2011-01-17', '2011-01-10');

INSERT INTO Purchase
VALUES (3048000, 140000, '2012-12-01', 4532867091347, NULL, '2012-12-17', '2012-12-17');

INSERT INTO Purchase
VALUES (3049000, 145000, '2012-12-01', 4532867091348, '2018-10-01', '2012-12-17', '2012-12-30');

INSERT INTO Purchase
VALUES (3050000, 150000, '2011-10-31', 4532867091349, '2018-10-01', '2011-11-14', '2011-11-14');

INSERT INTO Purchase
VALUES (3051000, NULL, '2011-10-31', 4532867091350, '2018-10-01', '2011-11-14', '2011-11-16');

INSERT INTO Purchase
VALUES (3052000, NULL, '2011-10-31', 4532867091351, '2018-10-01', NULL, NULL);

INSERT INTO Purchase
VALUES (3053000, NULL, '2011-10-31', NULL, NULL, NULL, NULL);

INSERT INTO PurchaseItem
VALUES (3032000, 123456801, 8);

INSERT INTO PurchaseItem
VALUES (3032000, 123456804, 2);

INSERT INTO PurchaseItem
VALUES (3032000, 123456807, 1);

INSERT INTO PurchaseItem
VALUES (3032000, 123456789, 3);

INSERT INTO PurchaseItem
VALUES (3032000, 123456792, 5);	

INSERT INTO PurchaseItem
VALUES (3032000, 123456795, 4);	

INSERT INTO PurchaseItem
VALUES (3032000, 123456798, 3);	

INSERT INTO PurchaseItem
VALUES (3040000, 123456789, 3);

INSERT INTO PurchaseItem
VALUES (3041000, 123456792, 6);

INSERT INTO PurchaseItem
VALUES (3042000, 123456795, 2);

INSERT INTO PurchaseItem
VALUES (3043000, 123456798, 6);

INSERT INTO PurchaseItem
VALUES (3043000, 123456801, 8);

INSERT INTO PurchaseItem
VALUES (3043000, 123456804, 2);

INSERT INTO PurchaseItem
VALUES (3043000, 123456807, 1);

INSERT INTO PurchaseItem
VALUES (3043000, 123456789, 3);

INSERT INTO PurchaseItem
VALUES (3044000, 123456801, 8);

INSERT INTO PurchaseItem
VALUES (3045000, 123456804, 2);

INSERT INTO PurchaseItem
VALUES (3046000, 123456807, 1);

INSERT INTO PurchaseItem
VALUES (3047000, 123456789, 3);

INSERT INTO PurchaseItem
VALUES (3048000, 123456792, 5);	

INSERT INTO PurchaseItem
VALUES (3049000, 123456795, 4);	

INSERT INTO PurchaseItem
VALUES (3050000, 123456798, 3);	

INSERT INTO PurchaseItem
VALUES (3051000, 123456798, 1);	

INSERT INTO PurchaseItem
VALUES (3052000, 123456804, 500);	

INSERT INTO PurchaseItem
VALUES (3053000, 123456807, 1);

INSERT INTO Return 
VALUES (103040, 3040000, '2013-01-31');

INSERT INTO Return 
VALUES (103041, 3041000, '2013-01-21');

INSERT INTO Return 
VALUES (103042, 3042000, '2013-01-26');

INSERT INTO Return 
VALUES (103043, 3043000, '2011-01-12');

INSERT INTO Return 
VALUES (103044, 3044000, '2011-01-04');

INSERT INTO Return 
VALUES (103045, 3045000, '2011-01-06');

INSERT INTO Return 
VALUES (103046, 3046000, '2011-01-14');

INSERT INTO ReturnItem
VALUES (103040, 123456789, 2);

INSERT INTO ReturnItem
VALUES (103041, 123456792, 3);

INSERT INTO ReturnItem
VALUES (103042, 123456795, 1);

INSERT INTO ReturnItem
VALUES (103043, 123456798, 2);

INSERT INTO ReturnItem
VALUES (103044, 123456801, 5);

INSERT INTO ReturnItem
VALUES (103045, 123456804, 2);

INSERT INTO ReturnItem
VALUES (103046, 123456807, 1);

