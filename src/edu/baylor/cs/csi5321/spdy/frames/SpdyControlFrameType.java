package edu.baylor.cs.csi5321.spdy.frames;

/**
 *
 * @author Lukas Camra
 */
public enum SpdyControlFrameType {

    SYN_STREAM((short) 1), SYN_REPLY((short) 2), RST_STREAM((short) 3), GOAWAY((short) 7), HEADERS((short) 8);
    
    private final short type;

    private SpdyControlFrameType(short type) {
        this.type = type;
    }
    
    public short getType() {
        return type;
    }
    
    public static SpdyControlFrameType getEnumTypeFromType(short type) {
        for(SpdyControlFrameType t : SpdyControlFrameType.values()) {
            if(t.getType() == type) {
                return t;
            }
        }
        return null;
    }
}
