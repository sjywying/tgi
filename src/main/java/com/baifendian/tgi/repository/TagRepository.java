package com.baifendian.tgi.repository;

import com.baifendian.common.BitmapHandler;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.roaringbitmap.RoaringBitmap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by crazyy on 16/9/28.
 */

@Repository("tagRepository")
public class TagRepository {

    private JdbcTemplate jdbcTemplate;

    private static String sql_list_all = "select name, bitmapsize from tgi_tag";
    private static String sql_list_all_contain_bitmap = "select name, bitmap, bitmapsize, createtime from tgi_tag";
    private static String sql_getbyname = "select name, bitmap, bitmapsize from tgi_tag where name = ?";
    private static String sql_replace_into = "REPLACE INTO tgi_tag (name, bitmap, bitmapsize) values (?,?,?)";
    private static String sql_delete_all = "delete from tgi_tag";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Tag> listAll() {
        return this.jdbcTemplate.query(sql_list_all, new RowMapper<Tag>() {
            @Override
            public Tag mapRow(ResultSet resultSet, int i) throws SQLException {
                Tag tag = new Tag();
                tag.setName(resultSet.getString("name"));
                tag.setBitmapSize(resultSet.getInt("bitmapsize"));
                return tag;
            }
        });
    }

    public List<Tag> listAllContainBitmap() {
        return this.jdbcTemplate.query(sql_list_all_contain_bitmap, new RowMapper<Tag>() {
            @Override
            public Tag mapRow(ResultSet resultSet, int i) throws SQLException {
                Tag tag = new Tag();
                tag.setName(resultSet.getString("name"));
                tag.setBitmapSize(resultSet.getInt("bitmapsize"));
                tag.setCreateTime(resultSet.getDate("createtime"));
                InputStream is = resultSet.getBinaryStream("bitmap");
                tag.setBitmap(BitmapHandler.byteStreamToBitmap(is));
                return tag;
            }
        });
    }

    public Tag get(String name) {
//        this.jdbcTemplate.query(sql_getbyname, new ResultSetExtractor<Tag>() {
//
//            @Override
//            public Tag extractData(ResultSet resultSet) throws SQLException, DataAccessException {
//                Tag tag = new Tag();
//
//                tag.setName(resultSet.getString("name"));
//                tag.setBitmapSize(resultSet.getInt("bitmapsize"));
//                InputStream is = resultSet.getBinaryStream("bitmap");
//                tag.setBitmap(BitmapHandler.byteStreamToBitmap(is));
//                return tag;
//            }
//        }, name);
        return this.jdbcTemplate.queryForObject(
                    sql_getbyname,
                    new String[]{name},
                    new RowMapper<Tag>() {
                        @Override
                        public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
                            Tag tag = new Tag();
                            tag.setName(rs.getString("name"));
                            tag.setBitmapSize(rs.getInt("bitmapsize"));
                            InputStream is = rs.getBinaryStream("bitmap");
                            tag.setBitmap(BitmapHandler.byteStreamToBitmap(is));
                            return tag;
                        }
                    });
//                new String[]{name}, new ResultSetExtractor<Tag>() {
//
//            @Override
//            public Tag extractData(ResultSet resultSet) throws SQLException, DataAccessException {
//                Tag tag = new Tag();
//
//                tag.setName(resultSet.getString("name"));
//                tag.setBitmapSize(resultSet.getInt("bitmapsize"));
//                InputStream is = resultSet.getBinaryStream("bitmap");
//                tag.setBitmap(BitmapHandler.byteStreamToBitmap(is));
//                return tag;
//            }
//        });
    }

    public int save(String name, RoaringBitmap bitmap) {

        int res = 0;

        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(
                    BitmapHandler.bitmapToByteArray(bitmap));
            res = this.jdbcTemplate.update(sql_replace_into, name, byteArrayInputStream, bitmap.getCardinality());
        } catch (DataAccessException e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteArrayInputStream != null)
                    byteArrayInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public int deleteAll() {
        return this.jdbcTemplate.update(sql_delete_all);
    }

}
