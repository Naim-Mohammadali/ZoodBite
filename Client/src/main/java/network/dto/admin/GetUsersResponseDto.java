package network.dto.admin;

import network.dto.user.UserDto;
import java.util.List;

public class GetUsersResponseDto {
    public String token;
    public List<UserDto> users;
}
