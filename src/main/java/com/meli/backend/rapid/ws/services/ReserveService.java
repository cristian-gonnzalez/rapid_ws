package com.meli.backend.rapid.ws.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.meli.backend.rapid.common.CMutext;
import com.meli.backend.rapid.common.AppStatus.eRCode;
import com.meli.backend.rapid.req_ctx.DelReserveRequestContext;
import com.meli.backend.rapid.req_ctx.ReserveRequestContext;
import com.meli.backend.rapid.req_ctx.req_ctx_io.ReserveOutput;
import com.meli.backend.rapid.ws.models.ConcertSector;
import com.meli.backend.rapid.ws.models.TicketReserve;
import com.meli.backend.rapid.ws.models.TicketReserve.Concert;
import com.meli.backend.rapid.ws.models.TicketReserve.Total;
import com.meli.backend.rapid.ws.models.TicketReserve.User;
import com.meli.backend.rapid.ws.repositories.*;

@Service
public class ReserveService {

    ReserveRepository reserveRepository;
    ConcertRepository concertRepository;
    ConcertSectorRepository concertSectorRepository;
    
    public ReserveService() {
        this.reserveRepository = new ReserveRepository();
        concertRepository = new ConcertRepository();
        concertSectorRepository = new ConcertSectorRepository();
    }

    private double calculateSectorWithSeats( ReserveRequestContext ctx, ConcertSector cs ) {
        
        List<Integer> seats = ctx.input.getSeats();
        
        // checks that there is room for the seats
        if( cs.getOccupiedSpace() + seats.size() > cs.getRoomSpace() ) {
            ctx.setError(eRCode.noRoomAvailable , "No space available. The free space for sector "+ ctx.input.getSector() +" is " + ( cs.getRoomSpace() - cs.getOccupiedSpace() ) );
            return -1;
        }
        
        List<Integer> ocupies = new ArrayList<>();

        List<Integer> reserveds = cs.getSeats();
        for(int i = 0; i<reserveds.size(); i++) {
            for(int j = 0; j<seats.size(); j++) {
 
                /* checks that the seat num is not reserved */
                if(seats.get(j) == reserveds.get(i) ) {
                    ocupies.add(seats.get(j));
                }

                /* checks that the seat num is between the 1 and size of room space */
                if( seats.get(j) <= 0 || seats.get(j) > cs.getRoomSpace() ) {
                    ctx.setError(eRCode.invalidFields, "Invalid seat number for "+ ctx.input.getSector());
                    return -1;
                }
            }
        }

        if( !ocupies.isEmpty() ) {
            String errmsg ="The seats [";
            for(int i = 0; i<ocupies.size(); i++) {
                errmsg += ocupies.get(i);
                if(i != ocupies.size()-1) {
                    errmsg +=", ";
                }
            }
            errmsg +="] are reserved";
            
            ctx.setError( eRCode.seatReserved , errmsg);
            return -1;
        }

        double total_amount = cs.getPrice() * ctx.input.getSeats().size();

        return total_amount;
    }

    private double calculateSectorWithQty( ReserveRequestContext ctx, ConcertSector cs ) {

        if( cs.getHasSeat() ) {
            ctx.setError( eRCode.invalidFields, "Invalid field contents. The sector " + ctx.input.getSector() + " required to specify seat numbers" );
            return -1;
        }

        if( cs.getOccupiedSpace() + ctx.input.getQuantity() > cs.getRoomSpace() ) {
            ctx.setError( eRCode.noRoomAvailable, "No space available. The free space for sector "+ ctx.input.getSector() +" is " + ( cs.getRoomSpace() - cs.getOccupiedSpace() ) );
            return -1;
        }

        double total_amount = cs.getPrice() * ctx.input.getQuantity();

        return total_amount;
    }


