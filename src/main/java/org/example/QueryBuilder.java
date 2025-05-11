package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class QueryBuilder<T> {
        private final Class<T> entityClass;
        final List<Condition> conditions = new ArrayList<>();
        private String orderByColumn;
        private String tableName;

        public QueryBuilder(Class<T> entityClass) {
            this.entityClass = entityClass;
            this.tableName = entityClass.getSimpleName().toLowerCase();
        }
    public QueryBuilder(SFunction<T, ?>... columns) {
//        this.entityClass = entityClass;
//        this.tableName = entityClass.getSimpleName().toLowerCase();
    }

        public <R> ConditionBuilder<T, R> where(FieldGetter<T, R> column) {
            String columnName = FieldResolver.getColumnName(column);
            return new ConditionBuilder<>(this, columnName);
        }
    public QueryBuilder<T> limit(int limit) {
        if (limit < 0) throw new IllegalArgumentException("LIMIT 不能为负数");
        this.limit = limit;
        return this;
    }

    public QueryBuilder<T> offset(int offset) {
        if (offset < 0) throw new IllegalArgumentException("OFFSET 不能为负数");
        this.offset = offset;
        return this;
    }

        public QueryBuilder<T> orderBy(FieldGetter<T,?> column) {
            this.orderByColumn = FieldResolver.getColumnName((FieldGetter<?, ?>) column);
            return this;
        }

        public List<T> execute() {
            String sql = buildSql();
            List<Object> parameters = new ArrayList<>();
            
            for (Condition condition : conditions) {
                parameters.add(condition.getValue());
            }

            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                for (int i = 0; i < parameters.size(); i++) {
                    stmt.setObject(i + 1, parameters.get(i));
                }

                ResultSet rs = stmt.executeQuery();
                return mapResultSetToEntities(rs);
            } catch (Exception e) {
                throw new RuntimeException("Query execution failed", e);
            }
        }

        private String buildSql() {
            StringBuilder sb = new StringBuilder("SELECT * FROM ");
            sb.append(tableName);

            if (!conditions.isEmpty()) {
                sb.append(" WHERE ");
                List<String> conditionStrings = new ArrayList<>();
                
                for (Condition condition : conditions) {
                    if (condition instanceof OrCondition) {
                        // 对于 OR 条件，确保它被正确地括号包围
                        conditionStrings.add("(" + condition.getColumnName() + " " + 
                                            condition.getOperator().replace("OR ", "") + " ?)");
                    } else {
                        // 其他条件直接添加
                        conditionStrings.add(condition.getColumnName() + " " + 
                                            condition.getOperator() + " ?");
                    }
                }
                
                // 使用 AND 连接所有条件，但更高级的逻辑可能需要支持 OR 连接
                sb.append(String.join(" AND ", conditionStrings));
            }

            if (orderByColumn != null) {
                sb.append(" ORDER BY ").append(orderByColumn);
            }

            return sb.toString();
        }

        private List<T> mapResultSetToEntities(ResultSet rs) throws Exception {
            List<T> results = new ArrayList<>();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                T entity = entityClass.getDeclaredConstructor().newInstance();
                
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    
                    try {
                        Field field = entityClass.getDeclaredField(columnName);
                        field.setAccessible(true);
                        field.set(entity, value);
                    } catch (NoSuchFieldException e) {
                        // Handle column name to field name conversion if needed
                    }
                }
                results.add(entity);
            }
            return results;
        }

        private Connection getConnection() throws SQLException {
            // 实现获取数据库连接的实际逻辑
            return DriverManager.getConnection("jdbc:mysql://localhost/test", "user", "password");
        }

        // 修改 and 方法以支持条件组
        public QueryBuilder<T> and(Condition condition) {
            if (condition instanceof OrCondition) {
                // 对于 OR 条件，添加括号以确保正确的优先级
                conditions.add(new Condition("(" + condition.getColumnName() + ")", 
                              condition.getOperator(), condition.getValue()));
            } else {
                conditions.add(condition);
            }
            return this;
        }

        // 修改 or 方法以支持条件组
        public QueryBuilder<T> or(Condition condition) {
            // 创建 OR 条件并添加到查询构建器
            OrCondition orCondition = new OrCondition(condition);
            conditions.add(orCondition);
            return this;
        }
    }
// 自定义 OR 条件包装类
class OrCondition extends Condition {
    public OrCondition(Condition condition) {
        super(condition.getColumnName(), "OR " + condition.getOperator(), condition.getValue());
    }
}