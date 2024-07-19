package org.wallet.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wallet.model.PerformActionRequest;
import org.wallet.model.PerformActionResponse;

@RestController
public class WalletController {

    @PostMapping("/action")
    public PerformActionResponse performAction(@RequestBody @Valid PerformActionRequest request) {

        return null;
    }


}
