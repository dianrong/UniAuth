* [一. 关于UNIAUTH](#about)
* [二. 快速开始](#quickstart)
* [三. 系统结构](#architecture)
* [四. 参数配置](#configuration)
* [五. 系统接入](#integration)
* [六. SDK使用](#sdk)
* [七. 系统说明](#attention)

Uniauth项目的开源QQ交流群号: 463056564  欢迎您的加入。
Uniauth项目github开源代码: https://github.com/dianrong/UniAuth

<a id='about'/>

## 一. 关于UNIAUTH

### 1.1.uniauth干什么的

它是一个统一登录+鉴权+权限管理的综合系统，它适用于各系统需要通过统一认证/授权，并能根据既定的角色和权限来规范业务操作的多子系统环境。

### 1.2.uniauth技术选型

基于`jdk7`,使用`spring mvc4.2+mybatis3+spring security4+cas+cxf`作为基础框架，数据库为`mysql5.7`，配置管理使用`zookeeper3.4`，`redis`用于存储ticket，缓存使用，前端使用`angularJS 1.x`；

<a id='quickstart'/>

## 二. 快速开始

### 2.1.编译打包

在源码目录执行命令 (gradle版本`2.12`)

``` shell
gradle warUniAuthServer
gradle warCas
gradle warTechopsWebsite
```

分别可以在uniauth-server,cas,techops项目的build路径下得到`uniauth.war` 、 `cas.war`、 `techops.war`；

### 2.2.初始化数据库

在mysql中`create database uniauth`，然后按rt顺序执行`uniauth-server/src/script/sql`中的脚本。为了方便执行初始化脚本，某些版本中将多个rt的脚本集合到一起，比如：rt115-rt176-all-in-one.sql，代表将rt115到rt176的脚本集合在了一个脚本文件中。同一个rt的sql，ddl先于dml执行。
管理员账号：`first.admin@test.com`，密码： `$1234qweR`

### 2.3.配置`tomcat7`

#### (1) **添加环境参数**

##### linux环境

修改tomcat bin目录中`catalina.sh`文件，在正文首行后加入下面环境参数：
``` shell
# log path
JAVA_OPTS="$JAVA_OPTS -Dcatalina.logs=/var/lib/tomcat/logs"

# zookeeper cluster address (zookeeper集群不是必须的, 可单节点)
JAVA_OPTS="$JAVA_OPTS -DR_CFG_ZOOKEEPER_ENV_URL=10.8.12.85:2181,10.8.12.85:2182,10.8.12.85:2183"
```

##### windows环境

修改tomcat bin目录中`catalina.bat`文件，可在`set "JAVA_OPTS=%JAVA_OPTS% %LOGGING_CONFIG%"`这一命令下加入下面环境参数：
``` bat
rem log path
set APPLICATION.LOGS=-Dcatalina.logs=D:\logs\tomcat\tomcat8080
set "JAVA_OPTS=%JAVA_OPTS% %APPLICATION.LOGS%"

rem zookeeper cluster address (zookeeper集群不是必须的, 可单节点)
set DR_CFG_ZOOKEEPER_ENV_URL=-DDR_CFG_ZOOKEEPER_ENV_URL=10.8.12.85:2181,10.8.12.85:2182,10.8.12.85:2183
set "JAVA_OPTS=%JAVA_OPTS% %DR_CFG_ZOOKEEPER_ENV_URL%"
```

#### (2) **修改端口**

将上面tomcat复制粘贴三份，分别叫做 tomcat_uniauth、 tomcat_cas、 tomcat_techops,分别修改`server.xml`中监听端口，假定分别是：8080，8081，8082，
**注意**：同时修改其他shutdown，ajp端口

#### (3) **uniauth添加jndi数据源**

在tomcat_uniauth的`context.xml`中

``` xml
<Resource name="jdbc/uniauth" auth="Container" type="javax.sql.DataSource" driverClassName="com.mysql.jdbc.Driver" url="jdbc:mysql://localhost:3306/uniauth?useUnicode=true&amp;characterEncoding=utf8" username="root" password="root" maxActive="50" maxIdle="20" maxWait="200" />
```

### 2.4.配置zookeeper(必要配置)

在zokeeper中`create`以下节点(当然需要先创建`/com`，`/com/dianrong`，`/com/dianrong/cfg`，`/com/dianrong/cfg/1.0.0`，`/com/dianrong/cfg/1.0.0/uniauth`这5个父节点)

``` shell
# 线上环境一定是true
/com/dianrong/cfg/1.0.0/uniauth/cas_server.iscookiesecure false

# cas服务器的部署base地址
/com/dianrong/cfg/1.0.0/uniauth/cas_server http://localhost:8081/cas

# techops的部署base地址
/com/dianrong/cfg/1.0.0/uniauth/domains.techops http://localhost:8082/techops

# uniauth的webservice endpoint地址，base+/ws/rs
/com/dianrong/cfg/1.0.0/uniauth/uniauth_ws_endpoint http://localhost:8080/uniauth/ws/rs

# 关闭uniauth-server的api访问控制
/com/dianrong/cfg/1.0.0/uniauth/apicall.check.switch false

# 指定redis密码，如果没有密码则不配置该节点
/com/dianrong/cfg/1.0.0/uniauth/redis.password pwd

# 采用redis的类型.可配置值为:SINGLE(默认值),CLUSTER,SENTINEL
/com/dianrong/cfg/1.0.0/uniauth/redis.type SINGLE
```
根据redis.type的配置值，还需要配置redis的其他配置值，[具体请查看](#redis-config)。

zookeeper的配置根路径为`/com/dianrong/cfg/1.0.0`, 该默认路径可通过设置环境变量DR_CFG_ZOOKEEPER_BASE_PATH来更改，同时以上节点路径都需要做相应的修改。

* linux环境：   在catalina.sh中设置 `export DR_CFG_ZOOKEEPER_BASE_PATH=/newPath`
* windows环境： 在catalina.bat中设置 `set DR_CFG_ZOOKEEPER_BASE_PATH=/newPath`

### 2.5.部署启动

按下面顺序启动

1. 启动tomcat_uniauth

    将`uniauth.war`部署到tomcat_uniauth 的webapps目录，启动tomcat

2. 启动tomcat_cas

    将`cas.war`部署到tomcat_cas 的webapps目录，启动tomcat

3. 启动tomcat_techops

    将`techops.war`部署到tomcat_techops 的webapps目录，启动tomcat

访问[管理控制台techops](http://localhost:8082/techops)

<a id='architecture' />

## 三. 系统结构

### 3.1.模块组件依赖

![uniauth模块依赖图.png](http://upload-images.jianshu.io/upload_images/4122900-04105bd9cba75464.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

如上图，uniauth的核心模块：`uniauth-server`，`common`，`share-rw`，`ss-client`, `cas`, `techops`。

- 三个独立应用：

  (1) `uniauth-server`是提供数据访问的`REST`服务；用户，资源，权限数据通过它进行访问；

  (2) `techops`是web界面的管理控制台；通过`techops`录入资源，角色，做权限分配；

  (3) `cas`是单点登录系统；各个*subsystem*通过cas做身份认证，token生成，发布；

- 三个关键模块：

  (4) `common`定义了**读**uniauth-server的接口，接口的数据模型以及cxf方式的访问实现；由它定义了数据接口，因此各个模块必须依赖它；

  (5) `share-rw`提供对`uniauth-server`中**写**功能的访问接口定义，cxf方式访问实现，由于牵涉到对数据的修改，因此只有管理系统才能依赖这个模块，如`techops`

  (6) `ss-client`是uniauth提供的sdk，各子系统依赖它后只需要少量的配置就可以完成sso,authentication 和 authorization的业务

  (7) *subsystem*代表各个子系统,也是uniauth的客户端，各个需要用到sso,authentication和authorization的系统，比如`techops`。

### 3.2.系统架构

![组件调用关系](http://upload-images.jianshu.io/upload_images/4122900-c9db4273269a3aaa.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

上图中分两条线

1. step 1-7, include sso and authentication

    Between step 7 and 8,the `uniauth client` send request to cas server for authentication user with `st` (get from step 6) then the client can get `credentials`(* include `username` *) from response data.

2. step 8 , include authorization

    After got `credentials`, `uniauth client`  call `uniauth-server` to get user details and all permissions.

<a id='configuration'/>

## 四. 参数配置

目前所有参数都是配置在`zookeeper`中

### 4.1.redis相关配置

#### 通用配置:

``` shell
# redis的数据库index，默认为0。
/com/dianrong/cfg/1.0.0/uniauth/redis.database

# 指定redis密码，如果没有密码则不配置该节点。
/com/dianrong/cfg/1.0.0/uniauth/redis.password

# 连接超时毫秒数，默认为3000。
/com/dianrong/cfg/1.0.0/uniauth/redis.timeout

# 采用redis的类型，可配置值为：SINGLE（默认值），CLUSTER，SENTINEL。
/com/dianrong/cfg/1.0.0/uniauth/redis.type
```

<a id='redis-config'/>

#### 根据配置的redis.type来配置redis

##### a 单节点模式

``` shell
# redis的host。
/com/dianrong/cfg/1.0.0/uniauth/redis.host

# redis的port。
/com/dianrong/cfg/1.0.0/uniauth/redis.port              
```

##### b 哨兵模式

``` shell
# 指定master节点name。
/com/dianrong/cfg/1.0.0/uniauth/redis.master

# 指定 redis哨兵的节点列表，例如：10.18.19.67:5000,10.18.19.51:5000,10.18.19.101:5000。
/com/dianrong/cfg/1.0.0/uniauth/redis.sentinels
```

##### c 集群模式

``` shell
# 集群中请求失败之后，最多的转发次数，默认为:5。
/com/dianrong/cfg/1.0.0/uniauth/redis.maxRedirects

# 指定redis集群节点列表，例如：10.18.19.67:5000,10.18.19.51:5000,10.18.19.101:5000。
/com/dianrong/cfg/1.0.0/uniauth/redis.clusters            
```

### 4.2. cas配置

``` shell
# 是否采用https的形式写cookie，默认为false。
/com/dianrong/cfg/1.0.0/uniauth/cas_server.iscookiesecure

# 配置的uniauth-server的地址，比如：http://localhost:8090/uniauth/ws/rs。
/com/dianrong/cfg/1.0.0/uniauth/uniauth_ws_endpoint

# cas的地址，比如：http://localhost:8080/cas。
/com/dianrong/cfg/1.0.0/uniauth/cas_server

# cas服务器内部访问地址，不走dns域名解析的地址。一般配置为ip+port，比如:http://192.168.1.1:8080/cas。
/com/dianrong/cfg/1.0.0/uniauth/cas_server.internal_address

# 配置cas的service ticket可验证的次数，默认是2次，正常情况下1次是最安全的。
/com/dianrong/cfg/1.0.0/uniauth/cas.st_use_times

# 是否将短信和邮箱验证码返回给前端，可配置为true或false，默认为false。该配置参数一般配置在开发和测试环境，方便快速验证通过。
/com/dianrong/cfg/1.0.0/uniauth/cas.verify_code.show

# 配置true或false，默认为false。是否部署多个cas，将ticket存储在redis中。
/com/dianrong/cfg/1.0.0/uniauth/cas.iscluster
```

#### 覆盖cas默认的webflow，tgc生成秘钥

这些配置主要是为了安全着想(optional)，[参考文档](https://apereo.github.io/2016/04/08/commonsvulndisc/)。

``` shell
# cas中webflow的加密key
/com/dianrong/cfg/1.0.0/uniauth/cas.webflow.encryption.key

# cas中webflow的签名的key
/com/dianrong/cfg/1.0.0/uniauth/cas.webflow.signing.key

# cas中tgc加密的key
/com/dianrong/cfg/1.0.0/uniauth/cas.tgc.encryption.key

# cas中tgc的签名的key
/com/dianrong/cfg/1.0.0/uniauth/cas.tgc.signing.key
```

```code
这四个节点的值通过下面这个工具生成，具体步骤：
a. git clone https://github.com/mitreid-connect/json-web-key-generator.git
b. 切换到json-web-key-generator目录
c. 使用maven命令 mvn package
d. 切换到json-web-key-generator目录下的 target目录
e. 分别执行
java -jar json-web-key-generator-0.4-SNAPSHOT-jar-with-dependencies.jar -t oct -s 256
java -jar json-web-key-generator-0.4-SNAPSHOT-jar-with-dependencies.jar -t oct -s 512
java -jar json-web-key-generator-0.4-SNAPSHOT-jar-with-dependencies.jar -t oct -s 96
java -jar json-web-key-generator-0.4-SNAPSHOT-jar-with-dependencies.jar -t oct -s 512
得到四个k，按顺序赋值给上面的四个zookeeper节点。
```

#### 配置cas生成jwt的秘钥对

非必须配置，但是为了安全着想，还是重新生成一份来配置。

``` shell
# 生成jwt的私钥
/com/dianrong/cfg/1.0.0/uniauth/authentication.jwt.private.key

# 验证jwt的公钥
/com/dianrong/cfg/1.0.0/uniauth/authentication.jwt.public.key

# jwt存放的cookie的后缀。默认jwt存放的cookie为: uniauth_jwt. 如果配置了后缀：_dev，则存放jwt的cookie为：uniauth_jwt_dev。
/com/dianrong/cfg/1.0.0/uniauth/authentication.jwt.cookie.suffix

# jwt存放的cookie的域。默认情况下jwt只存放在当前cas所在的域，可以设置类似.dianrong.com这样的域，使得jwt能够跨域被访问。
/com/dianrong/cfg/1.0.0/uniauth/authentication.jwt.cookie.domain

# 是否是https才设置cookie，默认为false。
/com/dianrong/cfg/1.0.0/uniauth/authentication.jwt.cookie.secure
```

### 4.3. 集成系统配置

下面的`xxx`就是定义好的`domain`，如techops,crm,pms,cms,etc。

``` shell
# xxx系统的地址。比如：http://localhost:8100/techops/
/com/dianrong/cfg/1.0.0/uniauth/domains.xxx

# xxx系统自定义登陆页面地址。
/com/dianrong/cfg/1.0.0/uniauth/domains.xxx.loginPage

# 如果xxx系统在进行st认证失败的时候跳转到失败页面（需要客户端集成做一些配置）。
/com/dianrong/cfg/1.0.0/uniauth/domains.xxx.auth_fail_url

# 配置true或false，默认为false。自定义登录页面的系统(配置了domains.xxx.loginPage)是否在cas统一登录页面的下拉框中展示。
/com/dianrong/cfg/1.0.0/uniauth/domains.xxx.showInHomePage

# 指定xxx系统的统一登出地址。可配置为内网地址，更稳定速度更快。如不配，则根据domains.xxx地址进行统一登出。
/com/dianrong/cfg/1.0.0/uniauth/domains.xxx.logout_address

# 指定ticket验证失败之后的跳转地址（）需要使用uniauth提供的SSAuthenticationFailureHandler作为CasAuthentication的authenticationFailureHandler）
/com/dianrong/cfg/1.0.0/uniauth/domains.xxx.auth_fail_url  
```

### 4.4. 配置邮箱

``` shell
# 邮箱服务器host，比如：smtp-dev.sl.com（默认值。
/com/dianrong/cfg/1.0.0/uniauth/internal.mail.smtp.host

# 邮箱服务器port，比如：25（默认值）。
/com/dianrong/cfg/1.0.0/uniauth/internal.mail.smtp.port

# 系统邮件的发送者，比如：TechOps-Notification<noreply@dianrong.com>（默认值）。
/com/dianrong/cfg/1.0.0/uniauth/internal.mail.smtp.femail  
```

### 4.5. uniauth-Server 配置

``` shell
# uniauth-server使用redis缓存指定的database。如果不指定该值，则以redis.database配置为准。
/com/dianrong/cfg/1.0.0/uniauth/redis.uniauth.database

# 配置true或false，默认值为true。在Uniauth-server中是否使用redis作为缓存实现。
/com/dianrong/cfg/1.0.0/uniauth/redis.uniauth.use_redis

# uniauth-server使用的redis缓存的过期秒数，默认为3600。
/com/dianrong/cfg/1.0.0/uniauth/redis.uniauth.expiration  

# 配置true或false，默认为true。用于指定是否开启uniauth-server api访问的权限控制。
/com/dianrong/cfg/1.0.0/uniauth/apicall.check.switch

# 配置api访问控制采用jwt的秘钥。如apicall.check.switch配置为false，则不需要配置。
/com/dianrong/cfg/1.0.0/uniauth/apicall.jwt.security.key

# 配置api访问控制techops的密码字符串
/com/dianrong/cfg/1.0.0/uniauth/apicall.techops.pwd

# 配置true或false，默认为false。用于指定是否强制api访问的租户标识信息，如果不验证，则采用默认的dianrong作为租户标识。
/com/dianrong/cfg/1.0.0/uniauth/tenancyIdentity.check.switch

# swaggger配置，兼容uniauth-server发布之后，带有contextpath处理有问题。
/com/dianrong/cfg/1.0.0/uniauth/uniauth.contextpath

#  说明uniauth-server api的联系人名单。
/com/dianrong/cfg/1.0.0/uniauth/uniauth.server.api.contact  
```

### 4.6. 其他配置

``` shell
# 配置为true或false，默认为false。指定是否采用旧的GlobalVarQueue实现。
/com/dianrong/cfg/1.0.0/uniauth/global.var.queue.old.impl
```

<a id='integration'/>

## 五. 系统接入指南

### 5.1.Spring系统

#### 5.1.1非Spring boot系统

客户端系统需要使用[spring security](http://projects.spring.io/spring-security/)，引入`ss-client`模块的jar包
下面以`techops`项目为例

##### (1) web.xml

``` xml
<!-- spring session publisher -->
<listener>
    <listener-class>org.springframework.security.web.session.HttpSessionEventPublisher</listener-class>
</listener>
<!-- In generally  Spring Security Filter order first before other filters  -->
<filter>
    <filter-name>springSecurityFilterChain</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>
<filter-mapping>
    <filter-name>springSecurityFilterChain</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

##### (2) applicationContext.xml

不要把下面这一段配置放到SpringMVC对应的xml当中，必须放置到Spring Bean的xml当中。

```xml
<import resource="classpath:ss-uniauth-client.xml"/> //导入ss-client中的Spring配置文件

<bean id="domainDefine" class="com.dianrong.common.uniauth.common.client.DomainDefine">
    <property name="domainCode" value="techops"/>
    <!-- <property name="userInfoClass" value="com.dianrong.common.techops.sscustom.TechOpsUserExtInfo"/> -->
    <property name="rejectPublicInvocations" value="false"/>
    <property name="innerCacheUseRedis" value="true"/>
</bean>

<sec:http entry-point-ref="casAuthEntryPoint" use-expressions="true"
          request-matcher="ant" security-context-repository-ref="uniauthSecurityContextRepository"
          disable-url-rewriting="false">
    <sec:headers>
        <sec:frame-options disabled="true"/>
    </sec:headers>
    <sec:custom-filter ref="authenticationFilter"
                       position="CAS_FILTER"/>
    <sec:custom-filter after="EXCEPTION_TRANSLATION_FILTER"
                       ref="exceptionTranslationFilter"/>
    <sec:custom-filter ref="singleLogoutFilter" before="CAS_FILTER"/>
    <sec:custom-filter ref="requestSingleLogoutFilter"
                       before="LOGOUT_FILTER"/>
    <sec:csrf disabled="true"/>
    <sec:custom-filter position="CONCURRENT_SESSION_FILTER"
                       ref="concurrencyFilter"/>
    <sec:session-management session-authentication-strategy-ref="sas"
                            invalid-session-url="#{uniauthConfig['cas_server']}/login?service=#{uniauthConfig['domains.'+domainDefine.domainCode]}/login/cas"/>
    <sec:logout delete-cookies="JSESSIONID"/>
    <!-- 在此处可以添加业务系统常用的稳定的intercept-url定义 -->
    // 重要：如果你的系统基于url_pattern进行权限判断，应尽可能地将需要保护的url进行分解和登记，
    // 否则就会退而求其次地选用这条规则，因为这条定义是涵盖任何url的默认定义，即登录用户能访问所有url。
    <sec:intercept-url pattern="/**" access="isAuthenticated()"/>
</sec:http>

<!-- do not change: start -->
<bean id="exceptionTranslationFilter" class="com.dianrong.common.uniauth.client.custom.filter.SSExceptionTranslationFilter">
    <constructor-arg ref="casAuthEntryPoint"/>
    <property name="accessDeniedHandler">
        <bean class="org.springframework.security.web.access.AccessDeniedHandlerImpl">
            <property name="errorPage" value="/errors/403.jsp"/>
        </bean>
    </property>
</bean>

<bean id="concurrencyFilter" class="org.springframework.security.web.session.ConcurrentSessionFilter">
    <constructor-arg index="0" ref="sessionRegistry"/>
    <constructor-arg index="1" value="#{uniauthConfig['cas_server']}/logout?dupsession=true"/>
</bean>

<bean id="sessionRegistry" class="org.springframework.security.core.session.SessionRegistryImpl"/>

<bean id="sas" class="org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy">
    <constructor-arg>
        <list>
            <bean class="org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy">
                <constructor-arg ref="sessionRegistry"/>
            </bean>
            <bean class="org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy">
                <constructor-arg ref="sessionRegistry"/>
                <property name="maximumSessions" value="1"/>
                <property name="exceptionIfMaximumExceeded" value="false"/>
            </bean>
        </list>
    </constructor-arg>
</bean>

<!--AuthenticationFilter -->
<bean id="authenticationFilter" class="com.dianrong.common.uniauth.client.custom.filter.DelegateUniauthAuthenticationFilter">
    <constructor-arg>
        <list>
            <ref bean="casAuthenticationFilter"/>
            <ref bean="jwtAuthenticationFilter"/>
            <ref bean="basicAuthAuthenticationFilter"/>
        </list>
    </constructor-arg>
    <constructor-arg value="#{domainDefine.authenticationTypeList}"/>
</bean>

<!-- CAS方式登陆成功之后,验证ST失败的处理Handler -->
<bean id="ssAuthenticationFailHandler" class="com.dianrong.common.uniauth.client.custom.handler.SSAuthenticationFailureHandler"/>

<bean id="jwtAuthenticationFilter" class="com.dianrong.common.uniauth.client.custom.filter.UniauthJWTAuthenticationFilter">
    <constructor-arg index="0" ref="uniauthJWTSecurity"/>
    <constructor-arg index="1" ref="jwtQuery"/>
    <property name="authenticationManager" ref="authenticationManager"/>
    <property name="authenticationSuccessHandler" ref="ssAuthenticationSuccessHandler"/>
    <property name="authenticationFailureHandler" ref="jwtAuthenticationFailureHandler"/>
    <property name="sessionAuthenticationStrategy" ref="sas"/>
</bean>

<bean id="casAuthenticationFilter" class="com.dianrong.common.uniauth.client.custom.filter.UniauthCasAuthenticationFilter">
    <property name="authenticationManager" ref="authenticationManager"/>
    <property name="filterProcessesUrl" value="/login/cas"/>
    <property name="authenticationSuccessHandler" ref="ssAuthenticationSuccessHandler"/>
    <property name="authenticationFailureHandler" ref="ssAuthenticationFailHandler"/>
    <property name="sessionAuthenticationStrategy" ref="sas"/>
</bean>

<bean id="basicAuthAuthenticationFilter" class="com.dianrong.common.uniauth.client.custom.filter.UniauthBasicAuthAuthenticationFilter">
    <property name="authenticationManager" ref="authenticationManager"/>
    <property name="sessionAuthenticationStrategy" ref="sas"/>
</bean>

<!-- 提供两个jwt获取的方式,可配置header和parameter名来自定义参数名 -->
<bean class="com.dianrong.common.uniauth.client.custom.jwt.RequestHeaderJWTQuery"/>
<bean class="com.dianrong.common.uniauth.client.custom.jwt.RequestParameterJWTQuery"/>
<!-- do not change: end -->
<!-- 注册PermissionEvaluator实现，以便支持hasPermission表达式，如果不使用则不用配置
     注意：ID必须为uniauthPermissionEvaluator，且primary="true" -->
<bean id="uniauthPermissionEvaluator" class="com.dianrong.common.techops.sscustom.TechOpsPermissionEvaluator" primary="true"></bean>
```

##### (3) 获取当前登录用户

可以通过调用ssclient中LoginUserInfoHolder的getLoginUserInfo()方法获取当前登录用户对象。

```java
com.dianrong.common.uniauth.client.custom.LoginUserInfoHolder.getLoginUserInfo();
```

#### 5.1.1.2 Spring Boot接入

如果集成的系统使用的是Spring Boot，需要配置两部分，系统自定义部分和uniauth提供的模版配置。

##### (1) 系统自定义配置(可以替换为java配置)

```xml
<!--引入uniauth的xml配置-->
<import resource="classpath:ss-uniauth-client.xml" />
<bean id="domainDefine" class="com.dianrong.common.uniauth.common.client.DomainDefine">
 <property name="domainCode" value="springboot-ssclient"/>
 <property name="userInfoClass" value="com.dianrong.uniauth.ssclient.bean.SSClientUserExtInfo"/>
 <property name="rejectPublicInvocations" value="false"/>
 <property name="customizedLoginRedirecUrl"  value="/content"/>
</bean>

<bean id="sessionRegistry" class="org.springframework.security.core.session.SessionRegistryImpl" />
<bean id="sas" class="org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy">
 <constructor-arg>
  <list>
   <bean
    class="org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy">
    <constructor-arg ref="sessionRegistry" />
   </bean>
   <bean
    class="org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy">
    <constructor-arg ref="sessionRegistry" />
    <property name="maximumSessions" value="1" />
    <property name="exceptionIfMaximumExceeded" value="false" />
   </bean>
  </list>
 </constructor-arg>
</bean>
<bean id="uniauthPermissionEvaluator" class="com.dianrong.uniauth.ssclient.bean.SSClientPermissionEvaluator" primary="true"/>
```

说明：该配置相对于原来的配置方式，主要是将filter的配置以及spring security的配置给删掉了。

##### (2) 集成系统引入Uniauth的Spring Security代码配置,继承Uniauth提供的模版UniauthSecurityConfig。

```java
@Configuration
public class WebSecurityConfiguration extends UniauthSecurityConfig {
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/favicon.ico");
    }
}
```

说明：模版配置全部在父类的configure(final HttpSecurity http)中，所以如果重写该方法，不要忘记了调用super.configure(http)。UniauthSecurityConfig提供了很多模版方法，集成子系统可以通过重写来自定义配置。

```java
/**
 * 构造session错误跳转的url. #{uniauthConfig['cas_server']}/login?
 * service=#{uniauthConfig['domains.'+domainDefine.domainCode]}/login/cas
 */
protected String getInvalidSessionUrl() {
  String invalidSessionUrl = uniauthConfig.get("cas_server") + "/login?service="
      + uniauthConfig.get("domains." + domainDefine.getDomainCode()) + "/login/cas";
  log.info("invalidSessionUrl is " + invalidSessionUrl);
  return invalidSessionUrl;
}


/**
 * 获取SessionAuthenticationStrategy.
 */
protected SessionAuthenticationStrategy getSessionAuthenticationStrategy() {
  return this.sas;
}

/**
 * 获取AuthEntryPoint.
 */
protected AuthenticationEntryPoint getAuthEntryPoint() {
  return this.casAuthEntryPoint;
}

/**
 * 获取SecurityContextRepository.
 */
protected SecurityContextRepository getSecurityContextRepository() {
  return this.securityContextRepository;
}


/**
 * 获取CasAuthenticationFilter.
 *
 * @return 返回结果可为空, 为空则不配置该filter.
 */
protected UniauthAbstractAuthenticationFilter getCasAuthenticationFilter() {
  return beanCreator.create(UniauthCasAuthenticationFilter.class);
}

/**
 * 获取JWTAuthenticationFilter.
 *
 * @return 返回结果可为空, 为空则不配置该filter.
 */
protected UniauthAbstractAuthenticationFilter getJWTAuthenticationFilter() {
  return beanCreator.create(UniauthJWTAuthenticationFilter.class);
}

/**
 * 获取BasicAuthAuthenticationFilter.
 *
 * @return 返回结果可为空, 为空则不配置该filter.
 */
protected UniauthAbstractAuthenticationFilter getBasicAuthAuthenticationFilter() {
  return beanCreator.create(UniauthBasicAuthAuthenticationFilter.class);
}

/**
 * 获取UniauthAuthenticationFilter.一般情况下该filter用于代理Uniauth中提供的所有的AuthenticationFilter.
 *
 * @return 返回结果可为空, 为空则不配置该filter.
 */
protected Filter getUniauthAuthenticationFilter() {
  ...
  return beanCreator.create(DelegateUniauthAuthenticationFilter.class, args);
}

/**
 * 获取ConcurrentSessionFilter.
 *
 * @return 返回结果可为空, 为空则不配置该filter.
 */
protected Filter getConcurrentSessionFilter() {
  return beanCreator.create(ConcurrentSessionFilter.class);
}

/**
 * 获取LogoutFilter.
 *
 * @return 返回结果可为空, 为空则不配置该filter.
 */
protected Filter getLogoutFilter() {
  return beanCreator.create(LogoutFilter.class);
}

/**
 * 获取SingleSignOutFilter.支持SSO,该filter必须要有.
 *
 * @return 返回结果可为空, 为空则不配置该filter.
 */
protected Filter getSingleSignOutFilter() {
  return beanCreator.create(SingleSignOutFilter.class);
}

/**
 * 获取ExceptionTranslationFilter.
 *
 * @return 返回结果可为空, 为空则不配置该filter.
 */
protected Filter getExceptionTranslationFilter() {
  return beanCreator.create(SSExceptionTranslationFilter.class);
}
```

#### 5.1.3 DomainDefine配置说明

集成uniauth的子系统都需要配置一个com.dianrong.common.uniauth.common.client.DomainDefine类型的bean，用于自定义子系统的各种配置。DomainDefine中的各种配置说明如下：

```config
# 指定当前集成系统的域名code，该code与zk和techops的域名模块中配置的域名code要一致。通过该code,uniauth能找到该域的配置信息
domainCode

# 指定集成系统登录成功之后返回的登录用户对象实现, 该实现必须集成自uniauth提供的UserExtInfo。其中有uniauth自动填充的用户信息，权限信息等。
userInfoClass

# 指定是否拒绝请求任何未明确配置权限控制的资源。比如：aaaa/bbbb/ccc这个请求没有添加任何spring-security的权限控制(hasRole这种)，默认是可以请求到的。但是如果将参数设置为true，那请求就会被拒绝。
rejectPublicInvocations

# 指定通过访问子系统跳转到统一登录页面，登录成功之后跳转的地址，可为绝对地址或相对地址，需要与`customizedLoginRedirecUrl`相区别。
customizedSavedRequestUrl

# 直接访问统一登录页面，登录成功之后的跳转地址，需要与`customizedSavedRequestUrl`相区别。
customizedLoginRedirecUrl

# uniauth自动起效的权限控制类型。
# 可选配置值为:
# URI_PATTERN（默认） 主动加载Techops中配置的UR_PATTERN类型权限进行权限控制
# REGULAR_PATTERN 主动加载Techops中配置的REGULAR_PATTERN类型权限进行权限控制
# ALL URI_PATTERN和REGULAR_PATTERN同时起效
# NONE URI_PATTERN和REGULAR_PATTERN都不起效
controlType

# 配置集成系统支持的认证方式集合。可以通过','来分隔连接配置，以达到同时支持多种认证方式的目的. 比如: CAS,BASIC_AUTH，表示同时支持CAS和BasicAuth两种方式。
# 可选配置:
# CAS 支持通过Cas颁发的ServiceTicket的方式登录系统
# JWT 支持通过JWT的方式登录系统
# BASIC_AUTH 支持通过BasicAuth的方式访问系统
# ALL 三种方式同时支持.等同于配置CAS,JWT,BASIC_AUTH.
authenticationType

# ssclient的内部缓存是否使用Redis作为实现。
# 目前缓存有两种实现:
# 1 基于HashMap的内存实现。默认选择(innerCacheUseRedis = false)
# 2 使用Redis作为缓存实现。(innerCacheUseRedis = true)
# 如果该配置为true,则需要考虑配置参数:innerCacheRedisConfiguration.
innerCacheUseRedis

# 当innerCacheUseRedis=true时，配置redis的连接参数。
innerCacheRedisConfiguration
```

##### 5.1.3.1 innerCacheRedisConfiguration配置项说明

```config
# redis的连接类型。有三个选项：SINGLE 单节点(默认), CLUSTER 集群, SENTINEL 哨兵模式.
type

# 使用的数据库的DataBase，默认为0。
database

# 连接Redis的密码。
password

# 连接超时的毫秒数，默认为3000。
timeout

# 单节点模式下Redis的Host。
host

# 单节点模式下Redis的端口。
port

# 哨兵模式的Redis的master节点名称。
master

# 哨兵模式下,哨兵的节点集合。比如配置:127.0.0.1:8081,127.0.0.1:8082,127.0.0.1:8083。
sentinels

# 集群模式下，集群节点集合。
clusters

# 集群模式下，客户端请求失败之后，转发的最大次数，默认为5。
maxRedirects
```

### 5.2.非Spring系统

#### 5.2.1 与CAS集成

##### (1) 引入Cas jar包

jar包：'org.jasig.cas.client:cas-client-core:3.4.1'

##### (2) 添加filter

说明：需要添加三个filter，SingleSignOutFilter（统一登出filter），AuthenticationFilter（统一认证），Cas20ProxyReceivingTicketValidationFilter（ticket验证filter）

使用方式举例（以jetty内置服务器为例，其他tomcat容器的配置类似）

```java
// first
FilterHolder singleSignOutFilter = new FilterHolder(SingleSignOutFilter.class);
singleSignOutFilter.setInitParameter("casServerUrlPrefix", "http://localhost:8080/cas"); // 配置cas的统一地址cas_server
root.addFilter(singleSignOutFilter, "/*", Handler.DEFAULT);

// second
FilterHolder authenticationFilter = new FilterHolder(AuthenticationFilter.class);
authenticationFilter.setInitParameter("casServerLoginUrl", "http://localhost:8080/cas/login"); // 配置cas的登陆页面： cas_server/login
authenticationFilter.setInitParameter("gateway", Boolean.FALSE.toString()); // 默认采用false
authenticationFilter.setInitParameter("service", "http://localhost:8120/login/cas"); // 配置业务系统的地址：custom_url/login/cas. 后缀/login/cas 必须要，为了与统一登陆页面兼容。
root.addFilter(authenticationFilter, "/*", Handler.DEFAULT);

// third
FilterHolder ticketValidationFilter = new FilterHolder(Cas20ProxyReceivingTicketValidationFilter.class);
ticketValidationFilter.setInitParameter("service", "http://localhost:8120/login/cas"); // 配置业务系统的地址：custom_url/login/cas. 后缀/login/cas 必须要，为了与统一登陆页面兼容。如：https://passport-dev.dianrong.com
ticketValidationFilter.setInitParameter("casServerUrlPrefix", "http://localhost:8080/cas"); // 配置cas的统一地址cas_server
root.addFilter(ticketValidationFilter, "/*", Handler.DEFAULT);
```

注意：可以注意到添加filter的顺序，1 SingleSignOutFilter，2 AuthenticationFilter，3 ticketValidationFilter。

#### 5.2.2 登录成功之后可从session中获取登陆人Principal

```java
Assertion assertion = （Assertion）session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION)；
AttributePrincipal principal = assertion.getPrincipal(); // principal中有登陆人的相关信息，邮箱，service等。
```

说明：其中CONST_CAS_ASSERTION为字符串“_const_cas_assertion_”，可以直接使用AbstractCasFilter.CONST_CAS_ASSERTION获取。

#### 5.2.3 登陆用户信息获取

1. 首先通过第5.2.2步得到的AttributePrincipal对象。
2. 获取principal中的租户id，principal.getAttributes().get("tenancyid")，返回租户的id。该attribute还有其他属性：登录账号，用户主键id等。
3. call uniauth的api获取用户信息：

```java
// 构造登陆参数
LoginParam loginParam = new LoginParam();
emailloginParam.setAccount(account); // 登录账号
idloginParam.setTenancyId(tenancyId); // 账号所在租户的id

// 访问uniauth-server接口获取用户详情
Response<UserDetailDto> response = uniClientFacade.getUserResource().getUserDetailInfo(loginParam);
```

<a id='sdk'/>

## 六. SDK使用

`common`模块针对`uniauth-server`提供的Rest API进行了客户端封装，客户系统引入`com.dianrong.common.uniauth.common.client.UniClientFacade`即可获取人员，组，域，角色，权限等信息，具体参[UniClientFacade](http://xxx/com/dianrong/common/uniauth/common/client/UniClientFacade.java)看如下：

```java
public static void main(String args[]) {
    //这个构造函数中的字符串是uniauth-server的webservice endpoint
    UniClientFacade uniClientFacade = new UniClientFacade("http://localhost:8080/ws/rs");
    System.out.println(uniClientFacade.getConfigResource().getAllCfgTypes().getData());
}
```

<a id='attention'/>

## 七. 系统说明

### 7.1 Uniauth内置的3种PermissionType说明

1. **URI_PATTERN**：

    效果和Spring Security配置文件中的`<sec:intercept-url pattern="/xxx" access="hasAnyRole('ROLE_XXX','ROLE_YYY','ROLE_ZZZ')" />`一样，主要用于Web形式的URL资源定义。可用于保护真实URL访问地址，或者用于`<sec:authorize url="/xxx">`的标签中。

    需要尤其指明的是，如果你的多个role共享了一个role_code，则使用URL_PATTERN可能会有影响。

    注意：无论在Spring Security中还是在db中都可以定义url pattern的允许访问的http method，如GET，POST等等，如果不指定则该url默认支持所有类型的http method。比如在配置文件中：`<sec:intercept-url pattern="/admin/**" access="hasRole('ROLE_ADMIN')" **method="GET"**/>`在使用method定义时，这里面有一个注意点，就是Spring Security默认会优先比较请求的http method和定义中的是否匹配，如果不匹配则该条定义就直接被忽略掉了，哪怕url本身是匹配的。所以如果你要使用http method保护你的url，则应该把controller中需要受保护的url模式和可访问方法进行完整定义(在配置文件或db中)，这样才能起到应有的作用。

2. **DOMAIN**：

    TechOps使用的专有Permission Type，其他业务系统可以忽略此种类型的Permission。

3. **PRIVILEGE**：

    各个业务系统可以根据自己的需要定义权限控制点列表，这些权限用于保护任何需要保护的资源，比如通过SpEL表达式principal.hasPrivilege('xxx')。注意：不像` URI_PATTERN`，这种权限类型不参与Spring Security的内部权限控制机制，适用于纯业务级别权限控制点的定义。

4. **REGULAR_PATTERN**：

    根据正则表达式来匹配请求，然后进行权限判断。该类型与`URI_PATTERN`权限类型，区别是：`URI_PATTERN`是根据请求的url唯一匹配一个权限，然后判断当前用户是否有对应权限访问。但是`REGULAR_PATTERN`将请求url依次匹配当前登录用户的所有regular_pattern类型权限，如果其中某一个权限判定通过，则判断有权限访问。

### 7.2 Uniauth支持jwt

Uniauth对jwt有支持。Cas登录成功之后，不仅会生成service ticket，同时也会往浏览器的cookie中写入名叫uniauth_jwt的cookie。集成子系统如果检测到有jwt，就通过jwt来完成身份认证。

jwt与cas通过验证service ticket完成身份认证不同。cas的验证方式是对session有依赖的，所以集成子系统如果需要部署多实例，就需要做session共享处理。但是jwt不是，jwt对session没有依赖，完全是无状态的。

### 7.3 集成uniauth的子系统支持basic auth

如果集成Uniauth的子系统需要被其他系统访问Api，这怎么办呢？

Uniauth提供了通过Basic auth的认证方式。其他系统如果需要访问子系统Api，只需要通过basic auth的方式访问接口即可，免除了冗长的登录过程(访问cas，获取st或者jwt，然后再访问子系统)，同时也是无状态的。

#### 7.3.1 Uniauth中的账号类型

目前Uniauth中有两种账号：

* `普通账号` 即通过登录接口登录的用户账号，该类型账号3个月需要修改一次密码。
* `系统账号` 专门用于Basic auth的访问访问集成子系统，没有定时密码修改需求。

#### 7.3.2 Basic auth的访问方式

普通的Basic auth需要提供account，password即可完成认证。

Uniauth是有多租户的概念的，所以除了account和password之外，还需要一个租户标识，即tenancyCode。所以Uniauth的basic auth是需要提供tenancyCode，account，password。 只需要将tenancyCode和account拼凑为：tenancyCode:account作为账号传入即可。

例如：tenancyCode为dianrong，account为admin，则通过Basic auth访问需要传入的account为：dianrong:admin
