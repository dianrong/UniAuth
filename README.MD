* [一. 关于UNIAUTH](#about)
* [二. 快速开始](#quickstart)
* [三. 系统结构](#architecture)
* [四. 参数配置](#configuration)
* [五.系统集成](#integration)

<a name='about'/>
## 一. 关于UNIAUTH
- uniauth干什么的，它是一个统一登录+鉴权+权限管理的综合系统；它非常适用于多子系统环境，且各系统需要通过统一认证/授权，并能根据既定的角色和权限来规范业务操作范围的企业。 
- uniauth技术体系，它基于jdk7,使用spring mvc4.2+mybatis3+spring security4+cas+cxf作为基础框架，数据库为mysql5.1，配置管理使用zookeeper3.4，利用redis保存ticket和session信息；

<a name='quickstart'/>
## 二. 快速开始
###1 .编译打包
在源码目录执行命令
``` shell 
gradle warUniAuthServer、 gradle warCas、 gradle warTechopsWebsite, 
```
分别可以在uniauth-server,cas,techops项目的build路径下得到uniauth.war 、  cas.war、  techops.war；
###2 .初始化数据库
在mysql中`create database uniauth`，然后顺序执行`uniauth-server/src/script/sql`  中所有的脚本；
管理员账号：` shuanggui.fan@dianrong.com `，密码： `$1234qweR`
###3 .配置tomcat7
- **添加环境参数**
修改tomcat bin目录中` catalina.sh`文件，在正文首行后加入下面环境参数：
``` shell
JAVA_OPTS="$JAVA_OPTS -Dcatalina.logs=/var/lib/tomcat/logs"  # log output
DR_CFG_ZOOKEEPER_ENV_URL="10.8.12.85:2181,10.8.12.85:2182,10.8.12.85:2183"  # zookeeper cluster address
export DR_CFG_ZOOKEEPER_ENV_URL 
```
- **修改端口**
将上面tomcat复制粘贴三份，分别叫做 tomcat_uniauth、  tomcat_cas、 tomcat_techops,分别修改server.xml中监听端口，假定分别是：8080，8081，8082，
**注意**：同时修改其他shutdown，ajp端口
- **uniauth添加jndi数据源**
``` xml
<Resource name="jdbc/uniauth" auth="Container" type="javax.sql.DataSource" driverClassName="com.mysql.jdbc.Driver" url="jdbc:mysql://localhost:3306/uniauth?useUnicode=true&amp;characterEncoding=utf8" username="root" password="root" maxActive="50" maxIdle="20" maxWait="200" />
```

###4.配置zookeeper
在zokeeper中`create`下面节点数据
```shell
/com/dianrong/cfg/1.0.0/uniauth/cas_server.iscookiesecure false   // 线上环境一定是true
/com/dianrong/cfg/1.0.0/uniauth/cas_server http://localhost:8081/cas // cas服务器的部署base地址
/com/dianrong/cfg/1.0.0/uniauth/domains.techops http://localhost:8082/techops // techops的部署base地址
/com/dianrong/cfg/1.0.0/uniauth/uniauth_ws_endpoint http://localhost:8080/uniauth/ws/rs  // uniauth的webservice endpoint地址， base+/ws/rs
```
zookeeper的配置根路径为`/com/dianrong/cfg/1.0.0`, 该默认路径可通过在catalina.sh中 `export DR_CFG_ZOOKEEPER_BASE_PATH`变量来更改
###5.启动应用

<a name='architecture' />
## 三. 系统结构
- 模块组件依赖
![uniauth模块依赖图.png](http://upload-images.jianshu.io/upload_images/4122900-04105bd9cba75464.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
如上图，uniauth的核心模块：`uniauth-server`，`common`，`share-rw`，`ss-client`,`cas`,`techops`。
三个独立应用：
（1）`uniauth-server`是提供数据访问的`REST`服务；所有的用户，资源，权限通过得到；
（2）`techops`是web界面的管理控制台；通过`techops`录入资源，角色，做权限分配；
（3）`cas`是单点登录系统；各个*subsystem*通过cas做sso认证；
三个关键模块：
（4）`common`定义了**读**uniauth-server的接口，接口的数据模型以及cxf方式的访问实现；由它定义了数据接口，因此各个模块必须依赖它；
（5）`share-rw`提供对`uniauth-server`中**写**功能的访问接口定义  ，cxf方式访问实现，由于牵涉到对数据的修改，因此只有管理系统才能依赖这个模块，如`techops`
（6）`ss-client`是uniauth提供的sdk，各子系统依赖它后只需要少量的配置就可以完成sso,authentication 和 authorization的业务
（7）*subsystem*代表各个子系统,也是uniauth的客户端，各个需要用到sso,authentication和authorization的系统，比如`techops`。

- 系统架构
![uniauth components.png](http://upload-images.jianshu.io/upload_images/4122900-c9db4273269a3aaa.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

<a name='configuration'/>
## 四. 参数配置

<a name='integration'/>
## 五. 系统接入