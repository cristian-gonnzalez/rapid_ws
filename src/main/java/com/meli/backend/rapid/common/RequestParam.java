/**
 *  Request paramaters that specifies the number of records to give in the ouput 
 **/
package com.meli.backend.rapid.common;

public class RequestParam {

    /** Record number to start giving in the response. */
    private int rec_num;

    /** Number of records to give in the response. */
    private int offset;

    /** Initializes.
     * 
     */
    public RequestParam() {
        this.rec_num = 0;
        this.offset = 100;
    }

    public void setRecNum( int rec_num ) {
        this.rec_num = rec_num;
    }

    public int getRecNum() {
        return this.rec_num;
    }

    public void setOffset( int offset ) {
        this.offset = offset;
    }

    public int getOffset() {
        return this.offset;
    }
}
