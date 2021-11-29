package com.gitee.conghucai.blog.service.impl;

import com.gitee.conghucai.blog.config.BlogParamConfig;
import com.gitee.conghucai.blog.mapper.*;
import com.gitee.conghucai.blog.model.*;
import com.gitee.conghucai.blog.service.HomeService;
import com.gitee.conghucai.blog.utils.DateTimeUtil;
import com.gitee.conghucai.blog.utils.UUIDUtil;
import com.gitee.conghucai.blog.vo.CategoryListVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class HomeServiceImpl implements HomeService {

    @Resource
    RedisTemplate<Object, Object> redis;

    @Resource
    BlogTitleMapper blogTitleMapper;
    @Resource
    MenuMapper menuMapper;
    @Resource
    CategoryMapper categoryMapper;
    @Resource
    OwnerInfoMapper ownerInfoMapper;
    @Resource
    MessageMapper messageMapper;
    @Resource
    TagMapper tagMapper;
    @Resource
    LinkMapper linkMapper;
    @Resource
    ArticleMapper articleMapper;

    private int ttl = BlogParamConfig.ttl;
    private TimeUnit timeUnit = BlogParamConfig.timeUnit;
    private int carouselNum = BlogParamConfig.carouselNum;

    @Override
    public Map<String, Object> getMenu() {
        if(redis.hasKey("menu")){
            System.out.println("menu-Redis命中");
            return (Map<String, Object>) redis.opsForValue().get("menu");
        }

        Map<String, Object> res = new HashMap<>();

        //获取标题
        BlogTitle blogTitle = blogTitleMapper.selectByPrimaryKey("blog");
        if(blogTitle == null){
            return returnNullObjectMsg("blogTitle");
        }
        Map<String, Object> title = new HashMap<>();
        title.put("name", blogTitle.getBlogname());
        title.put("sname", blogTitle.getShortname());
        title.put("avatar", blogTitle.getBlogavatar());

        Map<String, Object> data = new HashMap<>();
        data.put("title", title);

        //菜单栏
        List<Menu> menuList = menuMapper.selectAll();
        if(menuList == null){
            res.put("error", "get menuList error! get null result!");
            res.put("code", 500);
            return res;
        }

        List<Object> menu = new ArrayList<>();
        for(Menu m : menuList){
            List<CategoryListVo> children = categoryMapper.selectByMenuId(m.getId());
            if(m.getMain()){
                // 主菜单
                menu.add(children.get(0));
            } else {
                Map<String, Object> subMenu = new HashMap<>();
                subMenu.put("label", m.getName());
                subMenu.put("icon", m.getIcon());
                subMenu.put("children", children);

                menu.add(subMenu);
            }
        }

        data.put("menu", menu);

        res.put("code", 200);
        res.put("data", data);

        System.out.println("menu-sql命中");
        redis.opsForValue().set("menu", res, 24, TimeUnit.HOURS);
        return res;
    }

    @Override
    public Map<String, Object> getOwnerInfo() {
        if(redis.hasKey("ownerInfo")){
            System.out.println("ownerInfo-redis命中");
            return (Map<String, Object>) redis.opsForValue().get("ownerInfo");
        }

        OwnerInfo ownerInfo = ownerInfoMapper.selectByPrimaryKey("owner");
        if(ownerInfo == null){
            return returnNullObjectMsg("ownerInfo");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("ownerInfo", ownerInfo);

        Map<String, Object> res = new HashMap<>();
        res.put("code", 200);
        res.put("data", data);
        System.out.println("ownerInfo-sql命中");

        redis.opsForValue().set("ownerInfo", res, 24, TimeUnit.HOURS);
        return res;
    }

    @Override
    public Map<String, Object> getMessage() {
        Object messageList = messageMapper.getMessageDetail();
        Map<String, Object> data = new HashMap<>();
        data.put("leaveCommentData", messageList);

        Map<String, Object> label = new HashMap<>();
        label.put("userName", "访客");
        label.put("message", "留言内容");
        label.put("time", "留言时间");
        data.put("leaveCommentLabel", label);

        Map<String, Object> res = new HashMap<>();
        res.put("code", 200);
        res.put("data", data);

        return res;
    }

    @Override
    public Map<String, Object> addMessage(User user, String message) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        if(user == null){
            data.put("addRes", "failed. user is null.");
            res.put("code", 500);
            res.put("data", data);
            return res;
        }
        String login = user.getLogin();
        String time = DateTimeUtil.getSysTime();
        String id = UUIDUtil.getUUID();
        try{
            Message m = new Message();
            m.setId(id);
            m.setUser(login);
            m.setTime(time);
            m.setMessage(message);

            messageMapper.insert(m);
        } catch(Exception e){
            data.put("addRes", e.getMessage());
            res.put("code", 500);
            res.put("data", data);
            return res;
        }

        data.put("addRes", "success");
        res.put("code", 200);
        res.put("data", data);
        return res;
    }

    @Override
    public Map<String, Object> getTag() {
        if(redis.hasKey("tag")){
            System.out.println("tag-redis命中");
            return (Map<String, Object>) redis.opsForValue().get("tag");
        }
        List<Tag> tagList = tagMapper.selectAll();
        String[] type = {"info", "warning", "success", "danger"};
        List<Object> tags = new ArrayList<>();

        // index是为了保证每个相邻的tag颜色不一样
        int lastIndex = -1;
        for(Tag t: tagList){
            Map<String, String> tagMap = new HashMap<>();
            tagMap.put("tagName", t.getName());
            tagMap.put("tagUrl", t.getName());

            Random rand = new Random();
            int index = rand.nextInt(4);
            if(index == lastIndex){
                index = (index + 1) % type.length;
            }
            tagMap.put("tagType", type[index]);
            lastIndex = index;
            tags.add(tagMap);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("tags", tags);

        Map<String, Object> res = new HashMap<>();
        res.put("code", 200);
        res.put("data", data);
        System.out.println("tag-sql命中");
        redis.opsForValue().set("tag", res, 4, TimeUnit.HOURS);
        return res;
    }

    @Override
    public Map<String, Object> getLink() {
        if(redis.hasKey("link")){
            System.out.println("link-redis命中");
            return (Map<String, Object>) redis.opsForValue().get("link");
        }
        List<Link> links = linkMapper.selectAll();
        Map<String, Object> data = new HashMap<>();
        data.put("links", links);

        Map<String, Object> res = new HashMap<>();
        res.put("code",200);
        res.put("data",data);

        System.out.println("link-sql命中");
        redis.opsForValue().set("link", res, 24, TimeUnit.HOURS);
        return res;
    }

    private Map<String, Object> returnNullObjectMsg(String name){
        Map<String, Object> res = new HashMap<>();
        res.put("error", "get "+name +" error! get null result!");
        res.put("code", 500);
        return res;
    }
}
