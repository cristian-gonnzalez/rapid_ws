package com.meli.backend.rapid.ws.services;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.meli.backend.rapid.common.CMutext;
import com.meli.backend.rapid.common.AppStatus.eRCode;
import com.meli.backend.rapid.req_ctx.ConcertRequestContext;
import com.meli.backend.rapid.req_ctx.DelReserveRequestContext;
import com.meli.backend.rapid.req_ctx.ReserveRequestContext;
import com.meli.backend.rapid.req_ctx.req_ctx_io.ConcertInput;
import com.meli.backend.rapid.req_ctx.req_ctx_io.ReserveOutput;
import com.meli.backend.rapid.ws.models.SectorRecord;
import com.meli.backend.rapid.ws.models.ConcertRecord;
import com.meli.backend.rapid.ws.models.ReserveRecord;
import com.meli.backend.rapid.ws.repositories.*;

@Service
public class ReserveService {

    ReserveRepository reserveRepository;
    ConcertRepository concertRepository;
    ConcertService concertService;

    public ReserveService() {
        this.reserveRepository = new ReserveRepository();
        this.concertRepository = new ConcertRepository();
        this.concertService = new ConcertService();
    }

    private ConcertRecord getSector( ReserveRequestContext ctx, String artist, String place, Date concertDate, String sectorname  ) {
        List<ConcertRecord> concerts = null;
        try {
            ConcertInput input = new ConcertInput();
            input.setArtist(artist);
            input.setPlace(place);
            input.setConcertDate(concertDate);

            concerts = concertRepository.getConcerts( new ConcertRequestContext(input));        
        }
        catch( Exception e) {
        }

        if( concerts == null || concerts.size() == 0) {
            ctx.setError(eRCode.valueNotFound, "Concert not found");
            return null;
        }
        
        boolean found = false;
        ConcertRecord concert = concerts.get(0);
        List<SectorRecord> sectors = concerts.get(0).getSectors();

        for(int i = 0;  i < sectors.size(); i++)
        {
            SectorRecord s = sectors.get(i);
                
            if(s.getName().equals( sectorname ) ) {
                List<SectorRecord> aux =  new ArrayList<>();
                aux.add(s);
                concert.setSectors(aux);

                found = true;
                break;
            }
        }

        if( !found ) {
            ctx.setError(eRCode.valueNotFound, "Sector not found");
            return null;
        }

        return concert;
    }

    private void buildTicketReserve( ReserveOutput reserve, ReserveRequestContext ctx, SectorRecord s ) {

        com.meli.backend.rapid.req_ctx.req_ctx_io.ReserveOutput.ConcertInfo concertinfo = reserve.getConcertInfo();
        concertinfo.setArtist( ctx.input.getArtist() );
        concertinfo.setPlace( ctx.input.getPlace() );
        concertinfo.setConcertDate(ctx.input.getConcertDate() );
        concertinfo.setSector(concertService.sectorRecordToOutput(s));

        com.meli.backend.rapid.req_ctx.req_ctx_io.ReserveOutput.UserInfo  userinfo = reserve.getUserInfo();
        userinfo.setName(ctx.input.getName());
        userinfo.setSurname(ctx.input.getSurname());
        userinfo.setDNI(ctx.input.getDNI());

    }

    /** Make the reserve of the concert
     * 
     * @param ctx Reserce request context.
     */
    public void reserveConcert( ReserveRequestContext ctx) {

        ReserveOutput reserve = (ReserveOutput)ctx.output.getData();
        
        synchronized(CMutext.getInstance()) {

            ConcertRecord c = getSector(ctx, ctx.input.getArtist(), 
                                             ctx.input.getPlace(), 
                                             ctx.input.getConcertDate(), 
                                             ctx.input.getSector() );
            if(ctx.isOnError())
                return;

            List<SectorRecord> sectors = c.getSectors();
            SectorRecord s = sectors.get(0);

            com.meli.backend.rapid.req_ctx.req_ctx_io.ReserveOutput.Total total = reserve.getTotalInfo();            
            double total_amount = 0;
            if( s.getHasSeat() ) {
                if( ctx.input.getQuantity() != null) {
                    ctx.setError(eRCode.invalidFields, "seats field is required");
                    return;
                }
                
                List<Integer> reservedseats = s.getSeats();
                List<Integer> seats = ctx.input.getSeats();

                int newOcupiedSpace =  s.getOccupiedSpace() + seats.size();
                if( newOcupiedSpace > s.getRoomSpace() ) {
                    ctx.setError(eRCode.noRoomAvailable, "No room is availble");
                    return;
                }

                for(int i=0; i<reservedseats.size(); i++) {
                    for(int j=0; j<seats.size(); j++) {            
                        
                        if( seats.get(j) <= 0 || seats.get(j) > seats.size() ) {
                            ctx.setError(eRCode.invalidFields, "Seat number out of bounds");
                            return;   
                        }
                        
                        if( reservedseats.get(i).equals(seats.get(j)) ) {
                            ctx.setError(eRCode.seatReserved, "Seat number is ocuppied");
                            return;
                        }
                    }    
                }

                total_amount = s.getPrice() * seats.size();

                total.setQuantity(seats.size());
                
                s.setSeats(seats);
                s.setOccupiedSpace(newOcupiedSpace);
            }
            else {

                if( ctx.input.getSeats() != null) {
                    ctx.setError(eRCode.invalidFields, "qty field is required");
                    return;
                }

                int newOcupiedSpace = s.getOccupiedSpace() + ctx.input.getQuantity() ;
                if( s.getOccupiedSpace() + ctx.input.getQuantity() > s.getRoomSpace() ) {
                    ctx.setError(eRCode.noRoomAvailable, "No room is availble");
                    return;
                }

                total.setQuantity(ctx.input.getQuantity());
                total_amount = s.getPrice() * ctx.input.getQuantity();

                s.setOccupiedSpace(newOcupiedSpace);
            }

            total.setPrice(s.getPrice());
            total.setTotal(total_amount);

            if( ctx.isOnError() ) {
                return;
            }
            
            buildTicketReserve(reserve, ctx, s);
            
            try {
                Boolean r = reserveRepository.saveReserve( ctx, reserve, s );
                if( !r ) {
                    ctx.setError(eRCode.failToApllyEffect, "Error when saving reserve");
                }    
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    
    public void deleteReserve( DelReserveRequestContext ctx ) throws SQLException {

        ReserveRecord rec = reserveRepository.getReserve( ctx );
        if(rec == null ){
            ctx.setError(eRCode.valueNotFound, "Reserve not found");
            return;
        }
        synchronized(CMutext.getInstance()) {
            try {

                int occupiedSpace = rec.getConcert().getSectors().get(0).getOccupiedSpace();
                occupiedSpace -= rec.getQuantity();
                rec.getConcert().getSectors().get(0).setOccupiedSpace(occupiedSpace);
                
                reserveRepository.deleteReserve( rec );           
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
       
    }
}
