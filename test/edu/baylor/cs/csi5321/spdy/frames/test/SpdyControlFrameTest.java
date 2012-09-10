package edu.baylor.cs.csi5321.spdy.frames.test;

import edu.baylor.cs.csi5321.spdy.frames.*;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.*;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author Lukas Camra
 */
@RunWith(Parameterized.class)
public class SpdyControlFrameTest {

    private static final boolean CONTROL_BIT_CORRECT = true;
//    private static final byte[] FLAGS = new byte[]{0x01, 0x02};
    private static final int[] STREAM_IDS_CORRECT = new int[]{0, 1, (int) Math.pow(2, 31)};
    private static final int[] STREAM_IDS_INCORRECT = new int[]{-1, (int) Math.pow(2, 31) + 1, (int) Math.pow(2, 31) + 2};
    private static final byte[] priorityCorrect = new byte[]{0, 1, 2, 7};
    private static final byte[] PRIORITYINCORRECT = new byte[]{-1, -6, -7};
    private static final String[][] correctPairs = new String[][]{{"name", "value"}, {"darkness", "chaos"}, {"abcdefghijklmnopq", "this is value, this is super super cool test"}, {"name", ""}, {".goodday.#", "perfect!%^&*"}};
    private static final String[][] incorrectPairs = new String[][]{{"", "test stest test"}, {"eLo", "this should fail"}};
    private static final int[] goodStatusCodes_rst = new int[]{1, 4, 11};
    private static final int[] badStatusCodes_rst = new int[]{0, 12, 13};
    private static final int[] goodStatusCode_GoAway = SpdyFrameGoAway.STATUS_CODES;
    private static final int[] badStatusCode_GoAway = new int[]{2, 3, 10};
    private int streamId;
    private int associatedStreamid;
    private int streamIdIncorrect;
    private int associatedStreamIdIncorrect;
    private byte priority;
    private byte priorityIncorrect;
    private byte slot;
    private byte slotIncorrect = 1;
    private SpdyNameValueBlock blockCorrect;
    private SpdyNameValueBlock blockIncorrect;
    private int statusCodeGoAway;
    private int statusCodeGoAwayincorrect;
    private int statusRstStream;
    private int statusRstStreamincorrect;

    public SpdyControlFrameTest(int streamId, int associatedStreamid, int streamIdIncorrect, int associatedStreamIdIncorrect, byte priority, byte priorityIncorrect, byte slot, SpdyNameValueBlock blockCorrect, SpdyNameValueBlock blockIncorrect, int statusCodeGoAway, int statusCodeGoAwayincorrect, int statusRstStream, int statusRstStreamincorrect) {
        this.streamId = streamId;
        this.associatedStreamid = associatedStreamid;
        this.streamIdIncorrect = streamIdIncorrect;
        this.associatedStreamIdIncorrect = associatedStreamIdIncorrect;
        this.priority = priority;
        this.priorityIncorrect = priorityIncorrect;
        this.slot = slot;
        this.blockCorrect = blockCorrect;
        this.blockIncorrect = blockIncorrect;
        this.statusCodeGoAway = statusCodeGoAway;
        this.statusCodeGoAwayincorrect = statusCodeGoAwayincorrect;
        this.statusRstStream = statusRstStream;
        this.statusRstStreamincorrect = statusRstStreamincorrect;
    }

    @Parameters
    public static Collection<Object[]> getParameters() throws Exception {
        List<Object[]> res = new ArrayList<>();
        SpdyNameValueBlock blockCorrect = new SpdyNameValueBlock();
        SpdyNameValueBlock blockCorrect2 = new SpdyNameValueBlock();
        SpdyNameValueBlock blockinCorrect = new SpdyNameValueBlock();
        for (String[] arr : correctPairs) {
            blockCorrect.getPairs().put(arr[0], arr[1]);
        }
        for (String[] arr : incorrectPairs) {
            blockinCorrect.getPairs().put(arr[0], arr[1]);
        }

        for (int i = 0; i < STREAM_IDS_CORRECT.length; i++) {

            for (int l = 0; l < PRIORITYINCORRECT.length; l++) {
                for (int k = 0; k < priorityCorrect.length; k++) {
                    for (int t = 0; t < goodStatusCodes_rst.length; t++) {
                        for (int u = 0; u < goodStatusCode_GoAway.length; u++) {
                            Object[] o = new Object[13];
                            o[0] = STREAM_IDS_CORRECT[i];
                            o[1] = STREAM_IDS_CORRECT[i];
                            o[2] = STREAM_IDS_INCORRECT[i];
                            o[3] = STREAM_IDS_INCORRECT[i];
                            o[4] = priorityCorrect[k];
                            o[5] = PRIORITYINCORRECT[l];
                            o[6] = (byte) 0;
                            o[7] = (i % 2 == 0) ? blockCorrect : blockCorrect2;
                            o[8] = blockinCorrect;
                            o[9] = goodStatusCodes_rst[t];
                            o[10] = badStatusCodes_rst[t];
                            o[11] = goodStatusCode_GoAway[u];
                            o[12] = badStatusCode_GoAway[u];
                            res.add(o);
                        }
                    }
                }
            }
        }
        System.out.println(res.size());
        return res;
    }

    @Test
    public void testEncodingSynStream() throws Exception {
        SpdyFrameSynStream sfss = new SpdyFrameSynStream(associatedStreamid, priority, slot, blockCorrect, streamId, SpdyControlFrame.VERSION_CONTROL_FRAME, CONTROL_BIT_CORRECT, (byte) 0x00, 0x00);
        byte[] arr = sfss.encode();
        SpdyFrameSynStream res = (SpdyFrameSynStream) SpdyFrame.decode(new ByteArrayInputStream(arr));
        assertEquals(sfss, res);


    }
    
     @Test
    public void testEncodingSynReply() throws Exception {
        SpdyFrameSynReply sfss = new SpdyFrameSynReply(blockCorrect, streamId, CONTROL_BIT_CORRECT, (byte) 0x00, 0x00);
        byte[] arr = sfss.encode();
        SpdyFrameSynReply res = (SpdyFrameSynReply) SpdyFrame.decode(new ByteArrayInputStream(arr));
        assertEquals(sfss, res);


    }
}
