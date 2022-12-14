package com.muedsa.mock.wechat.mch.container;

public class ErrorCodeContainer {

    // 商户号与appid不匹配 请绑定调用接口的商户号和APPID后重试
    public static final String APPID_MCHID_NOT_MATCH = "APPID_MCHID_NOT_MATCH";

    // 银行系统异常 此状态代表退款申请失败，商户可根据具体的错误提示做相应的处理
    public static final String BANK_ERROR = "BANK_ERROR";

    // 商户订单号重复 请核实商户订单号是否重复提交
    public static final String OUT_TRADE_NO_USED = "OUT_TRADE_NO_USED";

    // 请求受阻 此状态代表退款申请失败，商户可根据具体的错误提示做相应的处理
    public static final String REQUEST_BLOCKED = "REQUEST_BLOCKED";

    // 退款业务流程错误 请不要更换商户退款单号，请使用相同参数再次调用API
    public static final String BIZ_ERR_NEED_RETRY = "BIZ_ERR_NEED_RETRY";

    // 用户支付中，需要输入密码 等待5秒，然后调用被扫订单结果查询API，查询当前订单的不同状态，决定下一步的操作
    public static final String USERPAYING = "USERPAYING";

    // 参数错误 根据错误提示，传入正确参数
    public static final String PARAM_ERROR = "PARAM_ERROR";

    // 请商户检查需要查询的id或者请求URL是否正确
    public static final String ORDER_NOT_EXIST = "请求的资源不存在";

    // 签约协议不存在 请检查签约协议号是否正确，是否已解约
    public static final String CONTRACT_NOT_EXIST = "CONTRACT_NOT_EXIST";

    // 手机号不存在 请检查手机号码是否正确
    public static final String PHONE_NOT_EXIST = "PHONE_NOT_EXIST";

    // 签名验证失败 请检查签名参数和方法是否都符合签名算法要求
    public static final String SIGN_ERROR = "SIGN_ERROR";

    // 系统错误 5开头的状态码都为系统问题，请使用相同参数 稍后重新调用
    public static final String SYSTEM_ERROR = "SYSTEM_ERROR";

    // 收银员扫描的不是微信支付的条码 请扫描微信支付被扫条码/二维码
    public static final String AUTH_CODE_INVALID = "AUTH_CODE_INVALID";

    // 频率超限 请求量不要超过接口调用频率限制
    public static final String FREQUENCY_LIMITED = "FREQUENCY_LIMITED";

    // 频率限制 请降低频率后重试
    public static final String RATELIMIT_EXCEEDED = "RATELIMIT_EXCEEDED";

    // 手机号不存在 请检查手机号码是否正确
    public static final String NO_AUTH = "NO_AUTH";

    // 业务规则限制 请	因业务规则限制请求频率，请查看接口返回的详细信息
    public static final String RULE_LIMIT = "RULE_LIMIT";

    // 用户的条码已经过期 请收银员提示用户，请用户在微信上刷新条码，然后请收银员重新扫码。 直接将错误展示给收银员
    public static final String AUTH_CODE_EXPIRE = "AUTH_CODE_EXPIRE";

    // 交易错误 因业务原因交易失败，请查看接口返回的详细信息
    public static final String TRADE_ERROR = "TRADE_ERROR";

    // 用户账户注销 请检查用户账户是否正确
    public static final String USER_NOT_EXIST = "USER_NOT_EXIST";

    // 接口限频 该错误都会返回具体的错误原因，请根据实际返回做相应处理
    public static final String ERROR = "ERROR";

    // 业务错误 请降低调用频率
    public static final String FREQUENCY_LIMIT_EXCEED = "FREQUENCY_LIMIT_EXCEED";

    // 协议已存在 已开通自动扣费服务功能，无需重复开通
    public static final String CONTRACT_EXISTED = "CONTRACT_EXISTED";

    // 用户账户异常 该确认用户账号是否正常，商家可联系微信支付或让用户联系微信支付客服处理
    public static final String USER_ACCOUNT_ABNORMAL = "USER_ACCOUNT_ABNORMAL";

    // 当前用户签约状态失效 请通过查询用户接口核实签约状态
    public static final String CONTRACT_ERROR = "CONTRACT_ERROR";

    // 订单号错误或订单状态不正确 请检查订单号是否有误以及订单状态是否正确，如：未支付、已支付未退款
    public static final String REFUND_NOT_EXISTS = "REFUND_NOT_EXISTS";

    // 二级商户未开启手动提现权限 二级商户号提现权限已关闭，无法发起提现
    public static final String CONTRACT_NOT_CONFIRMED = "CONTRACT_NOT_CONFIRMED";

    // 账单文件不存在 请检查当前商户号是否在指定日期有交易或退款发生
    public static final String NO_STATEMENT_EXIST = "NO_STATEMENT_EXIST";

    // 账单生成中 请先检查当前商户号在指定日期内是否有成功的交易或退款，若有，则在T+1日上午8点后再重新下载
    public static final String STATEMENT_CREATING = "STATEMENT_CREATING";

    // 商户号不存在 请确认传入的商户号是否正确
    public static final String MCH_NOT_EXISTS = "MCH_NOT_EXISTS";

    // 请求参数符合参数格式，但不符合业务规则 请确认相同单号是否使用了不同的参数
    public static final String INVALID_REQUEST = "INVALID_REQUEST";

    // 查询的资源不存在 请检查查询资源的对应id是否填写正确
    public static final String RESOURCE_NOT_EXISTS = "RESOURCE_NOT_EXISTS";

    // 用户已签约该商户，不可重复签约 请通过查询用户接口获取用户的签约信息
    public static final String RESOURCE_ALREADY_EXISTS = "RESOURCE_ALREADY_EXISTS";

    // 资源已存在 尝试创建的资源已存在，无需重复创建
    public static final String ALREADY_EXISTS = "ALREADY_EXISTS";

    // 服务未开通或账号未注册 该用户尚未注册或开通当前服务，请开通后再试
    public static final String USER_NOT_REGISTERED = "USER_NOT_REGISTERED";

    // openid不正确 	请确认传入的openid是否正确
    public static final String USER_NOT_EXISTS = "USER_NOT_EXISTS";

    // 订单已关闭 当前订单已关闭，请重新下单
    public static final String ORDER_CLOSED = "ORDER_CLOSED";

    // 订单已支付 请确认该订单号是否重复支付，如果是新单，请使用新订单号提交
    public static final String ORDER_PAID = "ORDER_PAID";

    // 订单已撤销 当前订单状态为“订单已撤销”，请提示用户重新支付
    public static final String ORDER_REVERSED = "ORDER_REVERSED";

    // 订单已关闭 商户订单号异常，请重新下单支付
    public static final String ORDERCLOSED = "ORDERCLOSED";

    // 订单已支付 请确认该订单号是否重复支付，如果是新单，请使用新订单号提交
    public static final String ORDERPAID = "ORDERPAID";

    // 订单已撤销 当前订单状态为“订单已撤销”，请提示用户重新支付
    public static final String ORDERREVERSED = "ORDERREVERSED";

    // 二级商户下行打款未成功 二级商户号结算银行卡信息有误，修改后重试
    public static final String ACCOUNT_NOT_VERIFIED = "ACCOUNT_NOT_VERIFIED";

    // 请求的资源不存在 请商户检查需要查询的id或者请求URL是否正确
    public static final String NOT_FOUND = "NOT_FOUND";
}
