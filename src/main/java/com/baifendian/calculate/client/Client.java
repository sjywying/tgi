package com.baifendian.calculate.client;

import com.baifendian.common.CollectionUtil;
import com.baifendian.tgi.repository.Tag;
import com.baifendian.tgi.repository.TagRepository;
import org.roaringbitmap.RoaringBitmap;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by crazyy on 16/9/30.
 */
public class Client {

    java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat("#.00");

    private TagRepository tagRepository;

    private Configuration config;

    public Client(TagRepository tagRepository, Configuration conf) {
        this.tagRepository = tagRepository;
        this.config = conf;
    }

    public LinkedHashMap<String, Double> execute() {
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();

        List<Tag> tags = tagRepository.listAllContainBitmap();
        for (Tag tag : tags) {
            double bitmapsize = tag.getBitmap().getCardinality() * 1.0;
            double i = RoaringBitmap.and(tag.getBitmap(), config.getBitmap()).getCardinality()/bitmapsize;
            double j = bitmapsize/config.getAllNum();
            result.put(tag.getName(), Double.valueOf(decimalFormat.format(i/j)));
        }

        return CollectionUtil.sortByValue(result);
    }

}
