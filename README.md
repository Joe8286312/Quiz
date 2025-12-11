### Quiz

本仓库中的源码是Windows本地运行版本，如要部署到Ubuntu上，我让ai总结了部署方式

----

### 部署前的准备：

**后端修改：**

1. 注释config文件夹中的CorsConfig代码（因为使用Nginx作为java后端的反向代理，那么java后端可以不用进行跨域设置）
2. pom.xml中添加以下依赖，防止泄露开发环境下application.yml中可能涉及的部分敏感账户信息，然后在Ubuntu中的Java代码同目录下创建application.yml（idea右侧maven插件的 生存期 里的 package 双击打包生成 jar 包）

```
<!-- 可以在 pom.xml 的 <build> 配置中，使用 <resources> 标签排除 application.yml 文件。这样打包时不会将项目根目录下的 application.yml 文件打进 jar 包。-->
<build>
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <excludes>
                <exclude>application.yml</exclude>
            </excludes>
        </resource>
    </resources>
</build>
```

```
joe@joe-ubuntu:~$ cd /opt/quiz/
joe@joe-ubuntu:/opt/quiz$ ls
application.yml  app.log  dist  logs  quiz-0.0.1-SNAPSHOT.jar  quiz-0.0.1-SNAPSHOT-v2.jar
joe@joe-ubuntu:/opt/quiz$ sudo cat application.yml 
[sudo] joe 的密码： 
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/quiz
    username: testme
    password: 123123123

server:
  port: 8080
    #  address: localhost
joe@joe-ubuntu:/opt/quiz$ 
```

**前端修改：**

1. 修改用户端前端的请求地址

```
// login.html
baseURL: "http://localhost:8080" -> baseURL: "http://localhost"
// quiz.html
baseURL: "http://localhost:8080" -> baseURL: "http://localhost"
// register.html
fetch("http://localhost:8080/register", ... ) -> fetch("http://localhost/register", ... )
```

2. 修改管理端前端的请求地址（npm run build进行打包，生成dist文件夹）

```
// main.js:
axios.defaults.baseURL = 'http://localhost:8080'; -> axios.defaults.baseURL = '/api';
```

3. 将用户端文件夹user(里面有login.html, quiz.html, register.html)放入管理端打包文件夹dist中

​	结构类似：

```
/var/www/dist
  |- index.html           # 管理端入口
  |- static/...           # 管理端静态资源
  |- user/
      |- login.html
      |- register.html
      |- quiz.html
```



**迁移数据库**：

使用mysqldump导出数据库：

```
mysqldump -u root -p --databases quiz > quiz.sql
```

如果出现如下的错误：

