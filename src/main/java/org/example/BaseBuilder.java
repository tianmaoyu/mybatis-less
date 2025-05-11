package org.example;

import jakarta.persistence.Table;

abstract class BaseBuilder<T> {
    protected final Class<T> entityClass;
    protected final SqlSession sqlSession;
    
    protected BaseBuilder(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.sqlSession = MyBatisUtils.getSqlSession();
    }
    
    protected String getTableName() {
        Table table = entityClass.getAnnotation(Table.class);
        return table != null ? table.value() : entityClass.getSimpleName();
    }
}