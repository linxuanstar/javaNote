# 抽象类和接口

## 抽象类

在面向对象的概念中，所有的对象都是通过类来描绘的，但是反过来，并不是所有的类都是用来描绘对象的，如果一个类中没有包含足够的信息来描绘一个具体的对象，这样的类就可以称之为抽象类。

1. 抽象方法：使用abstract修饰且没有方法体的方法。

   特点：

   1. 抽象方法没有方法体，交给子类实现
   2. 抽象方法修饰符不能是private final static
   3. 抽象方法必须定义在抽象类或者接口中

2. 抽象类：包含抽象方法的类，即使用abstract修饰的类。

   特点：

   1. 抽象类不能被实例化，只能被继承
   2. 抽象类中可以不包含抽象方法（不包含抽象方法就没有太大意义，可以作为工具类防止被实例化）
   3. 抽象类的子类可以不实现该类所有的抽象方法，但也必须作为抽象类（抽象派生类）
   4. 抽象类的构造方法不能定义成私有（子类构造方法会调用父类构造方法）
   5. 抽象类不能使用final修饰，final修饰的类不能被继承

实例：武器作为一个抽象类具备攻击行为（attack方法），具体如何攻击（how2Attack方法）需要子类实现。如：机关枪可以射击敌人，手榴弹可以丢向敌人。

```java
public class Test3 {
    public static void main(String[] args) {
        Gun gun = new Gun("机关枪");
        Grenade grenade = new Grenade("手榴弹");
        gun.attack();
        grenade.attack();
        gun.how2Attack();
        grenade.how2Attack();
    }
}
 
abstract class Weapon{
    String name;
    public void attack(){
        System.out.println(name+"具有攻击行为");
    }
 
    abstract void how2Attack();
 
}
class Gun extends Weapon{
    public Gun(String name) {
        this.name = name;
    }
 
    @Override
    void how2Attack() {
        System.out.println(name+"射击敌人");
    }
}
class Grenade extends Weapon{
    public Grenade(String name){
        this.name = name;
    }
    @Override
    void how2Attack() {
        System.out.println(name+"丢向敌人");
    }
}
```

## 接口

什么是接口：

1. 硬件接口：是指同一计算机不同功能层之间的通信规则称为接口。

2. 软件接口：是指对协定进行定义的引用类型。其他类型实现接口，以保证它们支持某些操作。

Java中的接口：在JAVA编程语言中是接口一个抽象类型，是抽象方法的集合，接口通常以interface来声明。一个类通过继承接口的方式，从而来继承接口的抽象方法。
特点：

1. 接口中没有构造器，不能被实例化
2. 接口只能继承接口，不能继承类，接口支持多继承
3. 接口中的定义的成员变量，默认是public static final修饰的静态常量
4. 接口中定义的方法，默认是public abstract修饰的抽象方法
5. 接口中定义的内部类，默认是public static修饰的静态内部类

标志接口：仅仅作为一个定义，就是一个标志

常量接口：用来封装多个常量信息

> 注意：在JDK8中，接口也可以定义静态方法和默认非静态方法，可以直接用接口名调用静态方法，实现类可以调用默认非静态方法。如果同时实现两个接口，接口中定义了一样的默认方法，则实现类必须重写默认方法，不然会报错。

实例：走路和游泳作为两个抽象的行为，青蛙可以实现两个行为接口来具体实现行为的内容

```java
public class Test4 {
    public static void main(String[] args) {
        Frog frog = new Frog("青蛙");
        frog.getName();
        frog.work();
        frog.swim();
    }
}
class Animal {
    String name;
 
    public String getName() {
        System.out.println("Animal:"+name);
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
}
interface IWalk{
    void work();
}
interface ISwim{
    void swim();
}
class Frog extends Animal implements IWalk,ISwim{
 
    public Frog(String name) {
        this.name = name;
    }
 
    @Override
    public void work() {
        System.out.println(this.name+":蹦蹦跳跳地走路");
    }
 
    @Override
    public void swim() {
        System.out.println(this.name+":在水里蛙泳");
    }
}
```

## 抽象类和接口的异同点

语法层面

* 相同点：
  1. 抽象类和接口都不能被实例化
  2. 抽象类和接口都可以定义抽象方法，子类/实现类必须覆写这些抽象方法

* 不同点：
  1. 抽象类有构造方法，接口没有构造方法
  2. 抽象类可以包含普通方法，接口中只能是public abstract修饰抽象方法（Java8之后可以）
  3. 抽象类只能单继承，接口可以多继承
  4. 抽象类可以定义各种类型的成员变量，接口中只能是public static final修饰的静态常量

设计层面

1. 抽象类是对一种事物的抽象，即对类抽象，而接口是对行为的抽象。

   抽象类是对整个类整体进行抽象，包括属性、行为，但是接口却是对类局部（行为）进行抽象。

   举个简单的例子，飞机和鸟是不同类的事物，但是它们都有一个共性，就是都会飞。那么在设计的时候，可以将飞机设计为一个类Airplane，将鸟设计为一个类Bird，但是不能将 飞行 这个特性也设计为类，因此它只是一个行为特性，并不是对一类事物的抽象描述。此时可以将 飞行 设计为一个接口Fly，包含方法fly( )，然后Airplane和Bird分别根据自己的需要实现Fly这个接口。

   然后至于有不同种类的飞机，比如战斗机、民用飞机等直接继承Airplane即可，对于鸟也是类似的，不同种类的鸟直接继承Bird类即可。

   从这里可以看出，继承是一个 "是不是"的关系，而 接口 实现则是 "有没有"的关系。如果一个类继承了某个抽象类，则子类必定是抽象类的种类，而接口实现则是有没有、具备不具备的关系，比如鸟是否能飞（或者是否具备飞行这个特点），能飞行则可以实现这个接口，不能飞行就不实现这个接口。

2. 设计层面不同，抽象类作为很多子类的父类，它是一种模板式设计。

   而接口是一种行为规范，它是一种辐射式设计。什么是模板式设计？

   最简单例子，大家都用过ppt里面的模板，如果用模板A设计了ppt B和ppt C，ppt B和ppt C公共的部分就是模板A了，如果它们的公共部分需要改动，则只需要改动模板A就可以了，不需要重新对ppt B和ppt C进行改动。而辐射式设计，比如某个电梯都装了某种报警器，一旦要更新报警器，就必须全部更新。

   也就是说对于抽象类，如果需要添加新的方法，可以直接在抽象类中添加具体的实现，子类可以不进行变更；而对于接口则不行，如果接口进行了变更，则所有实现这个接口的类都必须进行相应的改动。

##  抽象类和接口的使用场景

抽象类的使用场景

- 既想约束子类具有共同的行为（但不再乎其如何实现），又想拥有缺省的方法，又能拥有实例变量
- 如：模板方法设计模式，模板方法使得子类可以在不改变算法结构的情况下，重新定义算法中某些步骤的具体实现。

接口的应用场景

1. 约束多个实现类具有统一的行为，但是不在乎每个实现类如何具体实现

2. 作为能够实现特定功能的标识存在，也可以是什么接口方法都没有的纯粹标识。

3. 实现类需要具备很多不同的功能，但各个功能之间可能没有任何联系。

4. 使用接口的引用调用具体实现类中实现的方法（多态）