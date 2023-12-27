package com.pixel.jda.bot.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.Reader;

public class SqlUtil {
    public static SqlSession getSqlSession() {
        return getSqlSession(true);
    }

    /**
     * SqlSessionFactory 생성
     *
     * @return SqlSessionFactory
     */
    public static SqlSession getSqlSession(boolean autoCommit) {
        try (Reader reader = Resources.getResourceAsReader("mybatis-config.xml")) {
            return new SqlSessionFactoryBuilder().build(reader).openSession(autoCommit);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("SqlSessionFactory Create Failed", e);
        }
    }
}
