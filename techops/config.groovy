environments {
    local {
        frontend {
            apibase = 'http://192.168.56.101:8080/techops'
            projectBase = '/techops'
        }
        tomcat {
            tomcatAppName = 'techops'  //在Tomcat访问的application名字，配置在Tomcat的根目录
            tomcatUsername = 'admin'        //Tomcat用户
            tomcatPassword = 'admin'        //Tomcat密码
            tomcatHost = '192.168.56.101:8080'        //
            tomcatWarFile = 'build/' + tomcatAppName + '.war'
        }
    }
    dev {
        frontend {
            apibase = 'https://techops-dev.dianrong.com'
            projectBase = ''
        }
        tomcat {
            tomcatAppName = 'techops'  //在Tomcat访问的application名字，配置在Tomcat的根目录
            tomcatUsername = 'admin'        //Tomcat用户
            tomcatPassword = 'admin'        //Tomcat密码
            tomcatHost = '192.168.56.101:8080'        //
            tomcatWarFile = 'build/' + tomcatAppName + '.war'
        }
    }
    demo {
        frontend {
            apibase = 'https://techops-demo.dianrong.com'
            projectBase = ''
        }
        tomcat {
            tomcatAppName = 'techops'  //在Tomcat访问的application名字，配置在Tomcat的根目录
            tomcatUsername = 'admin'        //Tomcat用户
            tomcatPassword = 'admin'        //Tomcat密码
            tomcatHost = '192.168.56.101:8080'        //
            tomcatWarFile = 'build/' + tomcatAppName + '.war'
        }
    }
    pre {
        frontend {
            apibase = 'https://techops-pre.dianrong.com'
            projectBase = ''
        }
        tomcat {
            tomcatAppName = 'techops'  //在Tomcat访问的application名字，配置在Tomcat的根目录
            tomcatUsername = 'admin'        //Tomcat用户
            tomcatPassword = 'admin'        //Tomcat密码
            tomcatHost = '192.168.56.101:8080'        //
            tomcatWarFile = 'build/' + tomcatAppName + '.war'
        }
    }
    prod {
        frontend {
            apibase = 'https://techops.dianrong.com'
            projectBase = ''
        }
        tomcat {
            tomcatAppName = 'techops'  //在Tomcat访问的application名字，配置在Tomcat的根目录
            tomcatUsername = 'admin'        //Tomcat用户
            tomcatPassword = 'admin'        //Tomcat密码
            tomcatHost = '192.168.56.101:8080'        //
            tomcatWarFile = 'build/' + tomcatAppName + '.war'
        }
    }
}
