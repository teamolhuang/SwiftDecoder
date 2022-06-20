import Program.SwiftMessages.IO;
import SwiftMessages.MessageTypes.MT103;
import SwiftMessages.MessageTypes.MTMessage;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class MTMessageTests {

    @Test
    public void testMT103() {
        for (String content : IO.getSwiftContents(Paths.get("C:\\Users\\v06v2\\Desktop\\swift_ref\\15114340.SWI"))) {
            MTMessage mt = MTMessage.parse(content);
            assertEquals(mt.getMessageType(), MT103.class);
        }
    }
}
