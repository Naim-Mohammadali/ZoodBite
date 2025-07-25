import network.dto.BankInfoDto;
import network.dto.LoginRequestDto;
import network.dto.LoginResponseDto;
import network.dto.RegisterRequestDto;
import network.endpoint.UserEndpoint;
import util.SessionManager;

public class Main {
    public static void main(String[] args) {
        try {
//            var dto = new LoginRequestDto("admin", "admin");
//            var endpoint = new UserEndpoint();
//            var response = endpoint.login(dto);
//
//            SessionManager.getInstance().setToken(response.token);
//            SessionManager.getInstance().setLoggedInUser(response.user);
//
//            System.out.println("Login success. Token: " + response.token);
//            System.out.println("User: " + response.user.name);
            RegisterRequestDto dto = new RegisterRequestDto(
                    "Alexis Seller",
                    "9890274654",
                    "alexis@example.com",
                    "SecurePass123!",
                    "456 Seller St",
                    "SELLER",
                    new BankInfoDto("Test Bank", "0987654321")
            );

            LoginResponseDto response = new UserEndpoint().register(dto);
            SessionManager.getInstance().setToken(response.token);
            SessionManager.getInstance().setLoggedInUser(response.user);

            System.out.println("Registered as: " + response.user.name + " (" + response.user.role + ")");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
