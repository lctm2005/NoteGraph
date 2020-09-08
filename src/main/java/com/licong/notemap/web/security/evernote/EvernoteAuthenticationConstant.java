package com.licong.notemap.web.security.evernote;

import java.util.regex.Pattern;

public class EvernoteAuthenticationConstant {
    public static final String ACCESS_TOKEN_URI = "/accessToken";
    public static final String OAUTH_VERIFIER_PARAM = "oauth_verifier";
    public static final String LOGIN_SERVICE_NAME = "evernoteLoginServiceImpl";
    public static final Pattern EDAM_SHARD_REGEX = Pattern.compile("edam_shard=([^&]+)");
    public static final Pattern EDAM_USER_ID_REGEX = Pattern.compile("edam_userId=([^&]*)");
    public static final Pattern EDAM_EXPIRES_REGEX = Pattern.compile("edam_expires=([^&]*)");
    public static final Pattern EDAM_NOTE_STORE_URL_REGEX = Pattern.compile("edam_noteStoreUrl=([^&]*)");
    public static final String USER_AGENT = "NoteGraph/1.0.0";
    public static final String APP_NOTEBOOK_NAME = "NoteGraph";
    public static final String OAUTH_PARAM_NOTEBOOK = "&preferRegistration=true&supportLinkedSandbox=true&suggestedNotebookName=" + APP_NOTEBOOK_NAME;
}
