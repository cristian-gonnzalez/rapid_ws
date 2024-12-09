package com.meli.backend.rapid.ws.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.meli.backend.rapid.req_ctx.concert.ConcertOutput;
import com.meli.backend.rapid.req_ctx.concert.ConcertRequestContext;
import com.meli.backend.rapid.req_ctx.concert.ConcertRgRequestContext;
import com.meli.backend.rapid.req_ctx.concert.SectorOutput;
import com.meli.backend.rapid.ws.models.ConcertRecord;
import com.meli.backend.rapid.ws.models.SectorRecord;
import com.meli.backend.rapid.ws.repositories.*;


@Service
public class ConcertService {

    ConcertRepository concertRepository;
    public ConcertService() {
        concertRepository = new ConcertRepository();
    }

    public SectorOutput sectorRecordToOutput( SectorRecord record ) {
        SectorOutput output = new SectorOutput();
        output.setName(record.getName());
        output.setHasSeat(record.getHasSeat());
        output.setOccupiedSpace(record.getOccupiedSpace());
        output.setRoomSpace(record.getRoomSpace());
        output.setPrice(record.getPrice());
        output.setSeats(record.getSeats());
        return output;
    }

    public List<SectorOutput> sectorRecordsToOutputs( List<SectorRecord> records ) {
        List<SectorOutput> outputs = new ArrayList<>();
        for(int i=0; i<records.size(); i++) {
            outputs.add(sectorRecordToOutput(records.get(i)));
        }
        return outputs;
    }

    public ConcertOutput concertRecordToOutput( ConcertRecord record ) {
        ConcertOutput output = new ConcertOutput();
        output.setArtist(record.getArtist());
        output.setPlace(record.getPlace());
        output.setConcertDate(record.getConcerKey().getConcertDate());
        output.setConcertTime(record.getTime());
        output.setSectors(sectorRecordsToOutputs( record.getSectors() ));

        return output;
    }

    public List<ConcertOutput> concertRecordsToOutputs( List<ConcertRecord> records ) {
        List<ConcertOutput> outputs = new ArrayList<>();
        for(int i=0; i<records.size(); i++) {
            outputs.add(concertRecordToOutput(records.get(i)));
        }
        return outputs;
    }
    
    public List<ConcertOutput> getConcertsByRange(ConcertRgRequestContext ctx) {
        List<ConcertRecord> concerts = null;
        try {
            concerts = concertRepository.getConcertsByRange(ctx);
        } catch (Exception e) {
            System.err.println("Failing getting converts by range");
        }

        return concertRecordsToOutputs(concerts);
    }

    public List<ConcertOutput> getAllConcerts(ConcertRequestContext ctx) throws Exception{
        List<ConcertRecord> concerts = null;
        try {
            concerts = concertRepository.getConcerts(ctx);    
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("Failing getting all converts");
            throw e;
        }

        return concertRecordsToOutputs(concerts);
    }
}
