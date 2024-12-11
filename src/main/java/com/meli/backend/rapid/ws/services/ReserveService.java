package com.meli.backend.rapid.ws.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.meli.backend.rapid.common.CMutext;
import com.meli.backend.rapid.common.AppStatus.eRCode;
import com.meli.backend.rapid.req_ctx.concert.ConcertInput;
import com.meli.backend.rapid.req_ctx.concert.ConcertRequestContext;
import com.meli.backend.rapid.req_ctx.reserve.ReserveGetRequestContext;
import com.meli.backend.rapid.req_ctx.reserve.ReserveInput;
import com.meli.backend.rapid.req_ctx.reserve.ReserveOutput;
import com.meli.backend.rapid.req_ctx.reserve.ReserveRequestContext;
import com.meli.backend.rapid.req_ctx.reserve.ReserveOutput.ConcertInfo;
import com.meli.backend.rapid.req_ctx.reserve.ReserveOutput.Total;
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


    private void buildTicketReserve( ReserveOutput reserve, ReserveRequestContext ctx, SectorRecord s ) {

        com.meli.backend.rapid.req_ctx.reserve.ReserveOutput.ConcertInfo concertinfo = reserve.getConcertInfo();
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


    public List<ReserveOutput> getReserves(ReserveGetRequestContext ctx ) throws SQLException {
        List<ReserveRecord> reserveRecords = reserveRepository.getReserves(ctx);
        return reserveRecordsToOutputs( reserveRecords );
    }


    public void createReserve( ReserveRequestContext ctx) throws SQLException {

        ReserveOutput reserve = (ReserveOutput)ctx.output.getData();
        
        // syncronized this section in order to lock write access in db
        synchronized(CMutext.getInstance()) {
 
            // we need to update sector
            SectorRecord  sector = getSector( ctx.input );
            if( sector == null ) {
                ctx.setError(eRCode.notFound, "Concert or sector not found");
                return;
            }
            
            // checks if the sector has seats
            Total total = reserve.getTotalInfo();            
            double total_amount = 0;
            if( sector.getHasSeat() ) {

                if( ctx.input.getQuantity() != null) {
                    ctx.setError(eRCode.invalidField, "seats field is required");
                    return;
                }
                
                List<Integer> to_reserve_seats = ctx.input.getSeats();
                List<Integer> reserved_seats = sector.getSeats();

                // checks for repeated seats in the input
                Set<Integer> myset = new HashSet<Integer>(to_reserve_seats); 
                if(myset.size() != to_reserve_seats.size()) {
                    ctx.setError(eRCode.invalidFieldContent, "Repeated setas");
                    return;
                }

                // checks if there is space in the sector
                int newOcupiedSpace =  sector.getOccupiedSpace() + to_reserve_seats.size();
                if( newOcupiedSpace > sector.getRoomSpace() ) {
                    ctx.setError(eRCode.noRoomAvailable, "No room is availble");
                    return;
                }
                
                // checks for reserved seats
                List<Integer> intersection = new ArrayList<>( to_reserve_seats );
                intersection.retainAll( reserved_seats );
                if( intersection.size() != 0) {
                    ctx.setError(eRCode.alreadyRserved, "Seat number is ocuppied");
                    return;
                }
                
                // checks for boundaries
                for(int j=0; j<to_reserve_seats.size(); j++) {            
                    if( to_reserve_seats.get(j) <= 0 || to_reserve_seats.get(j) > sector.getRoomSpace() ) {
                        ctx.setError(eRCode.invalidFieldContent, "Seat number out of bounds");
                        return;   
                    }
                }    
            
                // calculates the total amount
                total_amount = sector.getPrice() * to_reserve_seats.size();

                // saves the queantity in ouput
                total.setQuantity(to_reserve_seats.size());
                
                // updates the sector
                sector.setSeats(to_reserve_seats);
                sector.setOccupiedSpace(newOcupiedSpace);
            }
            else {

                if( ctx.input.getSeats() != null) {
                    ctx.setError(eRCode.missingField, "qty field is required");
                    return;
                }

                // checks for space
                int newOcupiedSpace = sector.getOccupiedSpace() + ctx.input.getQuantity() ;
                if( sector.getOccupiedSpace() + ctx.input.getQuantity() > sector.getRoomSpace() ) {
                    ctx.setError(eRCode.noRoomAvailable, "No room is availble");
                    return;
                }

                // calculates the total amount
                total_amount = sector.getPrice() * ctx.input.getQuantity();

                // saves the quantity in the output
                total.setQuantity(ctx.input.getQuantity());
                // updates the sector
                sector.setOccupiedSpace(newOcupiedSpace);
            }

            // saves fields the output
            total.setPrice(sector.getPrice());
            total.setTotal(total_amount);

            if( ctx.isOnError() ) {
                return;
            }
            // if we reach here, it means we can create the reserve
            // builds the reserve information for the output 
            buildTicketReserve(reserve, ctx, sector);
            
            try {
                Boolean r = reserveRepository.saveReserve( ctx, reserve, sector );
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

    private SectorRecord getSector( ReserveInput input ) throws SQLException{
        ConcertInput concertInput = new ConcertInput();
        concertInput.setArtist(input.getArtist());
        concertInput.setPlace(input.getPlace());
        concertInput.setConcertDate(input.getConcertDate());
        ConcertRequestContext concertCtx = new ConcertRequestContext(concertInput);

        List<ConcertRecord> concerts = concertRepository.getConcerts(concertCtx);
        if( concerts.size() == 0)
            return null;

        ConcertRecord concert = concerts.get(0);
        List<SectorRecord> sectors = concert.getSectors();

        SectorRecord sector = null;
        for(int i=0; i<sectors.size(); i++ ) {
            if( sectors.get(i).getName().equals( input.getSector() ) ) {
                sector = sectors.get(i);
                break;
            }
        }
        return sector;
    }
    
    public void deleteReserve( ReserveRequestContext ctx ) throws SQLException {

        synchronized(CMutext.getInstance()) {

            ReserveRecord rec = reserveRepository.getReserve( ctx );
            if(rec == null ){
                ctx.setError(eRCode.notFound, "Reserve not found");
                return;
            }   

            // we need to update sector
            SectorRecord  sector = getSector( ctx.input );
            sector.setOccupiedSpace( ( sector.getOccupiedSpace() - rec.getQuantity() ) );
            
            reserveRepository.deleteReserve( rec, sector );           
        }
       
    }
}
