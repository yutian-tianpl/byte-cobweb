# byte-cobweb
![byte-cobweb icon](http://blog.tianpl.com/blog/img/Cobweb.png)


## Overview
基于ASM-tree API的字节码修改工具
 
旨在快速构建基于ASM的Instrumentation方案


### Component
1. Interceptor 
	- 拦截器用户界面
		- doOnStart 拦截的目标方法开始位置执行操作
		- doOnThrowableThrown 拦截的目标代码主动抛出异常时执行的操作
		- doOnThrowableSurprise 拦截的目标代码意外异常抛出时执行的操作
		- doOnFinish 希望在finally中执行的操作
			- 正常结束时执行
			- 代码异常抛出时执行
			- 代码未捕获异常抛出时执行
	- Match规则
		- matchBeforeReadClass ASM读取类文件读取后的匹配规则
		- matchAfterReadClass ASM读取类文件后匹配规则
		- matchMethod 目标方法匹配规则
2. Matcher 匹配目标类，目标方法
    - 利用Matcher快捷的组装Interceptor的Match规则
	- Class Matcher
	- Method Matcher
	- Basic Matcher
	- Matcher Interface
3. Modifier 字节码修改器:类修改器，方法修改器
	- ClassModifier，静态方法modify作为入口
	- MethodModifier，没有对外暴露的方法
4. Insn Creator
	- 工具类，创建可复用的指令列表
5. javaagent-example
	- 如何创建一个javaagent
6. target-example
	- 如何将一个javaagent应用到目标上
	
### When
1. 现有的手段没有办法满足需求，字节码修改做为仅有的手段时
2. 无侵入监控
3. 构建Instrumentation

### How to build instrumentation jar
1. 创建Premain
2. 实现ClassFileTransformer
3. 实现Interceptor
4. 在manifest中指定PremainClass(比如Maven插件maven-jar-plugin可以完成此工作)
### How to run on target
1. 将编译好的Jar包放到目标的项目的ClassPath中
2. 在目标项目启动命令上附加-javaagent参数项，参数内容为agentJar的路径
3. 参数示例:-javaagent:/Users/tianyu/Desktop/example-javaagent-1.0-SNAPSHOT.jar=NAME_START_WITH;com.tianpl.opcode.example.target
4. agentJar所以依赖的当前的ClassPath中
    - 单独管理agentJar的ClassPath
    - 将agentJar打包成fatJar
    - 目标项目中增加依赖