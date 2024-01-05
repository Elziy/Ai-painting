package com.zsheep.ai.common.core.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页数据信息
 *
 * @author Elziy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long current;
    
    private Long size;
    
    /**
     * 总记录数
     */
    private Long total;
    
    /**
     * 列表数据
     */
    private List<?> rows;
}
