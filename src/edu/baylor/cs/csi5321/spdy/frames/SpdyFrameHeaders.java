package edu.baylor.cs.csi5321.spdy.frames;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 *
 * @author Lukas Camra
 */
public class SpdyFrameHeaders extends SpdyFrameSynStream {

    private SpdyNameValueBlock headers;

    public SpdyFrameHeaders(SpdyNameValueBlock headers, int associatedToStreamId, byte priority, byte slot, SpdyNameValueBlock nameValueBlock, int streamId, short version, boolean controlBit, byte flags, int length) throws SpdyException {
        super(associatedToStreamId, priority, slot, nameValueBlock, streamId, version, controlBit, flags, length);
        this.headers = headers;
    }

    public SpdyFrameHeaders(boolean controlBit, byte flags, int length) throws SpdyException {
        super(controlBit, flags, length);
    }

    @Override
    public short getType() {
        return SpdyControlFrameType.HEADERS.getType();
    }

    public SpdyNameValueBlock getHeaders() {
        return headers;
    }

    public void setHeaders(SpdyNameValueBlock headers) {
        this.headers = headers;
    }

    @Override
    public byte[] encode() throws SpdyException {

        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            bout.write(headers.encode());
            byte[] body = bout.toByteArray();
            setLength(body.length + 4); //+4 for streamId
            byte[] header = super.encode();
            return SpdyUtil.concatArrays(header, body);
        } catch (IOException ex) {
            throw new SpdyException(ex);
        }
    }

    @Override
    public Byte[] getValidFlags() {
        return new Byte[]{0x01};
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
        final SpdyFrameHeaders other = (SpdyFrameHeaders) obj;
        if (!Objects.equals(this.headers, other.headers)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3 * super.hashCode();
        hash = 29 * hash + Objects.hashCode(this.headers);
        return hash;
    }
    
    
}
