--
-- My sql script to create db to test Rapid Ticket solution
--
-- Insert test values
INSERT INTO `Artist` (`artistId`, `name`) VALUES
(1, 'ARTIST_1' ),
(2, 'ARTIST_2' ),
(3, 'ARTIST_3' ),
(4, 'ARTIST_4' ),
(5, 'ARTIST_5' );


-- Insert test values
INSERT INTO `Place` (`placeId`, `name`) VALUES
(1, 'PLACE_1' ),
(2, 'PLACE_2' ),
(3, 'PLACE_3' ),
(4, 'PLACE_4' ),
(5, 'PLACE_5' );

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
    
INSERT INTO `ConcertSector` (`artistId`, `placeId`, `concertDate`, `sectorId`, `name`, `roomSpace`, `occupiedSpace`, `hasSeat`, `price`) VALUES
(1, 1, '2025-01-01', 1, "A1P1S1", 5, 0, 0, 200000.00),
(1, 1, '2025-01-01', 2, "A1P1S2", 5, 2, 1, 500000.00),
(1, 1, '2025-01-01', 3, "A1P1S3", 5, 0, 0, 200000.00),
(1, 1, '2025-01-02', 1, "A1P1S1", 5, 0, 0, 200000.00),
(2, 2, '2025-02-01', 1, "A2P2S1", 5, 0, 0, 200000.00),
(1, 3, '2025-02-06', 2, "A1P3S2", 5, 0, 1, 250000.00),
(1, 4, '2025-01-06', 2, "A1P4S2", 5, 0, 1, 200000.00),
(1, 1, '2025-01-06', 2, "A1P1S2", 5, 0, 1, 200000.00),
(2, 5, '2026-01-01', 2, "A2P5S2", 5, 0, 1, 350000.00),
(2, 2, '2025-01-01', 2, "A2P2S2", 5, 1, 1, 200000.00),
(2, 2, '2025-06-01', 2, "A2P2S2", 5, 0, 1, 201000.00),
(1, 1, '2025-01-03', 2, "A1P1S2", 5, 0, 1, 201000.00),
(1, 1, '2025-01-04', 2, "A1P1S2", 5, 0, 1, 201000.00),
(1, 1, '2025-01-05', 2, "A1P1S2", 5, 0, 1, 201000.00),
(1, 5, '2025-01-06', 2, "A1P5S2", 5, 0, 1, 201000.00),
(2, 1, '2025-05-01', 2, "A2P1S2", 5, 0, 1, 201000.00);

-- Insert tests values
-- INSERT INTO `Seat` (`artistId`, `placeId`, `concertDate`, `sectorId`, `seatNumber`) VALUES 
-- (1, 1, '2025-01-01', 2, 1),
-- (1, 1, '2025-01-01', 2, 2),
-- (2, 2, '2025-01-01', 2, 1);

