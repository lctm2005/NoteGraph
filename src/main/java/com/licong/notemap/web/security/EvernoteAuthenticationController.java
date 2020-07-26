package com.licong.notemap.web.security;

import com.licong.notemap.service.LoginService;
import com.licong.notemap.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.scribe.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class EvernoteAuthenticationController {

    @Autowired
    private LoginService loginService;

    /**
     * @param request
     * @param session
     * @param oauthToken    临时的Token
     * @param oauthVerifier （可选项）只有在用户成功授权了你的应用程序访问他们的印象笔记账户，才会有这个值
     * @return
     */
    @RequestMapping("/accessToken")
    public ModelAndView accessToken(HttpServletRequest request, HttpSession session,
                                    @RequestParam(value = "oauth_token", required = false) String oauthToken,
                                    @RequestParam(value = "oauth_verifier", required = false) String oauthVerifier) {
        Token requestToken = (Token) session.getAttribute("requestToken");
        // 取回 Access Token
        if (StringUtils.isEmpty(oauthVerifier)) {
            return new ModelAndView(new RedirectView("/"));
        }
        Token accessToken = loginService.getAccessToken(requestToken, oauthVerifier, request.getRequestURL().toString());
        session.setAttribute("accessToken", accessToken);
        return new ModelAndView(new RedirectView("/"));
    }
}
