package com.zsheep.ai.common.core.page;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.zsheep.ai.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页数据参数
 *
 * @author Elziy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDomain {
    /**
     * 当前记录起始索引
     */
    private Integer pageNum;
    
    /**
     * 每页显示记录数
     */
    private Integer pageSize;
    
    /**
     * 排序列
     */
    private String orderByColumn;
    
    /**
     * 排序的方向desc或者asc
     */
    private String isAsc = "asc";
    
    public OrderItem getOrderBy() {
        return new OrderItem(getOrderByColumn(), getIsAsc().equals("asc"));
    }
    
    public void setIsAsc(String isAsc) {
        if (StringUtils.isNotEmpty(isAsc)) {
            // 兼容前端排序类型
            if ("ascending".equals(isAsc)) {
                isAsc = "asc";
            } else if ("descending".equals(isAsc)) {
                isAsc = "desc";
            }
            this.isAsc = isAsc;
        }
    }
}
