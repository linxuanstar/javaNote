# 图片

![](D:\Java\笔记\图片\Java版本\5-10.png)

# jdk1.5的新特性

## 1. 自动装箱/拆箱

**什么是自动装箱拆箱？**
简单一点说，装箱就是自动将基本数据**类型转换**为包装器类型；拆箱就是自动将包装器类型转换为基本数据类型。

```java
Integer i = 10; //装箱
int n = i;  //拆箱
```

![img](D:\Java\笔记\图片\Java版本\自动拆箱)

在Java中每种基本数据类型都会存在对应的包装器类型，为什么这么设计？

Java是一个面相对象的编程语言，基本类型并不具有对象的性质，为了让基本类型也具有对象的特征，就出现了包装类型（如我们在使用集合类型Collection时就一定要使用包装类型而非基本类型），它相当于将基本类型“包装起来”，使得它具有了对象的性质，并且为其添加了属性和方法，丰富了基本类型的操作。

**关于装箱与拆箱常见面试题:**

```java
public class Main {
    public static void main(String[] args) {
         
        Integer i1 = 100;
        Integer i2 = 100;
        Integer i3 = 200;
        Integer i4 = 200;
         
        System.out.println(i1==i2);    //true
        System.out.println(i3==i4);    //false
    }
}
```

为什么会这样呢？看下Integer的valueOf方法的具体实现：

```java
public static Integer valueOf(int i) {
	final int offset = 128;
	if (i >= -128 && i <= 127) { // must cache 
	    return IntegerCache.cache[i + offset];
	}
        return new Integer(i);
    }
```

而其中IntegerCache类的实现为：

```java
private static class IntegerCache {
    private IntegerCache(){}
    static final Integer cache[] = new Integer[-(-128) + 127 + 1];
 
    static {
        for(int i = 0; i < cache.length; i++)
	    cache[i] = new Integer(i - 128);
    }
}
```

由上可知，<font color="red">Integer包装类型如果数值在[-128,127]之间，便返回指向IntegerCache.cache中已经存在的对象的引用；否则创建一个新的Integer对象。</font>

我们会联想到其他的包装类类型是否也有这样的规律，我们看下Double类

```java
public class Main {
    public static void main(String[] args) {
         
        Double i1 = 100.0;
        Double i2 = 100.0;
        Double i3 = 200.0;
        Double i4 = 200.0;
         
        System.out.println(i1==i2);    //false
        System.out.println(i3==i4);    //false
    }
}
```

为什么比较结果都是false呢？看下Double的valueof方法的实现：

```java
public static Double valueOf(double d) {
        return new Double(d);
    }
```

想想原因很简单：在某个范围内的整型数值的个数是有限的，而浮点数却不是。

<font color="red">注意，Integer、Short、Byte、Character、Long这几个类的valueOf方法的实现是类似的。Double、Float的valueOf方法的实现是类似的。</font>

对于Boolean类型比较一下就一目了然：

```java
public class Main {
    public static void main(String[] args) {
         
        Boolean i1 = false;
        Boolean i2 = false;
        Boolean i3 = true;
        Boolean i4 = true;
         
        System.out.println(i1==i2);    //true
        System.out.println(i3==i4);    //true
    }
}
```

源码：

```java
public static Boolean valueOf(boolean b) {
        return (b ? TRUE : FALSE);
    }
```

TRUE和FALSE都是静态常量

```java
public static final Boolean TRUE = new Boolean(true);
 
public static final Boolean FALSE = new Boolean(false);
```

另外还有一点需要注意：当 "=="运算符的两个操作数都是包装器类型的引用，则是比较指向的是否是同一个对象，而如果其中有一个操作数是表达式（即包含算术运算）则比较的是数值（即会触发自动拆箱的过程），对基础数据类型进行运算。对于包装器类型，equals方法并不会进行类型转换。

```java
public class Main {
    public static void main(String[] args) {
         
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Long g = 3L;
        Long h = 2L;
         
        System.out.println(c==d);            //true
        System.out.println(e==f);            //false
        System.out.println(c==(a+b));        //true
        System.out.println(c.equals(a+b));   //true
        System.out.println(g==(a+b));        //true
        System.out.println(g.equals(a+b));   //false
        System.out.println(g.equals(a+h));   //true
    }
}
```

equals()方法源码

```java
//Integer类中
public boolean equals(Object obj) {
	if (obj instanceof Integer) {
	    return value == ((Integer)obj).intValue();
	}
	return false;
    }
 
//Long类中
public boolean equals(Object obj) {
    if (obj instanceof Long) {
	return value == ((Long)obj).longValue();
    }
	return false;
}
```

我们指定equals比较的是内容本身，并且我们也可以看到equal的参数是一个Object对象，我们传入的是一个int类型，所以首先会进行装箱，然后比较，之所以返回true，是由于它比较的是对象里面的value值。当内容和类型都相同时才会返回true。

## 2. 枚举

问题：要定义星期几或性别的变量，该怎么定义？
假设用1-7分别表示星期一到星期日，但有人可能会写成`int weekday = 0;`或即使使用常量方式也无法阻止意外。

枚举就是要让某个类型的变量的取值只能为若干个固定值中的一个，否则，编译器就会报错。枚举可以让编译器在编译时就可以控制源程序中填写的非法值，普通变量的方式在开发阶段无法实现这一目标。

```java
/*
 * 用普通类如何实现枚举功能，定义一个Weekday的类来模拟枚举功能。 
	1、私有的构造方法
	2、每个元素分别用一个公有的静态成员变量表示
	3、可以有若干公有方法或抽象方法。采用抽象方法定义nextDay就将大量的if.else语句转移成了一个个独立的类。
*/
 
package cn.itheima;
 
public abstract class WeekDay {
	private WeekDay(){}
 
	public final static WeekDay SUN=new WeekDay(){
		public WeekDay nextDay(){
			return MON;
		}
	};
	
	public final static WeekDay MON=new WeekDay(){
		public WeekDay nextDay(){
			return SUN;
		}
	};
	
	public abstract WeekDay nextDay();
 
	public String toString(){
		return this==SUN?"SUM":"MON";
	}
}
```

### 枚举的基本应用

1. 通过enum关键字定义枚举类，枚举类是一个特殊的类，每个元素都是该类的一个实例对象。

2. 用枚举类规定值，如上面的WeekDay类。以后用此类型定义的值只能是这个类中规定好的那些值，若不是这些值，编译器不会通过。

3. 好处：在编译时期就会发现错误，表明值不符合，减少了运行时期的错误。

4. 如果调用者想打印枚举类中元素的信息，需由编写此类的人定义toString方法。
   注：枚举类是一个class，而且是一个不可被继承的final类，其中的元素都是类静态常量。

5. 常用方法：

   * 构造器：

     1. 构造器只是在构造枚举值的时候被调用。

     2. 构造器只有私有private，绝不允许有public构造器。这样可以保证外部代码无法重新构造枚举类的实例。因为枚举值是public static final的常量，但是枚举类的方法和数据域是可以被外部访问的。

     3. 构造器可以有多个，调用哪个即初始化相应的值。

        

   * 非静态方法：（所有的枚举类都继承了Enum方法）

     1. `String toString()` ;//返回枚举量的名称

     2. `int ordinal()` ;//返回枚举值在枚举类中的顺序，按定义的顺序排，默认第一个是0

     3. `Class getClass()` ;//获取对应的类名

     4. `String name()`;//返回此枚举常量的名称，在其枚举声明中对其进行声明。

        

   * 静态方法：

     1. `valueOf(String e)` ;//转为对应的枚举对象，即将字符串转为对象

     2. `values()` ;//获取所有的枚举对象元素

   ```java
   package cn.itheima;
    
   public class EnumDemo {
   public static void main(String[] args) {
   		WeekDay weekDay=WeekDay.MON;
   		System.out.println(weekDay);//输出枚举常量名
   		System.out.println("=====================1");
   		System.out.println(weekDay.name());//输出对象名
   		System.out.println("=====================2");
   		System.out.println(weekDay.getClass());//输出对应类
   		System.out.println("=====================3");
   		System.out.println(weekDay.toString());//输出枚举对象名
   		System.out.println("=====================4");
   		System.out.println(weekDay.ordinal());//输出此对象在枚举常量的次序
   		System.out.println("=====================5");
   		System.out.println(WeekDay.valueOf("WED"));//将字符串转化为枚举常量
   		System.out.println("=====================6");
   		System.out.println(WeekDay.values().length);//获取所以的枚举元素，并打印其长度
   	}
   	//定义枚举内部类
   	public enum WeekDay{
   		SUN(1),MON,TUE,WED,THI,FRI,SAT;
   		//分号可有可无，但如果下面还有方法或其他成员时，分号不能省。
   		//而且当有其他方法时，必须在这些枚举变量的下方。
    
   		//无参构造器
   		private WeekDay(){
   			System.out.println("First");
   		}
   		//带参数的构造器
   		private WeekDay(int day){
   			System.out.println("Second");
   		}
   	}
   }
   
   /*
   	Second
   	First
   	First
   	First
   	First
   	First
   	First
   	MON
   	=====================1
   	MON
   	=====================2
   	class Demo$WeekDay
   	=====================3
   	MON
   	=====================4
   	1
   	=====================5
   	WED
   	=====================6
   	7
   */
   ```

### 枚举的高级应用

1. 枚举就相当于一个类，其中也可以定义构造方法、成员变量、普通方法和抽象方法。
2. 枚举元素必须位于枚举体中的最开始部分，枚举元素列表的后要有分号与其他成员分隔。把枚举中的成员方法或变量等放在枚举元素的前面，编译器报告错误。
3. 带构造方法的枚举
   * 构造方法必须定义成私有的
   * 如果有多个构造方法，该如何选择哪个构造方法？
   * 枚举元素MON和MON()的效果一样，都是调用默认的构造方法。

4. 带方法的枚举

```java
/*
 * 抽象的枚举方法
 * 此时枚举中的常量需要子类来实现，这是可以利用内部类的方式来定义枚举常量
 * 带方法的枚举
	1）定义枚举TrafficLamp
	2）实现普通的next方法
	3）实现抽象的next方法：每个元素分别是由枚举类的子类来生成的实例对象，这些子类
	4）用类似内部类的方式进行定义。
	5）增加上表示时间的构造方法
 * */
 
package cn.itheima;
 
public class EnumTest {
	public enum TrafficLamp{
		RED(30){
			public TrafficLamp nextLamp(){
				return GREEN;
			}
		},
		GREEN(30){
			public TrafficLamp nextLamp(){
				return YELLOW;
			}
		},
		YELLOW(5){
			public TrafficLamp nextLamp(){
				return RED;
			}
		};
		private int time;
		//构造器
		private TrafficLamp(int time){
			this.time=time;}
		//抽象方法
		public abstract TrafficLamp nextLamp();
	}		
}
```

### 小结

1. 匿名内部类比较常用
2. 类的方法返回的类型可以是本类的类型
3. 类中可定义静态常量，常量的结果就是自己这个类型的实例对象
4. 枚举只有一个成员时，就可以作为一种单例的实现方式。

> 注意：
>
> * 所有的枚举都继承自java.lang.Enum类。由于Java不支持多继承，所以枚举对象不能再继承其他类。
>
> * switch语句支持int,char,enum类型，使用枚举，能让我们的代码可读性更强。

## 3. 静态导入

静态导入是JDK1.5中对import语句的增强,语法格式:`import static 包名….类名.方法名`;或者导入类中所有静态成员，此时方法名用*代替。

静态导入注意事项：

- 方法或变量必须是静态的
- 如果有多个同名的静态方法，这个时候要使用，必须加前缀。

示例：

```java
import static java.lang.System.out; 
import static java.lang.Integer.*; 
 
public class Test {
     
    public static void main(String[] args) { 
        out.println(MAX_VALUE);         //2147483647
        out.println(toHexString(42));   //2a
    } 
}
```

静态导入节省了大量重复代码的击键次数，但同时也牺牲了代码的可阅读性。

## 5. 可变参数

格式如下：

1. `数据类型...参数名`   真的是三个点,不要写多
2. 可以传入当前类型的任意个值
3. 可变参数本质就是一个数组可以直接传入数组

> 注意：
>
> * 一个方法只能有一个可变参数
>
> * 如果方法有多个参数,可变参数一定要放在最后    因为可变参数会将所有传入的值获取到,后面的形参永远拿不到值
>
> * 如果可变参数的类型为Object...obj    那么可以传入任意类型,任意个数

```java
public static void main(String[] args) {
 
        sum();
        sum(1);
        sum(10,20,30,40);
        sum(1,1,1,1,1,1,1,1,1,1);
 
        int[] arr = {3,8,2,5,0};
        int sum = sum(arr);
        System.out.println(sum);
 
    }
 
    public static int sum(int...a){
        int sum = 0;
        for (int i : a) {
            sum+=i;
        }
        return sum;
    }

    //本质上就是数组
    public static int sum(int[] arr){
        int sum = 0;
        for(int[] i : arr){
            sum+=i;
        }
        return sum;
    }
}
```

