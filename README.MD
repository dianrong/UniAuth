## 1. 关于UNIAUTH
- uniauth干什么的，它是一个统一登录+鉴权+权限管理的综合系统；它非常适用于多子系统环境，且各系统需要通过统一认证/授权，并能根据既定的角色和权限来规范业务操作范围的企业。 
- uniauth技术体系，它基于jdk7,使用spring mvc4.2+mybatis+spring security4+cas+cxf作为基础框架，数据库为mysql5.1，配置管理使用zookeeper3.4，利用redis保存ticket和session信息；
## 2. 快速开始
## 3. 系统结构
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

## 4. 参数配置
## 5. 系统接入