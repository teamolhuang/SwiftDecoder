package SwiftMessages;

import Annotations.*;

import Enums.PresenceType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MT103 extends MTMessage {

    public MT103()
    {
        messageType = this.getClass();
    }

    @Presence(PresenceType.Mandatory)
    @ColumnId("20")
    protected String col20_SendersReference;

    @Presence(PresenceType.Mandatory)
    @ColumnId("59")
    @BeginAt(pos = 1)
    @LineRange(bottom = 0)
    @ColumnSizeRange(max = 34)
    protected String col59a_account;

    @Presence(PresenceType.Mandatory)
    @ColumnId("59")
    @LineRange(top = 1, bottom = 4)
    @ColumnSizeRange(max = 35)
    @ListItemType(String.class)
    protected List<String> col59_nameAndAddress = new ArrayList<>();

    @Presence(PresenceType.Mandatory)
    @ColumnId("59A")
    @BeginAt(line = 1)
    protected String col59A_identifierCode;

    @Presence(PresenceType.Mandatory)
    @ColumnId("59F")
    @LineRange(top = 1, bottom = 4)
    @IfRegex("^1/")
    @BeginAt(pos = 2)
    @ListItemType(String.class)
    protected List<String> col59F_nameOfBeneficiaryCustomer = new ArrayList<>();

    @Presence(PresenceType.Mandatory)
    @ColumnId("59F")
    @LineRange(top = 1, bottom = 4)
    @IfRegex("^2/")
    @BeginAt(pos = 2)
    @ListItemType(String.class)
    protected List<String> col59F_addressLine = new ArrayList<>();

    @Presence(PresenceType.Mandatory)
    @ColumnId("59F")
    @LineRange(top = 1, bottom = 4)
    @IfRegex("^3?/")
    @BeginAt(afterRegex = "^3?/")
    @ListItemType(String.class)
    protected List<String> col59F_countryAndTown = new ArrayList<>();

}