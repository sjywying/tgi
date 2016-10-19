package com.baifendian.tgi.web.controller;

import com.baifendian.calculate.client.Client;
import com.baifendian.calculate.client.Configuration;
import com.baifendian.math.Expression;
import com.baifendian.math.ExpressionTree;
import com.baifendian.math.VarMap;
import com.baifendian.tgi.repository.TGI;
import com.baifendian.tgi.repository.TGIRepository;
import com.baifendian.tgi.repository.Tag;
import com.baifendian.tgi.repository.TagRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.roaringbitmap.RoaringBitmap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.*;

@Controller
public class TGIController {

    @Resource(name = "tagRepository")
    private TagRepository tagRepository;

    @Resource(name = "tgiRepository")
    private TGIRepository tgiRepository;

    @RequestMapping(value = "/init")
    public Map<String, Integer> init() {
        int deleteResult = tagRepository.deleteAll();

        RoaringBitmap bitmap1 = RoaringBitmap.bitmapOf(1,2,3,4);
        RoaringBitmap bitmap2 = RoaringBitmap.bitmapOf(3,4,5,6,7);
        RoaringBitmap bitmap3 = RoaringBitmap.bitmapOf(6,7,8,9,0);

        int r1 = tagRepository.save("b1", bitmap1);
        int r2 = tagRepository.save("b2", bitmap2);
        int r3 = tagRepository.save("b3", bitmap3);

        Map<String, Integer> result = new HashMap<>(3);
        result.put("r1", r1);
        result.put("r2", r2);
        result.put("r3", r3);
        result.put("delete", deleteResult);

        return result;
    }


    @RequestMapping(value = "/exp", method = RequestMethod.POST)
    public Map<String, String> exp(@RequestBody TGI tgi) {
        //拆解表达式并且计算形成结果bitmap
        Expression expression = ExpressionTree.parse(tgi.getExpression());
        VarMap vm = new VarMap(false);
        String[] names = expression.getVariableNames();
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            Tag tag = tagRepository.get(name);
            vm.setValue(name, tag.getBitmap());
        }
        RoaringBitmap bitmap = expression.eval(vm, null);
        tgi.setBitmap(bitmap);

        //计算TGI
        Configuration config = new Configuration(bitmap, 10);
        Client client = new Client(tagRepository, config);
        LinkedHashMap<String, Double> tgiResult = client.execute();

        ObjectMapper om = new ObjectMapper();
        String resultStr = null;
        try {
            resultStr = om.writeValueAsString(tgiResult);
            tgi.setTgi(resultStr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //存储计算结果
        tgiRepository.save(tgi);

        Map<String, String> result = new HashMap<>(1);
        result.put("result", resultStr);
        return result;
    }

}
