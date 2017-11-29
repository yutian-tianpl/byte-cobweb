# byte-cobweb
![byte-cobweb icon](http://blog.tianpl.com/blog/img/Cobweb.png)


## Overview
基于ASM-tree API的字节码AOP
 
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
	- Matcher配置
		- matchBeforeReadClass
		- matchAfterReadClass
		- matchMethod
2. Matcher 匹配目标类，目标方法
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