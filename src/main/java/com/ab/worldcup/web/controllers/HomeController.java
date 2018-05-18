package com.ab.worldcup.web.controllers;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.signin.Role;
import com.ab.worldcup.web.components.SessionManager;
import com.ab.worldcup.web.model.HeartBeatData;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "index.html";
    }


    @RequestMapping("/api/heartbeat")
    @ResponseBody
    public ResponseEntity<HeartBeatData> heartbeat(HttpSession session, @RequestBody Account account) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Boolean valid = true;
        //check session timeout
        Object attribute = session.getAttribute(SessionManager.LAST_REQUEST_TIME);
        if (attribute != null) {
            long lastRequestTime = (Long) attribute;
            // inactive
            if (System.currentTimeMillis() - lastRequestTime > SessionManager.MAX_INACTIVE_SESSION_TIME) {
                valid = false;
            }
        }
        // check that client and server roles are the same
        if (valid && account != null && CollectionUtils.isNotEmpty(account.getRoles())) {
            List<String> clientRoles = account.getRoles().stream()
                    .filter(r -> Role.supportedRolesAsString().contains(r))
                    .collect(Collectors.toList());

            List<String> serverRoles = authentication.getAuthorities().stream()
                    .map(Object::toString)
                    .filter(r -> Role.supportedRolesAsString().contains(r))
                    .collect(Collectors.toList());

            valid = serverRoles.equals(clientRoles);
        }
        HeartBeatData heartBeatData = HeartBeatData.builder().dateTime(LocalDateTime.now()).valid(valid).build();
        return new ResponseEntity<>(heartBeatData, HttpStatus.OK);
    }


}
