1. 检查参数
2. 分页查询 是否收藏、按用户查询、时间倒叙
3. 返回结果

java.lang.reflect.InaccessibleObjectException，而它提示 "module java.base does not "opens java.lang.invoke" to unnamed module", 这通常是由 Java 反射访问权限限制引起的。在 Java 9 及以上版本中，反射访问某些非公开成员需要特殊的模块配置，否则会出现 InaccessibleObjectException。MyBatis-Plus 在使用 LambdaQueryWrapper 时需要访问 SerializedLambda，而这正是引发问题的原因。

这边是 使用 QueryWrapper 代替 LambdaQueryWrapper
