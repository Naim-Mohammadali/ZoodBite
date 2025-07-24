package dto.user.request;

public record BankInfoDto(
        String bank_name,
        String account_number
) {}
