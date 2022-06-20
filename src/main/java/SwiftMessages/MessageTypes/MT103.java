package SwiftMessages.MessageTypes;

import SwiftMessages.Annotations.*;
import SwiftMessages.Enums.PresenceType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unused", "CanBeFinal"})
public class MT103 extends MTMessage {

    public MT103()
    {
        messageType = this.getClass();
    }

    @Presence(PresenceType.Mandatory)
    @ColumnId("20")
    protected String col20_sendersReference;

    public String get20() { return col20_sendersReference; }
    public String getSendersReference() { return col20_sendersReference; }

    @Presence(PresenceType.Optional)
    @ColumnId("13C")
    @BeginAt(pos = 1)
    @ColumnSizeRange(max = 8)
    @UntilLiteral("/")
    @ListItemType(String.class)
    protected List<String> col13C_code = new ArrayList<>();

    public List<String> get13C_Code() { return Collections.unmodifiableList(col13C_code); }

    @Presence(PresenceType.Optional)
    @ColumnId("13C")
    @BeginAt(pos = 1)
    @AfterLiteral("/")
    @ColumnSizeFixed(9)
    @ListItemType(String.class)
    protected List<String> col13C_time = new ArrayList<>();

    public List<String> get13C_Time() { return Collections.unmodifiableList(col13C_time); }

    @Presence(PresenceType.Mandatory)
    @ColumnId("23B")
    @ColumnSizeFixed(4)
    protected String col23B_bankOperationCode;

    public String get23B() { return col23B_bankOperationCode; }
    public String getBankOperationCode() { return col23B_bankOperationCode; }

    @Presence(PresenceType.Mandatory)
    @ColumnId("23E")
    @ColumnSizeFixed(4)
    @ListItemType(String.class)
    protected List<String> col23E_instructionCode = new ArrayList<>();

    public List<String> get23E_instructionCode() { return Collections.unmodifiableList(col23E_instructionCode); }

    @Presence(PresenceType.Mandatory)
    @ColumnId("23E")
    @AfterLiteral("/")
    @ColumnSizeRange(max = 30)
    @ListItemType(String.class)
    protected List<String> col23E_additionalInformation = new ArrayList<>();

    public List<String> getCol23E_instructionCode() { return Collections.unmodifiableList(col23E_additionalInformation); }

    @Presence(PresenceType.Optional)
    @ColumnId("26T")
    @ColumnSizeFixed(3)
    protected String col26T_transactionTypeCode;

    public String get26T() { return col26T_transactionTypeCode; }
    public String getTransactionTypeCode() { return col26T_transactionTypeCode; }

    @Presence(PresenceType.Mandatory)
    @ColumnId("32A")
    @ColumnSizeFixed(6)
    protected String col32A_valueDate;

    public String get32A_valueDate() { return col32A_valueDate; }

    @Presence(PresenceType.Mandatory)
    @ColumnId("32A")
    @BeginAt(pos = 6)
    @ColumnSizeFixed(3)
    protected String col32A_currency;

    public String get32A_currency() { return col32A_currency; }

    @Presence(PresenceType.Mandatory)
    @ColumnId("32A")
    @BeginAt(pos = 9)
    @ColumnSizeRange(max = 15)
    protected BigDecimal col32A_interbankSettledAmount;

    public BigDecimal get32A_amount() { return col32A_interbankSettledAmount; }

    @Presence(PresenceType.Optional)
    @ColumnId("33B")
    @ColumnSizeFixed(3)
    protected String col33B_currency;

    public String get33B_currency() { return col33B_currency; }

    @Presence(PresenceType.Optional)
    @ColumnId("33B")
    @BeginAt(pos = 3)
    @ColumnSizeRange(max = 15)
    protected BigDecimal col33B_instructedAmount;

    public BigDecimal get33B_amount() { return col33B_instructedAmount; }

    @Presence(PresenceType.Optional)
    @ColumnId("36")
    @ColumnSizeRange(max = 12)
    protected BigDecimal col36_exchangeRate;

