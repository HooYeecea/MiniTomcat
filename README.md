# MiniTomcat

一个手写的迷你 HTTP 服务器，用来理解 Tomcat 在处理请求时最核心的那几步：监听端口、解析 HTTP 报文、按路径分发到 Servlet、再把响应写回客户端。

它不是完整的 Servlet 容器，没有 `web.xml`、Session、Filter、JSP。目标是把「请求是怎么被接住、拆开、路由、响应」这条链路走通。

## 它能做什么

- 监听 `8080` 端口，接收浏览器或 `curl` 发来的 HTTP 请求
- 用固定大小线程池并发处理连接，主线程只负责 `accept`
- 解析请求行：`method`、`url`、`version`
- 用一张路由表把 URL 映射到 `Servlet`
- 支持自定义 Servlet，写法接近 Java Servlet 的 `service(req, resp)`
- 未匹配路径返回 `404 Not Found`

## 技术栈

- Java 21
- Maven
- 标准库：`ServerSocket` / `Socket` / `ExecutorService`

无第三方依赖。

## 请求处理流程

```
浏览器 / curl
    │
    ▼
HttpServer          监听 8080，accept 后把 Socket 丢进线程池
    │
    ▼
HandleRequest       读请求行 → 封装 HttpRequest → 查路由表
    │
    ├─ 命中 Servlet  → servlet.service(req, resp)
    └─ 未命中        → 设置 404
    │
    ▼
HttpResponse.write  拼 HTTP 响应头 + 正文，写回 Socket 并关闭连接
```

一次请求的大致步骤：

1. `HttpServer` 阻塞在 `serverSocket.accept()`，有连接进来后提交给 10 线程的线程池。
2. `HandleRequest` 从 Socket 输入流读出第一行，例如 `GET /hello HTTP/1.1`。
3. 拆成方法、路径、协议版本，填进 `HttpRequest`。
4. 用 URL 去 `SERVLET_MAP` 里找对应 Servlet。
5. 找到就调用 `service()`；找不到就写 404 页面。
6. `HttpResponse.write()` 按 HTTP/1.1 格式写出状态行、`Content-Type`、`Content-Length` 和 HTML 正文。

## 核心类

| 类 | 职责 |
| --- | --- |
| `HttpServer` | 入口。创建 `ServerSocket`、线程池，循环接受连接 |
| `HandleRequest` | 解析请求、维护路由表、调度 Servlet |
| `HttpRequest` | 请求对象，目前只保存 method / url / version |
| `HttpResponse` | 响应对象，设置状态码和正文，最后写回客户端 |
| `Servlet` | 业务接口，只有一个 `service` 方法 |
| `HelloServlet` | `/hello` 示例 |
| `TimeServlet` | `/time` 示例，返回当前时间 |

路由注册在 `HandleRequest` 的静态块里：

```java
SERVLET_MAP.put("/", (req, resp) -> resp.setBody("<h1>Welcome to MiniTomcat Home Page</h1>"));
SERVLET_MAP.put("/hello", new HelloServlet());
SERVLET_MAP.put("/time", new TimeServlet());
```

## 项目结构

```
MiniTomcat
├── pom.xml
└── src/main/java/com/minitomcat
    ├── HttpServer.java
    ├── HandleRequest.java
    ├── HttpRequest.java
    ├── HttpResponse.java
    ├── Servlet.java
    ├── HelloServlet.java
    └── TimeServlet.java
```

## 如何运行

需要本机已安装 **JDK 21** 和 **Maven**。

```bash
# 在项目根目录编译
mvn compile

# 启动服务器
mvn exec:java -Dexec.mainClass="com.minitomcat.HttpServer"
```

也可以在 IDE 里直接运行 `com.minitomcat.HttpServer` 的 `main` 方法。

控制台出现下面这行就说明已经起来了：

```
Server is running on port 8080
```

## 内置路由

启动后访问：

| 路径 | 说明 |
| --- | --- |
| http://localhost:8080/ | 首页欢迎语 |
| http://localhost:8080/hello | `Hello, MiniTomcat!` |
| http://localhost:8080/time | 服务器当前时间 |
| 其他路径 | `404 Not Found` |

用 curl 也可以：

```bash
curl http://localhost:8080/hello
curl http://localhost:8080/time
```

## 如何加一个自己的 Servlet

1. 实现 `Servlet` 接口：

```java
public class PingServlet implements Servlet {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        response.setBody("<h1>pong</h1>");
    }
}
```

2. 在 `HandleRequest` 的静态块里注册路径：

```java
SERVLET_MAP.put("/ping", new PingServlet());
```

3. 重启服务器，访问 `http://localhost:8080/ping`。

也可以直接用 lambda 注册，不必单独建类。

## 和真实 Tomcat 差在哪

当前版本刻意做小，下面这些都还没有：

- 只解析请求行，不解析 Header、Query、Cookie、请求体
- 没有 `web.xml` / 注解扫描，路由靠代码里手动 `put`
- 没有 Session、Filter、Listener、JSP
- 没有静态资源目录，响应固定为 `text/html`
- 连接处理完就关闭，没有 Keep-Alive
- 状态码目前主要区分 200 和 404

这些限制正好对应下一步可以动手扩展的方向。
