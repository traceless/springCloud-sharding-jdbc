# springCloud-sharding-jdbc
  This project is only for learning spring-cloud and sharding-jdbc

## 简介
  - 这个项目主要用于学习spring cloud和sharding-jdbc，以及可作为基础开发框架。里面实现的东西比较多，不建议新手用作为学习入门的项目。代码框架是用spring cloud搭建的，里面整合了sharding-jdbc的中间件，完整度比较高，有一些是企业开发所必备的基础组件。至于为什么选型这个中间件，我想是因为这个中间件相对轻一些。轻量级的框架意味着使用灵活，可改造性更强，支持二维路由。网上有很多关于选型的 sharding-jdbc mycat DRDS等各自的优缺点，这里我就不详细说明了。
  - 如果你完全不熟悉sharding-jdbc的话，建议先了解一下它的基本概念，使用方式。后面我会附上一篇结合这个项目的分库分表方案文章（分库分表有点大，三语两句说不完）。
  
## 项目框架
1. 项目目录如下图： 
  ![目录](/image/QQ20190225-162332@2x.png)
2. RPC框架使用spring cloud，数据库层用的是mybatis，sharding-jdbc中间件。里面可看到有admin（spring-boot-admin），eureka，以及zuul-gateway，相信这几个是开发微服务的基本东西，其他的组件没有集成进来，因为要轻嘛！common是属于项目通用类统一放置的模块。cloud-base是主要代码模块，里面实现了sharding-jdbc的分库分表定位逻辑，以及实现spring cloud的feign异常处理，拦截器等，它不归属于项目，不耦合，它可以被其他项目引用。后面会着重讲一下**cloud-base模块**。其他的server-user，server-payment是项目的分库分表的使用例子。

## cloud-base模块
### 1. spring cloud配置
- package-com.phantoms.framework.cloudbase.configuration;下面的类是spring cloud一些拦截器，全局异常处理等，着重讲一下全局异常处理。正常情况下，feign客户端调用微服务的时候，如果微服务发生业务异常或者网络异常，feign客户端应该是抛出异常，而不是返回异常信息。举个列子A调用B服务：
```
A.java

public CommonResult getUserInfo(String userName) throws Exception { 
    CommonResult res = null;
    try{
        res = BServiceFeign.getUserInfo(userName); 
    }catch(Exception e){
        if(e.getCause() instanceof NullPointerException)
            throw e;
    }
    return res;
}

```
- 在上面的列子中，你是希望BServiceFeign.getUserInfo在发生业务异常的时候，A会得到接受到异常，还是说B把异常信息放在res里面？
- GlobalConvertFeignException.java 就是全局异常的处理，转换成feign异常（修改http响应状态为500），如果B服务发生业务异常，那么A将会得到feign客户端的异常抛出，那么它为什么能抛出异常？FeignExceptionHandler.java 就是对调用后异常的信息的封装，如果http响应状态异常，比如405,500等，decode方法将会被执行，那么就会返回一个异常类，所以feign客户端就能抛出异常，具体实现看代码。
- 刚说的是业务异常，如果发生网络异常怎么处理？链接超时或服务挂了，通常这样的情况，我们会实现服务降级，容错或者重试。当然你在A服务下进行捕抓这类异常在处理就可以了，那么会就出现一个问题，C，D服务也要写这一部分网络异常捕抓处理的实现。所以才有Hystrix的使用，上面的FeignExceptionHandler，也可以实现这一部分功能，但是不同的服务可能处理不一样（容错或者重试）。
- Hystrix我也给出了例子，service-user-api模块下 UserServiceHystrixFallbackFactory.java 类，对于一般的网络异常，建议在这里集中处理，当然你认为一些通用的业务异常也可以在这里统一处理（比如参数校验失败异常），可以有你来决定是否抛出异常，或者返回容错的结果，如果Hystrix继续抛出异常，那么feign客户端也是会接到异常的抛出的。
- Hystrix和feignExceptionHandler是哪个执行先呢?我也忘记了，你们自己去测试一下吧！

### 2. sharding-jdbc
- package-com.phantoms.framework.cloudbase.dbconfig;这个包下面都是数据库源的配置了，也是就是到了sharding-jdbc的使用。
- 分库分表是挺复杂的一个问题，涉及到唯一ID，分区定位，数据扩容等。这里基本上能解决一部分问题。唯一ID算法很多，各有利弊。我采用的是网上给出来的Twitter开源分布式自增ID算法，简单好用，缺点就是时针回拨的问题（自行百度了解）。还要一些算法需要依赖redis，mysql等依赖太多。分区定位采用一致性hash，也是普遍采用的，扩容也方便。还要一个是类似mongodb的分区算法，扩容复杂。
- 分库分表当然有垂直分库，水平分库，垂直分库大概就是每个服务各自数据库，比如用户和支付记录，由于这两个数据级不同，一般会分开2个库。但是用户登录信息和用户详情，当然放在同一个数据库。水平分库那就是一个数据库放了多张用户信息的表和用户详情的表。
- 垂直分库一般按照微服务划分即可，项目中的案例就是一个server-user,server-payment, 一个是用户服务，一个是支付平台服务。
- sharding-jdbc支持弱事务，具体可以去百度了解一下，也基本满足业务要求了。

