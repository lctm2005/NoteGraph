package com.licong.notemap.web.security;

import com.evernote.auth.EvernoteService;
import com.evernote.clients.YinXiangApi;
import com.evernote.clients.YinXiangSandboxApi;
import com.evernote.edam.type.Notebook;
import com.licong.notemap.repository.evernote.EvernoteRepository;
import com.licong.notemap.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.EvernoteApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.licong.notemap.web.security.EvernoteAuthenticationConstant.*;

@Slf4j
@Service(LOGIN_SERVICE_NAME)
public class EvernoteLoginServiceImpl implements EvernoteLoginService {

    private static final String PRODUCTION = "production";
    private static final String CONSUMER_KEY = "licong2011";
    private static final String CONSUMER_SECRET = "7a1bb7c45518038f";

    @Value("${spring.profiles.active}")
    private String env;

    @Autowired
    private EvernoteRepository evernoteRepository;


    @Override
    public Notebook getAuthorizedNotebook(String noteStoreUrl, String accessToken) {
        List<Notebook> notebooks = evernoteRepository.findNotebooks(noteStoreUrl, accessToken);
        if (CollectionUtils.isEmpty(notebooks)) {
            Notebook notebook = evernoteRepository.createAppNotebook(noteStoreUrl, accessToken);
            return evernoteRepository.getNotebook(noteStoreUrl, accessToken, notebook.getGuid());
        } else {
            return evernoteRepository.getNotebook(noteStoreUrl, accessToken, notebooks.get(0).getGuid());
        }
    }


    @Override
    public Token getRequestToken(String callbackUrl) {
        OAuthService service = getOAuthService(callbackUrl);
        return service.getRequestToken();
    }

    @Override
    public String getUserOAuth(Token requestToken) {
        EvernoteService evernoteService = getEverNoteService();
        return evernoteService.getAuthorizationUrl(requestToken.getToken()) + OAUTH_PARAM_NOTEBOOK;
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
