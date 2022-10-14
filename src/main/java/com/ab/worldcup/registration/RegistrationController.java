package com.ab.worldcup.registration;

import com.ab.worldcup.account.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@Controller
@RequestMapping(path = "/api/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    @ResponseBody
    public Account register(@Valid @RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        registrationService.confirmToken(token);
        return "redirect:/confirmed";
    }

}
