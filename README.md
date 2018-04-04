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
* 技术：play framework、JPA、函数式编程、同步锁（数据库增删改操作）、JAVA8流处理、scala.html、play filter（请求过滤用于日志）
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
	    <td>/</td>
      <td>controllers.LinksController.index()</td>
      <td>主页面</td>
	  </tr>
     <tr>
	    <td>GET</td>
	    <td>/listlinks</td>
      <td>controllers.LinksController.getLinks()</td>
      <td>查看所有url及其点击数</td>
	  </tr>
    <tr>
	    <td>POST</td>
	    <td>/links</td>
      <td>controllers.LinksController.addLinks()</td>
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
GET     /                           controllers.LinksController.index()                主页面   <br/>
GET     /listlinks                  controllers.LinksController.getLinks()             查看所有url及其点击数  <br/>
POST    /links                      controllers.LinksController.addLinks()             添加url，支持自定义  <br/>
GET     /transfer/:url              controllers.LinksController.transfer(url:String)   跳转重定向  <br/>
