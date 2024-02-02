package com.zsheep.ai.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsheep.ai.domain.entity.Model;

import java.util.List;

/**
 * 模型Service接口
 *
 * @author elziy6
 */
public interface ModelService extends IService<Model> {
    List<Tree<String>> listTree();
    
    List<Tree<String>> listTree(int limit, int offset);
    
    Model getOneByHash(String hash);
}
