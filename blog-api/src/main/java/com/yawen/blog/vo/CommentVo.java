package com.yawen.blog.vo;

import com.yawen.blog.dao.pojo.SysUser;
import lombok.Data;

import java.util.List;

@Data
public class CommentVo {
    private String id;
    private String content;
    private String createDate;
    private Long level;
    private SysUser author;
    private List<CommentVo> childrens;
    private SysUser toUser;
}
