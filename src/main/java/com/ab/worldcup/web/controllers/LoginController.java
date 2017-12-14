package com.ab.worldcup.web.controllers;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/login")
public class LoginController {

//    @Autowired
//    private Facebook facebook;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private ConnectionFactoryLocator connectionFactoryLocator;


//    @GetMapping
//    public String helloFacebook(Model model) {
//        if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
//            return "redirect:/connect/facebook";
//        }
//
//        model.addAttribute("facebookProfile", facebook.userOperations().getUserProfile());
//        PagedList<Post> feed = facebook.feedOperations().getFeed();
//        model.addAttribute("feed", feed);
//        return "hello";
//    }

    @RequestMapping(value = "/identity", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET)
    @ResponseBody
    public Connection connect() {
        Connection connection = null;
        final Set<String> registeredProviderIds = connectionFactoryLocator.registeredProviderIds();
        final Map<String, List<Connection<?>>> connections = connectionRepository.findAllConnections();

        if (CollectionUtils.isNotEmpty(registeredProviderIds)) {
            for (String providerId : registeredProviderIds) {
                List<Connection<?>> connectionList = connections.get(providerId);
                if (CollectionUtils.isNotEmpty(connectionList)) {
                    connection = connectionList.get(0);
                }
            }
        }
        return connection;
    }
}
