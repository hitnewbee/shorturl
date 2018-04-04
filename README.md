# shorturl

* IDE:IntelliJ IDEA（scala插件）
* 构建工具:sbt
* 数据库：h2db in memeory
* 语言：JAVA(1.8)\scala
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
      <td>controllers.PersonController.index()</td>
      <td>主页面</td>
	  </tr>
     <tr>
	    <td>GET</td>
	    <td>/listlinks</td>
      <td>controllers.PersonController.getLinks()</td>
      <td>查看所有url及其点击数</td>
	  </tr>
    <tr>
	    <td>POST</td>
	    <td>/links</td>
      <td>controllers.PersonController.addLinks()</td>
      <td>添加url，支持自定义</td>
	  </tr>
     <tr>
	    <td>GET</td>
	    <td>/transfer/:url</td>
      <td>controllers.PersonController.transfer(url:String)</td>
      <td>跳转重定向</td>
	  </tr>
	</table>
</div>
GET     /                           controllers.PersonController.index()                主页面   <br/>
GET     /listlinks                  controllers.PersonController.getLinks()             查看所有url及其点击数  <br/>
POST    /links                      controllers.PersonController.addLinks()             添加url，支持自定义  <br/>
GET     /transfer/:url              controllers.PersonController.transfer(url:String)   跳转重定向  <br/>