```java
/*
     Collections方法
       static void  addAll(Collection<T>  c ,T...t)
 */
public class Test {
    public static void main(String[] args) {
 
        List<String> list = new ArrayList<>();
 
//        list.add("abc");
//        list.add("bcd");
//        list.add("aaa");
 
        Collections.addAll(list,"abc","bcd","aa");
 
        System.out.println(list);
    }
}
```

## 6. 内省

### 什么是内省

在计算机科学中，内省是指计算机程序在运行时（Run time）检查对象（Object）类型的一种能力，通常也可以称作**运行时类型检查**。 不应该将内省和反射混淆。

相对于内省，反射更进一步，是指计算机程序在运行时（Run time）可以访问、检测和修改它本身状态或行为的一种能力。

内省(Introspector) 是Java 语言对 JavaBean 类属性、事件的一种缺省处理方法。

JavaBean是一种特殊的类，主要用于传递数据信息，这种类中的方法主要用于访问私有的字段，且方法名符合某种命名规则。如果在两个模块之间传递信息，可以将信息封装进JavaBean中，这种对象称为“值对象”(Value Object)，或“VO”。方法比较少。这些信息储存在类的私有变量中，通过set()、get()获得。

### 内省和反射的区别

**反射**是在运行状态把Java类中的各种成分映射成相应的Java类，可以动态的获取所有的属性以及动态调用任意一个方法，强调的是运行状态。
.
**内省**(IntroSpector)是Java 语言**针对 Bean 类**属性、事件的一种缺省处理方法。　JavaBean是一种特殊的类，主要用于传递数据信息，这种类中的方法主要用于访问私有的字段，且方法名符合某种命名规则。如果在两个模块之间传递信息，可以将信息封装进JavaBean中，这种对象称为“值对象”(Value Object)，或“VO”。方法比较少。这些信息储存在类的私有变量中，通过set()、get()获得。内省机制是通过反射来实现的，BeanInfo用来暴露一个bean的属性、方法和事件，以后我们就可以操纵该JavaBean的属性。

![在这里插入图片描述](D:\Java\笔记\图片\Java版本\内省)

在Java内省中，用到的基本上就是上述几个类。

通过BeanInfo这个类就可以获取到类中的方法和属性。例如类 A 中有属性 name, 那我们可以通过 getName,setName 来得到其值或者设置新的值。通过 getName/setName 来访问 name 属性，这就是默认的规则。

Java 中提供了一套 API 用来访问某个属性的 getter/setter 方法，通过这些 API 可以使你不需要了解这个规则（但你最好还是要搞清楚），这些 API 存放于包 java.beans 中,

一般的做法是通过类 Introspector 的 getBeanInfo方法 来获取某个对象的 BeanInfo 信息,然后通过 BeanInfo 来获取属性的描述器(PropertyDescriptor),通过这个属性描述器就可以获取某个属性对应的 getter/setter 方法,然后我们就可以通过反射机制来调用这些方法，这就是内省机制。

例如类UserInfo :
```java
package com.peidasoft.Introspector;

public class UserInfo {
    private long userId;
    private String userName;
    private int age;
    private String emailAddress;

    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
```

在类UserInfo中有属性 userName, 那我们可以通过 getUserName,setUserName来得到其值或者设置新的值。通过 getUserName/setUserName来访问 userName属性，这就是默认的规则。 Java JDK中提供了一套 API 用来访问某个属性的 getter/setter 方法，这就是内省。

### JDK内省类库

#### PropertyDescriptor类

`PropertyDescriptor`类表示JavaBean类通过存储器导出一个属性。主要方法如下：

- `getPropertyType()`，获得属性的Class对象;
- `getReadMethod()`，获得用于读取属性值的方法；getWriteMethod()，获得用于写入属性值的方法;
- `hashCode()`，获取对象的哈希值;
- `setReadMethod(Method readMethod)`，设置用于读取属性值的方法;
- `setWriteMethod(Method writeMethod)`，设置用于写入属性值的方法。

　　实例代码如下：

```java
package com.peidasoft.Introspector;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class BeanInfoUtil {   
    public static void setProperty(UserInfo userInfo,String userName)throws Exception{
        PropertyDescriptor propDesc=new PropertyDescriptor(userName,UserInfo.class);
        Method methodSetUserName=propDesc.getWriteMethod();
        methodSetUserName.invoke(userInfo, "wong");
        System.out.println("set userName:"+userInfo.getUserName());
    }  
    public static void getProperty(UserInfo userInfo,String userName)throws Exception{
        PropertyDescriptor proDescriptor =new PropertyDescriptor(userName,UserInfo.class);
        Method methodGetUserName=proDescriptor.getReadMethod();
        Object objUserName=methodGetUserName.invoke(userInfo);
        System.out.println("get userName:"+objUserName.toString());
    }
}
```

#### Introspector类

将JavaBean中的属性封装起来进行操作。在程序把一个类当做JavaBean来看，就是调用Introspector.getBeanInfo()方法，得到的BeanInfo对象封装了把这个类当做JavaBean看的结果信息，即属性的信息。

getPropertyDescriptors()，获得属性的描述，可以采用遍历BeanInfo的方法，来查找、设置类的属性。具体代码如下：

```java
package com.peidasoft.Introspector;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;


public class BeanInfoUtil {
        
    public static void setPropertyByIntrospector(UserInfo userInfo,String userName)throws Exception{
        BeanInfo beanInfo=Introspector.getBeanInfo(UserInfo.class);
        PropertyDescriptor[] proDescrtptors=beanInfo.getPropertyDescriptors();
        if(proDescrtptors!=null&&proDescrtptors.length>0){
            for(PropertyDescriptor propDesc:proDescrtptors){
                if(propDesc.getName().equals(userName)){
                    Method methodSetUserName=propDesc.getWriteMethod();
                    methodSetUserName.invoke(userInfo, "alan");
                    System.out.println("set userName:"+userInfo.getUserName());
                    break;
                }
            }
        }
    }
    
    public static void getPropertyByIntrospector(UserInfo userInfo,String userName)throws Exception{
        BeanInfo beanInfo=Introspector.getBeanInfo(UserInfo.class);
        PropertyDescriptor[] proDescrtptors=beanInfo.getPropertyDescriptors();
        if(proDescrtptors!=null&&proDescrtptors.length>0){
            for(PropertyDescriptor propDesc:proDescrtptors){
                if(propDesc.getName().equals(userName)){
                    Method methodGetUserName=propDesc.getReadMethod();
                    Object objUserName=methodGetUserName.invoke(userInfo);
                    System.out.println("get userName:"+objUserName.toString());
                    break;
                }
            }
        }
    }
    
}
```

通过这两个类的比较可以看出，都是需要获得PropertyDescriptor，只是方式不一样：前者通过创建对象直接获得，后者需要遍历，所以使用PropertyDescriptor类更加方便。

使用实例：

```java
package com.peidasoft.Introspector;

public class BeanInfoTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        UserInfo userInfo=new UserInfo();
        userInfo.setUserName("peida");
        try {
            BeanInfoUtil.getProperty(userInfo, "userName");
            
            BeanInfoUtil.setProperty(userInfo, "userName");
            
            BeanInfoUtil.getProperty(userInfo, "userName");
            
            BeanInfoUtil.setPropertyByIntrospector(userInfo, "userName");            
            
            BeanInfoUtil.getPropertyByIntrospector(userInfo, "userName");
            
            BeanInfoUtil.setProperty(userInfo, "age");
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
```

输出：

```java
get userName:peida
set userName:wong
get userName:wong
set userName:alan
get userName:alan
java.lang.IllegalArgumentException: argument type mismatch
    at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
    at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
    at java.lang.reflect.Method.invoke(Method.java:597)
    at com.peidasoft.Introspector.BeanInfoUtil.setProperty(BeanInfoUtil.java:14)
    at com.peidasoft.Introspector.BeanInfoTest.main(BeanInfoTest.java:22)
```

**说明：**BeanInfoUtil.setProperty(userInfo, "age");报错是应为age属性是int数据类型，而setProperty方法里面默认给age属性赋的值是String类型。所以会爆出argument type mismatch参数类型不匹配的错误信息。

### BeanUtils工具包

由上述可看出，内省操作非常的繁琐，所以所以Apache开发了一套简单、易用的API来操作Bean的属性——BeanUtils工具包。
BeanUtils工具包：下载：`http://commons.apache.org/beanutils/`　
								 注意：应用的时候还需要一个logging包 `http://commons.apache.org/logging/`

使用BeanUtils工具包完成上面的测试代码:

```java
package com.peidasoft.Beanutil;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.peidasoft.Introspector.UserInfo;

public class BeanUtilTest {
    public static void main(String[] args) {
        UserInfo userInfo=new UserInfo();
         try {
            BeanUtils.setProperty(userInfo, "userName", "peida");
            
            System.out.println("set userName:"+userInfo.getUserName());
            
            System.out.println("get userName:"+BeanUtils.getProperty(userInfo, "userName"));
            
            BeanUtils.setProperty(userInfo, "age", 18);
            System.out.println("set age:"+userInfo.getAge());
            
            System.out.println("get age:"+BeanUtils.getProperty(userInfo, "age"));
             
            System.out.println("get userName type:"+BeanUtils.getProperty(userInfo, "userName").getClass().getName());
            System.out.println("get age type:"+BeanUtils.getProperty(userInfo, "age").getClass().getName());
            
            PropertyUtils.setProperty(userInfo, "age", 8);
            System.out.println(PropertyUtils.getProperty(userInfo, "age"));
            
            System.out.println(PropertyUtils.getProperty(userInfo, "age").getClass().getName());
                  
            PropertyUtils.setProperty(userInfo, "age", "8");   
        } 
         catch (IllegalAccessException e) {
            e.printStackTrace();
        } 
         catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
```

运行结果：

```java
set userName:peida
get userName:peida
set age:18
get age:18
get userName type:java.lang.String
get age type:java.lang.String
8
java.lang.Integer
Exception in thread "main" java.lang.IllegalArgumentException: Cannot invoke com.peidasoft.Introspector.UserInfo.setAge 
on bean class 'class com.peidasoft.Introspector.UserInfo' - argument type mismatch - had objects of type "java.lang.String" 
but expected signature "int"
    at org.apache.commons.beanutils.PropertyUtilsBean.invokeMethod(PropertyUtilsBean.java:2235)
    at org.apache.commons.beanutils.PropertyUtilsBean.setSimpleProperty(PropertyUtilsBean.java:2151)
    at org.apache.commons.beanutils.PropertyUtilsBean.setNestedProperty(PropertyUtilsBean.java:1957)
    at org.apache.commons.beanutils.PropertyUtilsBean.setProperty(PropertyUtilsBean.java:2064)
    at org.apache.commons.beanutils.PropertyUtils.setProperty(PropertyUtils.java:858)
    at com.peidasoft.orm.Beanutil.BeanUtilTest.main(BeanUtilTest.java:38)
Caused by: java.lang.IllegalArgumentException: argument type mismatch
    at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
    at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
    at java.lang.reflect.Method.invoke(Method.java:597)
    at org.apache.commons.beanutils.PropertyUtilsBean.invokeMethod(PropertyUtilsBean.java:2170)
    ... 5 more
```

说明：

1. 获得属性的值，例如，BeanUtils.getProperty(userInfo,"userName")，返回字符串
2. 设置属性的值，例如，BeanUtils.setProperty(userInfo,"age",8)，参数是字符串或基本类型自动包装。设置属性的值是字符串，获得的值也是字符串，不是基本类型。
3. BeanUtils的特点：
   * 对基本数据类型的属性的操作：在WEB开发、使用中，录入和显示时，值会被转换成字符串，但底层运算用的是基本类型，这些类型转到动作由BeanUtils自动完成。
   *  对引用数据类型的属性的操作：首先在类中必须有对象，不能是null，例如，private Date birthday=new Date();。操作的是对象的属性而不是整个对象，例如，BeanUtils.setProperty(userInfo,"birthday.time",111111);　

```java
package com.peidasoft.Introspector;
import java.util.Date;

public class UserInfo {

    private Date birthday = new Date();
    
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    public Date getBirthday() {
        return birthday;
    }      
}
```

```java
package com.peidasoft.Beanutil;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtils;
import com.peidasoft.Introspector.UserInfo;

public class BeanUtilTest {
    public static void main(String[] args) {
        UserInfo userInfo=new UserInfo();
         try {
            BeanUtils.setProperty(userInfo, "birthday.time","111111");  
            Object obj = BeanUtils.getProperty(userInfo, "birthday.time");  
            System.out.println(obj);          
        } 
         catch (IllegalAccessException e) {
            e.printStackTrace();
        } 
         catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
```

