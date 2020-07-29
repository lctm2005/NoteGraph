package com.licong.notemap.web.security;

import com.evernote.edam.type.Notebook;
import lombok.Getter;
import lombok.Setter;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;
import org.scribe.utils.OAuthEncoder;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.licong.notemap.web.security.EvernoteAuthenticationConstant.*;

@Getter
@Setter
public class EvernoteAccessToken extends Token {

    private String shard;
    private Long userId;
    private Long expired;
    private String noteStoreUrl;
    private Notebook notebook;

    public EvernoteAccessToken(Token token) {
        super(token.getToken(), token.getSecret(), token.getRawResponse());
        this.shard = extract(token.getRawResponse(), EDAM_SHARD_REGEX);
        this.userId =  Long.valueOf(extract(token.getRawResponse(), EDAM_USER_ID_REGEX));
        this.expired = Long.valueOf(extract(token.getRawResponse(), EDAM_EXPIRES_REGEX));
        this.noteStoreUrl = extract(token.getRawResponse(), EDAM_NOTE_STORE_URL_REGEX);
    }

    private String extract(String response, Pattern p) {
        Matcher matcher = p.matcher(response);
        if (matcher.find() && matcher.groupCount() >= 1) {
            return OAuthEncoder.decode(matcher.group(1));
        } else {
            throw new OAuthException("Response body is incorrect. Can't extract token and secret from this: '" + response + "'", null);
        }
    }

    /**
     * 提前1天失效
     * @return
     */
    public boolean expired() {
        Calendar deadline = Calendar.getInstance();
        deadline.setTimeInMillis(expired);
        deadline.add(Calendar.DATE, -1);

        Calendar now = Calendar.getInstance();
        return now.after(deadline);
    }
}
