package com.yusys.itemwriter_xmlfile;

import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huyang on 2019/10/11.
 */
@Configuration
public class DbJdbcReaderConfig {

    @Autowired
    private DataSource dataSource;

    // TODO JdbcPagingItemReader 实现数据库读取
    @Bean
    public JdbcPagingItemReader<Customer> dbJdbcReader() {
        JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<Customer>();
        reader.setDataSource(dataSource);
        reader.setFetchSize(2);//一次读取几个
        // TODO 把读取到的记录转换为Customer对象
        reader.setRowMapper(new RowMapper<Customer>() {
            @Override
            public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
                Customer user = new Customer();
                user.setId(rs.getInt(1));
                user.setName(rs.getString(2));
                user.setAge(rs.getInt(3));
                return user;
            }
        });

        // TODO 指定sql语句
        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setSelectClause("id,name,age");// 指明查询哪些字段
        provider.setFromClause("from customer");// 指明表名

        // TODO 指定根据哪个字段进行排序
        Map<String,Order> sort = new HashMap<>(1);
        sort.put("age",Order.ASCENDING);// 按年齡降序排列
        provider.setSortKeys(sort);

        reader.setQueryProvider(provider);
        return  reader;
    }
}
