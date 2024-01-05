package com.zsheep.ai.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文字转图片任务表
 *
 * @author Elziy
 */
@TableName(value ="txt_2_img_task")
@Data
public class Txt2ImgTask implements Serializable {
    @TableId
    private String tid;

    private Long uid;
    
    /**
     * 参数ID
     */
    private String pid;
    
    private Integer status;

    private Date createTime;
    
    @TableLogic
    private Integer delFlag;

    private static final long serialVersionUID = 1L;
}