package network.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BankInfoDto {
    @JsonProperty("bank_name")
    public String bankName;

    @JsonProperty("account_number")
    public String accountNumber;

    public BankInfoDto() {}

    public BankInfoDto(String bankName, String accountNumber) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
    }
}
