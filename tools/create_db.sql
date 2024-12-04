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
  PRIMARY KEY (`artistId`)
) ;

-- Insert test values
INSERT INTO `Artist` (`artistId`, `name`) VALUES
(1, 'ARTIST_1' ),
(2, 'ARTIST_2' ),
(3, 'ARTIST_3' ),
(4, 'ARTIST_4' ),
(5, 'ARTIST_5' );


-- Create the table Place (This represent the stadium or place where the artist can play)
CREATE TABLE IF NOT EXISTS `Place` (
  `placeId` int NOT NULL AUTO_INCREMENT,
  `name`  varchar(50)  NOT NULL,
  PRIMARY KEY (`placeId`)
) ;


-- Insert test values
INSERT INTO `Place` (`placeId`, `name`) VALUES
(1, 'PLACE_1' ),
(2, 'PLACE_2' ),
(3, 'PLACE_3' ),
(4, 'PLACE_4' ),
(5, 'PLACE_5' );

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

-- Insert test values
INSERT INTO `Concert` (`artistId`, `placeId`, `concertDate`, `hour` ) VALUES 
(1, 1, '2025-01-01', '09:00'),
(1, 1, '2025-01-02', '09:00'),
(1, 1, '2025-01-03', '09:00'),
(1, 1, '2025-01-04', '09:00'),
(1, 1, '2025-01-05', '09:00'),
(1, 1, '2025-01-06', '09:00'),
(1, 3, '2025-02-06', '09:00'),
(1, 4, '2025-01-06', '09:00'),
(1, 5, '2025-01-06', '09:00'),
(2, 2, '2025-02-01', '09:00'),
(2, 1, '2025-05-01', '09:00'),
(2, 2, '2025-06-01', '09:00'),
(2, 2, '2025-01-01', '09:00'),
(2, 5, '2026-01-01', '09:00');

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
  PRIMARY KEY (`artistId`, `placeId`, `concertDate`, `sectorId`)
) ;

-- Add a fk to the Concert since it is related to the concert (show)
ALTER TABLE `ConcertSector`
  ADD CONSTRAINT `fk_ConcertSector_1` FOREIGN KEY (`artistId`, `placeId`, `concertDate`) REFERENCES `Concert` (`artistId`, `placeId`, `concertDate`);
    
INSERT INTO `ConcertSector` (`artistId`, `placeId`, `concertDate`, `sectorId`, `name`, `roomSpace`, `occupiedSpace`, `hasSeat`, `price`) VALUES
(1, 1, '2025-01-01', 1, "A1P1S1", 5, 0, 0, 200000.00),
(1, 1, '2025-01-01', 2, "A1P1S2", 5, 2, 1, 500000.00),
(1, 1, '2025-01-01', 3, "A1P1S3", 5, 0, 0, 200000.00),
(1, 1, '2025-01-02', 1, "A1P1S1", 5, 0, 0, 200000.00),
(2, 2, '2025-02-01', 1, "A1P2S1", 5, 0, 0, 200000.00),
(1, 3, '2025-02-06', 2, "A1P3S2", 5, 0, 1, 250000.00),
(1, 4, '2025-01-06', 2, "A1P4S2", 5, 0, 1, 200000.00),
(1, 1, '2025-01-06', 2, "A1P4S2", 5, 0, 1, 200000.00),
(2, 5, '2026-01-01', 2, "A2P5S2", 5, 0, 1, 350000.00),
(2, 2, '2025-01-01', 2, "A2P2S2", 5, 1, 1, 200000.00),
(2, 2, '2025-06-01', 2, "A2P2S2", 5, 0, 1, 201000.00),
(1, 1, '2025-01-03', 2, "A2P2S2", 5, 0, 1, 201000.00),
(1, 1, '2025-01-04', 2, "A2P2S2", 5, 0, 1, 201000.00),
(1, 1, '2025-01-05', 2, "A2P2S2", 5, 0, 1, 201000.00),
(1, 5, '2025-01-06', 2, "A2P2S2", 5, 0, 1, 201000.00),
(2, 1, '2025-05-01', 2, "A2P2S2", 5, 0, 1, 201000.00);

-- Create the table Seat.
-- The table saves the reserved seats that belongs to one concert sector in a concert date
CREATE TABLE IF NOT EXISTS `Seat` (
  `artistId` int NOT NULL,
  `placeId` int NOT NULL,
  `concertDate` date NOT NULL,
  `sectorId` int NOT NULL,
  `seatNumber` int NOT NULL,
  PRIMARY KEY (`artistId`, `placeId`, `concertDate`, `sectorId`, `seatNumber`)
) ;

-- Point the fk to the concert sector
ALTER TABLE `Seat`
  ADD CONSTRAINT `fk_Seat_1` FOREIGN KEY (`artistId`, `placeId`, `concertDate`, `sectorId`) REFERENCES `ConcertSector` (`artistId`, `placeId`, `concertDate`, `sectorId`);

-- Insert tests values
INSERT INTO `Seat` (`artistId`, `placeId`, `concertDate`, `sectorId`, `seatNumber`) VALUES 
(1, 1, '2025-01-01', 2, 1),
(1, 1, '2025-01-01', 2, 2),
(2, 2, '2025-01-01', 2, 1);

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
  ADD CONSTRAINT `fk_Reserve_1` FOREIGN KEY (`artistId`, `placeId`, `concertDate`, `sectorId`) REFERENCES `ConcertSector` (`artistId`, `placeId`, `concertDate`, `sectorId`);
