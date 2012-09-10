package edu.baylor.cs.csi5321.spdy.frames;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Objects;

/**
 *
 * @author Lukas Camra
 */
public class SpdyFrameSynReply extends SpdyFrameStream {

    private SpdyNameValueBlock nameValueBlock;

    public SpdyNameValueBlock getNameValueBlock() {
        return nameValueBlock;
    }

    public void setNameValueBlock(SpdyNameValueBlock nameValueBlock) {
        this.nameValueBlock = nameValueBlock;
    }

    public SpdyFrameSynReply(SpdyNameValueBlock nameValueBlock, int streamId, boolean controlBit, byte flags, int length) throws SpdyException {
        super(streamId, controlBit, flags, length);
        this.nameValueBlock = nameValueBlock;
    }

    public SpdyFrameSynReply(boolean controlBit, byte flags, int length) throws SpdyException {
        super(controlBit, flags, length);
    }

    @Override
    public short getType() {
        return SpdyControlFrameType.SYN_REPLY.getType();
    }

    @Override
    public byte[] encode() throws SpdyException {
        
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            bout.write(nameValueBlock.encode());
            byte[] body = bout.toByteArray();
            setLength(body.length + 4);  //+4 for streamId
            byte[] header = super.encode();
            return SpdyUtil.concatArrays(header, body);
            
        } catch (IOException ex) {
            throw new SpdyException(ex);
        }
    }

    @Override
    public SpdyFrame decode(DataInputStream is) throws SpdyException {
        try {
            SpdyFrameSynReply f = (SpdyFrameSynReply) super.decode(is);
            byte[] pairs = new byte[f.getLength() - HEADER_LENGTH];
            is.readFully(pairs);
            f.setNameValueBlock(SpdyNameValueBlock.decode(pairs));
            return f;
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
        final SpdyFrameSynReply other = (SpdyFrameSynReply) obj;
        if (!Objects.equals(this.nameValueBlock, other.nameValueBlock)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.nameValueBlock);
        return hash;
    }
    
    
}
