//package org.deml;
//
//import org.example.User;
//
//import java.util.List;
//
//public class Main {
//    public static void main(String[] args) {
//        List<User> users = SQL.selectFrom(User.class)
//                .where(User::getAge).gt(18)
//                .and((User::getName).like("A%").or(User::getScore).ge(90))
//                .or((User::getStatus).eq(1).and(User::getVipLevel).ge(3))
//                .execute();
//
//
//        UserMapperImpl userMapper = new UserMapperImpl();
//        userMapper.update(new User());
//        org.deml.User userById = userMapper.getUserById(1L);
//        userMapper.insertBatch()
//    }
//}
