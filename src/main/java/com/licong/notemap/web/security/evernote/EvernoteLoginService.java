package com.licong.notemap.web.security.evernote;

import com.evernote.edam.type.Notebook;
import org.scribe.model.Token;

public interface EvernoteLoginService {

    Token getRequestToken(String callbackUrl);

    String getUserOAuth(Token requestToken);

    Token getAccessToken(Token requestToken, String oauthVerifier, String callbackUrl);

    Notebook getAuthorizedNotebook(String noteStoreUrl, String accessToken);
}
