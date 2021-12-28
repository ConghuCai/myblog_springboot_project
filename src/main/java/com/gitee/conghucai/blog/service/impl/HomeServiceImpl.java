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
    AboutInfoMapper aboutInfoMapper;

    private int ttl = BlogParamConfig.ttl;
    private TimeUnit timeUnit = BlogParamConfig.timeUnit;
    private int carouselNum = BlogParamConfig.carouselNum;
    private String redisStatName = BlogParamConfig.siteAccessName;

    @Override
    public Map<String, Object> getMenu() {
        if(redis.hasKey("menu")){
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

        redis.opsForValue().set("menu", res, 24, TimeUnit.HOURS);
        return res;
    }

    @Override
    public Map<String, Object> getOwnerInfo() {
        if(redis.hasKey("ownerInfo")){
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
        redis.opsForValue().set("tag", res, 4, TimeUnit.HOURS);
        return res;
    }

    @Override
    public Map<String, Object> getLink() {
        if(redis.hasKey("link")){
            return (Map<String, Object>) redis.opsForValue().get("link");
        }
        List<Link> links = linkMapper.selectAll();
        Map<String, Object> data = new HashMap<>();
        data.put("links", links);

        Map<String, Object> res = new HashMap<>();
        res.put("code",200);
        res.put("data",data);

        redis.opsForValue().set("link", res, 24, TimeUnit.HOURS);
        return res;
    }

    private Map<String, Object> returnNullObjectMsg(String name){
        Map<String, Object> res = new HashMap<>();
        res.put("error", "get "+name +" error! get null result!");
        res.put("code", 500);
        return res;
    }

    @Override
    public Map<String, Object> getCategoryInfo(String categoryName) {
        if(redis.hasKey("category_" + categoryName)){
            return (Map<String, Object>) redis.opsForValue().get("category_" + categoryName);
        }
        Map categoryInfo = categoryMapper.selectInfoByName(categoryName);
        Map<String, Object> data = new HashMap<>();
        data.put("categoryInfo", categoryInfo);

        Map<String, Object> res = new HashMap<>();
        res.put("code",200);
        res.put("data",data);

        redis.opsForValue().set("category_" + categoryName, res, 24, TimeUnit.HOURS);
        return res;
    }

    @Override
    public Map<String, Object> getAboutInfo() {
        if(redis.hasKey("aboutInfo")){
            return (Map<String, Object>) redis.opsForValue().get("aboutInfo");
        }

        AboutInfo aboutInfo = aboutInfoMapper.selectByPrimaryKey("about");
        Map<String, Object> data = new HashMap<>();
        data.put("aboutInfo", aboutInfo);

        Map<String, Object> res = new HashMap<>();
        res.put("code",200);
        res.put("data",data);

        redis.opsForValue().set("aboutInfo", res, 24, TimeUnit.HOURS);
        return res;
    }

    @Override
    public void helloService() {
        // count
        if(!redis.opsForHash().hasKey(redisStatName, "count")){
            Set<Object> set = redis.opsForHash().keys(redisStatName);
            int count = 0;
            Iterator<Object> iterator = set.iterator();
            while(iterator.hasNext()) {
                String num = (String) redis.opsForHash().get(redisStatName, iterator.next() + "");
                count += Integer.parseInt(num);
            }

            redis.opsForHash().put(redisStatName, "count", count+"");
        }

        // date
        String date = DateTimeUtil.getSysDate();
        if(!redis.opsForHash().hasKey(redisStatName, date)) {
            redis.opsForHash().put(redisStatName, date, "0");
        }

        String num = (String) redis.opsForHash().get(redisStatName, date);
        String countNum = (String) redis.opsForHash().get(redisStatName, "count");
        int newNum = Integer.parseInt(num) + 1;
        int count = Integer.parseInt(countNum) + 1;
        redis.opsForHash().put(redisStatName, date, newNum + "");
        redis.opsForHash().put(redisStatName, "count", count + "");
    }

    @Override
    public Map<String, Object> getGiteeLogo() {
        if(redis.hasKey("giteeLogo")){
            return (Map<String, Object>) redis.opsForValue().get("giteeLogo");
        }

        String url = blogTitleMapper.selectGiteeLogoByPrimaryKey("blog");
        Map<String, Object> res = new HashMap<>();
        res.put("code",200);
        res.put("data",url);
        redis.opsForValue().set("giteeLogo", res);
        return res;
    }
}
