package vn.edu.iuh.fit.olachatbackend.controllers;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.hibernate.engine.jdbc.mutation.internal.MutationExecutorSingleSelfExecuting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.olachatbackend.dtos.requests.*;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.MessageResponse;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.AuthenticationResponse;
import vn.edu.iuh.fit.olachatbackend.dtos.responses.IntrospectResponse;
import vn.edu.iuh.fit.olachatbackend.services.AuthenticationService;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    MessageResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request, HttpServletResponse response) throws ParseException {
        var result = authenticationService.authenticate(request, response);
        return MessageResponse.<AuthenticationResponse>builder()
                .message("Đăng nhập thành công")
                .data(result)
                .build();
    }

    @PostMapping("/login/google")
    public MessageResponse<AuthenticationResponse> googleLogin(@RequestBody Map<String, String> request,
                                                               @RequestParam("deviceId") String deviceId) {
        String idToken = request.get("idToken");
        AuthenticationResponse response = authenticationService.loginWithGoogle(idToken, deviceId);
        return MessageResponse.<AuthenticationResponse>builder()
                .message("Đăng nhập thành công")
                .data(response)
                .build();
    }

    @PostMapping("/login/facebook")
    public MessageResponse<AuthenticationResponse> facebookLogin(@RequestBody Map<String, String> request,
                                                                 @RequestParam("deviceId") String deviceId) {
        String accessToken = request.get("accessToken");
        AuthenticationResponse response = authenticationService.loginWithFacebook(accessToken, deviceId);
        return MessageResponse.<AuthenticationResponse>builder()
                .message("Đăng nhập thành công")
                .data(response)
                .build();
    }

    @PostMapping("/introspect")
    MessageResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return MessageResponse.<IntrospectResponse>builder()
                .message("Kiểm tra token thành công")
                .data(result)
                .build();
    }

    @PostMapping("/logout")
    MessageResponse<Void> logout(@RequestBody LogoutRequest request, HttpServletResponse response) throws ParseException, JOSEException {
        authenticationService.logout(request, response);
        return MessageResponse.<Void>builder()
                .message("Đăng xuất thành công")
                .data(null)
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<MessageResponse<AuthenticationResponse>> refreshToken(@RequestBody RefreshTokenRequest request, HttpServletResponse response) throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request.getRefreshToken(), response);
        return ResponseEntity.ok(
                MessageResponse.<AuthenticationResponse>builder()
                        .message("Làm mới token thành công")
                        .data(result)
                        .build()
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> sendOtp(@RequestParam String email) {
        authenticationService.processForgotPassword(email);
        return ResponseEntity.ok(MessageResponse.builder()
                .message("Đã gửi OPT về mail của bạn, vui lòng kiểm tra.")
                .build());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> verifyOtp(@RequestBody ResetPasswordRequest otpRequest) {
        authenticationService.resetPassword(otpRequest);
        return ResponseEntity.ok(MessageResponse.builder()
                .message("Reset mật khẩu thành công.")
                .build());
    }




}
