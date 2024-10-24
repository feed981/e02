com.baomidou.mybatisplus.annotation.TableName 类无法找到的问题，通常这是因为 MyBatis-Plus 的依赖没有正确引入

父工程 (pom.xml) 中已经通过 dependencyManagement 声明了 mybatis-plus-boot-starter 依赖

检查父工程 MyBatis-Plus 版本
正在使用的 MyBatis-Plus 版本是 3.4.1，该版本应该是支持 TableName 注解的。如果问题依旧，可以考虑升级到一个较新的版本，如 3.4.3 或 较新的 3.5.x，并查看是否能解决问题。

```xml 
<!-- 在父工程 pom.xml 中升级 MyBatis-Plus 版本 -->
<mybatis.plus.version>3.4.3</mybatis.plus.version>
<mybatis.plus.version>3.5.0</mybatis.plus.version>
```
