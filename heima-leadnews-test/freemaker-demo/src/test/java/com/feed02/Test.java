package com.feed02;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        String htmlContent ="";
        splitHtmlWithPattern(htmlContent);
    }

    public static void splitHtmlWithPattern(String htmlContent){
        StringBuilder titles = new StringBuilder();

        // 正则表达式匹配 <div class="title-txt">...</div>
        Pattern pattern = Pattern.compile("<div class=\"title-txt\">(.*?)</div>");
        Matcher matcher = pattern.matcher(htmlContent);

        // 找到所有匹配项并添加到 StringBuilder
        while (matcher.find()) {
            titles.append(matcher.group(1)).append("\n"); // 添加标题和换行符
        }
        System.out.println(titles.toString());
    }
}
