package com.licong.notemap.web.security;

import com.evernote.auth.EvernoteService;
import com.evernote.clients.YinXiangApi;
import com.evernote.clients.YinXiangSandboxApi;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.User;
import com.evernote.edam.userstore.UserStore;
import com.evernote.thrift.TException;
import com.evernote.thrift.protocol.TBinaryProtocol;
import com.evernote.thrift.transport.THttpClient;
import com.evernote.thrift.transport.TTransportException;
import lombok.extern.slf4j.Slf4j;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.EvernoteApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.licong.notemap.web.security.EvernoteAuthenticationConstant.LOGIN_SERVICE_NAME;
import static com.licong.notemap.web.security.EvernoteAuthenticationConstant.USER_AGENT;

@Slf4j
@Service(LOGIN_SERVICE_NAME)
public class EvernoteLoginServiceImpl implements EvernoteLoginService {

    private static final String PRODUCTION = "production";
    private static final String CONSUMER_KEY = "licong2011";
    private static final String CONSUMER_SECRET = "7a1bb7c45518038f";

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
