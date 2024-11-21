package com.OEzoa.OEasy.api.aioe;

import static com.OEzoa.OEasy.util.HeaderUtils.extractTokenFromHeader;

import com.OEzoa.OEasy.application.aioe.AioeService;
import com.OEzoa.OEasy.application.aioe.dto.AioeIntroMessageDTO;
import com.OEzoa.OEasy.application.aioe.dto.AioeRequestDTO;
import com.OEzoa.OEasy.application.aioe.dto.AioeResponseDTO;
import com.OEzoa.OEasy.application.aioe.dto.ChatHistoryDTO;
import com.OEzoa.OEasy.domain.aioe.ChatMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/aioe")
@Tag(name = "AI OE API", description = "서비스 챗봇 AI OE로 응답메세지를 생성합니다.")
public class AioeController {

    private final AioeService aioeService;

    // 챗봇 시작
    @PostMapping("/start")
    @Operation(
            summary = "챗봇 시작",
            description = "챗봇과 연결되며 기본 메세지를 출력합니다.")
    @ApiResponse(responseCode = "200", description = "챗봇이 성공적으로 시작되었습니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    public ResponseEntity<?> startChatbot(@RequestHeader("Authorization") String authorizationHeader) {
        String accessToken = extractTokenFromHeader(authorizationHeader);
        AioeIntroMessageDTO response = aioeService.startChatbot(accessToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/question")
    @Operation(
            summary = "사용자 질문 처리",
            description = "사용자의 질문을 받고, GPT 로직을 통해 응답합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "질문 처리 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    public ResponseEntity<AioeResponseDTO> handleUserQuestion(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @RequestBody AioeRequestDTO questionRequest) {
        String accessToken = extractTokenFromHeader(authorizationHeader);
        AioeResponseDTO response = aioeService.handleUserQuestionWithTimestamp(questionRequest.getQuestion(), accessToken);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    @Operation(
            summary = "대화 기록 조회",
            description = "사용자의 대화 기록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "대화 기록 조회 성공")
            }
    )
    public ResponseEntity<ChatHistoryDTO> getChatHistory(
            @RequestHeader(name = "Authorization") String authorizationHeader) {
        String accessToken = extractTokenFromHeader(authorizationHeader);
        ChatHistoryDTO history = aioeService.getChatHistory(accessToken);
        return ResponseEntity.ok(history);
    }

    @DeleteMapping("/history")
    @Operation(
            summary = "대화 기록 삭제",
            description = "사용자의 대화 기록을 모두 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "대화 기록 삭제 성공")
            }
    )
    public ResponseEntity<String> deleteChatHistory(
            @RequestHeader(name = "Authorization") String authorizationHeader) {
        String accessToken = extractTokenFromHeader(authorizationHeader);
        aioeService.deleteChatbotConnection(accessToken); // 메서드 이름 수정
        return ResponseEntity.ok("채팅 로그와 챗봇 연결이 삭제되었습니다.");
    }
}
