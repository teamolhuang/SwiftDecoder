package Program.SwiftMessages;

import SwiftMessages.MessageTypes.MTMessage;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Pattern;

public class Decoder {

    public static void workOnSwifts(List<File> files)
    {
        for (File file : files)
        {
            String content = "";

            List<String> swifts = IO.getSwiftContents(file.toPath());

            for (int i = 0; i < swifts.size(); i++)
            {
                MTMessage mt = MTMessage.parse(swifts.get(i));

                System.out.println("===========================================");
                System.out.println(file.getName()  + " 中的電文 " + (i+1));
                System.out.println("===========================================");

                mt.getBasicHeaderBlock().outputAsString();
                mt.getApplicationHeaderBlock().outputAsString();
                mt.getUserHeaderBlock().outputAsString();

                mt.outputFields();

                mt.getTrailerBlock().outputAsString();
            }
        }

        System.out.println("===========================================");
    }

}