3. PropertyUtils类和BeanUtils不同在于，运行getProperty、setProperty操作时，没有类型转换，使用属性的原有类型或者包装类。由于age属性的数据类型是int，所以方法PropertyUtils.setProperty(userInfo, "age", "8")会爆出数据类型不匹配，无法将值赋给属性。

## 7. 泛型(Generic)

```java
List<String> l1 = new ArrayList<String>();
List<Integer> l2 = new ArrayList<Integer>();
		
System.out.println(l1.getClass() == l2.getClass());
```

正确答案是 true。

上面的代码中涉及到了泛型，而输出的结果缘由是**类型擦除**。先好好说说泛型。

泛型的英文是 generics，generic 的意思是通用,而翻译成中文，泛应该意为广泛，型是类型。所以泛型就是能广泛适用的类型。

**但泛型还有一种较为准确的说法就是为了参数化类型，或者说可以将类型当作参数传递给一个类或者是方法。**	

​	泛型，即“**参数化类型**”，一提到参数，最熟悉的就是定义方法时有形参，然后调用此方法时传递实参。参数化类型就是将类型由原来的具体的类型参数化，类似于方法中的变量参数，此时类型也定义成参数形式(可以称之为类型形参)，然后在使用或调用时传入具体的类型(类型实参)。

​		泛型的本质是为了参数化类型(在不创建新的类型情况下，通过泛型指定的不同类型来控制形参具体限制的类型)。也就是说在泛型使用过程中，操作的数据类型被指定为一个参数，这种参数类型可以用在类、接口和方法中，分别被称之为泛型类、泛型接口、泛型方法。

泛型的好处：

* 将运行期间遇到的问题提前到编译期间
* 避免了向下转型
* 优化了程序设计，解决了黄色警告

实例1：

```java
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GenericDemo01 {
	public static void main(String[] args) {
		List list = new ArrayList();
		list.add("hello");
		list.add("world");
		list.add("java");
		list.add(100);
		
        Iterator<String> iter = list.iterator();
        while(iter.hasNext()){
            String s = iter.next();
            System.out.println(s);
		}
	}
}
```

毫无疑问，程序的运行结果会以崩溃结束：

```java
java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.String
	at se01.day05.GenericDemo01.main(GenericDemo01.java:18)
```

ArrayList可以存放任意类型，例子中添加了一个String类型，添加了一个Integer类型，再使用时都以String的方式使用，因此程序崩溃了。为了解决类似这样的问题（在编译阶段就可以解决），泛型应运而生。

我们将第一行声明初始化list的代码更改一下，编译器会在编译阶段就能够帮我们发现类似这样的问题。

```java
List<String> list = new ArrayList<String>();
...
//list.add(100); 在编译阶段，编译器就会报错
```

 实例2：

```java
//泛型类
class ObjectDemo<E>{
	private E e;
	public void setE(E e){
		this.e = e;
	}
	public E getE(){
		return e;
	}
}

//不使用泛型
class ObjectDemo02{
	private Object obj;

	public Object getObj() {
		return obj;
	}
	 
	public void setObj(Object obj) {
		this.obj = obj;
	}

}
public class GenericDemo02 {
	public static void main(String[] args) {
		ObjectDemo<String> od = new ObjectDemo<String>();
		od.setE("张伟");
		System.out.println(od.getE());
		

		ObjectDemo<Integer> od2 = new ObjectDemo<Integer>();
		od2.setE(12);
		int i = od2.getE();
		System.out.println(i);
		
		ObjectDemo02 obj = new ObjectDemo02();
		obj.setObj("最低配");

//		Integer inter =  (Integer) obj.getObj();//.ClassCastException
//		System.out.println(inter);
		
	}

}
```

由上可知，在不使用时我们会用到Object作为数据类型，在使用时再根据具体的情况向下转型还可能出现`java.lang.ClassCastException`异常，使用泛型后就避免了这种情况。

### 泛型的使用

#### 泛型类：定义在类上面

格式：`public class 类名<泛型类型1,...>`

* 例如：

  ```java
  //此处T可以随便写为任意标识，常见的如T、E、K、V等形式的参数常用于表示泛型
  //在实例化泛型类时，必须指定T的具体类型
  public class Generic<T,R>{     //一个类上可以定义多种泛型声明
      //key这个成员变量的类型为T,T的类型由外部指定  
      private T key;
  
      public Generic(T key) { //泛型构造方法形参key的类型也为T，T的类型由外部指定
          this.key = key;
      }
       
      public T getKey(){ //泛型方法getKey的返回值类型为T，T的类型由外部指定
          return key;
      }
      public R fun(T p){  //R是返回值类型，T为方法参数类型
          teturn null;    
      }
  
  }
  ```

* 测试：

  ```java
  public class GenericTest{
      public static void main(String[] args){
         //泛型的类型参数只能是类类型（包括自定义类），不能是简单类型
         //传入的实参类型需与泛型的类型参数类型相同，即为Integer.
         Generic<Integer,String> genericInteger = new Generic<Integer,String>(123456);
         //传入的实参类型需与泛型的类型参数类型相同，即为String.
         Generic<String,Integer> genericString = new Generic<String,Integer>("key_vlaue");
         System.out.println("泛型测试","key is " + genericInteger.getKey());
         System.out.println("泛型测试","key is " + genericString.getKey());
      }
  }
  ```

* 输出结果：

  ```html
  <!--
      泛型测试: key is 123456
      泛型测试: key is key_vlaue
  -->
  ```

**注意：泛型类的所有实例都具有相同的运行时类，而不管它们的实际类型参数如何。**

* 实例：

  ```java
  List<String> l1 = new ArrayList<String>();
  List<Integer> l2 = new ArrayList<Integer>();
  System.out.println(l1.getClass()== l2.getClass());    //true
  ```

> 注意：
>
> * 定义的泛型类，要么传入所有的泛型类型实参，要么不传入任何泛型类型实参。即:`Generic<Integer,String>...或Generic...`不能是`Generic<Integer>...`
> * 泛型的类型参数只能是类类型，不能是简单类型。
> * 不能对确切的泛型类型使用`instanceof`操作。如下操作是非法的，编译时会报错。`if(ex_num instanceof Generic<Number>){}`

#### 泛型接口：定义在接口上面

格式：`public interface 接口名<泛型类型1,...>`

任何情况下如果要使用接口，就必须定义相应的子类，而对于实现了泛型接口的子类而言，有以下两种实现方式：

1. 在子类继续设置泛型标记
2. 在子类不是设置泛型，而为接口明确定义一个泛型类型

* 示例：

  ```java
  //泛型接口
  interface Inter<T>{
  	void show(T t);
  }
  
  //实现类
  //1)知道实现什么泛型
  class InterImpl implements Inter<String>{
  	@Override
  	public void show(String s) {
  		System.out.println(s);
  	}
  }
  //2)不知道实现什么泛型
  class InterImpl02<T> implements Inter<T>{
  	@Override
  	public void show(T t) {
  		System.out.println(t);
  	}
  }
  
  public class GenericDemo03 {
  	public static void main(String[] args) {
  		//1)
  		InterImpl i = new InterImpl();
  		i.show("hello");           // hello
  		
  
  		//2)
  		InterImpl02<String> i2 = new InterImpl02<String>();
  		i2.show("world");          //world
  		InterImpl02<Integer> i3 = new InterImpl02<Integer>();
  		i3.show(13);               //13
  	}    
  
  }
  ```

> 注意：如果不声明泛型，如：`class InterImpl02 implements Inter<T>`，编译会报错`“Unknown class”`

#### 泛型方法：定义在方法上

格式：`修饰符 <泛型类型> 返回值类型 方法名(参数列表){}`

注意：

1. 泛型方法即在方法上设置泛型类型，参数中可以出现泛型类或类中未定义的泛型标识
2. 在方法上定义泛型时，这个方法不一定要在泛型类中定义
3. 泛型方法中可以出现任意多个泛型标识符
4. 静态的泛型方法需要额外的泛型声明，即使使用了泛型类声明过的泛型类型
5. 静态方法若有返回值其类型不能为泛型

* 示例：

  ```java
  //泛型方法
  class Generic<T>{//这个类是个泛型类，在上面已经介绍过
  	private T key;
  	
  
  	//这只是类中一个普通的成员方法，只不过他的返回值是在声明泛型类已经声明过的泛型。
  	public T getKey(){
  	    return key;
  	    }
  	/* 因为在类的声明中并未声明泛型K，所以在使用K做形参和返回值类型时，编译器会无法识别。
  	 * K cannot be resolved to a type
  	 * public K setKey(K key){	
  	    this.key = key;
  	}*/
  	//在泛型类中声明了一个泛型方法，使用泛型T，注意这个T是一种全新的类型，可以与泛型类中声明的T不是同一种类型
  	public <T> void show(T t){
  		System.out.println(t);
  	}
  	public <E> T fun(E e){
  		System.out.println(e);
  		return key;
  	}
  	//泛型的数量也可以为任意多个 
  	public <R,V> R showKeyName(R r,V v){
  		return null;
  	} 
  	/*
  	 * 如果在类中定义使用泛型的静态方法，需要添加额外的泛型声明（将这个方法定义成泛型方法）
  	 * 即使静态方法要使用泛型类中已经声明过的泛型也不可以。
  	 * 如：public static void method(T t){...},此时编译器会提示错误信息：
  	 * Cannot make a static reference to the non-static type T
  	 */
  	public static <E> void method(E t){}
  
  }
  public class GenericTest {
  	public static void main(String[] args) {
  		Generic od = new Generic();
  		od.show("hello");    //hello
  		Generic<Integer> od2 = new Generic<Integer>();
  		od2.show(132);       //132
  	}
  }
  ```

### 通配符

除了用 `<T>`表示泛型外，还有 `<?>`这种形式。`？` 被称为通配符。

```java
class Base{}

class Sub extends Base{}

Sub sub = new Sub();
Base base = sub;			
```

```java
List<Sub> lsub = new ArrayList<>();
List<Base> lbase = lsub;
```

最后一行代码成立吗？编译会通过吗？答案是**否定**的。

编译器不会让它通过的。Sub 是 Base 的子类，不代表 List<Sub>和 List<Base>有继承关系。

但是，在现实编码中，确实有这样的需求，希望泛型能够处理某一范围内的数据类型，比如某个类和它的子类，对此 Java 引入了通配符这个概念。所以，**通配符的出现是为了指定泛型中的类型范围**。

* `?`：表示任意类型
* `？ extends 类`：上边界限定通配符；
* `? super 类`：下边界限定通配符；

实例：

```java
import java.util.ArrayList;
import java.util.Collection;

//通配符
class Animal{}
class Cat extends Animal{}
class Dog extends Animal{}
public class GenericDemo05 {
	public static void main(String[] args) {
		//？ 任意类型
		Collection<?> c1 = new ArrayList<Animal>();
		Collection<?> c2 = new ArrayList<Cat>();
		Collection<?> c3 = new ArrayList<Dog>();
		

		//? extends E (向下限定)
		Collection<? extends Animal> c4 = new ArrayList<Cat>();
		Collection<? extends Animal> c5 = new ArrayList<Dog>();
		Collection<? extends Animal> c6 = new ArrayList<Animal>();

//		Collection<? extends Animal> c4 = new ArrayList<Object>();
		

		//? super E (向上限定)
		Collection<? super Animal> c7 = new ArrayList<Animal>();
		Collection<? super Animal> c8 = new ArrayList<Object>();

//		Collection<? super Animal> c7 = new ArrayList<Cat>();	
	}
}
```

#### 无限定通配符

无限定通配符经常与容器类配合使用，它其中的 `?` 其实代表的是未知类型，所以涉及到 `?` 时的操作，一定与具体类型无关。

```
public void testWildCards(Collection<?> collection){
}
```

上面的代码中，方法内的参数是被无限定通配符修饰的 `Collection` 对象，它隐略地表达了一个意图或者可以说是限定，那就是 `testWidlCards()` 这个方法内部无需关注 `Collection` 中的真实类型，因为它是未知的。所以，你只能调用 `Collection` 中与类型无关的方法。

![这里写图片描述](D:\Java\笔记\图片\Java版本\泛型)

我们可以看到，当 `<?>`存在时，`Collection` 对象丧失了 `add()` 方法的功能，编译器不通过。
我们再看代码。

```java
List<?> wildlist = new ArrayList<String>();
wildlist.add(123);// 编译不通过
```

有人说，`<?>`提供了只读的功能，也就是它删减了增加具体类型元素的能力，只保留与具体类型无关的功能。它不管装载在这个容器内的元素是什么类型，它只关心元素的数量、容器是否为空？我想这种需求还是很常见的吧。

有同学可能会想，`<?>`既然作用这么渺小，那么为什么还要引用它呢？ 

个人认为，提高了代码的可读性，程序员看到这段代码时，就能够迅速对此建立极简洁的印象，能够快速推断源码作者的意图。

