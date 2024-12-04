/**
 *  Output for reserve request.
 */
package com.meli.backend.rapid.req_ctx.req_ctx_io;

import com.meli.backend.rapid.ws.models.TicketReserve;

public class ReserveOutput {

    /** Reserve fields */
    private TicketReserve reserve;

    public ReserveOutput() {
        reserve = new TicketReserve();
    }

    public void setTicketReserve(TicketReserve reserve) {
        this.reserve = reserve;
    }
    public TicketReserve getTicketReserve() {
        return this.reserve;
    }
}