    public BigDecimal get36() { return col36_exchangeRate; }
    public BigDecimal getExchangeRate() { return col36_exchangeRate; }

    @Presence(PresenceType.Mandatory)
    @ColumnId("50A")
    @LineRange(bottom = 0)
    @AfterLiteral("/")
    @ColumnSizeRange(max = 34)
    protected String col50A_account;

    public String get50A_account() { return col50A_account; }

    @Presence(PresenceType.Mandatory)
    @ColumnId("50A")
    @LineRange(top = 1, bottom = 1)
    protected String col50A_identifierCode;

    public String get50A_identifierCode() { return col50A_identifierCode; }

    @Presence(PresenceType.Mandatory)
    @ColumnId("50F")
    @LineRange(bottom = 0)
    @ColumnSizeRange(max = 35)
    protected String col50F_partyIdentifier;

    public String get50F_partyIdentifier() { return col50F_partyIdentifier; }

    @Presence(PresenceType.Mandatory)
    @ColumnId("50F")
    @LineRange(top = 1, bottom = 4)
    @AfterLiteral("/")
    @ColumnSizeRange(max = 33)
    @ListItemType(String.class)
    protected List<String> col50F_nameAndAddress;

    public List<String> get50F_nameAndAddress() { return Collections.unmodifiableList(col50F_nameAndAddress); }

    @Presence(PresenceType.Mandatory)
    @ColumnId("50K")
    @LineRange(bottom = 0)
    @AfterLiteral("/")
    @ColumnSizeRange(max = 34)
    protected String col50K_account;

    public String get50K_account() { return col50K_account; }

    @Presence(PresenceType.Mandatory)
    @ColumnId("50K")
    @LineRange(top = 1, bottom = 4)
    @ColumnSizeRange(max = 35)
    @ListItemType(String.class)
    protected List<String> col50K_nameAndAddress;

    public List<String> get50K_nameAndAddress() { return Collections.unmodifiableList(col50K_nameAndAddress); }

    @Presence(PresenceType.Optional)
    @ColumnId("51A")
    @LineRange(bottom = 0)
    protected String col51A_partyIdentifier;

    public String get51A_partyIdentifier() { return col51A_partyIdentifier; }

    @Presence(PresenceType.Optional)
    @ColumnId("51A")
    @LineRange(top = 1, bottom = 1)
    protected String col51A_identifierCode;

    public String get51A_identifierCode() { return col51A_identifierCode; }

    @Presence(PresenceType.Optional)
    @ColumnId("52A")
    @LineRange(bottom = 0)
    protected String col52A_partyIdentifier;

    public String get52A_partyIdentifier() { return col52A_partyIdentifier; }

    @Presence(PresenceType.Optional)
    @ColumnId("52A")
    @LineRange(top = 1, bottom = 1)
    protected String col52A_identifierCode;

    public String get52A_identifierCode() { return col52A_identifierCode; }

    @Presence(PresenceType.Optional)
    @ColumnId("52D")
    @LineRange(bottom = 0)
    protected String col52D_partyIdentifier;

    public String get52D_partyIdentifier() { return col52D_partyIdentifier; }

    @Presence(PresenceType.Optional)
    @ColumnId("52D")
    @LineRange(top = 1, bottom = 4)
    @ColumnSizeRange(max = 35)
    @ListItemType(String.class)
    protected List<String> col52D_nameAndAddress;

    public List<String> get52D_nameAndAddress() { return Collections.unmodifiableList(col52D_nameAndAddress); }

    @Presence(PresenceType.Optional)
    @ColumnId("53A")
    @LineRange(bottom = 0)
    protected String col53A_partyIdentifier;

    public String get53A_partyIdentifier() { return col53A_partyIdentifier; }

    @Presence(PresenceType.Optional)
    @ColumnId("53A")
    @LineRange(top = 1, bottom = 1)
    protected String col53A_identifierCode;

    public String get53A_identifierCode() { return col53A_identifierCode; }

    @Presence(PresenceType.Optional)
    @ColumnId("53B")
    @LineRange(bottom = 0)
    protected String col53B_partyIdentifier;