#### extends 上边界限定通配符

```java
public class GenericsAndCovariance {
    public static void main(String[] args) {
        // Wildcards allow covariance:
        List<? extends Fruit> flist = new ArrayList<Apple>();
        // Compile Error: can’t add any type of object:
        // flist.add(new Apple());
        // flist.add(new Fruit());
        // flist.add(new Object());
        flist.add(null); // Legal but uninteresting
        // We know that it returns at least Fruit:
        Fruit f = flist.get(0);
    }
}
```

上面的例子中， `flist` 的类型是 `List<? extends Fruit>`，我们可以把它读作：一个类型的 List， 这个类型可以是继承了 `Fruit` 的某种类型。注意，**这并不是说这个 List 可以持有** `Fruit` **的任意类型**。通配符代表了一种特定的类型，它表示 “某种特定的类型，但是 `flist` 没有指定”。这样不太好理解，具体针对这个例子解释就是，`flist` 引用可以指向某个类型的 List，只要这个类型继承自 `Fruit`，可以是 `Fruit` 或者 `Apple`，比如例子中的 `new ArrayList<Apple>`，但是为了向上转型给 `flist`，`flist` 并不关心这个具体类型是什么。

如上所述，通配符 `List<? extends Fruit>` 表示某种特定类型 ( `Fruit` 或者其子类 ) 的 List，但是并不关心这个实际的类型到底是什么，反正是 `Fruit` 的子类型，`Fruit` 是它的上边界。那么对这样的一个 List 我们能做什么呢？其实如果我们不知道这个 List 到底持有什么类型，怎么可能安全的添加一个对象呢？在上面的代码中，向 `flist` 中添加任何对象，无论是 `Apple` 还是 `Orange` 甚至是 `Fruit` 对象，编译器都不允许，唯一可以添加的是 `null`。所以如果做了泛型的向上转型 (`List<? extends Fruit> flist = new ArrayList<Apple>()`)，那么我们也就失去了向这个 List 添加任何对象的能力，即使是 `Object` 也不行。

另一方面，如果调用某个返回 `Fruit` 的方法，这是安全的。因为我们知道，在这个 List 中，不管它实际的类型到底是什么，但肯定能转型为 `Fruit`，所以编译器允许返回 `Fruit`。

了解了通配符的作用和限制后，好像任何接受参数的方法我们都不能调用了。其实倒也不是，看下面的例子：

```java
public class CompilerIntelligence {
    public static void main(String[] args) {
        List<? extends Fruit> flist =
        Arrays.asList(new Apple());
        Apple a = (Apple)flist.get(0); // No warning
        flist.contains(new Apple()); // Argument is ‘Object’
        flist.indexOf(new Apple()); // Argument is ‘Object’
        
        //flist.add(new Apple());   无法编译

    }
}
```

在上面的例子中，`flist` 的类型是 `List<? extends Fruit>`，泛型参数使用了受限制的通配符，所以我们失去了向其中加入任何类型对象的例子，最后一行代码无法编译。

但是 `flist` 却可以调用 `contains` 和 `indexOf` 方法，它们都接受了一个 `Apple` 对象做参数。如果查看 `ArrayList` 的源代码，可以发现 `add()` 接受一个泛型类型作为参数，但是 `contains` 和 `indexOf` 接受一个 `Object` 类型的参数，下面是它们的方法签名：

```java
public boolean add(E e)
public boolean contains(Object o)
public int indexOf(Object o)

```

所以如果我们指定泛型参数为 `<? extends Fruit>` 时，`add()` 方法的参数变为 `? extends Fruit`，编译器无法判断这个参数接受的到底是 `Fruit` 的哪种类型，所以它不会接受任何类型。

然而，`contains` 和 `indexOf` 的类型是 `Object`，并没有涉及到通配符，所以编译器允许调用这两个方法。这意味着一切取决于泛型类的编写者来决定那些调用是 “安全” 的，并且用 `Object` 作为这些安全方法的参数。如果某些方法不允许类型参数是通配符时的调用，这些方法的参数应该用类型参数，比如 `add(E e)`。

当我们自己编写泛型类时，上面介绍的就有用了。下面编写一个 `Holder` 类：

```java
public class Holder<T> {
    
    private T value;
    public Holder() {}
    public Holder(T val) { value = val; }
    public void set(T val) { value = val; }
    public T get() { return value; }
    public boolean equals(Object obj) {
    return value.equals(obj);
    }
    
    public static void main(String[] args) {
        
        Holder<Apple> Apple = new Holder<Apple>(new Apple());
        Apple d = Apple.get();
        Apple.set(d);
        
        // Holder<Fruit> Fruit = Apple; // Cannot upcast
        Holder<? extends Fruit> fruit = Apple; // OK
        Fruit p = fruit.get();
        d = (Apple)fruit.get(); // Returns ‘Object’
        
        try {
            Orange c = (Orange)fruit.get(); // No warning
        } catch(Exception e) { System.out.println(e); }
        // fruit.set(new Apple()); // Cannot call set()
        // fruit.set(new Fruit()); // Cannot call set()
        
        System.out.println(fruit.equals(d)); // OK
    }
} 
/* Output: (Sample)
java.lang.ClassCastException: Apple cannot be cast to Orange
true
*///:~

```

在 `Holer` 类中，`set()` 方法接受类型参数 `T` 的对象作为参数，`get()` 返回一个 `T` 类型，而 `equals()` 接受一个 `Object` 作为参数。`fruit` 的类型是 `Holder<? extends Fruit>`，所以`set()`方法不会接受任何对象的添加，但是 `equals()` 可以正常工作。

#### 下边界限定通配符

通配符的另一个方向是　“超类型的通配符“: `? super T`，`T` 是类型参数的下界。使用这种形式的通配符，我们就可以 ”传递对象” 了。还是用例子解释：

```java
public class SuperTypeWildcards {
    static void writeTo(List<? super Apple> apples) {
        apples.add(new Apple());
        apples.add(new Jonathan());
        // apples.add(new Fruit()); // Error
    }
}
```

`writeTo` 方法的参数 `apples` 的类型是 `List<? super Apple>`，它表示某种类型的 List，这个类型是 `Apple` 的基类型。也就是说，我们不知道实际类型是什么，但是这个类型肯定是 `Apple` 的父类型。因此，我们可以知道向这个 List 添加一个 `Apple` 或者其子类型的对象是安全的，这些对象都可以向上转型为 `Apple`。但是我们不知道加入 `Fruit` 对象是否安全，因为那样会使得这个 List 添加跟 `Apple` 无关的类型。

在了解了子类型边界和超类型边界之后，我们就可以知道如何向泛型类型中 “写入” ( 传递对象给方法参数) 以及如何从泛型类型中 “读取” ( 从方法中返回对象 )。下面是一个例子：

```java
public class Collections { 
  public static <T> void copy(List<? super T> dest, List<? extends T> src) 
  {
      for (int i=0; i<src.size(); i++) 
        dest.set(i,src.get(i)); 
  } 
}
```

`src` 是原始数据的 List，因为要从这里面读取数据，所以用了上边界限定通配符：`<? extends T>`，取出的元素转型为 `T`。`dest` 是要写入的目标 List，所以用了下边界限定通配符：`<? super T>`，可以写入的元素类型是 `T` 及其子类型。

### Java类型擦除机制

泛型是 Java 1.5 版本才引进的概念，在这之前是没有泛型的概念的，但显然，泛型代码能够很好地和之前版本的代码很好地兼容。

这是因为，**泛型信息只存在于代码编译阶段，在进入 JVM 之前，与泛型相关的信息会被擦除掉，专业术语叫做类型擦除。**

通俗地讲，泛型类和普通类在 java 虚拟机内是没有什么特别的地方。回顾文章开始时的那段代码

```java
List<String> l1 = new ArrayList<String>();
List<Integer> l2 = new ArrayList<Integer>();
		
System.out.println(l1.getClass() == l2.getClass());
```

打印的结果为 true 是因为 `List<String>`和 `List<Integer>`在 jvm 中的 Class 都是 List.class。

泛型信息被擦除了。

可能同学会问，那么类型 String 和 Integer 怎么办？答案是泛型转译。

```java
public class Erasure <T>{
	T object;

	public Erasure(T object) {
		this.object = object;
	}

}
```

Erasure 是一个泛型类，我们查看它在运行时的状态信息可以通过反射。

```java
Erasure<String> erasure = new Erasure<String>("hello");
Class eclz = erasure.getClass();
System.out.println("erasure class is:"+eclz.getName());
```

打印的结果是

```java
erasure class is:com.frank.test.Erasure
```

Class 的类型仍然是 Erasure 并不是 `Erasure<T>`这种形式，那我们再看看泛型类中 `T` 的类型在 `jvm` 中是什么具体类型。

```java
Field[] fs = eclz.getDeclaredFields();
for ( Field f:fs) {
	System.out.println("Field name "+f.getName()+" type:"+f.getType().getName());
}
```

打印结果是

```java
Field name object type:java.lang.Object
```

那我们可不可以说，泛型类被类型擦除后，相应的类型就被替换成 Object 类型呢？

这种说法，不完全正确。

我们更改一下代码。

```java
public class Erasure <T extends String>{
//	public class Erasure <T>{
	T object;

	public Erasure(T object) {
		this.object = object;
	}

}
```

现在再看测试结果：

```java
Field name object type:java.lang.String
```

我们现在可以下结论了，在泛型类被类型擦除的时候，之前泛型类中的类型参数部分如果没有指定上限，如 `<T>`则会被转译成普通的 Object 类型，如果指定了上限如 `<T extends String>`则类型参数就被替换成类型上限。

所以，在反射中。

```java
public class Erasure <T>{
	T object;

	public Erasure(T object) {
		this.object = object;
	}
	
	public void add(T object){
		
	}
	
}
```

add() 这个方法对应的 Method 的签名应该是 Object.class。

```java
Erasure<String> erasure = new Erasure<String>("hello");
Class eclz = erasure.getClass();
System.out.println("erasure class is:"+eclz.getName());

Method[] methods = eclz.getDeclaredMethods();
for ( Method m:methods ){
	System.out.println(" method:"+m.toString());
}
```

打印结果是

```java
 method:public void com.frank.test.Erasure.add(java.lang.Object)
```

也就是说，如果你要在反射中找到 add 对应的 Method，你应该调用 `getDeclaredMethod("add",Object.class)`否则程序会报错，提示没有这么一个方法，原因就是类型擦除的时候，T 被替换成 Object 类型了。

#### 类型擦除带来的局限性

类型擦除，是泛型能够与之前的 java 版本代码兼容共存的原因。但也因为类型擦除，它会抹掉很多继承相关的特性，这是它带来的局限性。

理解类型擦除有利于我们绕过开发当中可能遇到的雷区，同样理解类型擦除也能让我们绕过泛型本身的一些限制。比如

![这里写图片描述](D:\Java\笔记\图片\Java版本\类型擦除局限性)

正常情况下，因为泛型的限制，编译器不让最后一行代码编译通过，因为类似不匹配，但是，基于对类型擦除的了解，利用反射，我们可以绕过这个限制。

```java
public interface List<E> extends Collection<E>{
	
	 boolean add(E e);
}
```

上面是 List 和其中的 add() 方法的源码定义。

因为 E 代表任意的类型，所以类型擦除时，add 方法其实等同于

```java
boolean add(Object obj);
```

那么，利用反射，我们绕过编译器去调用 add 方法。

```java
public class ToolTest {

	public static void main(String[] args) {
		List<Integer> ls = new ArrayList<>();
		ls.add(23);
//		ls.add("text");
		try {
			Method method = ls.getClass().getDeclaredMethod("add",Object.class);
			
			
			method.invoke(ls,"test");
			method.invoke(ls,42.9f);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for ( Object o: ls){
			System.out.println(o);
		}
	}
}
```

打印结果是：

```html
<!--
    23
    test
    42.9
-->
```

可以看到，利用类型擦除的原理，用反射的手段就绕过了正常开发中编译器不允许的操作限制。

### 泛型与数组

关于泛型数组的理解真的有点令人恼火（annoying）下面内容看看就行。在The Java™ Tutorials: Generics中讲到了泛型数组，并说道：除非使用通配符，否则一个数组对象的元素不能是泛型。即：在java中是”不能创建一个确切的泛型类型的数组”的。也就是说下面的这个例子是不可以的：

```java
List<String>[] ls = new ArrayList<String>[10];//Cannot create a generic array of ArrayList<String>
```

 而使用通配符创建泛型数组是可以的，如下面这个例子：

```java
List<?>[] ls = new ArrayList<?>[10]; 
//这样也是可以的：
List<String>[] ls = new ArrayList[10];
```

这么做的原因，是为了防止下述代码产生的类型安全问题：

