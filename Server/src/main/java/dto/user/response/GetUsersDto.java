package dto.user.response;

import java.util.ArrayList;

public record GetUsersDto(String token, ArrayList<LoginResponseDto> users) {}