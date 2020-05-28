202000308 Miles
1、对服务功能的初步验证;
2、对9patch图片的功能有了了解;
3、对menu功能的使用验证;
4、gradle中增加 targetSdkVersion 24的忽略注释;
5、验证APK常规安装方法;
6、验证APK静默安装方法，且安装之后自启动；自启动的方式也可用在应用A打开应用B的设置;
7、加入BOOT完成广播且apk自启动且开机占用界面;
8、Utils中添加FileUtils/ApkUtils两个工具包;
9、在ReflectTest.java中做了反射的验证;
10、对线程操作的回顾：
     ①弄清了继承Thread类和继承implement接口的区别和原因：
		 * 这种继承Runnable接口的线程调用方式比起继承Thread类的优势：
	 *         MyThreadRunable myRunable1= new MyThreadRunable("aa");
	 *         Thread my1 = new Thread(myRunable1);
	 *         Thread my2 = new Thread(myRunable1);
	 *         Thread my3 = new Thread(myRunable1);
	 *         my1.start();
	 *         my2.start();
	 *         my3.start();
	 *    1、因为以上my1\my2\my3都是通过Thread对同一个myRunable1进行操作，
	 *      所以多个线程对MyThreadRunable中的属性可共用
	②对线程同步(synchronized)：用于对信息名称和内容的同步;
	 等待(wait)/唤醒(notify)的使用：用于生产1条/取1条的原则取出;
11、通过反射实现了动态代理，暂时不知道有什么用；
12、知道了trim()的结论:去掉了头尾小于空格（十进制32）的ASCII码;
13、结合Okhttp/apache服务器访问PC本地服务器中的xml/jason文件，并在手机端完成xml/jason的解析;
    比较json解析的三种模式，并选用Fastjson方式解析;
14、加入文件选择器的调试按钮;
15、调整按键布局，采用自定义方式,且每个测试案例有单独对应的布局;
16、优化单例模式，具体体现在LogHelper的使用中;
17、加入apache.commons.io里面提供了输入流输出流的常用工具方法;
18、增加Databinding方式的简洁UI刷新方法,包含Stuent和UserObservable两种方式;
19、完成RxJava的基本操作
20、Retrofit及Retrofit+RxJava的实例未完待续；