```java
// Not really allowed.
List<String>[] lsa = new List<String>[10];     //1
Object o = lsa;
Object[] oa = (Object[]) o;
List<Integer> li = new ArrayList<Integer>();
li.add(new Integer(3));
// Unsound, but passes run time store check
oa[1] = li;

// Run-time error: ClassCastException.
String s = lsa[1].get(0);                      //2
```

 如果允许泛型数组的存在（第1处代码编译通过），那么在第2处代码就会报出ClassCastException，因为lsa[1]是List<Integer>。Java设计者本着首要保证类型安全（type-safety）的原则，不允许泛型数组的存在，使得编译期就可以检查到这类错误。

解决方案

但是连Java的设计者也承认，这样在使用上很令人恼火（原文是annoying），所以提供了变向的解决方案：显式类型转换。

①通配符

The Java™ Tutorials: Generics给出的解决方案如下：

```java
// OK, array of unbounded wildcard type.
List<?>[] lsa = new List<?>[10];                //1
Object o = lsa;
Object[] oa = (Object[]) o;
List<Integer> li = new ArrayList<Integer>();
li.add(new Integer(3));
// Correct.
oa[1] = li;
// Run time error, but cast is explicit.
String s = (String) lsa[1].get(0);              //2
```

 在第1处，用?取代了确定的参数类型。根据通配符的定义以及Java类型擦除的保留上界原则，在2处lsa[1].get(0)取出的将会是Object，所以需要程序员做一次显式的类型转换。

②反射

使用java.util.reflect.Array，可以不使用通配符，而达到泛型数组的效果：

```java
List<String>[] lsa = (List<String>[])Array.newInstance(ArrayList.class, 4);     //1
Object o = lsa;
Object[] oa = (Object[]) o;
List<Integer> li = new ArrayList<Integer>();
li.add(new Integer(3));
// Correct.
oa[1] = li;
// Run time error, but cast is explicit.
String s = lsa[1].get(0);                                                      //2
```

 可以看到，我们利用了Array.newInstance()生成了泛型数组，这里没有使用任何通配符，在第2处也没有做显式的类型转换，但是在第1处，仍然存在显式类型转换。

类似地，尝试创建元素类型为类型变量的数组对象会导致编译时错误：

```java
//不能用泛型来建立数组的实例
private T[] arr= new T[10];    //Cannot create a generic array of T
//可以参数化数组本身类型
private T[] arr;

<T> T[] makeArray(T t) {
    return new T[100]; // Error.
}

```

## 8. for-each循环

foreach 语法格式如下：

```java
for(元素类型t 元素变量x : 遍历对象obj){ 
     引用了x的java语句; 
} 
```

* 实例：用foreach遍历数组、集合

  ```java
  import java.util.ArrayList;
  import java.util.List;
   
  public class AddForDemo {
  	public static void main(String[] args) {
  		// foreach遍历数组
  		int[] arr = {1,2,3,4,5};
  		for(int num:arr){//num指的是arr数组里面所有元素
  			System.out.println(num);
  		}
  		//foreach遍历List
  		List<String> list = new ArrayList<String>();
  		list.add("hello");
  		list.add("world");
  		list.add("java");
  		for(String str:list){//str指的是list集合中所有元素
  			System.out.println(str);
  		}
  	}
  }
  ```

> **注意：**foreach虽然能遍历数组或者集合，但是只能用来遍历，无法在遍历的过程中对数组或者集合进行修改，而for循环可以在遍历的过程中对源数组或者集合进行修改。

* 实例：

  ```java
  public static void main(String[] args) {
      List<String> names = new ArrayList<String>();
      names.add("beibei");
      names.add("jingjing");
      //foreach
      for(String name:names){
        name = "huanhuan";
      }
      System.out.println(Arrays.toString(names.toArray()));
      //for
      for (int i = 0; i < names.size(); i++) {
        names.set(i,"huanhuan");
      }
      System.out.println(Arrays.toString(names.toArray()));
    }
  }
   
  输出：
  [beibei, jingjing]
  [huanhuan, huanhuan]
  ```

foreach遍历的是一组元素，但可以在外部定义一个索引(int index = 0;)，在内部进行自增操作(index++)，来实现类似普通for中需要使用索引的操作。

foreach遍历集合类型和数组类型底层实现的不同

- 集合类型的遍历本质是使用迭代器实现的
- 数组的遍历是通过for循环来实现的

# JDK1.8的新特性

## 1. 接口的默认方法

我们通常所说的接口的作用是用于定义一套标准、约束、规范等，接口中的方法只声明方法的签名，不提供相应的方法体，方法体由对应的实现类去实现。

在JDK1.8中打破了这样的认识，接口中的方法可以有方法体，**但需要关键字static或者default来修饰**，使用static来修饰的称之为**静态方法**，静态方法通过接口名来调用，使用default来修饰的称之为**默认方法**，默认方法通过实例对象来调用。

静态方法和默认方法都有自己的方法体，用于提供一套默认的实现，这样子类对于该方法就不需要强制来实现，可以选择使用默认的实现，也可以重写自己的实现。

<font color = "red">当为接口扩展方法时，只需要提供该方法的默认实现即可，至于对应的实现类可以重写也可以使用默认的实现，这样所有的实现类不会报语法错误：Xxx不是抽象的, 并且未覆盖Yxx中的抽象方法</font>。

IHello接口

```java
public interface IHello {

    // 使用abstract修饰不修饰都行
    void sayHi();

    static void sayHello(){
        System.out.println("static method: say hello");
    }

    default void sayByebye(){
        System.out.println("default mehtod: say byebye");
    }
}
```

HelloImpl实现类

```java
public class HelloImpl implements IHello {
    @Override
    public void sayHi() {
        System.out.println("normal method: say hi");
    }
}
```

Main

```java
public class Main {
    public static void main(String[] args) {
        HelloImpl helloImpl = new HelloImpl();
        // 对于abstract抽象方法通过实例对象来调用
        helloImpl.sayHi();
        // default方法只能通过实例对象来调用
        helloImpl.sayByebye();

        // 静态方法通过 接口名.方法名() 来调用
        IHello.sayHello();


        // 接口是不允许new的，如果使用new后面必须跟上一对花括号用于实现抽象方法， 这种方式被称为匿名实现类，匿名实现类是一种没有名称的实现类
        // 匿名实现类的好处：不用再单独声明一个类，缺点：由于没有名字，不能重复使用，只能使用一次
        new IHello() {
            @Override
            public void sayHi() {
                System.out.println("normal method: say hi");
            }
        }.sayHi();
    }
}
```

执行结果：

```html
<!--
    normal method: say hi
    default mehtod: say byebye
    static method: say hello
    normal method: say hi
-->
```

## 2. Lambda表达式

<!-- D:\Java\笔记\第二个老师课程\day07有详解 -->

lambda表达式本质上是一段匿名内部类，也可以是一段可以传递的代码

Lambda 允许把函数作为一个方法的参数，使用Lambda表达式可以使代码变的更加简洁紧凑、简洁表达。

lambda表达式的语法：

```java
(parameters) -> expression
或
(parameters) ->{ statements; }

(x)->{system.out.prinln} //直接打印x参数; 
```

lambda表达式简单例子：

```java
public class Java8TestLambda {

    public static  void main(String [] args){
        CacualtePriceService cacualteAddPriceService=(x1,x2)->(x1+x2);
        CacualtePriceService cacualteSubPriceService=(x1,x2)->(x1-x2)
        
        System.out.println(cacualteAddPriceService.cacluatePrice(1.2f,2.2f));//3.4
        System.out.println(cacualteSubPriceService.cacluatePrice(3.2f,1.2f));//2.0
        System.out.println("------------------使用策略模式----------------------");
        System.out.println(operate(1.2f,2.2f,(x1,x2)->(x1+x2)));//3.4
        System.out.println(operate(3.2f,1.2f,(x1,x2)->(x1-x2)));//2.0
    }
    /**
     * 计算价格接口
     */
    interface CacualtePriceService{
        Float cacluatePrice(Float x1,Float x3);
    }

    static Float operate(float x1, float x2, CacualtePriceService cacualtePriceService){
       return cacualtePriceService.cacluatePrice(x1,x2);
    }
}
```

打印结果：

```java
3.4
2.0
------------------使用策略模式----------------------
3.4
2.0
```

由上面的例子可知，Lambda可以简化函数接口(接口中只有一个方法，例如上述例子中CacualtePriceService接口)的实现方法。

**lambda表达式的重要特征:**

- 可选类型声明：不需要声明参数类型，编译器可以统一识别参数值((x1,x2))。
- 可选的参数圆括号：一个参数无需定义圆括号，但多个参数需要定义圆括号。
- 可选的大括号：如果函数主体包含了一个语句，就不需要使用大括号。
- 可选的返回关键字：如果主体只有一个表达式返回值则编译器会自动返回值，大括号需要指定表达式返回一个数值。
- **lambda 表达式的局部变量可以不用声明为 final，但是必须不可被后面的代码修改**（即隐性的具有 final 的语义,详细见如下案例）

```java
// static final float num=1.1f;
public static  void main(String [] args){
    float num=1.1f;
    CacualtePriceService cacualteAddPriceService=(x1,x2)->(x1+x2+num);
    CacualtePriceService cacualteSubPriceService=(x1,x2)->(x1-x2+num);
    num=2；

}

console://当修改num=2 时候会报错：
Error:(14, 70) java: 从lambda 表达式引用的本地变量必须是最终变量或实际上的最终变量
```

当修改num=2 时候会报错：**从lambda 表达式引用的本地变量必须是最终变量或实际上的最终变量**。

### 优点：

1. 简洁。

2. 非常容易并行计算。

3. 可能代表未来的编程趋势。

4. 结合 hashmap 的 computeIfAbsent 方法，递归运算非常快。java有针对递归的专门优化。

### 缺点：

1. 若不用并行计算，很多时候计算速度没有比传统的 for 循环快。（并行计算有时需要预热才显示出效率优势）
2. 不容易调试。
3. 若其他程序员没有学过 lambda 表达式，代码不容易让其他语言的程序员看懂。
4. 在 lambda 语句中强制类型转换貌似不方便，一定要搞清楚到底是 map 还是 mapToDouble 还是 mapToInt

## 3. 函数式接口

<!-- D:\Java\笔记\第二个老师课程\day12有详解 -->

### 消费型接口（有去无回）

`Consumer` : 消费型接口 `void accept(T t)`；传入一个参数消费，没有返回值

```java
public static void main(String[] args) {
	// 消费型接口
    test1(10,v -> System.out.println("我想要消费这个数字"+v));
}


// 消费型接口
public static void test1(Integer num, Consumer<Integer> consumer){
    consumer.accept(num);
}
```

### 供给型接口

`Supplier` : 供给型接口 `T get()`；没有参数，返回一个指定的类型数据

```java
public static void main(String[] args) {
	// 生成十个随机数
	// 供给型接口
    System.out.println(test2(10, () -> (int)(Math.random()*100)));
}

// 供给型接口
public static List<Integer> test2(Integer num, Supplier<Integer> supplier){
    List<Integer> list = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
        list.add(supplier.get());
    }
    return list;
}
```

### 函数型接口

`Function<T,R>` : 函数型接口 `R apply(T t)`；指定两个类型，第一个类型为参数类型，第二个为返回值类型

```java
public static void main(String[] args) {
	// 函数式接口
    System.out.println(test3("    777   ",v -> v.trim()));
}
// 函数式接口
public static String test3(String str, Function<String,String> fun){
    return fun.apply(str);
}
```

### 断言型接口

`Predicate` : 断言型接口 `boolean test(T t)`;

```java
public static void main(String[] args) {

    List<User> list = Arrays.asList(
            new User("测试1","男",18),
            new User("测试1","男",14),
            new User("测试1","男",27),
            new User("测试1","男",36)
    );
    // 筛选出年龄大于17岁的用户
	// 断言型接口
    System.out.println(test4(list, v -> v.getAge() > 17));
}

// 断言型接口
public static List<User> test4(List<User> users, Predicate<User> predicate){
    List<User> list = new ArrayList<>();
    users.forEach(v -> {
        if(predicate.test(v)){
            list.add(v);
        }
    });
    return list;
}
```

## 4. 方法与构造函数引用

<!--之前有老师讲过 D:\Java\笔记\第二个老师课程\day13-->

jdk1.8提供了另外一种调用方式`::`，当 你 需 要使用 方 法 引用时 ， 目 标引用 放 在 分隔符`::`前 ，方法 的 名 称放在 后 面 ，即`ClassName :: methodName` 。例如 ，`Apple::getWeight`就是引用了Apple类中定义的方法getWeight。请记住，不需要括号，因为你没有实际调用这个方法。方法引用就是Lambda表达式`(Apple a) -> a.getWeight()`的快捷写法，如下示例。

```java
//先定义一个函数式接口
@FunctionalInterface
public interface TestConverT<T, F> {
    F convert(T t);
}
```

