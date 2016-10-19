package com.baifendian.tgi.web.controller;

import com.baifendian.tgi.repository.Tag;
import com.baifendian.tgi.repository.TagRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class TagController {

    @Resource(name = "tagRepository")
    private TagRepository tagRepository;

    @RequestMapping("/listTag")
    public List<Tag> list() {
        return tagRepository.listAll();
    }

}
