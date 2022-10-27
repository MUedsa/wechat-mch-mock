## CA

### 生成CA根密钥
```shell
keytool -genkeypair -alias ca-root -keyalg RSA -keysize 2048 -dname "CN=MUEDSA MOCK CA, OU=MUEDSA MOCK CA, O=Unknown, L=Unknown, ST=Unknown, C=Unknown" -validity 36500 -storetype pkcs12 -keystore ca_roor.p12 -keypass muedsacapassword -storepass muedsacapassword -v

```
### 自签名CA证书
```shell
keytool -exportcert -file ca_roor.cer -alias ca-root -keystore ca_roor.p12 -storepass muedsacapassword -v
```

## Mock服务器

### 生成服务器密钥

```shell
keytool -genkeypair -keyalg RSA -alias mch-1900006000 -keysize 2048 -dname "CN=mch-1900006000, OU=mch-1900006000, O=Unknown, L=Unknown, ST=Unknown, C=Unknown" -validity 365 -storetype pkcs12 -keystore mock_server.p12 -keypass 123456 -storepass 123456 -v
```

### 用服务器密钥生成证书签名请求
```shell
keytool -certreq -file mock_server_1900006000.csr -keystore mock_server.p12 -alias mch-1900006000 -keypass 123456 -storepass 123456 -v
```

### CA颁发证书 用CA根密钥给请求文件签名
```shell
keytool -gencert -infile mock_server_1900006000.csr -outfile mock_server_1900006000.cer -alias ca-root -keystore ca_roor.p12 -storepass muedsacapassword -v
```

--------

## Mock客户端

### 客户端生成密钥
```shell
keytool -genkeypair -keyalg RSA -alias mch-1900006000 -keysize 2048 -dname "CN=mch-1900006000, OU=mch-1900006000, O=Unknown, L=Unknown, ST=Unknown, C=Unknown" -validity 365 -storetype pkcs12 -keystore mock_client_1900006000.p12 -keypass 123456 -storepass 123456 -v
```

### 客户端密钥导出证书
```shell
keytool -export -alias mch-1900006000 -keystore mock_client_1900006000.p12 -storetype PKCS12 -storepass 123456 -keypass 123456 -file mock_client_1900006000.cer -v
```

------



### 添加信任
```shell
keytool -import -v -file mock_client1.cer -keystore mock_server.jks -storepass 123456
```

### 导出服务端证书
```shell
keytool -keystore mock_server.jks -export -alias mock-server -file mock_server.cer -storepass 123456
```



## openssl

```shell
openssl pkcs12 -nocerts -in mock_client_1900006000.p12 -out mock_client_1900006000.encrypted.pem
```


```shell
openssl pkcs8 -in mock_client_1900006000.encrypted.pem -out mock_client_1900006000.pem
```

```shell
echo -n -e \
"GET\n/v3/certificates\n1666661128\n593BEC0C930BF1AFEB40B4A08C8FB242\n\n" \
 | openssl dgst -sha256 -sign mock_client_1900006000.pem \
 | openssl base64 -A
```


```shell
qekP35Ot6NNAUu96uGWWSTsqpL7kkKxHyJg2RuTZ47NNG7orWOtjFyGyBzWGzMDboH19k5U0nDjYJDgx2BMcuzOYhMUGB/GNY9HXcyIIAXJ3HQ1qaeKXA3RPHAyemSvkEWlR8CxVNHalJKud64p6TpojihDi/hpGYPPRB97Qki+WfXEI5/u3CTs/5C2MyOCCHuO8RW83WiAuE/kdDoBEJYiroGrLFvesFin6vPwwKgYrN7Gb21Jf1TUADZXCfxZBY4Ws5fWy9IOy/82hYjuYl/NvVbetCLNNnpNIG/I27ITaakJqCQoKuC5QhnLIpCWP4QLtnBEGspVTyoN7HME3/Q==
```


```shell
WECHATPAY2-SHA256-RSA2048 mchid="1900006000",nonce_str="593BEC0C930BF1AFEB40B4A08C8FB242",signature="WEwXUJnKfAZUax+ME+ioaO4EeKeTh4pk4pCD/n/THD70ytYiYyc9dQeAMxiG4ggy7+kQn2DVed1IDI9c6t0GyK9kHGGM/6PjdTBS1I+gG6MnyfXxZM6VOrnqRrJxnehTYawn3qADoed7r0HUynhgEoNkQHqB7yTOmp0wLG/rQxE0dmyABAQwwZVrJhJoe2i0WN/BMq8oQC1g58rOJZ0M39OY2yWgE/1mB+ctCX54jgJblyp4Iy4REU6OmyLhjZxtCGyknzt0Lrd9DusskaTUOBPpyde0bJ2dssQsOtU1zzA+p/XIMU72zyqCkumCiDfXa2kc5lVJFBYxYMcTX9ddSQ==",timestamp="1666661128",serial_no="3b6b459e"
```
