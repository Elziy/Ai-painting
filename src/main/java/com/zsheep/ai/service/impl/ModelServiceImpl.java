package com.zsheep.ai.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsheep.ai.common.core.redis.RedisCache;
import com.zsheep.ai.domain.entity.Model;
import com.zsheep.ai.mapper.ModelMapper;
import com.zsheep.ai.service.ModelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.zsheep.ai.common.constants.CacheConstants.STABLE_DIFFUSION_MODELS;

/**
 * 模型Service业务层处理
 *
 * @author elziy6
 */
@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {
    @Resource
    private ModelMapper modelMapper;
    
    @Resource
    private RedisCache redisCache;
    
    @Override
    public List<Tree<String>> listTree() {
        List<Tree<String>> trees = redisCache.getCacheObject(STABLE_DIFFUSION_MODELS);
        if (trees != null) {
            return trees;
        }
        LambdaQueryWrapper<Model> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Model::getCreateTime);
        queryWrapper.eq(Model::getStatus, 1);
        queryWrapper
                .eq(Model::getType, "SD_MODEL")
                .or()
                .eq(Model::getType, "SD_MODEL_VERSION");
        List<Model> models = list(queryWrapper);
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setChildrenKey("versions");
        treeNodeConfig.setDeep(2);
        trees = TreeUtil.build(models, "0", treeNodeConfig, (model, tree) -> {
            tree.setId(String.valueOf(model.getId()));
            tree.setParentId(String.valueOf(model.getParentId()));
            tree.setName(model.getModelName());
            tree.putExtra("hash", model.getHash());
            tree.putExtra("modelVersion", model.getModelVersion());
            tree.putExtra("modelName", model.getModelName());
            tree.putExtra("modelImage", model.getModelImage());
        });
        redisCache.setCacheObject(STABLE_DIFFUSION_MODELS, trees);
        return trees;
    }
    
    @Override
    public List<Tree<String>> listTree(int limit, int offset) {
        List<Tree<String>> tree = listTree();
        return tree.subList(offset, offset + limit);
    }
    
    @Override
    public Model getOneByHash(String hash) {
        LambdaQueryWrapper<Model> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Model::getHash, hash);
        return getOne(queryWrapper);
    }
}
