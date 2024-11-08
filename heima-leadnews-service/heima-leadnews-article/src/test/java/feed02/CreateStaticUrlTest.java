package feed02;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.feed02.ArticleApplication;
import com.feed02.article.service.IApArticleContentService;
import com.feed02.article.service.IApArticleService;
import com.feed02.file.service.FileStorageService;
import com.feed02.model.article.pojos.ApArticleContent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;


@SpringBootTest(classes = ArticleApplication.class)
@RunWith(SpringRunner.class)
public class CreateStaticUrlTest {

    @Autowired
    private IApArticleContentService apArticleContentService;

    @Autowired
    private Configuration configuration;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private IApArticleService apArticleService;
    @Test
    public void createStaticUrlTest() throws Exception {
        //1.获取文章内容
        ApArticleContent apArticleContent = apArticleContentService.query().eq("article_id", 1302864436297482242L).one();

        if(apArticleContent != null && StringUtils.isNotBlank(apArticleContent.getContent())){

            //2.文章内容通过freemarker生成html文件
            Template template = configuration.getTemplate("article.ftl");

            // 数据模型
            Map<String ,Object> content = new HashMap<>();
            content.put("content" , JSONArray.parseArray(apArticleContent.getContent()));
            StringWriter out = new StringWriter();

            // 合成
            template.process(content ,out);

            //3.把html文件上传到minio中
            InputStream io = new ByteArrayInputStream(out.toString().getBytes());
            String path = fileStorageService.uploadHtmlFile("", apArticleContent.getArticleId() + ".html", io);

            //4.修改ap_article表，保存static_url字段
            apArticleService.update().eq("id" ,apArticleContent.getArticleId()).set("static_url",path).update();
        }
    }
}
