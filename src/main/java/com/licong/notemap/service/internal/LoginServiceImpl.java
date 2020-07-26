package com.licong.notemap.service.internal;

import com.evernote.auth.EvernoteService;
import com.evernote.clients.YinXiangApi;
import com.evernote.clients.YinXiangSandboxApi;
import com.licong.notemap.repository.evernote.EvernoteRepository;
import com.licong.notemap.service.LoginService;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.EvernoteApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private static final String PRODUCTION = "production";
    private static final String CONSUMER_KEY = "licong2011";
    private static final String CONSUMER_SECRET = "7a1bb7c45518038f";

    @Autowired
    private EvernoteRepository evernoteRepository;

    @Value("${spring.profiles.active}")
    private String env;


    @Override
    public Token getRequestToken(String callbackUrl) {
        OAuthService service = getOAuthService(callbackUrl);
        return service.getRequestToken();
    }

    @Override
    public String getUserOAuth(Token requestToken) {
        EvernoteService evernoteService = getEverNoteService();
        return evernoteService.getAuthorizationUrl(requestToken.getToken());
    }

    @Override
    public Token getAccessToken(Token requestToken, String oauthVerifier, String callbackUrl) {
        OAuthService service = getOAuthService(callbackUrl);
        return service.getAccessToken(requestToken, new Verifier(oauthVerifier));
    }

    private OAuthService getOAuthService(String callbackUrl) {
        Class<? extends EvernoteApi> providerClass = YinXiangSandboxApi.class;

        if (PRODUCTION.equals(env)) {
            providerClass = YinXiangApi.class;
        }
        return new ServiceBuilder()
                .provider(providerClass)
                .apiKey(CONSUMER_KEY)
                .apiSecret(CONSUMER_SECRET)
                .callback(callbackUrl)
                .build();
    }

    private EvernoteService getEverNoteService() {
        EvernoteService evernoteService = EvernoteService.YINXIANG_SANDBOX;
        if (PRODUCTION.equals(env)) {
            evernoteService = EvernoteService.YINXIANG;
        }
        return evernoteService;
    }

}
