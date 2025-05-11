package org.deml;

public interface UserMapper {

    default void insert(User user) {
        SQL.insert(user).execute();
    }

    default void insertBatch(User... users) {
        SQL.insertBatch(User.class).execute(users);
    }

     default void update(User user) {
        SQL.update(user).execute();
    }
    default User getUserById(Long id) {
        return SQL.selectFrom(User.class).where(User::getId).eq(id).execute().get(0);
    }
}
