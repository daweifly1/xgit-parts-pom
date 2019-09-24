package cn.com.xgit.pay.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/goods")
@Slf4j
public class GoodsOrderController {

    static final String mchId = "10000000";
    // 加签key
    static final String reqKey = "M86l522AV6q613Ii4W6u8K48uW8vM1N6bFgyv769220MdYe9u37N4y7rI5mQ";
    // 验签key
    static final String resKey = "Hpcl522AV6q613KIi46u6g6XuW8vM1N8bFgyv769770MdYe9u37M4y7rIpl8";
    //static final String baseUrl = "http://api.xxpay.org/api";
    static final String baseUrl = "http://10.3.1.21:3020/api";
    //static final String notifyUrl = "http://10.3.1.21:8081/goods/payNotify";
    static final String notifyUrl = "http://10.3.1.21:8081/goods/payNotify";
    private AtomicLong seq = new AtomicLong(0L);
    private final static String QR_PAY_URL = "http://10.3.1.21:8081/goods/qrPay.html";
    static final String AppID = "wx077cb62e341f8a5c";
    static final String AppSecret = "e663ea068f3e4f952f143de1432a35c2";
    private final static String GetOpenIdURL = "http://10.3.1.21:8081/goods/getOpenId";
    private final static String GetOpenIdURL2 = "http://10.3.1.21:8081/goods/getOpenId2";

    //支付宝参数
    private final static String APP_ID = "2016101300673574";
    private final static String APP_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCCmb3jEvjOds/pei8PCRSoiRdafA795w0vqwxO4WMbjUQYdibXNtPZw0fz9+Z6rVIwySKWmZfo2KUwOZANwDl9tdolVrJixEjOxxYZZrwgYsiM3NmDrtI/A9KI8sALMQCqyofv7teprITICOGE7Eb2gz/VFhIv8pHzo6Ww2+nc7ZcLxW1zAfhU5lYFmB8EkJZceyCV81IM/L5GfcOoSNIeSfmNt27IpDz3DxG81o/0lqGUXn6+QOqBAMpukPr0MBySMxRON5FtceZeQ6yPKN933rQoVWCM0tL/iRcgNV71BrzLqEZtwD0HB3gllRYq8eTEjp0vNUc7f4nMV7U4Dw0DAgMBAAECggEAfbtxmklLi8nKxGZDI+CEKPVCrHQ/IqXshfXQtag7d+z6n+ov3oYEUuO0Q+Zn17dBJ4KccAySuTLJZbIlQ3fKYYJP8B6VQozh42C7n9zORQeVODfumN4Xv174s6uww5V7pPGzCYURz2Ituz8BjqzCbNVTANxsyghoBv2HqMsvTOd1gkior2cHimgD4on3ey8aao45ypIQwafFFjQnmC2/axOQnlJr0s4qYONbO1v6GY3B0s3CCopjvxvK3sd5KRb4yyEGjvH/zVbplrY6XqoeH3mTOTIzDwR2RSNjA6HH8peEsBSI4kkprqz/aQ5VxQ9fH2fsdsSpd3TkpTnTHP5XcQKBgQDMVlyX7XdSmbRezHgZt8/SKtYSWyI+x8FEQoSpABCmmAznhXaONyEurw2+j/G2d2ZB9Gy0mnPaKJh0cax+ZEeDo/ZD0ZWsrW6kq0P1+0btGyH0hMhwMULijXIoy/9TSksufYgdLpfpdP2VwFhBLPuPhIKDkrOSQsc+Cr2yo97pTwKBgQCjns0ACxjuB2lyKUkS4zd23ExJGZY8rol1I3pkIs4u6b0628/o0PZSov+UaG+du6RBqZIH2BqNOBZ/WNlSHGahR8kz+bBA/IW/icKDt9Zvx4GF/NdDE2YLhtD8/QefQCuVrNRS3VUe5b+43xVh2j55OGQEY12BaL6LQTKwqfqMDQKBgQCVy8ndZqxO1Xcw99wjnmivvc9pbX3RYEcCb3EpI28Rce4j2+dpomik/Jeq14NvLGC+gR0f2QqNJ617z8mQbpsumghy9zYufIXcGeGcdjUspPNjeeQmuzY5N1A1MDdhK9mpn0Ulf37GGom8OmF4hi10rMOxCN6znljJ1r3yM8e6aQKBgQCWAFXMPmBCiAUBphvZ9vi4PUqqkYyMYS6fd0ETYF1J4PucZDx9ImVQaSKKOhxprMbvc7fu/Q2KfesSILBLeMIWBavNvJD7YwZzDKaI/xbWLz7tSVlHoxcgZh5ecNJ2CuxqMaINvd/1jYdwwwPlS6GOnEYFR9tHcnGNVN5V299YWQKBgQCmL5rCNchIgBPiRtD55qq/z22P+koaGs6SJK1zDWWuez5jYpCPY35tzWDc2BaReYuk7Nz0QXtHpA0EGEgIRfEZOfS5osuVKS871XrFSvC9xQohsI0pceer/2WdFFmtqTZ3dpQJuUV1t/qcXAWVquNbkHTRf1ip2YEkggZaM0xs1w==";
    private final static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp4CDrKTDe06H6hil/5671zqTemDpJgmMpzJlTVnad+Qqw2emqsqDTHXgFbXfx97pEY+EwRO36cMMIDikXOC5Id9tDeVC6/Ur82Jy+pixLZhOf0ZWX4WIjBBgNSNkowWzpXrWp8nyi+hlBBLc/TjniK94HLqI6eoKYrXmscVf9RTPmzF5D105pmOs3ujOdXtUdQfnTGftaU4Twc4XvYpUT/UnVU9VjwnW1Of+vyEGxasB/a5di268xKu7WcqD9Y2UjcHx0OR241r6aShS2p5Qtq8Ba2HNeIEcgdC3WTyUu3kgph5+uiJBPiXFk7NM12SQHyp5iHOIAwh6yf4v2RNYqQIDAQAB";
    private final static String APP_ALI_GATEWAY = "https://openapi.alipaydev.com/gateway.do";
    private final static String CHARSET = "UTF-8";