![image-20250808191412843](https://gitee.com/bigbug55/webdesign/raw/master/img/image-20250808191412843.png)

需要找到mysql的安装目录的/bin/下的mysqldump.exe程序；

![image-20250808191601438](https://gitee.com/bigbug55/webdesign/raw/master/img/image-20250808191601438.png)

出现拒绝访问的问题；以管理员方式打开cmd; 然后将 quiz.sql 通过共享文件夹传入Ubuntu中

以root账号登录服务器上的mysql，创建quiz数据库，并创新一个新的用户，比如test，将quiz的全部权限赋予test用户(尽量不要直接使用root账户)：

```
#使用root账户登录；
mysql -u root -p   
# 然后输入root密码登录

#创建quiz数据库；
create database quiz;

##创建一个test普通用户；
# 我的MySQL版本过高，就算low的密码策略也得长度>=8，故设置密码为：123123123
create user 'testme' identified by '12345'; 

##赋予test用户操作quiz数据库的所有权限
# 我为 by "123123123"
grant all privileges on quiz.* to testme identified by "12345"; 

##退出mysql账户后，以test用户导入quiz.sql；
mysql -u test -p quiz < quiz.sql
```

---



- 下面是**按你现在真实做法**整理的精简版「上线部署 checklist」，去掉了 `chown` 和 systemd，用“手动启动 Jar + Nginx 反向代理”的方式；同时保留你现有的前端访问形式：

  - 用户端：`http://localhost/login` / `register` / `getQuestion`
  - 管理端：axios.baseURL = `/api`，请求 `/api/...`

  ---

  # 部署 Checklist（当前实际做法）

  ## 0. 前提确认

  - 后端 Jar：`quiz-0.0.1-SNAPSHOT.jar`
  - 运行方式：**手动在终端执行 `java -jar ...`，端口 8080**
  - 前端 dist：
    - 管理端：`/var/www/dist`
    - 用户端：`/var/www/dist/user`（里面有 `login.html`, `register.html`, `quiz.html`）
  - Nginx 配置为你给出的 `default`（80 端口）。

  ---

  ## 1. 启动后端（每次上线 / 重启都要做）

  ### 1.1 切到 Jar 所在目录并启动

  假设 Jar 在共享目录 `/mnt/hgfs/Shared_Folder/quiz`（按你实际路径改）：

  ```bash
  cd /mnt/hgfs/Shared_Folder/quiz
  
  # 后台启动（推荐），日志输出到 quiz.log，避免关闭终端后后端退出
  # quiz-0.0.1-SNAPSHOT.jar是你maven生成的Java源码，我实际是quiz-0.0.1-SNAPSHOT-v2.jar
  # nohup java -jar quiz-0.0.1-SNAPSHOT.jar > quiz.log 2>&1 &
  nohup java -jar quiz-0.0.1-SNAPSHOT-v2.jar > quiz.log 2>&1 &
  ```

  ### 1.2 确认后端是否在 8080 端口监听
  
  ```bash
  # 查看进程（任选其一）
  ps aux | grep quiz-0.0.1-SNAPSHOT.jar
  # 或
  ss -tlnp | grep 8080
  ```

  期望能看到类似：`LISTEN 0  ... :8080 ... java`

  > 如果端口不是 8080，要记得同步修改 Nginx 中的 `proxy_pass http://127.0.0.1:8080/...` 那些地方。

  ---

  ## 2. 部署前端静态文件（每次前端打包后）

  ### 2.1 拷贝 dist 到服务器目录

  假设打包后的 dist 目录在 `/mnt/hgfs/Shared_Folder/quiz/dist`：
  
  ```bash
  # 清理旧文件
  sudo rm -rf /var/www/dist/*
  
  # 拷贝新的前端资源
  sudo cp -r /mnt/hgfs/Shared_Folder/quiz/dist/* /var/www/dist/
  ```

  确认结构：
  
  ```bash
  ls /var/www/dist
  # 预期有：
  # index.html  (管理端)
  # user/       (用户端)
  # 以及 js / css / static 之类目录
  ls /var/www/dist/user
  # 预期有：login.html register.html quiz.html
  ```

  ---

  ## 3. 确认 Nginx 配置（仅在修改过配置或首次部署时检查）

  当前 `/etc/nginx/sites-available/default` 已经是你贴出的内容：
  
  ```nginx
  server {
      listen 80 default_server;
      listen [::]:80 default_server;
  
      server_name localhost;
  
      # 整个前端（管理端 + 用户端）都在 /var/www/dist
      root /var/www/dist;
      index index.html index.htm;
  
      # 管理端：Vue SPA，访问 http://localhost/ 或 http://localhost/#/login
      location / {
          try_files $uri $uri/ /index.html;
      }
  
      # 用户端：访问 http://localhost/user/register.html /login.html /quiz.html
      location /user/ {
          alias /var/www/dist/user/;
          try_files $uri $uri/ =404;
      }
  
      # ===================== 用户端后端接口 反向代理 =====================
  
      # 用户端 JS 里写的是 http://localhost/login
      location /login {
          proxy_pass http://127.0.0.1:8080/login;
  
          proxy_set_header Host $host;
          proxy_set_header X-Real-IP $remote_addr;
          proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
          proxy_set_header X-Forwarded-Proto $scheme;
  
          add_header Access-Control-Allow-Origin $http_origin always;
          add_header Access-Control-Allow-Credentials true always;
          add_header Access-Control-Allow-Methods 'GET, POST, OPTIONS' always;
          add_header Access-Control-Allow-Headers $http_access_control_request_headers always;
  
          if ($request_method = OPTIONS) {
              return 204;
          }
      }
  
      location /register {
          proxy_pass http://127.0.0.1:8080/register;
  
          proxy_set_header Host $host;
          proxy_set_header X-Real-IP $remote_addr;
          proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
          proxy_set_header X-Forwarded-Proto $scheme;
  
          add_header Access-Control-Allow-Origin $http_origin always;
          add_header Access-Control-Allow-Credentials true always;
          add_header Access-Control-Allow-Methods 'GET, POST, OPTIONS' always;
          add_header Access-Control-Allow-Headers $http_access_control_request_headers always;
  
          if ($request_method = OPTIONS) {
              return 204;
          }
      }
  
      location /getQuestion {
          proxy_pass http://127.0.0.1:8080/getQuestion;
  
          proxy_set_header Host $host;
          proxy_set_header X-Real-IP $remote_addr;
          proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
          proxy_set_header X-Forwarded-Proto $scheme;
  
          add_header Access-Control-Allow-Origin $http_origin always;
          add_header Access-Control-Allow-Credentials true always;
          add_header Access-Control-Allow-Methods 'GET, POST, OPTIONS' always;
          add_header Access-Control-Allow-Headers $http_access_control_request_headers always;
  
          if ($request_method = OPTIONS) {
              return 204;
          }
      }
  
      # ===================== 管理端 axios 统一走 /api =====================
  
      location /api/ {
          proxy_pass http://127.0.0.1:8080/;  # 末尾 /：/api/x -> /x
  
          proxy_set_header Host $host;
          proxy_set_header X-Real-IP $remote_addr;
          proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
          proxy_set_header X-Forwarded-Proto $scheme;
  
          add_header Access-Control-Allow-Origin $http_origin always;
          add_header Access-Control-Allow-Credentials true always;
          add_header Access-Control-Allow-Methods 'GET, POST, PUT, DELETE, OPTIONS' always;
          add_header Access-Control-Allow-Headers $http_access_control_request_headers always;
  
          if ($request_method = OPTIONS) {
              return 204;
          }
      }
  }
  ```

  ### 3.1 如有修改，测试并重载 Nginx
  
  ```bash
  sudo nginx -t
  sudo systemctl reload nginx
  ```

  ---

  ## 4. 前端 axios 配置（打包前检查一次）

  管理端 `main.js` 中应是类似：
  
  ```js
  import axios from 'axios';
  
  // 部署环境：统一相对前缀 /api
  axios.defaults.baseURL = '/api';
  axios.defaults.withCredentials = true;
  ```

  组件中统一写：
  
  ```js
  axios.post('/admin/login', {...})
  axios.get('/users', {...})
  axios.get('/questions', {...})
  // 等
  ```

  **用户端 HTML** 仍然是你现在的形式（不强制改）：
  
  - `http://localhost/login`
  - `http://localhost/register`
  - `http://localhost/getQuestion`

  这些由 Nginx 上 `/login` `/register` `/getQuestion` 三个 location 反向代理。

  ---

  ## 5. 验证联调（上线后检查）

  ### 5.1 管理端验证

  1. 浏览器访问：
  
  ```text
  http://localhost/#/login
  ```
  
  2. 输入管理员账号密码，点击“登录”；
  3. 打开浏览器开发者工具 → Network：
     - 应看到请求：`POST http://localhost/api/admin/login`  
     - 状态码：200  
     - 响应体是后端 JSON（`{"code":1,...}`）

  如果报错：
  
  - 看 Response / Status；
  - 再看 Nginx 日志：
    ```bash
    sudo tail -n 50 /var/log/nginx/error.log
    ```
  - 再看后端日志（你用 `nohup` 启动时指定的 `quiz.log`）：
    ```bash
    tail -n 50 /mnt/hgfs/Shared_Folder/quiz/quiz.log
    ```

  ### 5.2 用户端验证

  1. 浏览器访问：
  
  ```text
  http://localhost/user/login.html
  http://localhost/user/register.html
  http://localhost/user/quiz.html
  ```
  
  2. 在 Network 面板中：
     - 登录时：`POST http://localhost/login` → 状态 200
     - 注册时：`POST http://localhost/register` → 状态 200
     - 获取题目：`GET http://localhost/getQuestion` → 状态 200

  如果看到 404 或 JSON.parse 报错，说明 Nginx 的 `/login` 等没有命中或后端没有启动。

  ---

  ## 6. 常用排错命令（快捷表）
  
  ```bash
  # 1. 后端是否在 8080 监听
  ss -tlnp | grep 8080
  ps aux | grep quiz-0.0.1-SNAPSHOT.jar
  
  # 2. Nginx 配置是否正确
  sudo nginx -t
  sudo systemctl status nginx
  
  # 3. Nginx 错误日志
  sudo tail -f /var/log/nginx/error.log
  
  # 4. 后端运行日志（如果用 nohup）
  cd /mnt/hgfs/Shared_Folder/quiz
  tail -f quiz.log
  ```

  ---

  以后你上线/重启时，按这个顺序简单走一遍就可以：
  
  1. `nohup java -jar ... &` 启动后端（确认 8080 正在监听）。  
  2. `cp dist/* /var/www/dist/` 更新前端。  
  3. `nginx -t && systemctl reload nginx`（如果动过配置）。  
  4. 浏览器访问 `http://localhost/#/login` 和 `http://localhost/user/login.html` 做一次冒烟测试。