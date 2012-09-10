package edu.baylor.cs.csi5321.spdy.frames;

/**
 *
 * @author Lukas Camra
 */
public class SpdyFrameGoAway extends SpdyFrameRstStream {

    private static final int LENGTH = 8;
    public static final int[] STATUS_CODES = new int[]{0, 1, 11};

    public SpdyFrameGoAway(int statusCode, int streamId, boolean controlBit, byte flags) throws SpdyException {
        super(statusCode, streamId, controlBit, flags);
    }

    public SpdyFrameGoAway(boolean controlBit, byte flags, int length) throws SpdyException {
        super(controlBit, flags, length);
    }

    @Override
    public short getType() {
        return SpdyControlFrameType.GOAWAY.getType();
    }

    public long getLastGoodStreamId() {
        return getStreamId();
    }

    public void setLastGoodStreamId(int streamId) throws SpdyException {
        setStreamId(streamId);
    }

    @Override
    public int getLength() {
        return LENGTH;
    }
    
    @Override
    public int[] getValidStatusCodes() {
        return STATUS_CODES;
    }
    
    
    
}
