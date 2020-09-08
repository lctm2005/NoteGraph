package com.licong.notemap.web.security.evernote;

import com.evernote.edam.type.Notebook;
import lombok.Getter;
import lombok.Setter;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;
import org.scribe.utils.OAuthEncoder;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.licong.notemap.web.security.evernote.EvernoteAuthenticationConstant.*;

@Getter
@Setter
public class EvernoteAccessToken extends Token {

    private String shard;
    private Long userId;
    private Long expired;
    private String noteStoreUrl;
    private Notebook notebook;

    public EvernoteAccessToken(String token, String secret, String shard, Long userId, Long expired, String noteStoreUrl) {
        super(token, secret);
        this.shard = shard;
        this.userId = userId;
        this.expired = expired;
        this.noteStoreUrl = noteStoreUrl;
    }

    public EvernoteAccessToken(Token token) {
        super(token.getToken(), token.getSecret(), token.getRawResponse());
        this.shard = extract(token.getRawResponse(), EDAM_SHARD_REGEX);
        this.userId = Long.valueOf(extract(token.getRawResponse(), EDAM_USER_ID_REGEX));
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
     *
     * @return
     */
    public boolean expired() {
        Calendar deadline = Calendar.getInstance();
        deadline.setTimeInMillis(expired);
        deadline.add(Calendar.DATE, -1);

        Calendar now = Calendar.getInstance();
        return now.after(deadline);
    }

    public static EvernoteAccessToken DEVELOP_TOKEN = new EvernoteAccessToken(
            "S=s31:U=5a2e79:E=172dabeb71a:C=172b6b231d8:P=1cd:A=en-devtoken:V=2:H=8d10a1f653a40a619a74e89fb73b923c",
            "",
            "31",
            0L,
            1631005061L,
            "https://app.yinxiang.com/shard/s31/notestore");
}