    @RequestMapping(value = "/afterPay", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> hi(HttpServletRequest request) {
        Map<String, String> values = new HashMap<>();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = (String) paramNames.nextElement();
            String value = request.getParameter(name);
            values.put(name, value);
        }
        log.info("afterPay  {}", values);
        return values;
    }

    @RequestMapping(value = "/pay/{goodsOrderId}", method = RequestMethod.GET)
    public String pay(@PathVariable("goodsOrderId") String goodsOrderId, HttpServletResponse response) throws AlipayApiException, IOException {
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(APP_ALI_GATEWAY, APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
////实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
//        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
////SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
//        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

        // 封装请求支付信息
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        //订单编号，必填
        model.setOutTradeNo(goodsOrderId);
        //订单名称，必填
        model.setSubject("AppWap支付测试Java");
        //金额
        model.setTotalAmount("0.01");

        // 销售产品码 必填
        model.setProductCode("QUICK_WAP_WAY");

        //商品描述，可空
        model.setBody("我是测试数据");
//        超时时间 可空
        model.setTimeoutExpress("30m");

        request.setBizModel(model);
        request.setNotifyUrl("商户外网可以访问的异步地址");

        // 设置异步通知地址
        request.setNotifyUrl(AlipayConfig.notify_url);
        // 设置同步地址
        request.setReturnUrl(AlipayConfig.return_url);
        // form表单生产
        String form = "";
        try {
            // 调用SDK生成表单
            form = alipayClient.pageExecute(request).getBody();
            response.setContentType("text/html;charset=" + CHARSET);
            response.getWriter().write(form);//直接将完整的表单html输出到页面
            response.getWriter().flush();
            response.getWriter().close();
        } catch (AlipayApiException e) {
            log.error("", e);
        }
        return null;
    }


    @RequestMapping(value = "/queryPay/{goodsOrderId}", method = RequestMethod.GET)
    @ResponseBody
    public String queryPay(@PathVariable("goodsOrderId") String goodsOrderId, HttpServletResponse resp) throws AlipayApiException, IOException {
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(APP_ALI_GATEWAY, APP_ID, APP_PRIVATE_KEY, "json",
                CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

        request.setBizContent("{" +
                "\"out_trade_no\":\"" + goodsOrderId + "\" }");
        AlipayTradeQueryResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        System.out.println(response);
        return response.getBody();
    }


    @RequestMapping(value = "/fundTransfer/{orderNum}", method = RequestMethod.GET)
    @ResponseBody
    public String fundTransfer(@PathVariable("orderNum") String orderNum, HttpServletResponse resp) throws AlipayApiException, IOException {
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(APP_ALI_GATEWAY, APP_ID, APP_PRIVATE_KEY, "json",
                CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");

        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        request.setBizContent("{" +
                "\"out_biz_no\":\"" + orderNum + "\"," +
                "\"payee_type\":\"ALIPAY_LOGONID\"," +
                "\"payee_account\":\"smkprg0419@sandbox.com\"," +
                "\"amount\":\"0.01\"," +
                "\"payer_show_name\":\"上海交通卡退款\"," +
                "\"payee_real_name\":\"沙箱环境\"," +
                "\"remark\":\"转账备注\"" +
                "  }");
        AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return response.getBody();
    }



}
