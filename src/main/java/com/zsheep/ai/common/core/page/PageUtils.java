package com.zsheep.ai.common.core.page;

import cn.hutool.core.convert.Convert;
import com.zsheep.ai.common.constants.enums.OrderBy;
import com.zsheep.ai.utils.ServletUtils;

/**
 * 分页数据处理
 *
 * @author Elziy
 */
public class PageUtils {
    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";
    
    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";
    
    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderBy";
    
    private static PageDomain getPageDomain() {
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNum(Convert.toInt(ServletUtils.getParameter(PAGE_NUM), 1));
        pageDomain.setPageSize(Convert.toInt(ServletUtils.getParameter(PAGE_SIZE), 10));
        String orderBy = ServletUtils.getParameter(ORDER_BY_COLUMN);
        OrderBy order = OrderBy.getEnumByKey(orderBy);
        if (order != null) {
            pageDomain.setOrderByColumn(order.getValue());
        }
        return pageDomain;
    }
    
    public static PageDomain get() {
        return getPageDomain();
    }
}
