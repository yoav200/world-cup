package com.ab.worldcup.signin;

import com.ab.worldcup.account.AccountService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.util.SocialAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;


@Controller
@RequestMapping({"/socialauth"})
public class SocialauthController {

    @Autowired
    private SocialauthControllerConfig config;

    @Autowired
    private SocialAuthTemplate socialAuthTemplate;

    @Autowired
    private SocialAuthManager socialAuthManager;

    @Autowired
    private AccountService accountService;

    private final Log logger = LogFactory.getLog(this.getClass());

    @RequestMapping(params = {"id"})
    public String connect(@RequestParam("id") String providerId, HttpServletRequest request) throws Exception {
        this.logger.debug("Getting Authentication URL for :" + providerId);
        String callbackURL = config.getBaseCallbackUrl() + request.getServletPath();
        String url = this.socialAuthManager.getAuthenticationUrl(providerId, callbackURL);
        if (callbackURL.equals(url)) {
            url = config.getSuccessPageUrl();
            this.socialAuthManager.connect(new HashMap<>());
        }
        this.socialAuthTemplate.setSocialAuthManager(this.socialAuthManager);
        return "redirect:" + url;
    }

    @RequestMapping(params = {"oauth_token"})
    private String oauthCallback(HttpServletRequest request) {
        this.callback(request);
        return "redirect:/" + config.getSuccessPageUrl();
    }

    @RequestMapping(params = {"code"})
    private String oauth2Callback(HttpServletRequest request) {
        this.callback(request);
        return "redirect:/" + config.getSuccessPageUrl();
    }

    @RequestMapping(params = {"wrap_verification_code"})
    private String hotmailCallback(HttpServletRequest request) {
        this.callback(request);
        return "redirect:/" + config.getSuccessPageUrl();
    }

    @RequestMapping(params = {"openid.claimed_id"})
    private String openidCallback(HttpServletRequest request) {
        this.callback(request);
        return "redirect:/" + config.getSuccessPageUrl();
    }

    private void callback(HttpServletRequest request) {
        SocialAuthManager m = this.socialAuthTemplate.getSocialAuthManager();
        if (m != null) {
            try {
                AuthProvider provider = m.connect(SocialAuthUtil.getRequestParametersMap(request));
                this.logger.debug("Connected Provider : " + provider.getProviderId());
                accountService.registerWithProfile(provider.getUserProfile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.logger.debug("Unable to connect provider because SocialAuthManager object is null.");
        }
    }

    @RequestMapping(params = {"error", "error_reason"})
    private String fbCancel(@RequestParam("error_reason") String error) {
        this.logger.debug("Facebook send an error : " + error);
        return "user_denied".equals(error) ? "redirect:/" + config.getAccessDeniedPageUrl() : "redirect:/";
    }

    @RequestMapping(params = {"openid.mode=cancel"})
    private String googleCancel(@RequestParam("openid.mode") String error) {
        this.logger.debug("Google send an error : " + error);
        return "cancel".equals(error) ? "redirect:/" + config.getAccessDeniedPageUrl() : "redirect:/";
    }

    @RequestMapping(params = {"wrap_error_reason"})
    private String hotmailCancel(@RequestParam("wrap_error_reason") String error) {
        this.logger.debug("Hotmail send an error : " + error);
        return "user_denied".equals(error) ? "redirect:/" + config.getAccessDeniedPageUrl() : "redirect:/";
    }

    @RequestMapping(params = {"oauth_problem"})
    private String myspaceCancel(@RequestParam("oauth_problem") String error) {
        this.logger.debug("MySpace send an error : " + error);
        return "user_refused".equals(error) ? "redirect:/" + config.getAccessDeniedPageUrl() : "redirect:/";
    }

    @RequestMapping(params = {"error"})
    private String gitHubCancel(@RequestParam("error") String error) {
        this.logger.debug("Provider send an error : " + error);
        return "access_denied".equals(error) ? "redirect:/" + config.getAccessDeniedPageUrl() : "redirect:/";
    }

}
