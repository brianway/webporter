# webporter

webporter 一个基于垂直爬虫框架 [webmagic](http://webmagic.io/) 的 Java 爬虫应用，旨在提供一套完整的数据爬取，持久化存储和可视化展示的实践样例。

webporter 寓意“我们不生产数据，我们只是互联网的搬运工～”

*如果觉得不错，请先在这个仓库上点个 star 吧*，这也是对我的肯定和鼓励，谢谢了。

不定时进行调整和补充，需要关注更新的请 watch、star、fork




## 仓库目录

- [webporter-configure](/webporter-configure):使用 json 配置文件自动化配置模块
- [webporter-collector-zhihu](/webporter-collector-zhihu):知乎用户信息的爬取模块




## 使用指南

### 数据爬取

1.定制配置文件

修改 `webporter-collector-zhihu/src/main/resources/site-config.json` ，加入 `authorization` 的 http 头，相应数据需要自行在浏览器抓包提取

配置文件示例

```json
{
  "domain": "www.zhihu.com",
  "headers": {
    "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36",
    "authorization": "Your own authorization here."
  }
}
```

2.启动爬虫

运行 `webporter-collector-zhihu` 模块的 `com.brianway.webporter.collector.ZhihuUserPageProcessor` 即可

3.数据持久化

将数据导入到 ElasticSearch 中，待完善

4.数据可视化

待完善


## TODO 

* [x] 数据爬取，获取知乎用户数据
* [ ] 数据持久化，将数据导入到 ElasticSearch 中
* [ ] 可视化展示，通过前端框架对数据进行简单的分析和展示


-----

## 联系作者

- [Brian's Personal Website](http://brianway.github.io/)
- [CSDN](http://blog.csdn.net/h3243212/)
- [oschina](http://my.oschina.net/brianway)


Email: weichuyang@163.com



## Lisence

Lisenced under [Apache 2.0 lisence](http://opensource.org/licenses/Apache-2.0)



