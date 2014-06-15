package com.angry.web;

import com.google.api.server.spi.config.Api;

/**
 * Created by Artur on 2014-06-08.
 */
@Api(
        name = "deviceStateEndpoint",
        version = "v1",
        scopes = {Constants.EMAIL_SCOPE},
        clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID, Constants.IOS_CLIENT_ID},
        audiences = {Constants.ANDROID_AUDIENCE}
)

public class Constants {
    public static final String WEB_CLIENT_ID = "469538464821-hsd1qh2pt58bigmjkh6nkib8r00vkm0f.apps.googleusercontent.com";
    public static final String ANDROID_CLIENT_ID = "469538464821-79tlguh45dos56irvtr4nhsa0965jlvp.apps.googleusercontent.com";
    public static final String IOS_CLIENT_ID = "3-ios-apps.googleusercontent.com";
    public static final String ANDROID_AUDIENCE = WEB_CLIENT_ID;

    public static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
}