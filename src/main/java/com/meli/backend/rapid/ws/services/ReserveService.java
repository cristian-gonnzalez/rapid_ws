package com.meli.backend.rapid.ws.services;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.meli.backend.rapid.common.CMutext;
import com.meli.backend.rapid.common.AppStatus.eRCode;
import com.meli.backend.rapid.req_ctx.ConcertRequestContext;
import com.meli.backend.rapid.req_ctx.DelReserveRequestContext;
import com.meli.backend.rapid.req_ctx.GetReserveRequestContext;
import com.meli.backend.rapid.req_ctx.ReserveRequestContext;
import com.meli.backend.rapid.req_ctx.req_ctx_io.ConcertInput;
import com.meli.backend.rapid.req_ctx.req_ctx_io.ReserveOutput;
import com.meli.backend.rapid.req_ctx.req_ctx_io.ReserveOutput.ConcertInfo;
import com.meli.backend.rapid.req_ctx.req_ctx_io.ReserveOutput.Total;
import com.meli.backend.rapid.ws.models.SectorRecord;
import com.meli.backend.rapid.ws.models.User;
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
            ctx.setError(eRCode.notFound, "Concert not found");
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
            ctx.setError(eRCode.notFound, "Sector not found");
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

        User  userinfo = reserve.getUserInfo();
        userinfo.setName(ctx.input.getName());
        userinfo.setSurname(ctx.input.getSurname());
        userinfo.setDNI(ctx.input.getDNI());

    }



    public ReserveOutput reserveRecordToOutput( ReserveRecord record ) {
        ReserveOutput output = new ReserveOutput();
        
        output.setDatetime(record.getDatetime());
        output.setReserveId(record.getReserveKey().getReserveId());
        
        output.setUserInfo(record.getUser());
        
        ConcertInfo concertInfo = output.getConcertInfo();
        concertInfo.setArtist(record.getConcert().getArtist());
        concertInfo.setPlace(record.getConcert().getPlace());
        concertInfo.setConcertDate(record.getReserveKey().getSectorKey().getConcerKey().getConcertDate() );
        concertInfo.setSector(concertService.sectorRecordToOutput( record.getConcert().getSectors().get(0) ));
        output.setConcertInfo(concertInfo);

        Total totalInfo = output.getTotalInfo();
        totalInfo.setPrice(record.getConcert().getSectors().get(0).getPrice());
        totalInfo.setQuantity(record.getQuantity());
        totalInfo.setTotal(record.geTotalAmount());
        output.setTotalInfo(totalInfo);

        return output;
    }

    public List<ReserveOutput> reserveRecordsToOutputs( List<ReserveRecord> records ) {
        List<ReserveOutput> outputs = new ArrayList<>();
        for(int i=0; i<records.size(); i++) {
            outputs.add(reserveRecordToOutput(records.get(i)));
        }
        return outputs;
    }


    public List<ReserveOutput> getReserves(GetReserveRequestContext ctx ) throws SQLException {
        List<ReserveRecord> reserveRecords = reserveRepository.getReserves(ctx);
        return reserveRecordsToOutputs( reserveRecords );
    }


    public void createReserve( ReserveRequestContext ctx) {

        ReserveOutput reserve = (ReserveOutput)ctx.output.getData();
        
        // syncronized this section in order to lock write access in db
        synchronized(CMutext.getInstance()) {

            // gets the sector
            ConcertRecord c = getSector(ctx, ctx.input.getArtist(), 
                                             ctx.input.getPlace(), 
                                             ctx.input.getConcertDate(), 
                                             ctx.input.getSector() );
            if(ctx.isOnError())
                return;

            // the method returns a list but we know that there is one record
            List<SectorRecord> sectors = c.getSectors();
            SectorRecord s = sectors.get(0);

            // checks if the sector has seats
            com.meli.backend.rapid.req_ctx.req_ctx_io.ReserveOutput.Total total = reserve.getTotalInfo();            
            double total_amount = 0;
            if( s.getHasSeat() ) {

                if( ctx.input.getQuantity() != null) {
                    ctx.setError(eRCode.invalidField, "seats field is required");
                    return;
                }
                
                List<Integer> reservedseats = s.getSeats();
                List<Integer> seats = ctx.input.getSeats();

                // checks if there is space in the sector
                int newOcupiedSpace =  s.getOccupiedSpace() + seats.size();
                if( newOcupiedSpace > s.getRoomSpace() ) {
                    ctx.setError(eRCode.noRoomAvailable, "No room is availble");
                    return;
                }

                // checks for repeated seats in the input
                Set<Integer> myset = new HashSet<Integer>(seats); 
                if(myset.size() != seats.size()) {
                    ctx.setError(eRCode.invalidFieldContent, "Repeated setas");
                    return;
                }

                // checks for reserved seats
                Set<Integer> intersection = new HashSet<Integer>(reservedseats);
                intersection.retainAll(myset);
                if( intersection.size() != 0) {
                    ctx.setError(eRCode.alreadyRserved, "Seat number is ocuppied");
                    return;
                }
                
                // checks for boundaries
                for(int i=0; i<reservedseats.size(); i++) {
                    for(int j=0; j<seats.size(); j++) {            
                        if( seats.get(j) <= 0 || seats.get(j) > seats.size() ) {
                            ctx.setError(eRCode.invalidFieldContent, "Seat number out of bounds");
                            return;   
                        }
                    }    
                }

                // calculates the total amount
                total_amount = s.getPrice() * seats.size();

                // saves the queantity in ouput
                total.setQuantity(seats.size());
                
                // updates the sector
                s.setSeats(seats);
                s.setOccupiedSpace(newOcupiedSpace);
            }
            else {

                if( ctx.input.getSeats() != null) {
                    ctx.setError(eRCode.missingField, "qty field is required");
                    return;
                }

                // checks for space
                int newOcupiedSpace = s.getOccupiedSpace() + ctx.input.getQuantity() ;
                if( s.getOccupiedSpace() + ctx.input.getQuantity() > s.getRoomSpace() ) {
                    ctx.setError(eRCode.noRoomAvailable, "No room is availble");
                    return;
                }

                // calculates the total amount
                total_amount = s.getPrice() * ctx.input.getQuantity();

                // saves the quantity in the output
                total.setQuantity(ctx.input.getQuantity());
                // updates the sector
                s.setOccupiedSpace(newOcupiedSpace);
            }

            // saves fields the output
            total.setPrice(s.getPrice());
            total.setTotal(total_amount);

            if( ctx.isOnError() ) {
                return;
            }
            // if we reach here, it means we can create the reserve
            // builds the reserve information for the output 
            buildTicketReserve(reserve, ctx, s);
            
            try {
                Boolean r = reserveRepository.saveReserve( ctx, reserve, s );
                if( !r ) {
                    ctx.setError(eRCode.internalError, "Error when saving reserve");
                }    
            } catch (Exception e) {
                System.out.println("Failed to saving reserve");
                if(!ctx.isOnError())
                    ctx.setError(eRCode.internalError, "Error when creating reserve");
            }
        }
    }

    
    public void deleteReserve( DelReserveRequestContext ctx ) throws SQLException {

        ReserveRecord rec = reserveRepository.getReserve( ctx );
        if(rec == null ){
            ctx.setError(eRCode.notFound, "Reserve not found");
            return;
        }
        synchronized(CMutext.getInstance()) {
            try {

                int occupiedSpace = rec.getConcert().getSectors().get(0).getOccupiedSpace();
                occupiedSpace -= rec.getQuantity();
                rec.getConcert().getSectors().get(0).setOccupiedSpace(occupiedSpace);
                
                reserveRepository.deleteReserve( rec );           
            } catch (Exception e) {
                System.out.println("Failed to delete reserve");
                if(!ctx.isOnError())
                    ctx.setError(eRCode.internalError, "Error when deleting reserve");
            }
        }
       
    }
}
