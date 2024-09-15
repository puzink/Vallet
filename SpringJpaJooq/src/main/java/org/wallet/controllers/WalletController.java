package org.wallet.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wallet.model.PerformActionRequest;
import org.wallet.model.PerformActionResponse;
import org.wallet.services.WalletService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/action")
    public PerformActionResponse performAction(@RequestBody @Valid PerformActionRequest request) {
        log.info("Start {} performing...", request.getAction());
        long amount = switch (request.getAction()) {
            case WITHDRAW -> walletService.withdraw(request.getUuid(), request.getAmount());
            case DEPOSIT -> walletService.deposit(request.getUuid(), request.getAmount());
        };

        return new PerformActionResponse(request.getUuid(), amount);
    }


}
