package com.OEzoa.OEasy.api.member;

import com.OEzoa.OEasy.application.member.AuthService;
import com.OEzoa.OEasy.application.member.dto.AuthTokenResponseDTO;
import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Member Auth API", description = "회원 인증을 관리합니다.")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/refresh")
    @Operation(
            summary = "리프레시 토큰을 통한 액세스 토큰 재발급",
            description = "만료된 액세스 토큰을 리프레시 토큰으로 갱신합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "액세스 토큰과 리프레시 토큰 재발급 성공."),
                    @ApiResponse(responseCode = "401", description = "리프레시 토큰 만료로 인한 재발급 실패."),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 토큰 갱신 중 오류 발생.")
            }
    )
    public ResponseEntity<AuthTokenResponseDTO> refreshAccessToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {
        if (refreshToken == null) {
            throw new GlobalException(GlobalExceptionCode.NONE_REFRESH_TOKEN);
        }
        String newAccessToken = authService.refreshAccessToken(refreshToken, response);
        return ResponseEntity.ok(new AuthTokenResponseDTO(newAccessToken));
    }
}
