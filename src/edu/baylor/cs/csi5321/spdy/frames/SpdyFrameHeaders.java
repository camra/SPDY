package edu.baylor.cs.csi5321.spdy.frames;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
    
    public SpdyFrameHeaders(boolean controlBit, byte flags, int length) throws SpdyException  {
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
        byte[] header = super.encode();
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            bout.write(header);
            bout.write(headers.encode());
            return bout.toByteArray();
        } catch (IOException ex) {
            throw new SpdyException(ex);
        }
    }
}