    public String get53B_partyIdentifier() { return col53B_partyIdentifier; }

    @Presence(PresenceType.Optional)
    @ColumnId("53B")
    @LineRange(top = 1, bottom = 1)
    protected String col53B_location;

    public String get53B_location() { return col53B_location; }

    @Presence(PresenceType.Optional)
    @ColumnId("53D")
    @LineRange(bottom = 0)
    protected String col53D_partyIdentifier;

    public String get53D_partyIdentifier() { return col53D_partyIdentifier; }

    @Presence(PresenceType.Optional)
    @ColumnId("53D")
    @LineRange(top = 1, bottom = 4)
    @ListItemType(String.class)
    protected List<String> col53D_nameAndAddress;

    public List<String> get53D_nameAndAddress() { return Collections.unmodifiableList(col53D_nameAndAddress); }

    @Presence(PresenceType.Optional)
    @ColumnId("54A")
    @LineRange(bottom = 0)
    protected String col54A_partyIdentifier;

    public String get54A_partyIdentifier() { return col54A_partyIdentifier; }

    @Presence(PresenceType.Optional)
    @ColumnId("54A")
    @LineRange(top = 1, bottom = 1)
    protected String col54A_identifierCode;

    public String get54A_identifierCode() { return col54A_identifierCode; }

    @Presence(PresenceType.Optional)
    @ColumnId("54B")
    @LineRange(bottom = 0)
    protected String col54B_partyIdentifier;

    public String get54B_partyIdentifier() { return col54B_partyIdentifier; }

    @Presence(PresenceType.Optional)
    @ColumnId("54B")
    @LineRange(top = 1, bottom = 1)
    protected String col54B_location;

    public String get54B_location() { return col54B_location; }

    @Presence(PresenceType.Optional)
    @ColumnId("54D")
    @LineRange(bottom = 0)
    protected String col54D_partyIdentifier;

    public String get54D_partyIdentifier() { return col54D_partyIdentifier; }

    @Presence(PresenceType.Optional)
    @ColumnId("54D")
    @LineRange(top = 1, bottom = 4)
    @ListItemType(String.class)
    protected List<String> col54D_nameAndAddress;

    public List<String> get54D_nameAndAddress() { return Collections.unmodifiableList(col54D_nameAndAddress); }

    @Presence(PresenceType.Optional)
    @ColumnId("55A")
    @LineRange(bottom = 0)
    protected String col55A_partyIdentifier;

    public String get55A_partyIdentifier() { return col55A_partyIdentifier; }

    @Presence(PresenceType.Optional)
    @ColumnId("55A")
    @LineRange(top = 1, bottom = 1)
    protected String col55A_identifierCode;

    public String get55A_identifierCode() { return col55A_identifierCode; }

    @Presence(PresenceType.Optional)
    @ColumnId("55B")
    @LineRange(bottom = 0)
    protected String col55B_partyIdentifier;

    public String get55B_partyIdentifier() { return col55B_partyIdentifier; }

    @Presence(PresenceType.Optional)
    @ColumnId("55B")
    @LineRange(top = 1, bottom = 1)
    protected String col55B_location;

    public String get55B_location() { return col55B_location; }

    @Presence(PresenceType.Optional)
    @ColumnId("55D")
    @LineRange(bottom = 0)
    protected String col55D_partyIdentifier;

    public String get55D_partyIdentifier() { return col55D_partyIdentifier; }

    @Presence(PresenceType.Optional)
    @ColumnId("55D")
    @LineRange(top = 1, bottom = 4)
    @ListItemType(String.class)
    protected List<String> col55D_nameAndAddress;

    public List<String> get55D_nameAndAddress() { return Collections.unmodifiableList(col55D_nameAndAddress); }

    @Presence(PresenceType.Optional)
    @ColumnId("56A")
    @LineRange(bottom = 0)
    protected String col56A_partyIdentifier;

    public String get56A_partyIdentifier() { return col56A_partyIdentifier; }

