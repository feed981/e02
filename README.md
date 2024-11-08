## 将文章内容会吃的css、js先上传到minio

heima-leadnews-test/minio-demo/src/test/java/com/feed02/MinIOTest.java

local/plugins/css/index.css

local/plugins/js/axios.min.js

local/plugins/js/index.js


## 只是看下文章内容格式

heima-leadnews-service/heima-leadnews-article/src/main/resources/content.json


## nacos 记得补file.minio 相关配置

nacos/nacos.sql

## 模版
heima-leadnews-service/heima-leadnews-article/src/main/resources/templates/article.ftl


## 静态页面生成上传到minio

版本 SelectOne Wrapper 无法用，所以实作IService query eq 查

heima-leadnews-service/heima-leadnews-article/src/main/java/com/feed02/article/mapper/ApArticleContentMapper.java

heima-leadnews-service/heima-leadnews-article/src/main/java/com/feed02/article/service/IApArticleContentService.java

heima-leadnews-service/heima-leadnews-article/src/main/java/com/feed02/article/service/impl/ApArticleServiceContentImpl.java


## 新增static_url 栏位到ap_article表中

heima-leadnews-model/src/main/java/com/feed02/model/article/pojos/ApArticle.java

sql/leadnews_article.sql

## 实现

heima-leadnews-service/heima-leadnews-article/src/test/java/feed02/CreateStaticUrlTest.java
