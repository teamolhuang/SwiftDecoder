package SwiftMessages.Blocks;

import Annotations.SwiftMessages.BeginAt;
import Annotations.SwiftMessages.ColumnSizeFixed;
import lombok.Getter;

public class BasicHeaderBlock extends MTBlock {

    @ColumnSizeFixed(1)
    @Getter
    protected String applicationId;

    @BeginAt(pos = 1)
    @ColumnSizeFixed(2)
    @Getter
    protected int serviceId;

    @BeginAt(pos = 3)
    @ColumnSizeFixed(8)
    @Getter
    protected String businessIdentifierCode;

    @BeginAt(pos = 11)
    @ColumnSizeFixed(1)
    @Getter
    protected String logicalTerminalCode;

    @BeginAt(pos = 12)
    @ColumnSizeFixed(3)
    @Getter
    protected String branchCode;

    @BeginAt(pos = 15)
    @ColumnSizeFixed(4)
    @Getter
    protected int sessionNumber;

    @BeginAt(pos = 19)
    @ColumnSizeFixed(6)
    @Getter
    protected int getSessionNumber;
}
