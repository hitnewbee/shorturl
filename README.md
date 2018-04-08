# shorturl

* IDE:IntelliJ IDEA（scala插件）
* 构建工具:sbt
* 数据库：h2db in memeory
* 语言：JAVA(1.8)\scala
* 功能：
* 1.输入长链、生成短链
* 2.访问短链，跳转长链
* 3.访问计数，链接列表
* 4.自定义短链
* 5.完成测试代码
* 技术：play framework、JPA、函数式编程、同步锁（数据库增删改操作）、JAVA8流处理、scala.html、play filter（请求过滤用于日志）
* 通过如下restapi对系统进行访问，可用postman客户端，访问数据格式JSON
* routelist：  <br/>
<div>
        <table border="0">
	  <tr>
	    <th>Method</th>
	    <th>URL</th>
      <th>function</th>
      <th>description</th>
	  </tr>
     <tr>
	    <td>GET</td>
	    <td>/listrest</td>
      <td>controllers.LinksController.getLinksRest()</td>
      <td>查看所有url及其点击数</td>
	  </tr>
    <tr>
	    <td>POST</td>
	    <td>/linksRest</td>
      <td>controllers.LinksController.addLinksRest()</td>
      <td>添加url，支持自定义</td>
	  </tr>
     <tr>
	    <td>GET</td>
	    <td>/transfer/:url</td>
      <td>controllers.LinksController.transfer(url:String)</td>
      <td>跳转重定向</td>
	  </tr>
	</table>
</div>
* 可通过返回体中keyword使用跳转重定向api访问  <br/>
GET     /transfer/:url              controllers.LinksController.transfer(url:String)   跳转重定向  <br/>
例：  <br/>
{  <br/>
  "id": 100,  <br/>
  "url": "http://www.baidu.com",  <br/>
  "keyword": "123456",  <br/>
  "type": "custom",  <br/>
  "clickcount": 0,  <br/>
  "insert_at": "2018-04-08-16-53-47"  <br/>
}  <br/>
access: http://localhost:9000/transfer/123456 then you can redirect to www.baidu.com   <br/>
GET     /listrest                   controllers.LinksController.getLinksRest()         查看所有url及其点击数  <br/>
response bodyexp：  <br/>
[  <br/>
  {  <br/>
    "id": 100,  <br/>
    "url": "http://www.baidu.com",  <br/>
    "keyword": "0000wY",  <br/>
    "type": "system",  <br/>
    "clickcount": 0,  <br/>
    "insert_at": "2018-04-08-16-42-24"  <br/>
  },  <br/>
  {  <br/>
    "id": 101,  <br/>
    "url": "http://www.baidu.com",  <br/>
    "keyword": "0000wU",  <br/>
    "type": "system",  <br/>
    "clickcount": 0,  <br/>
    "insert_at": "2018-04-08-16-43-04"  <br/>
  }  <br/>
]  <br/>
POST    /linksRest                  controllers.LinksController.addLinksRest()	       添加url，支持自定义  <br/>
request bodyexp：  <br/>
{  <br/>
	"url":"http://www.baidu.com",  <br/>
	"keyword":"123456"  <br/>
}  <br/>
自定义短连接  <br/>
or:  <br/>
{  <br/>
	"url":"http://www.baidu.com",  <br/>
}  <br/>
系统生成短连接  <br/>

response bodyexp：  <br/>
{  <br/>
  "id": 100,  <br/>
  "url": "http://www.baidu.com",  <br/>
  "keyword": "123456",  <br/>
  "type": "custom",  <br/>
  "clickcount": 0,  <br/>
  "insert_at": "2018-04-08-16-53-47"  <br/>
}  <br/>
