package edu.baylor.cs.csi5321.spdy.frames;

/**
 *
 * @author Lukas Camra
 */
public class SpdyFrameGoAway extends SpdyFrameRstStream {

    /**
     *
     */
    public static final short TYPE = 7;

    public SpdyFrameGoAway(int statusCode, int streamId, boolean controlBit, byte flags) throws SpdyException {
        super(statusCode, streamId, controlBit, flags);
    }
 
    @Override
    public short getType() {
        return TYPE;
    }

    public long getLastGoodStreamId() {
        return getStreamId();
    }

    public void setLastGoodStreamId(int streamId) {
        setStreamId(streamId);
    }
    
}
