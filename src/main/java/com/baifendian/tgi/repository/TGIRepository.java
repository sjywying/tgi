package com.baifendian.tgi.repository;

import com.baifendian.common.BitmapHandler;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
* Created by crazyy on 16/9/28.
*/

@Repository("tgiRepository")
public class TGIRepository {

    private JdbcTemplate jdbcTemplate;

    private static String sql_list_all = "select name, expression, bitmapsize, tgi, createtime from tgi_tgi";
    private static String sql_save = "insert into tgi_tgi(name, expression, bitmapsize, bitmap, tgi) values (?,?,?,?,?)";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<TGI> listAll() {
        return this.jdbcTemplate.query(sql_list_all, new RowMapper<TGI>() {
            @Override
            public TGI mapRow(ResultSet resultSet, int i) throws SQLException {
                TGI tgi = new TGI();
                tgi.setName(resultSet.getString("name"));
                tgi.setBitmapsize(resultSet.getInt("bitmapsize"));
                tgi.setExpression(resultSet.getString("expression"));
                tgi.setCreatetime(resultSet.getDate("createtime"));
                return tgi;
            }
        });
    }

    public int save(TGI tgi) {
        int res = 0;

        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(
                    BitmapHandler.bitmapToByteArray(tgi.getBitmap()));

            res = this.jdbcTemplate.update(
                    sql_save,
                    tgi.getName(),
                    tgi.getExpression(),
                    tgi.getBitmap().getCardinality(),
                    byteArrayInputStream, tgi.getTgi());

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

}
