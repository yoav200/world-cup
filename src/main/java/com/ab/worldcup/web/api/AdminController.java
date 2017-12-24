package com.ab.worldcup.web.api;

import com.ab.worldcup.results.MatchResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @RequestMapping(value = "/match/{matchId}/result", method = RequestMethod.POST)
    public void setResults(@PathVariable MatchResult result) {

    }
}
