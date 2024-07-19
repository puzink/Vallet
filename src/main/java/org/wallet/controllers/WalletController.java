package org.wallet.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wallet.model.PerformActionRequest;
import org.wallet.model.PerformActionResponse;
import org.wallet.services.WalletService;

@RestController
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/action")
    public PerformActionResponse performAction(@RequestBody @Valid PerformActionRequest request) {

        long amount = switch (request.getAction()) {
            case WITHDRAW -> walletService.withdraw(request.getUuid(), request.getAmount());
            case DEPOSIT -> walletService.deposit(request.getUuid(), request.getAmount());
        };

        return new PerformActionResponse(request.getUuid(), amount);
    }


}
