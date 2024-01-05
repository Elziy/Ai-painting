package com.zsheep.ai.common.core.service;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.zsheep.ai.common.constants.HttpMethods;
import com.zsheep.ai.common.constants.HttpStatus;
import com.zsheep.ai.common.core.domain.model.TurnstileRequest;
import com.zsheep.ai.common.core.domain.model.TurnstileResponse;
import com.zsheep.ai.common.exception.user.UserException;
import com.zsheep.ai.utils.HttpUtil;
import com.zsheep.ai.utils.http.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

// @SpringBootTest
class TxServiceTest {
    @Resource
    private TxService txService;
    
    // @Test
    void saveTxt2Img() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        Map<String, String> querys = new HashMap<>();
        TurnstileRequest turnstileRequest = new TurnstileRequest();
        turnstileRequest.setSecret("0x4AAAAAAADQrwBHdp4HNF7A8-fu2edNy6o");
        turnstileRequest.setResponse("0.RitBWVL0scbn4tTBmMGmt9v0M6mdqbcvqr--j2lLTdQ5HZBi8YG66VblFfakFUtW87TAhVsP__1WW7c1LAz8VXPF40nfxCMFo-XFgwL2wza2TqtDKgr6oBFymXr3xOsCEfPR6y5h729cbY9_PpspGZJBp8OjL3SBL6dcuFM0Dx_J92-XvKMAq1UinDbPtbNLkYFIhlPLdWZe4YBbNttTb076xrj8IbY5qtPUPYgP4nsKEE3j7q1EH96dcthHp5Ks68Ecjf7uznn1pdHL7cjCyIMHstcQ6BWaHtg-4Ja_yzwu0ft2FveNe0gJYP2Il53UL2GAI1K5QvevikgSGzNOeLSJ6RjtzVMC-QOrwZZgkHE.kbk05vQjwNOVOXZKc0yp8Q.86d18735dae4d003bd93c15c4af5010f489eba7250e7ea7a20fddb5b7d627252");
        String bodys = JSON.toJSONString(turnstileRequest);
        try {
            // https://challenges.cloudflare.com/turnstile/v0/siteverify
            HttpResponse httpResponse = HttpUtil.doPost("https://challenges.cloudflare.com", "/turnstile/v0/siteverify", HttpMethods.POST, headers, querys, bodys, true);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SUCCESS) {
                String json = EntityUtils.toString(httpResponse.getEntity());
                TurnstileResponse turnstileResponse = JSON.parseObject(json, TurnstileResponse.class);
                System.out.println("turnstileResponse = " + turnstileResponse);
            } else {
                throw new UserException("user.captcha.error", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserException("user.captcha.error", null);
        }
    }
    
