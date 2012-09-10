package edu.baylor.cs.csi5321.spdy.frames;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author Lukas Camra
 */
public class SpdyFrameRstStream extends SpdyFrameStream {

    private static final int LENGTH = 8;
    public static final Integer[] STATUS_CODES = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) throws SpdyException {
        if(!Arrays.asList(getValidStatusCodes()).contains(statusCode)) {
            throw new SpdyException("Invalid status code: " + statusCode);
        }
        this.statusCode = statusCode;
    }

    public SpdyFrameRstStream(int statusCode, int streamId, boolean controlBit, byte flags) throws SpdyException {
        super(streamId, controlBit, flags, LENGTH);
        this.statusCode = statusCode;
    }

    public SpdyFrameRstStream(boolean controlBit, byte flags, int length) throws SpdyException {
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

    @Override
    public SpdyFrame decode(DataInputStream is) throws SpdyException {
        try {
            SpdyFrameRstStream f = (SpdyFrameRstStream) super.decode(is);
            int statusCode = is.readInt();
            f.setStatusCode(statusCode);
            return f;
        } catch (IOException ex) {
            throw new SpdyException(ex);
        }

    }
    
    @Override
    public int getLength() {
        return LENGTH;
    }

    @Override
    public Byte[] getValidFlags() {
        return new Byte[]{};
    }
    
    public Integer[] getValidStatusCodes() {
        return STATUS_CODES;
    }

    @Override
    public boolean equals(Object obj) {
        if(!super.equals(obj)) {
            return false;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SpdyFrameRstStream other = (SpdyFrameRstStream) obj;
        if (this.statusCode != other.statusCode) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7 * super.hashCode();
        hash = 29 * hash + this.statusCode;
        return hash;
    }
    
    
}
