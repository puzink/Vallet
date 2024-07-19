package org.wallet.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wallet.model.PerformActionResponse;
import org.wallet.model.PerformActionRequest;

@RestController
public class WalletController {

    @PostMapping("/action")
    public PerformActionResponse performAction(@RequestBody PerformActionRequest request){



        return null;
    }


}