### 2.1 下面讲讲sharding-jdbc使用实现
- 由于分库分表复杂，所以数据库信息以及分表信息我存放在数据库里面，没有放在配置文件写。先看几张表:
   - db_config:
 ![db_config](/image/db_config.png)
  - module_config:
 ![module_config](/image/module_config.png)
  - table_config:
 ![table_config](/image/table_config.png)
-  db_config 记录了所有数据库的连接信息，position是分区信息，采用一致性分库，大小0-1000，逆时针方向选择定位。
- module_config 就是记录垂直分库信息，记录每个库里面的水平分表数量，同一个库的分表数量是一样的，table_num就是分表数量，这里分表定位，是采用一般hash方式，取模定位。根据表后缀定位具体的表。
- table_config 就是记录分表的信息，这的table_columns 就是分表字段，也是分库字段。这里为什么可是是多个？其实他们存放的时候取模求值都是一致的的，所以可以允许多个，后面会举个例子讲一下。
- ShardingDatasourceConfiguration.java 就是分库分表的逻辑实现，最终会创建2个数据源ShardingDataSource，一个是master，一个slave（多个slave也可以，我这里没实现，改造也很方便，后面补上吧）。首先会执行initDbConfig()方法（这里踩了一些mybatis的坑），获取那三张表的数据，初始化多个数据库，每个数据库分表的情况。
   - AbstractShardingAlgorithm实现了默认的一些规则算法。
   - TableShardingAlgorithm是自行实现的分表算法，根据分表字段进行取值求模定位到具体的实际表。
   - SourceShardingAlgorithm是自行实现的分库算法，根据分表字段进行取值求模定位到具体的实际数据库。
   - TableRule构造方法有多个参数，其中TableName，actualDataNodes，也就是TableName对应的真实的表，比如 ph_user_info 对应三张表ph_user_info_0,ph_user_info_1,ph_user_info_2, 还有就是dataSourceMap 也就是拥有这张表的数据库列表。这样下来一张表就能正确的定位到具体的库，具体的真实表，还有2个参数就是TableShardingAlgorithm，SourceShardingAlgorithm，这2个算法参数如果为空，那么好像会有默认的算法。。。
   - ShardingRule的创建需要一个默认数据库 default，也就是db_config存放的那个数据库，一般就是公共数据库，比如存放字典表，地理城市这些，当sharding-jdbc无法在TableRule进行定位的时候，就会使用这张表进行执行sql。如果指定的”default“不存在，那么就随机找一个数据库里面的随机一张表执行，额好像挺多坑的。
- DynamicDataSource.java动态数据源路由，也就是master，slave的选择问题。ShardingDatasourceConfiguration这里创建了数据源有master和slave的，实际上读写分离Sharding-jdbc有现成的实现，但是我没有用它们的，用这些注解方式去实现读写分离，可能更简单点吧，你们可以了解一下Sharding-jdbc读写分离的实现。最后DynamicDataSource创建完成之后，交给MybatisConfiguration引用配置。
- BTW 如果你不需要分库分表那么复杂的，可以放弃它，然后使用DruidDataSourceConfiguration.java 使用这个创建数据源即可。（@Configuration）

### 2.2 sharding-jdbc分库分表字段问题
- 前面说了分库分表字段可以有多个，因为比如用户名注册时候是123，那么user_id可以取这个123放到后面，这样求模出来就达到一致了。但是问题来了，如果我还想手机号进行定位分库分表呢，或者注册日期，由于分库字段是不能改变的，像手机号邮箱这些不行。但是注册日期呢？如果你添加了注册日期作为其中一个分库分表定位的话，那么你insert的时候可能就存放到不同的表里面出了，就有2条数据了，至于这样的情况会有什么问题？统计用户总数会挺麻烦吧？还有其他呢，不知道，没细想。更好的方案是如果有多个业务字段需要查询方便的话，用mongodb去！或者其他的NOSQL都是你最好的选择。
- 还有一个叫复合字段分库分表，比如user_id 进行分库，user_id + order_id进行分表，可以重写 AbstractShardingAlgorithm.java 里面的doShardingByListShardingValue方法，对多个字段值进行分库定位。还有一个是范围值分库分表，那个好像没啥意思，使用场景好比较局限吧（通常可能定位了全部数据库和表）。这里的分库分表算法还算挺灵活，可能某些表需要特定的算法，那么需要开发者重新实现。
- 还有一个问题这里的实现缺少一些东西，那就是server-user按理不应该持有server-payment的数据库操作的，我预留个一个注释的地方可以改写。但是一个服务server-user很可能里面还会进行垂直分库，多个模块，比如user_info表和login_record表会拆分吧, 或者order_info表呢，所以我这里没做太多的局限，交给你们了。

## 总结
- 坑很多。分库分表比较复杂，server_user里面有测试案例，可以看看里面的方法说明。我的坑也有，但是项目整体比较轻，坑应该很小，相信你一定能平躺过去的。zuul-gateway没有做异常的捕抓，这个项目是我目前一个项目初期的时候搬出来的，没有太复杂的东西在里面，都很原生。有问题可以加我的QQ，3121604，欢迎讨论或者留下issue.
- [分库分表思想](https://www.cnblogs.com/sunny3096/p/8595783.html)
- [唯一ID生成](https://www.cnblogs.com/haoxinyue/p/5208136.html)
- [MTDDL-美团点评分布式数据访问层中间件](https://tech.meituan.com/2016/12/19/mtddl.html)

