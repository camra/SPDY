package edu.baylor.cs.csi5321.spdy.frames;

import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author Lukas Camra
 */
public class SpdyFrameGoAway extends SpdyFrameRstStream {

    private static final int LENGTH = 8;

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

    public void setLastGoodStreamId(int streamId) {
        setStreamId(streamId);
    }

    @Override
    public int getLength() {
        return LENGTH;
    }
}