    /** Make the reserve of the concert
     * 
     * @param ctx Reserce request context.
     */
    public void reserveConcert( ReserveRequestContext ctx) {

        TicketReserve reserve = ((ReserveOutput)( ctx.output.getData() ) ).getTicketReserve();
        ConcertSector cs;

        synchronized(CMutext.getInstance()) {

            if( !concertRepository.existsConcert( ctx.input.getArtist(), ctx.input.getPlace(), ctx.input.getConcertDate(), ctx.input.getSector() ) ) {
                ctx.setError( eRCode.valueNotFound, "No concert found for artist: " + ctx.input.getArtist() + 
                                                             " place: " + ctx.input.getPlace() + 
                                                             " concertDate: " + ctx.input.getConcertDate() + 
                                                             " sector: " + ctx.input.getSector() );
                return;
            }
        
            int artistId = concertRepository.getArtistID(ctx.input.getArtist());
            int placeId = concertRepository.getPlaceID(ctx.input.getPlace());
            int sectorId = concertRepository.getSectorID(artistId, placeId, ctx.input.getConcertDate(), ctx.input.getSector());
        
            List<ConcertSector> sectors = concertSectorRepository.getConcertSectors(null, artistId, placeId, ctx.input.getConcertDate(), ctx.input.getSector());
            cs = sectors.get(0);

            double total_amount = 0;
            int qty = 0;
            if( ctx.input.getQuantity() != null) {
                total_amount = calculateSectorWithQty( ctx, cs );
                qty = ctx.input.getQuantity();
            }
            else {
                total_amount = calculateSectorWithSeats( ctx, cs );
                qty = ctx.input.getSeats().size();
            
            }

            if( ctx.isOnError() ) {
                return;
            }

            Concert concertinfo = reserve.getConcertInfo();
            concertinfo.setArtist( ctx.input.getArtist() );
            concertinfo.setPlace( ctx.input.getPlace() );
            concertinfo.setConcerDate(ctx.input.getConcertDate() );
            concertinfo.setSector(ctx.input.getSector());
            concertinfo.setSeats(ctx.input.getSeats());
            
            Total totalinfo = reserve.getTotalInfo();
            totalinfo.setPrice(cs.getPrice());
            totalinfo.setQuantity(qty);
            totalinfo.setTotal(total_amount);

            User userinfo = reserve.getUser();
            userinfo.setName(ctx.input.getName());
            userinfo.setSurname(ctx.input.getSurname());
            userinfo.setDNI(ctx.input.getDNI());
            try {
                boolean r = reserveRepository.saveReserve(reserve, artistId, placeId, ctx.input.getConcertDate(), sectorId, ctx.input.getSeats(), cs );
                if( !r ) {
                    ctx.setError(eRCode.failToApllyEffect, "Fail to save the reserve");
                }    
            } catch (Exception e) {
                // TODO: handle exception
            }

            
        }
    }

    
    public void deleteReserve( DelReserveRequestContext ctx ) {

        int artistId = concertRepository.getArtistID(ctx.input.getArtist());
        int placeId = concertRepository.getPlaceID(ctx.input.getPlace());
        int sectorId = concertRepository.getSectorID(artistId, placeId, ctx.input.getConcertDate(), ctx.input.getSector());

        List<ConcertSector> sectors = concertSectorRepository.getConcertSectors(null, artistId, placeId, ctx.input.getConcertDate(), ctx.input.getSector() );
        if( sectors.size() == 0 ) {
            ctx.setError( eRCode.valueNotFound, "Sector not found");
        }

        ConcertSector cs = sectors.get(0);
        if( !cs.getHasSeat() && ctx.input.getSeats().size() > 0 ) {
            ctx.setError( eRCode.invalidFields, "The sector has not seats section");            
        }

        for( int i=0; i< ctx.input.getSeats().size(); i++ ) {
            boolean found = false;
            for( int j=0; j< cs.getSeats().size(); j++ ) {
                if( ctx.input.getSeats().get(i) == cs.getSeats().get(j)) {
                    found = true;
                    break;
                }
            }    

            if( !found ){
                ctx.setError( eRCode.invalidFields, "The seats was not found");
                break;            
            }
        }
        
        if( ctx.isOnError()) {
            return;
        }

        synchronized(CMutext.getInstance()) {
            try {
                reserveRepository.deleteReserve(ctx.input.getReserveId(), artistId, placeId, ctx.input.getConcertDate(), sectorId, cs.getSeats(), cs );           
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }
}
