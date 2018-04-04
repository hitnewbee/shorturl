# shorturl
IDE:IntelliJ IDEA（scala插件）
构建工具:sbt
数据库：h2db in memeory
语言：JAVA(1.8)\scala
routelist：
GET     /                           controllers.PersonController.index()                主页面
GET     /listlinks                  controllers.PersonController.getLinks()             查看所有url及其点击数
POST    /links                      controllers.PersonController.addLinks()             添加url，支持自定义
GET     /transfer/:url              controllers.PersonController.transfer(url:String)   跳转重定向
