# List

<#list></#list>

## 例子

<#list stus as stu>
<tr>
<td>${stu_index+1}</td>
<td>${stu.name}</td>
<td>${stu.age}</td>
<td>${stu.money}</td>
</tr>
</#list>

## 注意事项

${k_index}:得到循环的下标,使用方法是在stu后边加"_index",它的值是从0开始



# Map

## 获取map中的值

map['keyname']. property
map.keyname.property

## 遍历map

<#list userMap?keys as key>
key:${key}--value:${userMap["${key}"]}
</#list>


# if


## 指令
<#if expression>
<#else>
</#if>

## 需求:在list集合中判断学生为小红的数据字体显示为红色。

<#if stu.name=''>
<tr style="color: red">
<td>${stu_index}</td>
<td>${stu.name}</td>
<td>${stu.age}</td>
<td>${stu.money}</td>
</tr>
<#else >
<tr>
<td>${stu_index}</td>
<td>${stu.name}</td>
<td>${stu.age}</td>
<td>${stu.money}</td>
</tr>
</#if>

## 注意事项

在freemarker中,判断是否相等,=与==是一样的


# 空值处理

## 判断某变量是否存在使用"??"

用法为:variable??如果该变量存在,返回true,否则返回false

例:为防止stus为空报错可以加上判断如下:
<#if stus??>
<#list stus as stu>
</#list>
</#if>

## 缺失变量默认值使用"!"

使用!要以指定一个默认值,当变量为空时显示默认值
例:${name!''}表示如果name为空显示空字符串。

如果是嵌套对象则建议使用()括起来
例:${(stu.name)!''}表示,如果stu或name为空默认显示空字符串。



# 运算符

=和!=可以用于字符串、数值和日期来比较是否相等

=和!=两边必须是相同类型的值,否则会产生错误

其它的运行符可以作用于数字和日期,但不能作用于字符串

使用gt等字母运算符代替>会有更好的效果,因为 FreeMarker会把>**解释成FTL标签的结束字符



# 内建函数语法格式: 变量+?+函数名称

${集合名?size}
${today?latetime}
${today?string("yyyy年MM月")}
${point?c}
text?eval
