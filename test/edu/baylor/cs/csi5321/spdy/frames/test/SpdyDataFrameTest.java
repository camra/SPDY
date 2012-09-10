package edu.baylor.cs.csi5321.spdy.frames.test;

import edu.baylor.cs.csi5321.spdy.frames.SpdyDataFrame;
import edu.baylor.cs.csi5321.spdy.frames.SpdyException;
import edu.baylor.cs.csi5321.spdy.frames.SpdyFrame;
import java.io.ByteArrayInputStream;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author Lukas Camra
 */
@RunWith(Parameterized.class)
public class SpdyDataFrameTest {

    private static final String ENCODING = "ASCII";
    private static final boolean CONTROL_BIT_CORRECT = false;
    private static final int[] STREAM_IDS_CORRECT = new int[]{0, 1, (int) Math.pow(2, 31), (int) Math.pow(2, 31) - 1, (int) Math.pow(2, 15)};
    private static final int[] STREAM_IDS_INCORRECT = new int[]{-1, (int) Math.pow(2, 31) + 1, -20, -100, (int) Math.pow(2, 31) + 2};
    private static final byte[] FLAGS_CORRECT = new byte[]{0, 0x01};
    private static final byte[] FLAGS_INCORRECT = new byte[]{-1, 0x02};
    private static byte[][] data;
    private int streamId;
    private int incorrectStreamId;
    private byte[] dataTo;
    private byte flagCorrect;
    private byte flagIncorrect;
    

    public SpdyDataFrameTest(int streamId, int incorrectStreamId, byte[] dataTo, byte flagCorrect, byte flagIncorrect) {
        this.streamId = streamId;
        this.incorrectStreamId = incorrectStreamId;
        this.dataTo = dataTo;
        this.flagCorrect = flagCorrect;
        this.flagIncorrect = flagIncorrect;
    }

    @Before
    public void setup() throws Exception {
        data = new byte[][]{{}, "hello".getBytes(ENCODING), "".getBytes(ENCODING), "spdy protocol implementation is coming \r\n,\r\n".getBytes(ENCODING)};
    }

    @Parameters
    public static Collection<Object[]> getParameters() throws Exception {
        data = new byte[][]{{}, "hello".getBytes(ENCODING), "".getBytes(ENCODING), "spdy protocol implementation is coming \r\n,\r\n".getBytes(ENCODING)};
        List<Object[]> res = new ArrayList<>();
        for (int i = 0; i < STREAM_IDS_CORRECT.length; i++) {
            for (int j = 0; j < STREAM_IDS_INCORRECT.length; j++) {
                for (int k = 0; k < data.length; k++) {
                    Object[] o = new Object[5];
                    o[0] = STREAM_IDS_CORRECT[i];
                    o[1] = STREAM_IDS_INCORRECT[j];
                    o[2] = data[k];
                    o[3] = FLAGS_CORRECT[k % 2];
                    o[4] = FLAGS_INCORRECT[(k + 1) % 2];
                    res.add(o);
                }
            }
        }
        return res;
    }

    @Test
    public void testEncoding() throws SpdyException {
        SpdyDataFrame f = new SpdyDataFrame(streamId, dataTo, CONTROL_BIT_CORRECT, flagCorrect, dataTo.length);
        byte[] res = f.encode();
        ByteArrayInputStream is = new ByteArrayInputStream(res);
        SpdyDataFrame f2 = (SpdyDataFrame) SpdyFrame.decode(is);
        assertEquals(f, f2);
    }

    @Test(expected = SpdyException.class)
    public void testIncorrectParams() throws Exception {
        SpdyDataFrame f = new SpdyDataFrame(streamId, dataTo, !CONTROL_BIT_CORRECT, flagCorrect, dataTo.length);
        byte[] res = f.encode();

    }

    @Test(expected = SpdyException.class)
    public void testIncorrectParams2() throws Exception {
        SpdyDataFrame f = new SpdyDataFrame(streamId, dataTo, CONTROL_BIT_CORRECT, flagIncorrect, dataTo.length);
        byte[] res = f.encode();

    }

    @Test(expected = SpdyException.class)
    public void testIncorrectParams3() throws Exception {
        SpdyDataFrame f = new SpdyDataFrame(incorrectStreamId, dataTo, CONTROL_BIT_CORRECT, flagCorrect, dataTo.length);
        byte[] res = f.encode();
    }
}
