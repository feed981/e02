# 使用freemarker原生Api将页面生成html文件
1. 根据模板文件生成html文件

修改application.yml文件，添加以下模板存放位置的配置信息

```yaml
template-loader-path: classpath:/templates   #模板存放位置
```
2. freemarker.template

```java
import freemarker.template.Configuration;
import freemarker.template.Template;

    @Autowired
    private Configuration configuration;

    @Test
    public void test() throws IOException, TemplateException {
        // freemarker的模板对象，获取模板
        Template template = configuration.getTemplate("02-list.ftl");
        Map params = getData();
        // 合成(数据模型,输出流)
        template.process(params, new FileWriter("d:/list.html"));
    }
```