测试如下，可以以`::`形式调用。

```java
public void test(){
    TestConverT<String, Integer> t = Integer::valueOf;
    Integer i = t.convert("111");
    System.out.println(i);
}
```

此外，对于构造方法也可以这么调用。

```java
//实体类User和它的构造方法
public class User {    
    private String name;
    
    private String sex;

    public User(String name, String sex) {
        super();
        this.name = name;
        this.sex = sex;
    }
}
//User工厂
public interface UserFactory {
    User get(String name, String sex);
}
//测试类
    UserFactory uf = User::new;
    User u = uf.get("ww", "man");
```

这里的`User::new`就是调用了User的构造方法，Java编译器会自动根据`UserFactory.get`方法的签名来选择合适的构造函数。

## 5.多重注解

<!-- D:\Java\笔记\第三个老师课程\day00 【Junit单元测试、反射、注解】里面有一些基础的知识 -->

在JDK1.8之后标注在类，方法等上面的注解可以重复出现，如下图

![在这里插入图片描述](D:\Java\笔记\图片\Java版本\多重注解)

但是如果你直接在方法等上面注多个相同的注解，程序还是会报错，错误信息提示注解`MyAnnotation`没有被一个`Repeatable`注解修饰，而`Repeatable`注解里面传入的参数必须也是一个注解，这个注解所包含的值必须有一个要重复注解的这个注解类型的数组。

我们同样可以和以前一样通过反射得到注解的值，如下代码

```java
public class TestAnnotation {
    @MyAnnotation("Hello")
    @MyAnnotation("World")
    public void show(){

    }

    @Test
    public void test01() throws NoSuchMethodException {
        Class<TestAnnotation> testAnnotationClass = TestAnnotation.class;
        Method show = testAnnotationClass.getMethod("show");
        MyAnnotation[] annotationsByType = show.getAnnotationsByType(MyAnnotation.class);
        Arrays.stream(annotationsByType)
                .map(MyAnnotation::value)
                .forEach(System.out::println);
    }
}

@Repeatable(MyAnnotations.class)
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotation{
    String value() default "annotation";
}

@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotations{
    MyAnnotation[] value();
}
```

结果如图：

![在这里插入图片描述](D:\Java\笔记\图片\Java版本\多重注解2)

### 使用步骤如下

1、定义重复的注解容器注解

2、定义一个可以重复的注解

```java
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
 
 
@MyTest("ca")
@MyTest("cb")
public class Demo4 {
    @MyTest("ma")
    @MyTest("mb")
    public void show() {
 
    }
 
    public static void main(String[] args) {
        MyTest[] annotationsByType = Demo4.class.getAnnotationsByType(MyTest.class);
        for (MyTest myTest : annotationsByType) {
            System.out.println(myTest);
        }
        System.out.println("--------------------");
        try {
            MyTest[] shows = Demo4.class.getMethod("show").getAnnotationsByType(MyTest.class);
            for (MyTest show : shows) {
                System.out.println(show);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
 
/**
 * 1、定义重复的注解容器注解
 */
@Retention(RetentionPolicy.RUNTIME)
@interface MyTests {
    MyTest[] value();
}
 
/**
 * 2、定义一个可以重复的注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MyTests.class)
@interface MyTest {
    String value();
}
```

## 6. Stream流

<!--之前有老师讲过 D:\Java\笔记\第二个老师课程\day13-->

在Java1.8之前还没有stream流式算法的时候，我们要是在一个放有多个User对象的list集合中，将每个User对象的主键ID取出，组合成一个新的集合，首先想到的肯定是遍历，如下：

```java
List<Long> userIdList = new ArrayList<>();
for (User user: list) {
     userIdList.add(user.id);
}
```

或者在1.8有了lambda表达式以后，我们会这样写：

```java
List<Long> userIdList = new ArrayList<>();
list.forEach(user -> list.add(user.id));
```

在有了stream之后，我们还可以这样写：

```java
List<Long> userIdList = list.stream().map(User::getId).collect(Collectors.toList());　　
```

一行代码直接搞定，是不是很方便呢。那么接下来。我们就一起看一下`stream`这个流式算法的新特性吧。

由上面的例子可以看出，java8的流式处理极大的简化了对于集合的操作，**实际上不光是集合，包括数组、文件等，只要是可以转换成流，我们都可以借助流式处理，**类似于我们写SQL语句一样对其进行操作。

java8通过内部迭代来实现对流的处理，一个流式处理可以分为三个部分：转换成流、中间操作、终端操作。如下图：

 ![](D:\Java\笔记\图片\Java版本\1Stream流.png)

以集合为例，一个流式处理的操作我们首先需要调用`stream()`函数将其转换成流，然后再调用相应的中间操作达到我们需要对集合进行的操作，比如筛选、转换等，最后通过终端操作对前面的结果进行封装，返回我们需要的形式。

这里我们先创建一个User实体类：

```java
class User{
    private Long id;       //主键id
    private String name;   //姓名
    private Integer age;   //年龄
    private String school; //学校
    public User(Long id, String name, Integer age, String school) {
       this.id = id;
       this.name = name;
       this.age = age;
       this.school = school;
}
// 此处省略get、set方法。。。
```

初始化：

```java
List<User> list = new ArrayList<User>(){
    {
        add(new User(1l,"张三",10, "清华大学"));
        add(new User(2l,"李四",12, "清华大学"));
        add(new User(3l,"王五",15, "清华大学"));
        add(new User(4l,"赵六",12, "清华大学"));
        add(new User(5l,"田七",25, "北京大学"));
        add(new User(6l,"小明",16, "北京大学"));
        add(new User(7l,"小红",14, "北京大学"));
        add(new User(8l,"小华",14, "浙江大学"));
        add(new User(9l,"小丽",17, "浙江大学"));
        add(new User(10l,"小何",10, "浙江大学"));
    }
};
```

### 6.1 过滤

**6.1.1 filter**

可以通过filter方法将一个流转换成另一个子集流。方法：

```java
Stream<T> filter (Predicate<? super T> predicate);
```

该接口接收一个`Predicate`函数式接口参数（可以是一个Lambda或者方法引用）作为筛选条件。

此前学过`java.util.stream.Predicate`函数式接口，其中唯一的抽象方法为：

```java
boolean test(T t);
```

该方法将会产生一个Boolean值结果，代表指定的条件是否满足。如果结果为TRUE，那么Stream流的`filter`方法将会留用元素；如果结果为FALSE，那么`filter`方法将会舍弃元素。

我们希望过滤赛选处所有学校是清华大学的user：

```java
System.out.println("学校是清华大学的user");
List<User> userList1 = 
    					list.stream().filter(user -> "清华大学"
                        .equals(user.getSchool()))
    					.collect(Collectors.toList());
// Collectors.toList() 将流中的所有元素导出到一个列表( List )中。不会弄到一个流中
userList1.forEach(user -> System.out.print(user.name + '、'));
```

控制台输出结果为：

```html
<!--
    学校是清华大学的user
	张三、李四、王五、赵六、
-->
```

> Collectors.toList() 将流中的所有元素导出到一个列表( List )中。不会弄到一个流中

**6.1.2 distinct**

去重，我们希望获取所有user的年龄（年龄不重复）

```java
System.out.println("所有user的年龄集合");
List<Integer> userAgeList =
    						list.stream().map(User::getAge)
    						.distinct()
    						.collect(Collectors.toList());
System.out.println("userAgeList = " + userAgeList);
```

map在下面会讲到，现在主要是看distinct的用法，输出结果如下： 

```html
<!--
    所有user的年龄集合
    userAgeList = [10, 12, 15, 25, 16, 14, 17]
-->
```

**6.1.3 limit** 

返回前n个元素的流，当集合的长度小于n时，则返回所有集合。

如获取年龄是偶数的前2名user：

```java
System.out.println("年龄是偶数的前两位user");
List<User> userList3 = list.stream().filter(user -> user.getAge() % 2 == 0).limit(2).collect(Collectors.toList());
userList3.forEach(user -> System.out.print(user.name + '、'));
```

输出结果为：

```java
年龄是偶数的前两位user
张三、李四、
```

**6.1.4 sorted**

排序，如现在我想将所有user按照age从大到小排序：

```java
System.out.println("按年龄从大到小排序");
List<User> userList4 = list.stream().sorted((s1,s2) -> s2.age - s1.age).collect(Collectors.toList());
userList4.forEach(user -> System.out.print(user.name + '、'));
```

输出结果为： 

```java
按年龄从大到小排序
田七、小丽、小明、王五、小红、小华、李四、赵六、张三、小何、
```

**6.1.5 skip**

跳过n个元素后再输出

如输出list集合跳过前两个元素后的list

```java
System.out.println("跳过前面两个user的其他所有user");
List<User> userList5 = list.stream().skip(2).collect(Collectors.toList());
userList5.forEach(user -> System.out.print(user.name + '、'));
```

输出结果为： 

```java
跳过前面两个user的其他所有user
王五、赵六、田七、小明、小红、小华、小丽、小何、
```

### 6.2 映射

**6.2.1 map**

就是讲user这个几个精简为某个字段的集合

如我现在想知道学校是清华大学的所有学生的姓名：

```java
System.out.println("学校是清华大学的user的名字");
List<String> userList6 = list.stream().filter(user -> "清华大学".equals(user.school)).map(User::getName).collect(Collectors.toList());
userList6.forEach(user -> System.out.print(user + '、'));　　
```

输出结果如下：

```java
学校是清华大学的user的名字
张三、李四、王五、赵六、
```

除了上面这类基础的map，java8还提供了mapToDouble(ToDoubleFunction<? super T> mapper)，mapToInt(ToIntFunction<? super T> mapper)，mapToLong(ToLongFunction<? super T> mapper)，这些映射分别返回对应类型的流，java8为这些流设定了一些特殊的操作，比如查询学校是清华大学的user的年龄总和：

```java
System.out.println("学校是清华大学的user的年龄总和");
int userList7 = list.stream().filter(user -> "清华大学".equals(user.school)).mapToInt(User::getAge).sum();
System.out.println( "学校是清华大学的user的年龄总和为： "+userList7);
```

输出结果为：

```java
学校是清华大学的user的年龄总和
学校是清华大学的user的年龄总和为： 49
```

**6.2.2 flatMap**

flatMap与map的区别在于 flatMap是将一个流中的每个值都转成一个个流，然后再将这些流扁平化成为一个流 。举例说明，假设我们有一个字符串数组String[] strs = {"hello", "world"};，我们希望输出构成这一数组的所有非重复字符，那么我们用map和flatMap 实现如下：

```java
String[] strings = {"Hello", "World"};
List l11 = Arrays.stream(strings).map(str -> str.split("")).map(str2->Arrays.stream(str2)).distinct().collect(Collectors.toList());
List l2 = Arrays.asList(strings).stream().map(s -> s.split("")).flatMap(Arrays::stream).distinct().collect(Collectors.toList());
System.out.println(l11.toString());
System.out.println(l2.toString());　
```

输出结果如下：

```java
[java.util.stream.ReferencePipeline$Head@4c203ea1, java.util.stream.ReferencePipeline$Head@27f674d]
[H, e, l, o, W, r, d]　
```

由上我们可以看到使用map并不能实现我们现在想要的结果，而flatMap是可以的。这是因为在执行map操作以后，我们得到是一个包含多个字符串（构成一个字符串的字符数组）的流，此时执行distinct操作是基于在这些字符串数组之间的对比，所以达不到我们希望的目的；flatMap将由map映射得到的Stream<String[]>，转换成由各个字符串数组映射成的流Stream<String>，再将这些小的流扁平化成为一个由所有字符串构成的大流Steam<String>，从而能够达到我们的目的。

### 6.3 查找

**6.3.1 allMatch**

用于检测是否全部都满足指定的参数行为，如果全部满足则返回true，例如我们判断是否所有的user年龄都大于9岁，实现如下：

```java
System.out.println("判断是否所有user的年龄都大于9岁");
Boolean b = list.stream().allMatch(user -> user.age >9);
System.out.println(b);
```

输出结果为：

```
判断是否所有user的年龄都大于9岁
true
```

**6.3.2 anyMatch**

anyMatch则是检测是否存在一个或多个满足指定的参数行为，如果满足则返回true，例如判断是否有user的年龄大于15岁，实现如下：

```java
System.out.println("判断是否有user的年龄是大于15岁");
Boolean bo = list.stream().anyMatch(user -> user.age >15);
System.out.println(bo);
```

输出结果为：

```java
判断是否有user的年龄是大于15岁
true
```

**6.3.3 noneMatch**　　

noneMatch用于检测是否不存在满足指定行为的元素，如果不存在则返回true，例如判断是否不存在年龄是15岁的user，实现如下：

```java
System.out.println("判断是否不存在年龄是15岁的user");
Boolean boo = list.stream().noneMatch(user -> user.age == 15);
System.out.println(boo);
```

