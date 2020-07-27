package com.licong.notemap.web.security;

import org.scribe.model.Token;

public interface EvernoteLoginService {

    Token getRequestToken(String callbackUrl);

    String getUserOAuth(Token requestToken);

    Token getAccessToken(Token requestToken, String oauthVerifier, String callbackUrl);
}
