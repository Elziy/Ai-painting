package com.zsheep.ai.mapper;

import com.zsheep.ai.common.core.domain.model.LoginUser;
import com.zsheep.ai.common.security.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class Txt2ImgTaskMapperTest {
    
    @Resource
    private Txt2ImgTaskMapper txt2ImgTaskMapper;
    
    @Resource
    TokenService tokenService;
    
    @Test
    public void te() {
        List<LoginUser> users = tokenService.getAllSessionByUserId(1634198201602486274L);
        users.forEach(System.out::println);
    }
}