package com.muedsa.mock.wechat.mch.cert;

import java.security.PrivateKey;

public interface PlatformPrivateManager {
    PrivateKey getPrivateKey(String mchId);

    void loadPrivateKey();
}
