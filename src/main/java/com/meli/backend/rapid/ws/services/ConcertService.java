package com.meli.backend.rapid.ws.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.meli.backend.rapid.common.CMutext;
import com.meli.backend.rapid.common.AppStatus.eRCode;
import com.meli.backend.rapid.req_ctx.artist.ArtistInput;
import com.meli.backend.rapid.req_ctx.artist.ArtistRequestContext;
import com.meli.backend.rapid.req_ctx.concert.ConcertOutput;
import com.meli.backend.rapid.req_ctx.concert.ConcertRequestContext;
import com.meli.backend.rapid.req_ctx.concert.ConcertRgRequestContext;
import com.meli.backend.rapid.req_ctx.concert.SectorOutput;
import com.meli.backend.rapid.req_ctx.place.PlaceInput;
import com.meli.backend.rapid.req_ctx.place.PlaceRequestContext;
import com.meli.backend.rapid.ws.models.ArtistRecord;
import com.meli.backend.rapid.ws.models.ConcertKey;
import com.meli.backend.rapid.ws.models.ConcertRecord;
import com.meli.backend.rapid.ws.models.PlaceRecord;
import com.meli.backend.rapid.ws.models.SectorKey;
import com.meli.backend.rapid.ws.models.SectorRecord;
import com.meli.backend.rapid.ws.repositories.*;


@Service
public class ConcertService {

    ConcertRepository concertRepository;
    ArtistRepository artistRepository;
    PlaceRepository placeRepository;

    public ConcertService() {
        this.concertRepository = new ConcertRepository();
        this.artistRepository = new ArtistRepository();
        this.placeRepository = new PlaceRepository();
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

    public List<ConcertOutput> getAllConcerts(ConcertRequestContext ctx) throws SQLException{
        List<ConcertRecord> concerts = concertRepository.getConcerts(ctx);    
        return concertRecordsToOutputs(concerts);
    }

    private ArtistRecord getArtist( String name ) throws SQLException {
        ArtistInput artistInput = new ArtistInput();
        artistInput.setName(name);
        ArtistRequestContext artistCtx = new ArtistRequestContext(artistInput);

        List<ArtistRecord> artists = this.artistRepository.getArtists(artistCtx);
        if( artists.size() == 0)
            return null;
    
        return artists.get(0);
    }

    private PlaceRecord getPlace( String name ) throws SQLException {
        PlaceInput placeInput = new PlaceInput();
        placeInput.setName( name );
        PlaceRequestContext placeCtx = new PlaceRequestContext(placeInput);

        List<PlaceRecord> places = placeRepository.getPlaces(placeCtx);
        if( places.size() == 0)
            return null;
    
        return places.get(0);
    }

    public void createConcert(ConcertRequestContext ctx) throws SQLException {
        synchronized(CMutext.getInstance()) {

            ArtistRecord artist = getArtist( ctx.input.getArtist() );
            if( artist == null )
            {
                ctx.setError(eRCode.notFound, "Artist not found");
                return;
            }
            
            PlaceRecord place = getPlace( ctx.input.getPlace() );
            if( place == null )
            {
                ctx.setError(eRCode.notFound, "Place not found");
                return;
            }

            Integer artist_id = artist.getArtistId();
            Integer place_id = place.getPlaceId();
            ConcertKey key = new ConcertKey();
            key.setArtistId(artist_id);
            key.setConcertDate(ctx.input.getConcertDate());
            key.setPlaceId(place_id);

            ConcertRecord record = new ConcertRecord();
            record.setArtist(artist.getName());
            record.setPlace(ctx.input.getPlace());
            record.setTime(ctx.input.getTime());            
            record.setConcerKey(key);

            System.out.println("Saving concert into db");

            this.concertRepository.saveConcert(ctx, record);
        }
    }

    public void deleteConcert(ConcertRequestContext ctx) throws SQLException {
        synchronized(CMutext.getInstance()) {
            
            ArtistRecord artist = getArtist( ctx.input.getArtist() );
            if( artist == null )
            {
                ctx.setError(eRCode.notFound, "Artist not found");
                return;
            }
            
            PlaceRecord place = getPlace( ctx.input.getPlace() );
            if( place == null )
            {
                ctx.setError(eRCode.notFound, "Place not found");
                return;
            }

            Integer artist_id = artist.getArtistId();
            Integer place_id = place.getPlaceId();
            ConcertKey key = new ConcertKey();
            key.setArtistId(artist_id);
            key.setConcertDate(ctx.input.getConcertDate());
            key.setPlaceId(place_id);

            this.concertRepository.deleteConcert(ctx, key);
        }
    }

    public void createSector(ConcertRequestContext ctx) throws SQLException {
        synchronized(CMutext.getInstance()) {

            List<ConcertRecord> concerts = concertRepository.getConcerts(ctx);    
            if( concerts.size() == 0 )
            {
                ctx.setError(eRCode.notFound, "Concert not found");
                return;
            }
            ConcertRecord concert = concerts.get(0);

            SectorKey key = new SectorKey();
            key.setConcerKey( concert.getConcerKey() );
            key.setSectorId( concert.getSectors().size());

            SectorRecord record = new SectorRecord();
            record.setSetorKey(key);
            record.setName(ctx.input.getSector().getName());
            record.setRoomSpace(ctx.input.getSector().getRoomSpace());
            record.setOccupiedSpace(ctx.input.getSector().getOccupiedSpace());
            record.setPrice(ctx.input.getSector().getPrice());
            record.setHasSeat(ctx.input.getSector().getHasSeat());
           
            this.concertRepository.saveSector(ctx, record);
        }
    }

    public void deleteSector(ConcertRequestContext ctx) throws SQLException {
        synchronized(CMutext.getInstance()) {
            
            List<ConcertRecord> concerts = concertRepository.getConcerts(ctx);    
            if( concerts.size() == 0 ) {
                ctx.setError(eRCode.notFound, "Concert not found");
                return;
            }
            ConcertRecord concert = concerts.get(0);
            List<SectorRecord> sectors = concert.getSectors();

            SectorKey key = new SectorKey();
            key.setConcerKey( concert.getConcerKey() );

            Integer sector_id = -1;
            for( int i = 0; i<sectors.size(); i++) {
                if( sectors.get(i).getName().equals(ctx.input.getSector().getName() ) ) {
                    sector_id = sectors.get(i).getSetorKey().getSectorId();
                }
            }
            if( sector_id == -1) {
                ctx.setError(eRCode.notFound, "Sector not found");
                return;        
            }
            key.setSectorId( sector_id );
            this.concertRepository.deleteSector(ctx, key);
        }
    }
}
