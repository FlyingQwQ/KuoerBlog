package top.kuoer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kuoer.common.Result;
import top.kuoer.entity.*;
import top.kuoer.enums.ResultCode;
import top.kuoer.mapper.LabelMapper;
import top.kuoer.mapper.PostsMapper;
import top.kuoer.service.PostsService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@SuppressWarnings("all")
public class PostsServiceImpl implements PostsService {

    private final PostsMapper postsMapper;
    private final LabelMapper labelMapper;

    @Autowired
    public PostsServiceImpl(PostsMapper postsMapper, LabelMapper labelMapper) {
        this.postsMapper = postsMapper;
        this.labelMapper = labelMapper;
    }

    public Result findPostsById(int id, HttpServletRequest request) {
        if(!request.getHeader("user-agent").contains("HeadlessChrome")) {
            this.postsMapper.addReadCount(id, 1);
        }
        Posts posts = this.postsMapper.findPostsById(id);
        if(null != posts) {
            posts.setLabelName(labelMapper.findLabelById(posts.getLabel()).getName());
            return new Result(ResultCode.SUCCESS, posts);
        }
        return new Result(ResultCode.NOTFOUND, null);
    }

    public Result findPostsAll() {
        List<Label> labels = labelMapper.findLabelAll();
        List<PostsInfo> postsList = this.postsMapper.findPostsAll();
        Map<Integer, List<PostsInfo>> postsMap = new LinkedHashMap<>();
        List<PostsYear> postsYears = this.postsMapper.findAllYear();

        Collections.sort(postsYears, new Comparator<PostsYear>() {
            @Override
            public int compare(PostsYear o1, PostsYear o2) {
                return o2.getYear() - o1.getYear();
            }
        });

        for(int i = 0; i < postsYears.size(); i++) {
            postsMap.put(postsYears.get(i).getYear(), new ArrayList<>());
        }
        for(PostsInfo posts : postsList) {
            posts.setLabelName(this.getLabelInfo(labels, posts.getLabel()).getName());
            postsMap.get(posts.getYear()).add(posts);
        }

        return new Result(ResultCode.SUCCESS, postsMap);
    }

    @Override
    public Result findPostsByLabel(int id) {
        Label label = labelMapper.findLabelById(id);
        List<PostsInfo> postsList = this.postsMapper.findPostsByLabel(id);
        Map<Integer, List<PostsInfo>> postsMap = new LinkedHashMap<>();
        List<PostsYear> postsYears = this.postsMapper.findAllYear();

        Collections.sort(postsYears, new Comparator<PostsYear>() {
            @Override
            public int compare(PostsYear o1, PostsYear o2) {
                return o2.getYear() - o1.getYear();
            }
        });

        for(int i = 0; i < postsYears.size(); i++) {
            postsMap.put(postsYears.get(i).getYear(), new ArrayList<>());
        }
        for(PostsInfo posts : postsList) {
            posts.setLabelName(label.getName());
            postsMap.get(posts.getYear()).add(posts);
        }

        Iterator<Integer> it = postsMap.keySet().iterator();
        while(it.hasNext()) {

            Object ele = it.next();
            if(postsMap.get(ele).size() < 1) {
                it.remove();
            }
        }

        return new Result(ResultCode.SUCCESS, postsMap);
    }

    @Override
    public Result addPosts(String title, String content, String labelName) {
        if("".equals(title) || "".equals(content) || "".equals(labelName)) {
            return new Result(ResultCode.OPERATIONFAIL, "提交的必要参数不能为空");
        }

        Integer labelId = null;

        if(null == (labelId = labelMapper.findLabelByName(labelName))) {
            Label label = new Label();
            label.setName(labelName);
            if(!labelMapper.addLabel(label)) {
                return new Result(ResultCode.OPERATIONFAIL, "创建新的标签时出现问题");
            }
            labelId = label.getId();
        }

        Posts posts = new Posts();
        posts.setTitle(title);
        posts.setContent(content);
        posts.setDate(System.currentTimeMillis());
        posts.setLabel(labelId);

        if(postsMapper.addPosts(posts)) {
            return new Result(ResultCode.SUCCESS, null);
        }
        return new Result(ResultCode.OPERATIONFAIL, "请检查数据库是否有问题");
    }

    @Override
    public Result modifyPosts(int id, String title, String content, String labelName) {
        Integer labelId = null;

        if(null == (labelId = labelMapper.findLabelByName(labelName))) {
            Label label = new Label();
            label.setName(labelName);
            if(!labelMapper.addLabel(label)) {
                return new Result(ResultCode.OPERATIONFAIL, "创建新的标签时出现问题");
            }
            labelId = label.getId();
        }

        if(postsMapper.modifyPosts(id, title, content, labelId) > 0) {
            return new Result(ResultCode.SUCCESS, null);
        }
        return new Result(ResultCode.OPERATIONFAIL, "请检查数据库是否有问题");
    }



    @Override
    public Result removePosts(int id) {
        if(postsMapper.removePosts(id) > 0) {
            return new Result(ResultCode.SUCCESS, null);
        }
        return new Result(ResultCode.OPERATIONFAIL, "请检查数据库是否有问题");
    }

    public Label getLabelInfo(List<Label> labels, int labelId) {
        for(Label label : labels) {
            if(labelId == label.getId()) {
                return label;
            }
        }
        return null;
    }

}
