# wechat-mch-mock
微信支付API V3 Mock, 也是一个简单的非对称签名验签Demo

用了Springboot3，所以需要Java17+

## Mock接口
- /v3/

## 说明

- `src`目录下为Mock服务器
- `wechat-pay-demo`为测试客户端demo

### Mock服务器需要的一些密钥
相关的密钥保存在`src/main/resources/secret`目录下，可以通过命令生成自己的，按下面方式命名。
- `mock_client_nnnn_xxxx.cer`为客户端(也就是商户)的证书(即提交给微信支付平台的证书、序列号)，`nnnn`为商户号，`xxxx`为ApiV3Key。服务器通过加载`mock_client_*_*.cer`名称的文件来加载商户信息。
- `mock_server_xxxx.cer`为服务端证书，`nnnn`为商户号。（即`https://api.mch.weixin.qq.com/v3/certificates`接口返回的证书)
- `mock_server.p12`中保存服务端的密钥(储存了多个)，每个商户一个根据密钥的名称（`mch-xxxx`）区分。

`com.muedsa.mock.wechat.mch.cert.MerchantManagerImpl`管理商户的相关信息(mchId、apiV3Key、商户上传的证书`mock_client_nnnn_xxxx.cer`)。

`com.muedsa.mock.wechat.mch.cert.PlatformSecretManager`管理服务器生成密钥的证书(发送给商户的证书`mock_server_xxxx.cer`)
