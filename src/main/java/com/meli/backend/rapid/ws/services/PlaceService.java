package com.meli.backend.rapid.ws.services;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.meli.backend.rapid.common.CMutext;
import com.meli.backend.rapid.req_ctx.place.PlaceOutput;
import com.meli.backend.rapid.req_ctx.place.PlaceRequestContext;
import com.meli.backend.rapid.ws.repositories.PlaceRepository;


@Service
public class PlaceService {

    PlaceRepository repository;

    public PlaceService() {
        this.repository = new PlaceRepository();
    }

    public void createPlace(PlaceRequestContext ctx) throws SQLException {
        synchronized(CMutext.getInstance()) {
            this.repository.savePlace(ctx);
        }
    }

    public void deletePlace(PlaceRequestContext ctx) throws SQLException {
        synchronized(CMutext.getInstance()) {
            this.repository.deletePlace(ctx);
        }
    }

    public List<PlaceOutput> getPlaces(PlaceRequestContext ctx) throws SQLException {
        return this.repository.getPlaces(ctx);
    }
}
