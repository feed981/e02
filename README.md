文章相关的表共有三个
- ap_article
- ap_article_config
- ap_article_content

# 为什么文章表要拆成三张表

将大表拆分为多个小表，特别是当某些列（如 longtext 类型）频繁影响查询性能时，可以显著提高操作效率。例如，ap_article 表的操作效率高于 ap_article_config 表，因为后者包含 longtext 列，这可能导致性能下降

# 表的拆分-垂直分表

垂直分表:将一个表的字段分散到多个表中,每个表存储其中一部分字段。

优势:
1. 减少IO争抢,减少锁表的几率,查看文章概述与文章详情互不影响
2. 充分发挥高频数据的操作效率,对文章概述数据操作的高效率不会被操作文章详情数据的低效率所拖累。

拆分规则:
1. 把不常用的字段单独放在一张表
2. 把text、blob等大字段拆分出来单独放在一张表
3. 经常组合查询的字段单独放在一张表中
