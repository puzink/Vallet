package org.wallet.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.transaction.annotation.Transactional;
import org.wallet.entities.Wallet;
import org.wallet.model.Action;
import org.wallet.model.ErrorType;
import org.wallet.model.PerformActionRequest;
import org.wallet.model.PerformActionResponse;

import java.util.UUID;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class WalletControllerTest {

    static final String URL = "/action";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    static Wallet existingWallet;

    @BeforeAll
    public static void setup() {
        existingWallet = new Wallet(
                UUID.fromString("4906eed1-3ee9-47bb-b10f-596e2d071ea7"),
                1000L
        );
    }

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

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideWrongRequests")
    @DisplayName("wrong args - 400 error")
    public void wrongArgs(String name, String uuid, String action, long amount) throws Exception {
        String json =
                """
                            {
                               "uuid": "%s",
                               "action": "%s",
                               "amount": %d
                            }
                        """.formatted(uuid, action, amount);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> provideWrongRequests() {
        return Stream.of(
                Arguments.of("bad uuid", "fdasfsa", Action.DEPOSIT.name(), 10L),
                Arguments.of("bad action", UUID.randomUUID().toString(), "FDSSA", 10L),
                Arguments.of("negative amount", UUID.randomUUID().toString(), Action.WITHDRAW.name(), -10L)

        );
    }

    @Nested
    public class WithdrawTest {

        @Test
        @DisplayName("no wallet is found - 404 error")
        public void noWalletExists() throws Exception {
            UUID userId = UUID.randomUUID();
            PerformActionRequest request = new PerformActionRequest(userId, Action.WITHDRAW, 100L);

            performActionAndExpectError(request, ErrorType.WALLET_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("no money - 400 error")
        public void notEnoughMoney() throws Exception {
            long amount = existingWallet.getAmount();
            PerformActionRequest request = new PerformActionRequest(
                    existingWallet.getUuid(), Action.WITHDRAW, amount + 100);

            performActionAndExpectError(request, ErrorType.NOT_ENOUGH_FUNDS, HttpStatus.BAD_REQUEST);
        }

        @Test
        public void completeWithdraw() throws Exception {
            PerformActionRequest request = new PerformActionRequest(
                    existingWallet.getUuid(), Action.WITHDRAW, existingWallet.getAmount());

            performActionAndExpectOk(request, new PerformActionResponse(existingWallet.getUuid(), 0L));
        }

    }

    @Nested
    public class DepositTest {

        @Test
        @DisplayName("no wallet is found - new wallet")
        public void noWalletExists() throws Exception {
            UUID userId = UUID.randomUUID();
            long amount = 100L;
            PerformActionRequest request = new PerformActionRequest(userId, Action.DEPOSIT, amount);

            performActionAndExpectOk(request, new PerformActionResponse(userId, 100L));
            System.out.println();
        }

        @Test
        public void depositToExistingWallet() throws Exception {
            long amount = 100L;
            PerformActionRequest request =
                    new PerformActionRequest(existingWallet.getUuid(), Action.DEPOSIT, amount);

            performActionAndExpectOk(request,
                    new PerformActionResponse(existingWallet.getUuid(),
                            existingWallet.getAmount() + 100L)
            );
        }
    }

    private void performActionAndExpectOk(PerformActionRequest request, PerformActionResponse expected) throws Exception {
        String contentJson = objectMapper.writeValueAsString(request);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(contentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(expected.getUuid().toString()))
                .andExpect(jsonPath("$.amount").value(expected.getAmount()));
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
