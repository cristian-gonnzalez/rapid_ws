package com.meli.backend.rapid.req_ctx.concert;


public class SectorInput {
    
    private String name;

    private Double price;

    private Integer roomSpace;

    private Integer occupiedSpace;

    private Boolean hasSeat;

    public SectorInput() {
    }

    public void setPrice(Double price ) {
        this.price = price;
    }

    public Double getPrice() {
        return this.price;
    }

    public void setRoomSpace(Integer roomSpace ) {
        this.roomSpace = roomSpace;
    }
    
    public Integer getRoomSpace() {
        return this.roomSpace;
    }
    
    public void setOccupiedSpace(Integer occupiedSpace ) {
        this.occupiedSpace = occupiedSpace;
    }

    public Integer getOccupiedSpace() {
        return this.occupiedSpace;
    }

    public void setHasSeat(Boolean hasSeat ) {
        this.hasSeat = hasSeat;
    }

    public Boolean getHasSeat() {
        return this.hasSeat;
    }

    public void setName(String name ) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }


}
