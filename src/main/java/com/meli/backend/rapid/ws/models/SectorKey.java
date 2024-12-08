package com.meli.backend.rapid.ws.models;

public class SectorKey {

    ConcertKey concertKey;

    int sectorId;

    public SectorKey() {
    }

    public void setConcerKey( ConcertKey concertKey ) {
        this.concertKey = concertKey;
    }
    public ConcertKey getConcerKey() {
        return this.concertKey;
    }

    public void setSectorId( int serctorId ) {
        this.sectorId = serctorId;
    }
    public int getSectorId() {
        return this.sectorId;
    }

}
