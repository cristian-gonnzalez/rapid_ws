package com.meli.backend.rapid.ws.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.meli.backend.rapid.common.CMutext;
import com.meli.backend.rapid.common.AppStatus.eRCode;
import com.meli.backend.rapid.req_ctx.ReserveReqCtx;
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

    private double calculateSectorWithSeats( ReserveReqCtx ctx, ConcertSector cs ) {
        
        List<Integer> seats = ctx.input.getSeats();
        
        // checks that there is room for the seats
        if( cs.getOccupiedSpace() + seats.size() > cs.getRoomSpace() ) {
            ctx.output.getAppStatus().setCode( eRCode.noRoomAvailable );
            ctx.output.getAppStatus().setMessage("No space available. The free space for sector "+ ctx.input.getSector() +" is " + ( cs.getRoomSpace() - cs.getOccupiedSpace() ) );
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
                    ctx.output.getAppStatus().setCode( eRCode.invalidFields );
                    ctx.output.getAppStatus().setMessage("Invalid seat number for "+ ctx.input.getSector());
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
            
            ctx.output.getAppStatus().setCode(eRCode.seatReserved );
            ctx.output.getAppStatus().setMessage(errmsg);
            return -1;
        }

        double total_amount = cs.getPrice() * ctx.input.getSeats().size();

        return total_amount;
    }

    private double calculateSectorWithQty( ReserveReqCtx ctx, ConcertSector cs ) {

        if( cs.getHasSeat() ) {
            ctx.output.getAppStatus().setCode( eRCode.invalidFields);
            ctx.output.getAppStatus().setMessage("Invalid field contents. The sector " + ctx.input.getSector() + " required to specify seat numbers" );
            return -1;
        }

        if( cs.getOccupiedSpace() + ctx.input.getQuantity() > cs.getRoomSpace() ) {
            ctx.output.getAppStatus().setCode( eRCode.noRoomAvailable );
            ctx.output.getAppStatus().setMessage("No space available. The free space for sector "+ ctx.input.getSector() +" is " + ( cs.getRoomSpace() - cs.getOccupiedSpace() ) );
            return -1;
        }

        double total_amount = cs.getPrice() * ctx.input.getQuantity();

        return total_amount;
    }


    /** Make the reserve of the concert
     * 
     * @param ctx Reserce request context.
     */
    public void reserveConcert( ReserveReqCtx ctx) {

        TicketReserve reserve = ((ReserveOutput)( ctx.output.getData() ) ).getTicketReserve();
        ConcertSector cs;

        synchronized(CMutext.getInstance()) {

            if( !concertRepository.existsConcert( ctx.input.getArtist(), ctx.input.getPlace(), ctx.input.getConcertDate(), ctx.input.getSector() ) ) {
                ctx.output.getAppStatus().setCode( eRCode.valueNotFound);
                ctx.output.getAppStatus().setMessage("No concert found for artist: " + ctx.input.getArtist() + 
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
                    ctx.output.getAppStatus().setCode(eRCode.failToApllyEffect);
                    ctx.output.getAppStatus().setMessage("Fail to save the reserve");
                }    
            } catch (Exception e) {
                // TODO: handle exception
            }

            
        }
    }
}