    // @Test
    void testBaidu() {
        // 测试百度
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        Map<String, String> querys = new HashMap<>();
        TurnstileRequest turnstileRequest = new TurnstileRequest();
        turnstileRequest.setSecret("0x4AAAAAAADQrwBHdp4HNF7A8-fu2edNy6o");
        try {
            HttpResponse httpResponse = HttpUtil.doGet("https://www.baidu.com", "/", HttpMethods.GET, headers, querys);
            System.out.println("httpResponse = " + httpResponse.getEntity().getContent());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    // @Test
    void test() {
        TurnstileRequest turnstileRequest = new TurnstileRequest();
        turnstileRequest.setSecret("0x4AAAAAAADQrwBHdp4HNF7A8-fu2edNy6o");
        turnstileRequest.setResponse("0.RitBWVL0scbn4tTBmMGmt9v0M6mdqbcvqr--j2lLTdQ5HZBi8YG66VblFfakFUtW87TAhVsP__1WW7c1LAz8VXPF40nfxCMFo-XFgwL2wza2TqtDKgr6oBFymXr3xOsCEfPR6y5h729cbY9_PpspGZJBp8OjL3SBL6dcuFM0Dx_J92-XvKMAq1UinDbPtbNLkYFIhlPLdWZe4YBbNttTb076xrj8IbY5qtPUPYgP4nsKEE3j7q1EH96dcthHp5Ks68Ecjf7uznn1pdHL7cjCyIMHstcQ6BWaHtg-4Ja_yzwu0ft2FveNe0gJYP2Il53UL2GAI1K5QvevikgSGzNOeLSJ6RjtzVMC-QOrwZZgkHE.kbk05vQjwNOVOXZKc0yp8Q.86d18735dae4d003bd93c15c4af5010f489eba7250e7ea7a20fddb5b7d627252");
        String bodys = JSON.toJSONString(turnstileRequest);
        String post = cn.hutool.http.HttpUtil.post("https://challenges.cloudflare.com/turnstile/v0/siteverify", bodys, 100000);
        System.out.println("post = " + post);
    }
    
    // @Test
    void tesxt2() {
        Map<String, String> querys = new HashMap<>();
        TurnstileRequest turnstileRequest = new TurnstileRequest();
        turnstileRequest.setSecret("0x4AAAAAAADQrwBHdp4HNF7A8-fu2edNy6o");
        turnstileRequest.setResponse("0.RitBWVL0scbn4tTBmMGmt9v0M6mdqbcvqr--j2lLTdQ5HZBi8YG66VblFfakFUtW87TAhVsP__1WW7c1LAz8VXPF40nfxCMFo-XFgwL2wza2TqtDKgr6oBFymXr3xOsCEfPR6y5h729cbY9_PpspGZJBp8OjL3SBL6dcuFM0Dx_J92-XvKMAq1UinDbPtbNLkYFIhlPLdWZe4YBbNttTb076xrj8IbY5qtPUPYgP4nsKEE3j7q1EH96dcthHp5Ks68Ecjf7uznn1pdHL7cjCyIMHstcQ6BWaHtg-4Ja_yzwu0ft2FveNe0gJYP2Il53UL2GAI1K5QvevikgSGzNOeLSJ6RjtzVMC-QOrwZZgkHE.kbk05vQjwNOVOXZKc0yp8Q.86d18735dae4d003bd93c15c4af5010f489eba7250e7ea7a20fddb5b7d627252");
        String bodys = JSON.toJSONString(turnstileRequest);
        String s = HttpUtils.sendSSLPost("https://challenges.cloudflare.com/turnstile/v0/siteverify", bodys);
        System.out.println("s = " + s);
    }
    
    // @Test
    void testPropy() {
        Map<String, String> querys = new HashMap<>();
        TurnstileRequest turnstileRequest = new TurnstileRequest();
        turnstileRequest.setSecret("0x4AAAAAAADQrwBHdp4HNF7A8-fu2edNy6o");
        turnstileRequest.setResponse("0.RitBWVL0scbn4tTBmMGmt9v0M6mdqbcvqr--j2lLTdQ5HZBi8YG66VblFfakFUtW87TAhVsP__1WW7c1LAz8VXPF40nfxCMFo-XFgwL2wza2TqtDKgr6oBFymXr3xOsCEfPR6y5h729cbY9_PpspGZJBp8OjL3SBL6dcuFM0Dx_J92-XvKMAq1UinDbPtbNLkYFIhlPLdWZe4YBbNttTb076xrj8IbY5qtPUPYgP4nsKEE3j7q1EH96dcthHp5Ks68Ecjf7uznn1pdHL7cjCyIMHstcQ6BWaHtg-4Ja_yzwu0ft2FveNe0gJYP2Il53UL2GAI1K5QvevikgSGzNOeLSJ6RjtzVMC-QOrwZZgkHE.kbk05vQjwNOVOXZKc0yp8Q.86d18735dae4d003bd93c15c4af5010f489eba7250e7ea7a20fddb5b7d627252");
        String bodys = JSON.toJSONString(turnstileRequest);
        String result2 = HttpRequest.post("https://challenges.cloudflare.com/turnstile/v0/siteverify")
                .setHttpProxy("127.0.0.1", 7890)
                .body(bodys)
                .execute().body();
        System.out.println("result2 = " + result2);
    }
}