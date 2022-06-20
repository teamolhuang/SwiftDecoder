package SwiftMessages.Blocks;

import SwiftMessages.Annotations.BeginAt;
import SwiftMessages.Annotations.ColumnSizeFixed;
import SwiftMessages.Annotations.ColumnSizeRange;
import lombok.Getter;

public class ApplicationHeaderBlock extends MTBlock {

    @ColumnSizeFixed(1)
    @Getter
    protected String messageMode;

    @BeginAt(pos = 1)
    @ColumnSizeFixed(3)
    @Getter
    protected String messageType;

    @BeginAt(pos = 4)
    @ColumnSizeFixed(8)
    @Getter
    protected String senderBusinessIdentifierCode;

    @BeginAt(pos = 12)
    @ColumnSizeFixed(1)
    @Getter
    protected String recipientsLogicalTerminalCode;

    @BeginAt(pos = 13)
    @ColumnSizeFixed(3)
    @Getter
    protected String recipientsBranchCode;

    @BeginAt(pos = 16)
    @ColumnSizeFixed(1)
    @Getter
    protected String messagePriority;

    @BeginAt(pos = 17)
    @ColumnSizeRange(max = 1)
    @Getter
    protected String deliveryMonitoring;

    @BeginAt(pos = 18)
    @ColumnSizeRange(max = 3)
    @Getter
    protected String nondeliveryNotificationPeriod;
}