    @Presence(PresenceType.Optional)
    @ColumnId("56A")
    @LineRange(top = 1, bottom = 1)
    protected String col56A_identifierCode;

    public String get56A_identifierCode() { return col56A_identifierCode; }

    @Presence(PresenceType.Optional)
    @ColumnId("56C")
    protected String col56C_partyIdentifier;

    public String get56C_partyIdentifier() { return col56C_partyIdentifier; }

    @Presence(PresenceType.Optional)
    @ColumnId("56D")
    @LineRange(bottom = 0)
    protected String col56D_partyIdentifier;

    public String get56D_partyIdentifier() { return col56D_partyIdentifier; }

    @Presence(PresenceType.Optional)
    @ColumnId("56D")
    @LineRange(top = 1, bottom = 4)
    @ListItemType(String.class)
    protected List<String> col56D_nameAndAddress;

    public List<String> get56D_nameAndAddress() { return Collections.unmodifiableList(col56D_nameAndAddress); }

    @Presence(PresenceType.Optional)
    @ColumnId("57A")
    @LineRange(bottom = 0)
    protected String col57A_partyIdentifier;

    public String get57A_partyIdentifier() { return col57A_partyIdentifier; }

    @Presence(PresenceType.Optional)
    @ColumnId("57A")
    @LineRange(top = 1, bottom = 1)
    protected String col57A_identifierCode;

    public String get57A_identifierCode() { return col57A_identifierCode; }

    @Presence(PresenceType.Optional)
    @ColumnId("57B")
    @LineRange(bottom = 0)
    protected String col57B_partyIdentifier;

    public String get57B_partyIdentifier() { return col57B_partyIdentifier; }

    @Presence(PresenceType.Optional)
    @ColumnId("57B")
    @LineRange(top = 1, bottom = 1)
    protected String col57B_location;

    public String get57B_location() { return col57B_location; }

    @Presence(PresenceType.Optional)
    @ColumnId("57C")
    protected String col57C_partyIdentifier;

    public String get57C_partyIdentifier() { return col57C_partyIdentifier; }

    @Presence(PresenceType.Optional)
    @ColumnId("57D")
    @LineRange(bottom = 0)
    protected String col57D_partyIdentifier;

    public String get57D_partyIdentifier() { return col57D_partyIdentifier; }

    @Presence(PresenceType.Optional)
    @ColumnId("57D")
    @LineRange(top = 1, bottom = 4)
    @ListItemType(String.class)
    protected List<String> col57D_nameAndAddress;

    public List<String> get57D_nameAndAddress() { return Collections.unmodifiableList(col57D_nameAndAddress); }

    @Presence(PresenceType.Mandatory)
    @ColumnId("59")
    @BeginAt(pos = 1)
    @LineRange(bottom = 0)
    @ColumnSizeRange(max = 34)
    protected String col59_account;

    public String get59_Account() { return col59_account; }

    @Presence(PresenceType.Mandatory)
    @ColumnId("59")
    @LineRange(top = 1, bottom = 4)
    @ColumnSizeRange(max = 35)
    @ListItemType(String.class)
    protected List<String> col59_nameAndAddress = new ArrayList<>();

    public List<String> get59_nameAndAddress() { return Collections.unmodifiableList(col59_nameAndAddress); }

    @Presence(PresenceType.Mandatory)
    @ColumnId("59A")
    @BeginAt(pos = 1)
    @LineRange(bottom = 0)
    @ColumnSizeRange(max = 34)
    protected String col59A_account;

    public String get59A_Account() { return col59A_account; }

    @Presence(PresenceType.Mandatory)
    @ColumnId("59A")
    @BeginAt(line = 1)
    protected String col59A_identifierCode;

    public String get59A_identifierCode() { return col59A_identifierCode; }

    @Presence(PresenceType.Mandatory)
    @ColumnId("59F")
    @BeginAt(pos = 1)
    @LineRange(bottom = 0)
    @ColumnSizeRange(max = 34)
    protected String col59F_account;

    public String get59F_Account() { return col59F_account; }

