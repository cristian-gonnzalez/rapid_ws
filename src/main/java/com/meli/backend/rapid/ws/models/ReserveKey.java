package com.meli.backend.rapid.ws.models;

public class ReserveKey {

    int reserveId;

    SectorKey sectorKey;

    public ReserveKey() {
    }

    public void setReserveId( int reserveId ) {
        this.reserveId = reserveId;
    }

    public int getReserveId() {
        return this.reserveId;
    }

    public void setSectorKey( SectorKey sectorKey ) {
        this.sectorKey = sectorKey;
    }

    public SectorKey getSectorKey() {
        return this.sectorKey;
    }
}
