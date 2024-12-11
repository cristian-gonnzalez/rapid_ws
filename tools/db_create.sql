--
-- My sql script to create db to test Rapid Ticket solution
--

-- Drop the db and create a new one
DROP DATABASE `dbtest`;
CREATE DATABASE `dbtest`;

-- Select the db created
USE `dbtest`;

-- Create the table Artist
CREATE TABLE IF NOT EXISTS `Artist` (
  `artistId` int NOT NULL AUTO_INCREMENT,
  `name`  varchar(50)  NOT NULL,
  PRIMARY KEY (`artistId`),
  UNIQUE (`name`)
) ;

-- Create the table Place (This represent the stadium or place where the artist can play)
CREATE TABLE IF NOT EXISTS `Place` (
  `placeId` int NOT NULL AUTO_INCREMENT,
  `name`  varchar(50)  NOT NULL,
  PRIMARY KEY (`placeId`),
  UNIQUE (`name`)
) ;

-- Create the table Concert (This represent the show or event where the artist play). 
-- NOTE: we cannot use the word show or event since it is a key word used by mysql
CREATE TABLE IF NOT EXISTS `Concert` (
  `artistId` int NOT NULL,
  `placeId` int NOT NULL,
  `concertDate` date NOT NULL,
  `hour` time NOT NULL,
  PRIMARY KEY (`artistId`, `placeId`, `concertDate`)
) ;

-- Add a fk pointing to the place and artist
ALTER TABLE `Concert`
  ADD CONSTRAINT `fk_Concert_1` FOREIGN KEY (`placeId`) REFERENCES `Place` (`placeId`),
  ADD CONSTRAINT `fk_Concert_2` FOREIGN KEY (`artistId`) REFERENCES `Artist` (`artistId`);


-- Create the table Concert Sector. This represent the sector o section that a place has. 
-- A Place has different sectors and it can changed in each show o concert. That's why I named it Concert sector
CREATE TABLE IF NOT EXISTS `ConcertSector` (
  `artistId` int NOT NULL,
  `placeId` int NOT NULL,
  `concertDate` date NOT NULL,
  `sectorId` int NOT NULL,
  `name`  varchar(50)  NOT NULL,
  `roomSpace` int NOT NULL,
  `occupiedSpace` int NOT NULL,
  `hasSeat` int NOT NULL,
  `price` double NOT NULL,
  PRIMARY KEY (`artistId`, `placeId`, `concertDate`, `sectorId`),
  UNIQUE (`artistId`, `placeId`, `concertDate`, `name`)
) ;

-- Add a fk to the Concert since it is related to the concert (show)
ALTER TABLE `ConcertSector`
  ADD CONSTRAINT `fk_ConcertSector_1` FOREIGN KEY (`artistId`, `placeId`, `concertDate`) REFERENCES `Concert` (`artistId`, `placeId`, `concertDate`);


-- Create table reserve that saves the reservation or booking of the concert
CREATE TABLE IF NOT EXISTS `Reserve` (
  `datetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `reserveId` int NOT NULL AUTO_INCREMENT,
  `artistId` int NOT NULL,
  `placeId` int NOT NULL,
  `concertDate` date NOT NULL,
  `sectorId` int NOT NULL,
  `qty` int NULL,
  `name` varchar(50) NOT NULL,
  `surname` varchar(50) NOT NULL,
  `dni` double NOT NULL,
  `total` double NOT NULL,
  PRIMARY KEY (`reserveId`, `artistId`, `placeId`, `concertDate`, `sectorId`)
);

-- Points the fk to the concert sector 
ALTER TABLE `Reserve`
  ADD CONSTRAINT `fk_Reserve_1` FOREIGN KEY (`artistId`, `placeId`, `concertDate`, `sectorId`) REFERENCES `ConcertSector` (`artistId`, `placeId`, `concertDate`, `sectorId`),
  ADD CONSTRAINT `fk_Reserve_2` FOREIGN KEY (`reserveId`, `artistId`, `placeId`, `concertDate`, `sectorId`) REFERENCES `Seat` (`reserveId`, `artistId`, `placeId`, `concertDate`, `sectorId`);

-- Create the table Seat.
-- The table saves the reserved seats that belongs to one concert sector in a concert date
CREATE TABLE IF NOT EXISTS `Seat` (
  `reserveId` int NOT NULL,
  `artistId` int NOT NULL,
  `placeId` int NOT NULL,
  `concertDate` date NOT NULL,
  `sectorId` int NOT NULL,
  `seatNumber` int NOT NULL,
  PRIMARY KEY (`reserveId`, `artistId`, `placeId`, `concertDate`, `sectorId`, `seatNumber`)
) ;


ALTER TABLE `Seat`
  ADD CONSTRAINT `fk_Seat_1` FOREIGN KEY (`reserveId`, `artistId`, `placeId`, `concertDate`, `sectorId`) REFERENCES `Reserve` (`reserveId`, `artistId`, `placeId`, `concertDate`, `sectorId`),
  ADD CONSTRAINT `fk_Reserve_2` FOREIGN KEY (`reserveId`, `artistId`, `placeId`, `concertDate`, `sectorId`) REFERENCES `Seat` (`reserveId`, `artistId`, `placeId`, `concertDate`, `sectorId`);