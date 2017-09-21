# simple-tomcat
学习Tomcat

学习和了解Tomcat更加深入的内容,不仅仅是会使用而已,知其然更要知其所以然

在浏览器内输入 `http://127.0.0.1:8081/primitive/index.html`
或者在终端中执行`curl http://127.0.0.1:8081/primitive/index.html`

就可以看到正常的内容了，注意其针对`favicon.ico`并未处理,会看到如下的错误日志
```java
java.lang.Exception: don't find wrapper container with /favicon.ico
	at tomcat.manager.Mapper.mapWrapper(Mapper.java:28) ~[classes/:na]
	at tomcat.http.HttpProcessor.processor(HttpProcessor.java:57) [classes/:na]
	at tomcat.http.HttpProcessor.run(HttpProcessor.java:112) [classes/:na]
	at java.lang.Thread.run(Thread.java:745) [na:1.8.0_40]

```

通过切换tag分支运行相关功能

## simple-connect

- 能够解析基本的http请求头部信息,`但是并不完整`
- 学习和模拟Tomcat4 httpconnect和process对socket的处理方式

## simple-container

- Tomcat容器本身包含了4层,但是只实现了context和wrapper两层
- 请求和容器拆开

## simple-lifecycle

本demo中只实现了一个简单的生命周期的监听过程,打印出日志,直接启动即可
此处并没有实现Tomcat4 5中的LifeSupport类,而是参照7,直接写到了`LifecycleBase`中,每个组件都可以直接继承

## elegant-close

当程序运行,需要关闭,如何做到优雅关闭的呢?

## 可以完善改进

- 解析请求更加细化
- 采用NIO读写socket
- digester读写xml



