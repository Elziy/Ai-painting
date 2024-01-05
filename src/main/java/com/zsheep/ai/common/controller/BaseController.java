package com.zsheep.ai.common.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsheep.ai.common.core.page.PageDomain;
import com.zsheep.ai.common.core.page.PageUtils;

public class BaseController {
    /**
     * 获取分页对象
     *
     * @return {@link Page}<{@link T}>
     */
    protected <T> Page<T> getPage() {
        PageDomain pageDomain = PageUtils.get();
        Page<T> page = new Page<>();
        page.setCurrent(pageDomain.getPageNum());
        page.setSize(pageDomain.getPageSize());
        page.addOrder(pageDomain.getOrderBy());
        return page;
    }
}