输出结果如下：

```java
判断是否不存在年龄是15岁的user
false
```

**6.3.4 findFirst**

findFirst用于返回满足条件的第一个元素，比如返回年龄大于12岁的user中的第一个，实现如下:

```java
System.out.println("返回年龄大于12岁的user中的第一个");
Optional<User> first = list.stream().filter(u -> u.age > 10).findFirst();
User user = first.get();
System.out.println(user.toString());
```

输出结果如下：

```java
返回年龄大于12岁的user中的第一个
User{id=2, name='李四', age=12, school='清华大学'}
```

**6.3.5 findAny**

findAny相对于findFirst的区别在于，findAny不一定返回第一个，而是返回任意一个，比如返回年龄大于12岁的user中的任意一个：

```java
System.out.println("返回年龄大于12岁的user中的任意一个");
Optional<User> anyOne = list.stream().filter(u -> u.age > 10).findAny();
User user2 = anyOne.get();
System.out.println(user2.toString());
```

输出结果如下：

```java
返回年龄大于12岁的user中的任意一个
User{id=2, name='李四', age=12, school='清华大学'}
```

### 6.4 归约

**6.4.1 reduce**

现在我的目标不是返回一个新的集合，而是希望对经过参数化操作后的集合进行进一步的运算，那么我们可用对集合实施归约操作。java8的流式处理提供了reduce方法来达到这一目的。

比如我现在要查出学校是清华大学的所有user的年龄之和：

```java
//前面用到的方法
Integer ages = list.stream().filter(student -> "清华大学".equals(student.school)).mapToInt(User::getAge).sum();
System.out.println(ages);
System.out.println("归约 - - 》 start ");
Integer ages2 = list.stream().filter(student -> "清华大学".equals(student.school)).map(User::getAge).reduce(0,(a,c)->a+c);
Integer ages3 = list.stream().filter(student -> "清华大学".equals(student.school)).map(User::getAge).reduce(0,Integer::sum);
Integer ages4 = list.stream().filter(student -> "清华大学".equals(student.school)).map(User::getAge).reduce(Integer::sum).get();
System.out.println(ages2);
System.out.println(ages3);
System.out.println(ages4);
System.out.println("归约 - - 》 end ");
```

输出结果为：

```java
49
归约 - - 》 start
49
49
49
归约 - - 》 end
```

### 6.5 收集

前面利用collect(Collectors.toList())是一个简单的收集操作，是对处理结果的封装，对应的还有toSet、toMap，以满足我们对于结果组织的需求。这些方法均来自于java.util.stream.Collectors，我们可以称之为收集器。

收集器也提供了相应的归约操作，但是与reduce在内部实现上是有区别的，收集器更加适用于可变容器上的归约操作，这些收集器广义上均基于Collectors.reducing()实现。

**6.5.1 counting**

计算个数

如我现在计算user的总人数，实现如下：

```java
System.out.println("user的总人数");
long COUNT = list.stream().count();//简化版本
long COUNT2 = list.stream().collect(Collectors.counting());//原始版本
System.out.println(COUNT);
System.out.println(COUNT2);
```

输出结果为:

```java
user的总人数
10
10
```

**6.5.2 maxBy、minBy**

计算最大值和最小值

如我现在计算user的年龄最大值和最小值：

```java
System.out.println("user的年龄最大值和最小值");
Integer maxAge =list.stream().collect(Collectors.maxBy((s1, s2) -> s1.getAge() - s2.getAge())).get().age;
Integer maxAge2 = list.stream().collect(Collectors.maxBy(Comparator.comparing(User::getAge))).get().age;
Integer minAge = list.stream().collect(Collectors.minBy((S1,S2) -> S1.getAge()- S2.getAge())).get().age;
Integer minAge2 = list.stream().collect(Collectors.minBy(Comparator.comparing(User::getAge))).get().age;
System.out.println("maxAge = " + maxAge);
System.out.println("maxAge2 = " + maxAge2);
System.out.println("minAge = " + minAge);
System.out.println("minAge2 = " + minAge2);
```

输出结果为：

```java
user的年龄最大值
maxAge = 25
maxAge2 = 25
minAge = 10
minAge2 = 10
```

**6.5.3 summingInt、summingLong、summingDouble**

总和

如计算user的年龄总和：

```java
System.out.println("user的年龄总和");
Integer sumAge =list.stream().collect(Collectors.summingInt(User::getAge));
System.out.println("sumAge = " + sumAge);
```

输出结果为：

```java
user的年龄总和
sumAge = 145
```

**6.5.4 averageInt、averageLong、averageDouble**

平均值

如计算user的年龄平均值：

```java
System.out.println("user的年龄平均值");
double averageAge = list.stream().collect(Collectors.averagingDouble(User::getAge));
System.out.println("averageAge = " + averageAge);
```

输出结果为：

```java
user的年龄平均值
averageAge = 14.5
```

**6.5.5 summarizingInt、summarizingLong、summarizingDouble**

一次性查询元素个数、总和、最大值、最小值和平均值

```java
System.out.println("一次性得到元素个数、总和、均值、最大值、最小值");
long l1 = System.currentTimeMillis();
IntSummaryStatistics summaryStatistics = list.stream().collect(Collectors.summarizingInt(User::getAge));
long l111 = System.currentTimeMillis();
System.out.println("计算这5个值消耗时间为" + (l111-l1));
System.out.println("summaryStatistics = " + summaryStatistics);
```

输出结果为：

```java
一次性得到元素个数、总和、均值、最大值、最小值
计算这5个值消耗时间为3
summaryStatistics = IntSummaryStatistics{count=10, sum=145, min=10, average=14.500000, max=25}
```

**6.5.6 joining**

字符串拼接

如输出所有user的名字，用“，”隔开

```java
System.out.println("字符串拼接");
String names = list.stream().map(User::getName).collect(Collectors.joining(","));
System.out.println("names = " + names);
```

输出结果为：

```java
字符串拼接
names = 张三,李四,王五,赵六,田七,小明,小红,小华,小丽,小何
```

**6.5.7 groupingBy**

分组

如将user根据学校分组、先按学校分再按年龄分、每个大学的user人数、每个大学不同年龄的人数：

```java
System.out.println("分组");
Map<String, List<User>> collect1 = list.stream().collect(Collectors.groupingBy(User::getSchool));
Map<String, Map<Integer, Long>> collect2 = list.stream().collect(Collectors.groupingBy(User::getSchool, Collectors.groupingBy(User::getAge, Collectors.counting())));
Map<String, Map<Integer, Map<String, Long>>> collect4 = list.stream().collect(Collectors.groupingBy(User::getSchool, Collectors.groupingBy(User::getAge, Collectors.groupingBy(User::getName,Collectors.counting()))));
Map<String, Long> collect3 = list.stream().collect(Collectors.groupingBy(User::getSchool, Collectors.counting()));
System.out.println("collect1 = " + collect1);
System.out.println("collect2 = " + collect2);
System.out.println("collect3 = " + collect3);
System.out.println("collect4 = " + collect4);
```

输出结果为：

```java
分组
collect1 = {浙江大学=[User{id=8, name='小华', age=14, school='浙江大学'}, User{id=9, name='小丽', age=17, school='浙江大学'}, User{id=10, name='小何', age=10, school='浙江大学'}], 北京大学=[User{id=5, name='田七', age=25, school='北京大学'}, User{id=6, name='小明', age=16, school='北京大学'}, User{id=7, name='小红', age=14, school='北京大学'}], 清华大学=[User{id=1, name='张三', age=10, school='清华大学'}, User{id=2, name='李四', age=12, school='清华大学'}, User{id=3, name='王五', age=15, school='清华大学'}, User{id=4, name='赵六', age=12, school='清华大学'}]}
collect2 = {浙江大学={17=1, 10=1, 14=1}, 北京大学={16=1, 25=1, 14=1}, 清华大学={10=1, 12=2, 15=1}}
collect3 = {浙江大学=3, 北京大学=3, 清华大学=4}
collect4 = {浙江大学={17={小丽=1}, 10={小何=1}, 14={小华=1}}, 北京大学={16={小明=1}, 25={田七=1}, 14={小红=1}}, 清华大学={10={张三=1}, 12={李四=1, 赵六=1}, 15={王五=1}}}
```

**6.5.8 partitioningBy**　　

分区，分区可以看做是分组的一种特殊情况，在分区中key只有两种情况：true或false，目的是将待分区集合按照条件一分为二，java8的流式处理利用ollectors.partitioningBy()方法实现分区。

如按照是否是清华大学的user将左右user分为两个部分：

```java
System.out.println("分区");
Map<Boolean, List<User>> collect5 = list.stream().collect(Collectors.partitioningBy(user1 -> "清华大学".equals(user1.school)));
System.out.println("collect5 = " + collect5);
```

输出结果为：

```java
分区
collect5 = {false=[User{id=5, name='田七', age=25, school='北京大学'}, User{id=6, name='小明', age=16, school='北京大学'}, User{id=7, name='小红', age=14, school='北京大学'}, User{id=8, name='小华', age=14, school='浙江大学'}, User{id=9, name='小丽', age=17, school='浙江大学'}, User{id=10, name='小何', age=10, school='浙江大学'}], true=[User{id=1, name='张三', age=10, school='清华大学'}, User{id=2, name='李四', age=12, school='清华大学'}, User{id=3, name='王五', age=15, school='清华大学'}, User{id=4, name='赵六', age=12, school='清华大学'}]}
```

## 7. 并行流和串行流

前面提到过，JDK1.8提供了StreamAPI，而Stream又分为两种

- 并行流
- 顺序流

### 并行流和串行流

并行流就是把一个内容分成多个数据块，并用不同的线程分别处理每个数据块中的流。JDK8中将并行进行了优化，我们可以很容易的对数据进行并行操作。

Stream Api可以声明式的通过parallel()与sequential()在并行流和串行流(又被称作“顺序流”)之间进行切换。

默认的Stream就是串行流，但其是一个单线程的执行任务

### Fork/Join框架

<font color = "red">**注意，由于Fork/Join会充分发挥CPU多核性能，因此电脑不好的朋友不要轻易运行上述代码，或者在测试时把计算的数据量调低。我的笔记本CPU型号i5-6300HQ ，直接死机了。**</font>

Fork/Join框架就是在必要的情况下，将一个大任务拆分(fork)成若干个子任务(拆到不可再拆为止)，再将一个个子任务就是并行运算，最终将值进行join汇总。

并行流底层的实现就是Fork/Join框架， 使用并行流其实就是调用StreamAPI的parallel方法。

![](D:\Java\笔记\图片\Java版本\并行流与串行流.png)

传统的线程池虽然也能把任务进行拆分成若干子任务，借助不同线程来执行任务，但有一个问题：一旦某个子任务由于某些原因无法继续执行，那么负责执行该任务的线程将进入阻塞状态，影响了分配给该线程的其他后续任务的执行。

在Fork/Join框架中情况则大不相同。如果线程A被某个子任务阻塞，A不会进入阻塞状态，而是会主动寻找尚未被执行的其它子任务(可以从自己的后续任务队列中找，也可以从其它线程的任务队列中偷)。这种模式被称作“工作窃取”模式(Working Stealing)，当执行新的任务时，线程利用Fork/join框架可以将任务拆分成更小的任务，并将小任务分配/加入到线程队列中，如果自己空闲，就会从一个随机线程的任务队列中偷一个尚未被执行的任务来运行。

其实早在jdk1.7中已经有Fork/Join的实现了，下面来看看在JDK1.7中如何使用:

```java
/**
 * jdk1.7中使用Fork/Join框架
 *
 * 需要定义:
 * 1. 如何拆分
 * 2. 拆到什么程度为止
 */
public class ForkJoinCalculate extends RecursiveTask<Long> {
    private static final long serialVersionUID = 1212853982210556381L;
 
    private long start;
    private long end;
    //拆分的阈值，任务的最小单位是100000，拆到这个程度就不用再拆了
    private static final long THRESHOLD = 100000;
 
    public ForkJoinCalculate(long start, long end){
        this.start = start;
        this.end = end;
    }
 
    @Override
    protected Long compute() {
        long length = start - end;
        if(length <= THRESHOLD){ //进行求和
            long sum  = 0;
            for(long i = start; i <= end; i++){
                sum += i;
            }
            return sum;
        }else{ //进行拆分
            long middle = (start + end) / 2;
 
            ForkJoinCalculate left = new ForkJoinCalculate(start, middle);
            left.fork(); //拆分子任务，并将任务压入线程队列
 
            ForkJoinCalculate right = new ForkJoinCalculate(middle + 1, end);
            right.fork();
 
            //结果汇总
            return left.join() + right.join();
        }
    }
}
```

