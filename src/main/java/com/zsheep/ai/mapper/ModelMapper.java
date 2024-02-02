package com.zsheep.ai.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsheep.ai.domain.entity.Model;

/**
 * 模型Mapper接口
 *
 * @author elziy6
 */
public interface ModelMapper extends BaseMapper<Model> {
    
    /**
     * 查询模型列表
     *
     * @param model 模型
     * @return 模型集合
     */
    List<Model> selectModelList(Model model);

}
