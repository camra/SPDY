package edu.baylor.cs.csi5321.spdy.frames;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Lukas Camra
 */
public class SpdyFrameRstStream extends SpdyFrameStream {
    
    private static final int LENGTH = 8;
    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public SpdyFrameRstStream(int statusCode, int streamId, boolean controlBit, byte flags) throws SpdyException {
        super(streamId, controlBit, flags, LENGTH);
        this.statusCode = statusCode;
    }
    
    public SpdyFrameRstStream(boolean controlBit, byte flags, int length) throws SpdyException  {
        super(controlBit, flags, length);
    }

    @Override
    public short getType() {
        return SpdyControlFrameType.RST_STREAM.getType();
    }

    @Override
    public byte[] encode() throws SpdyException {
        try {
            byte[] header = super.encode();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bout);
            out.write(header);
            out.writeInt(statusCode);
            out.close();
            return bout.toByteArray();
        } catch (IOException ex) {
            throw new SpdyException(ex);
        }
    }
}
