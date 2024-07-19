package org.wallet.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.wallet.model.Action;
import org.wallet.model.ErrorType;
import org.wallet.model.PerformActionRequest;

import java.util.UUID;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WalletControllerTest {

    static final String URL = "/action";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideNoArgsRequests")
    @DisplayName("null args - 400 error")
    public void noArgs(String name, UUID uuid, Action action, long amount) throws Exception {
        PerformActionRequest request = new PerformActionRequest(uuid, action, amount);

        performActionAndExpectError(request, ErrorType.BAD_INPUT, HttpStatus.BAD_REQUEST);
    }

    private static Stream<Arguments> provideNoArgsRequests() {
        return Stream.of(
                Arguments.of("no uuid", null, Action.DEPOSIT, 10L),
                Arguments.of("no action", UUID.randomUUID(), null, 10L)
        );
    }

    @Nested
    public class WithdrawTest{

        @Test
        @DisplayName("no wallet is found - 404 error")
        public void noWalletExists() throws Exception {
            UUID userId = UUID.randomUUID();
            PerformActionRequest request = new PerformActionRequest(userId, Action.WITHDRAW, 100L);

            performActionAndExpectError(request, ErrorType.WALLET_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

    }

    @Nested
    public class DepositTest{

        @Test
        @DisplayName("no wallet is found - new wallet")
        public void noWalletExists() throws Exception {
            UUID userId = UUID.randomUUID();
            long amount = 100L;
            PerformActionRequest request = new PerformActionRequest(userId, Action.DEPOSIT, amount);

            String contentJson = objectMapper.writeValueAsString(request);
            mockMvc.perform(post(URL)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(contentJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.uuid").value(userId.toString()))
                    .andExpect(jsonPath("$.amount").value(amount));
        }
    }

    private void performActionAndExpectError(PerformActionRequest request, ErrorType errorType, HttpStatus httpStatus) throws Exception {
        String contentJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(contentJson))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.type").value(errorType.name()))
                .andExpect(jsonPath("$.message").isString());
    }


}
