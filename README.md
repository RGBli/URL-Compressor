# 长链接压缩器（URL-Compressor）
### 功能
可以把长链接转为一个短链接，通过该服务访问。
还可以设置短链接的有效期。
短链接和长链接的对应关系为多对一。



### 为什么需要短链接
1）便于保存和传播；
2）某些网站内容发布有字数限制（如微博分享只能140字），短链接可以节约字数。



### 技术栈
SpringBoot + Redis



### 实现思路
1）发号器
为了避免短链接冲突，即多个长链接对应一个短链接的情况，使用了递增的发号器来保证唯一性。
为了扩大表示范围，使用了62进制（0-9 A-Z a-z）来表示发号器传来的号码。采用62进制的好处是仅仅使用5位62进制数就可以表示62^5=916132832，在保证短链接足够短的条件下可以存放9亿个长链接!

2)发号器的实现
发号器可以使用 MySQL 的自增主键，也可以使用 Redis 来实现。考虑到 MySQL 服务是比较重量级的，因此使用了 Redis 来维护一个递增的发号器。
发号器从1开始，每生成一个短链接，就将短链接-长链接作为 key-value 存入 Redis 中，并将发号器加一。

3）短链接的生成
使用5位的62进制数的字符串表示长链接对应发号器的号码。



### 使用
1）生成短 URL
使用 postman 工具或 curl 命令发送 POST 请求来将长 URL 转为短 URL
expire 表示短 URL 的有效期，单位是分钟，-1表示永不过期
```
POST http://localhost:8080/zip
{
    "url": "https://www.baidu.com",
    "expire": -1
}
```

2）使用短 URL 来访问
在浏览器输入生成的短链接 00001，加上服务前缀即可访问长链接
```
http://localhost:8080/api?url=00001
```



### 未来的改进
1）目前还没有仔细考虑过是否是并发安全的，下一步会仔细考虑并做出改进。

2）目前没有前端页面，下一步会做出一个简单的页面。



### 参考文章
https://baijiahao.baidu.com/s?id=1687332481025084170&wfr=spider&for=pc
