package com.licong.notemap.service;

import org.scribe.model.Token;

public interface LoginService {

    Token getRequestToken(String callbackUrl);

    String getUserOAuth(Token requestToken);

    Token getAccessToken(Token requestToken, String oauthVerifier, String callbackUrl);
}
