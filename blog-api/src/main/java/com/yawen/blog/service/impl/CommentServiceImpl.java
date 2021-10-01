package com.yawen.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yawen.blog.dao.mapper.CommentMapper;
import com.yawen.blog.dao.pojo.Comment;
import com.yawen.blog.dao.pojo.SysUser;
import com.yawen.blog.service.CommentService;
import com.yawen.blog.service.SysUserService;
import com.yawen.blog.utils.UserThreadLocal;
import com.yawen.blog.vo.CommentVo;
import com.yawen.blog.vo.Result;
import com.yawen.blog.vo.params.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 通过articleId查找Comment
     * @param id
     * @return
     */
    @Override
    public Result getCommentsById(long id) {
        //构造查询条件
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,id);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> CommentVoList = copyList(comments);
        return Result.success(CommentVoList);
    }

    /**
     * 将评论加载到数据库
     * @param commentParam
     * @return
     */
    @Override
    public Result comment(CommentParam commentParam) {
        Comment comment = new Comment();
        SysUser sysUser = UserThreadLocal.get();
        comment.setArticleId(commentParam.getArticleId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        comment.setAuthorId(sysUser.getId());
        Long parent = commentParam.getParent();
        comment.setParentId(parent==null?0:parent);
        comment.setLevel(parent==null?1L:2L);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId==null?0:toUserId);
        commentMapper.insert(comment);
        return Result.success(null);
    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentsVoList = new LinkedList<>();
        for(Comment comment:comments){
            CommentVo commentVo = copy(comment);
            commentsVoList.add(commentVo);
        }
        return commentsVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        commentVo.setId(String.valueOf(comment.getId()));
        commentVo.setAuthor(sysUserService.findByAuthorId(comment.getAuthorId()));
        if(comment.getLevel()==1){
            commentVo.setChildrens(findChildrensById(comment.getId()));
        }
        commentVo.setToUser(sysUserService.findByAuthorId(comment.getAuthorId()));

        return commentVo;
    }

    private List<CommentVo> findChildrensById(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        return copyList(comments);
    }
}
