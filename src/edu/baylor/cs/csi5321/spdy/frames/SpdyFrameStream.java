package edu.baylor.cs.csi5321.spdy.frames;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author ICPCDev
 */
public abstract class SpdyFrameStream extends SpdyControlFrame {

    protected int streamId;

    public int getStreamId() {
        return streamId;
    }

    public void setStreamId(int streamId) {
        this.streamId = streamId;
    }

    public SpdyFrameStream(int streamId, boolean controlBit, byte flags, int length) throws SpdyException {
        super(controlBit, flags, length);
        this.streamId = streamId;
    }

    public SpdyFrameStream(boolean controlBit, byte flags, int length) throws SpdyException {
        super(controlBit, flags, length);
    }

    @Override
    public byte[] encode() throws SpdyException {
        byte[] header = super.encode();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            bout.write(header);
            bout.write(getStreamId() & 0x7FFFFFFF);
        } catch (IOException ex) {
            throw new SpdyException(ex);
        }
        return bout.toByteArray();
    }

    @Override
    public SpdyFrame decode(DataInputStream is) throws SpdyException {
        try {
            int stream = is.readInt();
            setStreamId(stream & SpdyUtil.MASK_STREAM_ID_HEADER);
            return this;
        } catch (Exception ex) {
            throw new SpdyException(ex);
        }
    }
}