    @Presence(PresenceType.Mandatory)
    @ColumnId("59F")
    @LineRange(top = 1, bottom = 4)
    @IfRegex("^1/")
    @AfterLiteral("/")
    @ListItemType(String.class)
    protected List<String> col59F_nameOfBeneficiaryCustomer = new ArrayList<>();

    public List<String> get59F_beneficiaryCustomer() { return Collections.unmodifiableList(col59F_nameOfBeneficiaryCustomer); }

    @Presence(PresenceType.Mandatory)
    @ColumnId("59F")
    @LineRange(top = 1, bottom = 4)
    @IfRegex("^2/")
    @AfterLiteral("/")
    @ListItemType(String.class)
    protected List<String> col59F_addressLine = new ArrayList<>();

    public List<String> get59F_address() { return Collections.unmodifiableList(col59F_addressLine); }

    @Presence(PresenceType.Mandatory)
    @ColumnId("59F")
    @LineRange(top = 1, bottom = 4)
    @IfRegex("^3?/")
    @AfterLiteral("/")
    @ListItemType(String.class)
    protected List<String> col59F_countryAndTown = new ArrayList<>();

    public List<String> get59F_countryAndTown() { return Collections.unmodifiableList(col59F_countryAndTown); }

    @Presence(PresenceType.Optional)
    @ColumnId("70")
    @LineRange(top = 1, bottom = 4)
    @ListItemType(String.class)
    protected List<String> col70_remittanceInformation = new ArrayList<>();

    public List<String> get70() { return Collections.unmodifiableList(col70_remittanceInformation); }
    public List<String> getRemitInfo() { return Collections.unmodifiableList(col70_remittanceInformation); }

    @Presence(PresenceType.Mandatory)
    @ColumnId("71A")
    protected String col71A_detailsOfCharges;

    public String get71A() { return col71A_detailsOfCharges; }
    public String getDetailsOfCharges() { return col71A_detailsOfCharges; }

    @Presence(PresenceType.Optional)
    @ColumnId("71F")
    @ColumnSizeFixed(3)
    @ListItemType(String.class)
    protected List<String> col71F_currency = new ArrayList<>();

    public List<String> get71F_currency() { return Collections.unmodifiableList(col71F_currency); }

    @Presence(PresenceType.Optional)
    @ColumnId("71F")
    @BeginAt(pos = 3)
    @ColumnSizeRange(max = 15)
    @ListItemType(BigDecimal.class)
    protected List<BigDecimal> col71F_amount = new ArrayList<>();

    public List<BigDecimal> get71F_amount() { return Collections.unmodifiableList(col71F_amount); }

    @Presence(PresenceType.Optional)
    @ColumnId("71G")
    @ColumnSizeFixed(3)
    protected String col71G_currency;

    public String get71G_currency() { return col71G_currency; }

    @Presence(PresenceType.Optional)
    @ColumnId("71G")
    @BeginAt(pos = 3)
    @ColumnSizeRange(max = 15)
    protected BigDecimal col71G_amount;

    public BigDecimal get71G_amount() { return col71G_amount; }

    @Presence(PresenceType.Optional)
    @ColumnId("72")
    @LineRange(bottom = 5)
    @ColumnSizeRange(max = 35)
    @ListItemType(String.class)
    protected List<String> col72_senderToReceiverInformation = new ArrayList<>();

    public List<String> get72() { return Collections.unmodifiableList(col72_senderToReceiverInformation); }
    public List<String> getSenderToReceiverInfo() { return Collections.unmodifiableList(col72_senderToReceiverInformation); }

    @Presence(PresenceType.Optional)
    @ColumnId("77B")
    @LineRange(bottom = 2)
    @ColumnSizeRange(max = 35)
    @ListItemType(String.class)
    protected List<String> col77B_regulatoryReporting = new ArrayList<>();

    public List<String> get77B() { return Collections.unmodifiableList(col77B_regulatoryReporting); }
    public List<String> getRegulatoryReporting() { return Collections.unmodifiableList(col77B_regulatoryReporting); }

}