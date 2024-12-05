package com.meli.backend.rapid.ws.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.meli.backend.rapid.req_ctx.*;
import com.meli.backend.rapid.req_ctx.req_ctx_io.*;
import com.meli.backend.rapid.ws.repositories.*;


@Service
public class ConcertService {

    ConcertRepository concertRepository;
    public ConcertService() {
        concertRepository = new ConcertRepository();
    }
    
    /** Gets the list of concerts by range.
     * 
     * @param ctx Context.
     * @return the list of concerts.
     */
    public List<ConcertOutput> getConcertsByRange(ConcertRgReqCtx ctx) {
        List<ConcertOutput> concerts;
        //synchronized(CMutext.getInstance()) {
            concerts = concertRepository.getConcertsByRange(ctx);
        //}
        return concerts;
    }

    /** Gets all concerts or a reduce group if fields input is sent. 
     * 
     * @param ctx Context
     * @return The list of concerts.
     */
    public List<ConcertOutput> getAllConcerts(ConcertReqCtx ctx) {
        List<ConcertOutput> concerts;
        //synchronized(CMutext.getInstance()) {
            concerts = concertRepository.getConcerts(ctx);
        //}
        return concerts;
    }

}