```java
/**
 * 使用Fork/join求和
 */
@org.junit.Test
public void test2(){
	Instant start = Instant.now();
 
	//需要ForkJoin线程池的支持
	ForkJoinPool pool = new ForkJoinPool();
	ForkJoinCalculate fork = new ForkJoinCalculate(0, 1000000000L);
	Long sum = pool.invoke(fork);
	System.out.println(sum);
 
	Instant end = Instant.now();
 
	System.out.println(Duration.between(start, end).toMillis());
}
```

​       jdk1.8的写法如下:

```java
/**
 * 使用jdk1.8求和
 */
public void test3(){
	Instant start = Instant.now();
 
	Long sum = LongStream.rangeClosed(0, 1000000000L)
			.parallel()
			.reduce(0, Long::sum);
 
	Instant end = Instant.now();
 
	System.out.println(Duration.between(start, end).toMillis());
}
```

## 8. Optional 类

为了尽量避免空指针异常，Java8提供了Optional容器，是一个容器类来的（封装了对象），用来代表这个值存在还是不存在，**原来使用Null来表示一个值不存在，现在使用Optional可以更好的去表达值是否存在了，并且可以有效地避免空指针异常，从使用空对象会报空指针异常，到去获取对象就报空指针！！！本质就是延迟了空指针的报出**

Optional 类是一个可以为null的容器对象。如果值存在则isPresent()方法会返回true，调用get()方法会返回该对象。

```java
    public static void main(String[] args) {
        DafaultMethod java8Tester = new DafaultMethod();
        Integer value1 = null;
        Integer value2 = new Integer(10);

        // Optional.ofNullable - 允许传递为 null 参数
        Optional<Integer> a = Optional.ofNullable(value1);

        // Optional.of - 如果传递的参数是 null，抛出异常 NullPointerException
        Optional<Integer> b = Optional.of(value2);
       // Optional<Integer> c = Optional.of(value1); //Exception in thread "main" java.lang.NullPointerException
       
        System.out.println(java8Tester.sum(a,b));
    }
    public Integer sum(Optional<Integer> a, Optional<Integer> b){

        // Optional.isPresent - 判断值是否存在

        System.out.println("第一个参数值存在: " + a.isPresent());//false
        System.out.println("第二个参数值存在: " + b.isPresent());//true

        // Optional.orElse - 如果值存在，返回它，否则返回默认值
        Integer value1 = a.orElse(new Integer(0));

        //Optional.get - 获取值，值需要存在
        Integer value2 = b.get();
        return value1 + value2;
    }
```

常用方法：

1. 可以使用of静态方法可以构建对象，但注意，这个方法接受的容器对象不能为Null，似乎看起来违背了Optional的作用（避免空指针异常），但其实这是在创建Optional容器使用的对象就为空，不是使用时报空指针
2. ofEmprt：可以构建一个空Optional容器，调用get方法会报空指针

3. ofNullable：若给的对象不为null，创建Optional容器，否则就创建空实例，调用get方法也会报空指针

4. isPresent：如果Optional容器有值就返回，没值就什么都不做（不报空指针），返回一个空白（避免空指针异常）

5. orElse：如果容器有值就返回原有值，没有就返回传入的参数（避免空指针异常）

6. orElseGet：传一个Supplier函数式接口（供给型接口），如果容器有值返回原有值，如果没值返回供给型接口的返回值（避免空指针）

7. map：如果有值可以对函数进行处理，就返回处理后的Optional，如果没有就返回空Optional（避免空指针）

8. flatMap：有map相似，但返回处理后的Optional必须与原Optional一样，不能是另外的Optional

## 9. 日期

## 10. HashMap以及JVM的优化

<!--先看一下D:\Java\笔记\面试题\数据结构 -->

HashMap使用链表法避免哈希冲突（相同hash值），就是使用链表来避免哈希冲突。当链表长度大于TREEIFY_THRESHOLD（默认为8）时，将链表转换为红黑树。当小于等于UNTREEIFY_THRESHOLD（默认为6）时，又会退化回链表以达到性能均衡。 

下图为HashMap的数据结构（数组+链表+红黑树 ）
![](D:\Java\笔记\图片\Java版本\HashMap数据结构.png)

### HashMap在并发时出现的问题

1. 多线程put的时候可能导致元素丢失
   主要问题出在addEntry方法的`new Entry (hash, key, value, e)`，如果两个线程都同时取得了e,则他们下一个元素都是e，然后赋值给table元素的时候有一个成功有一个丢失。

2. 多线程put后可能导致get死循环
   造成死循环的原因是多线程进行put操作时，触发了HashMap的扩容（resize函数），出现链表的两个结点形成闭环，导致死循环。下图为JDK8中的put操作流程，详情请自行查看源码。

   ![](D:\Java\笔记\图片\Java版本\HashMap之put方法.jpg)

### 单线程resize过程

![](D:\Java\笔记\图片\Java版本\单线程resize过程.jpg)

首先我们把resize函数中的transfer()的关键代码贴出来：

```java
while(null != e) {	
    Entry<K,V> next = e.next;	
    if (rehash) {	
        e.hash = null == e.key ? 0 : hash(e.key);	
    }	
    int i = indexFor(e.hash, newCapacity);	
    e.next = newTable[i];	
    newTable[i] = e;	
    e = next;	
}
```

我们可以再简化一下，因为中间的i就是判断新表的位置，我们可以跳过。简化后代码：

```java
while(null != e) {	
    Entry<K,V> next = e.next;	
    e.next = newTable[i]; //假设线程一执行完这里就被调度挂起了	
    newTable[i] = e;	
    e = next;	
}
```

去掉了一些与本过程冗余的代码，意思就非常清晰了：

1. Entry<K,V> next = e.next;// 因为是单链表，如果要转移头指针，一定要保存下一个结点，不然转移后链表就丢了

2. e.next = newTable[i];// e要插入到链表的头部，所以要先用e.next指向新的 Hash表第一个元素（为什么不加到新链表最后？因为复杂度是 O（N））

3. newTable[i] = e;// 现在新Hash表的头指针仍然指向e没转移前的第一个元素，所以需要将新Hash表的头指针指向e

4. e = next;// 转移e的下一个结点

#### 并发时resize过程

1. 假设我们线程一执行完`e.next = newTable[i];`就被调度挂起了

   ![](D:\Java\笔记\图片\Java版本\并发时resize过程1.jpg)

而我们的线程二执行完成了。于是我们有下面的这个样子。

> 因为Thread1的 e 指向了key(3)，而next指向了key(7)，其在线程二rehash后，指向了线程二重组后的链表。我们可以看到链表的顺序被反转后。

2. 线程一被调度回来执行。

   1. 执行 newTalbe[i] = e。
   2. 执行e = next，导致了e指向了key(7)。
   3. 下一次循环的next = e.next导致了next指向了key(3)。

   ![](D:\Java\笔记\图片\Java版本\并发时resize过程2.jpg)

3. 一切安好。线程一接着工作。把key(7)摘下来，放到newTable[i]的第一个，然后把e和next往下移。

   ![](D:\Java\笔记\图片\Java版本\并发时resize过程3.jpg)

4. 环形链接出现。 e.next = newTable[i] 导致 key(3).next 指向了 key(7)。注意：此时的key(7).next 已经指向了key(3)， 环形链表就这样出现了。

   ![](D:\Java\笔记\图片\Java版本\并发时resize过程4.jpg)

于是，当我们的线程一调用到，HashTable.get(11)时，悲剧就出现了——Infinite Loop。

### JDK8中HashMap的优化

#### 长链表的优化

在JDK8中，如果链表的长度大于等于8 ，那么链表将转化为红黑树；当长度小于等于6时，又会变成一个链表

> 红黑树的平均查找长度是log(n)，长度为8的时候，平均查找长度为3，如果继续使用链表，平均查找长度为8/2=4，这才有转换为树的必要。链表长度如果是小于等于6，6/2=3，虽然速度也很快的，但是转化为树结构和生成树的时间并不会太短。 还有选择6和8，中间有个差值7可以有效防止链表和树频繁转换。假设一下，如果设计成链表个数超过8则链表转换成树结构，链表个数小于8则树结构转换成链表，如果一个HashMap不停的插入、删除元素，链表个数在8左右徘徊，就会频繁的发生树转链表、链表转树，效率会很低。

> 注意，当数组长度小于64（常量定义）的时候，扩张数组长度一倍，否则的话把链表转为树。并不是每次都直接树化的。

此外，在JDK7中，hash计算的时候会对操作数进行右移操作，计算复杂，目的是将高位也参与运算，减少hash碰撞；在JDK8中，链表可以转变成红黑树，所以hash计算也变得简单。下面的图为JDK8中的hash计算和索引计算。

![](D:\Java\笔记\图片\Java版本\长链表的优化.jpg)

#### hash碰撞的插入方式的优化

发生hash碰撞时，JDK7会在链表头部插入，而JDK8会在链表尾部插入。头插法是操作速度最快的，找到数组位置就直接找到插入位置了，但JDK8之前hashmap这种插入方法在并发场景下会出现死循环。JDK8开始hashmap链表在节点长度达到8之后会变成红黑树，这样一来在数组后节点长度不断增加时，遍历一次的次数就会少很多（否则每次要遍历所有），而且也可以避免之前的循环列表问题。同时如果变成红黑树，也不可能做头插法了。

#### 扩容机制的优化

在JDK7中，对所有链表进行rehash计算；在JDK8中，实际上也是通过取余找到元素所在的数组的位置，取余的方式在putVal里面：i = (n - 1) & hash。我们假设，在扩容之前，key取余之后留下了n位。扩容之后，容量变为2倍，所以key取余得到的就有n+1位。在这n+1位里面，如果第1位是0，那么扩容前后这个key的位置还是在相同的位置（因为hash相同，并且余数的第1位是0，和之前n位的时候一样，所以余数还是一样，位置就一样了）；如果这n+1位的第一位是1，那么就和之前的不同，那么这个key就应该放在之前的位置再加上之前整个数组的长度的位置。这样子就减少了移动所有数据带来的消耗。（慢慢读两遍，想明白了，就觉得这个其实不看图更好理解）

![](D:\Java\笔记\图片\Java版本\扩容机制的优化.jpg)

### 常见FAQ

#### HashMap扩容条件

查看JDK7源码的put函数，然后跳转到addEntry函数。然后你会发现JDK7中hashmap扩容是要同时满足两个条件：

1. 当前数据存储的数量（即size()）大小必须大于等于阈值

2. 当前加入的数据是否发生了hash冲突


因为上面这两个条件，所以存在下面两种情况：

1. 就是hashmap在存值的时候（默认大小为16，负载因子0.75，阈值12），可能达到最后存满16个值的时候，再存入第17个值才会发生扩容现象，因为前16个值，每个值在底层数组中分别占据一个位置，并没有发生hash碰撞。

2. 当然也有可能存储更多值（超多16个值，最多可以存26个值）都还没有扩容。原理：前11个值全部hash碰撞，存到数组的同一个位置（这时元素个数小于阈值12，不会扩容），后面所有存入的15个值全部分散到数组剩下的15个位置（这时元素个数大于等于阈值，但是每次存入的元素并没有发生hash碰撞，所以不会扩容），前面11+15=26，所以在存入第27个值的时候才同时满足上面两个条件，这时候才会发生扩容现象。


而在JDK8中，扩容的条件只有一个，就是当前容量大于阈值（阈值等于当前hashmap最大容量乘以负载因子）

#### HashMap在JDK7中扩容计算新索引的方法

通过transfer方法将旧数组中的元素复制到新数组，在这个方法中进行了包括释放旧的Entry中的对象引用，该过程中如果需要重新计算hash值就重新计算，然后根据indexfor（）方法计算索引值。而索引值的计算方法为: h & (length-1) ，即hashcode计算出的hash值和数组长度进行与运算。

> 一般认为，Java的%、/操作比&慢10倍左右，因此采用&运算而不是h % length会提高性能。

#### HashMap在JDK8中计算索引的方法

这个设计确实非常的巧妙，既省去了重新计算hash值的时间，也就是说1.8不用重新计算hash值而且同时，由于新增的1bit是0还是1可以认为是随机的，因此resize的过程，均匀的把之前的冲突的节点分散到新的bucket了。这一块就是JDK1.8新增的优化点。有一点注意区别，JDK1.7中rehash的时候，旧链表迁移新链表的时候，如果在新表的数组索引位置相同，则链表元素会倒置，因为他采用的是头插法，先拿出旧链表头元素。但是从下图可以看出，JDK1.8不会倒置，采用的尾插法。

![](D:\Java\笔记\图片\Java版本\HashMap在JDK8中计算索引的方法.jpg)

**为什么 HashMap中 String、Integer 这样的包装类适合作为 key键？**

![](D:\Java\笔记\图片\Java版本\HashMap中 String、Integer.jpg)

**HashMap中的key如果是Object类型，则需实现哪些方法？**

![](D:\Java\笔记\图片\Java版本\HashMap中的key如果是Object类型.jpg)