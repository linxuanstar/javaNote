

# 第一章网络编程入门

## 1.1 软件结构

<!--P403-->

<!--Clinet 客户端   Server 服务器-->

* **C/S结构**：全称为`Clinet/Server`结构，是指客户端和服务器结构。常用程序有QQ、迅雷等软件。

![Clinet/Server结构](D:\Java\笔记\图片\day11【网络编程】\1.png)

* **B/S结构**：全程为`Browser/Server`结构，是指浏览器和服务器结构。常用的浏览器有谷歌，Edge，火狐等。

![Browser/Server结构](D:\Java\笔记\图片\day11【网络编程】\2.png)

两种架构各有优势，但是无论哪种架构，都离不开网络的支持。

**网络编程**，就是在一定的协议下，实现两台计算机的通信的程序。

## 1.2 网络通信协议

<!--P404-->

**网络通信协议：**网络通信协议是一种网络通用语言，为连接不同操作系统和不同硬件体系结构的互联网络提供通信支持，<font color = "red">是一种网络通用语言</font>。

例如，网络中一个微机用户和一个大型主机的操作员进行通信，由于这两个数据终端所用字符集不同，因此操作员所输入的命令彼此不认识。为了能进行通信，规定每个终端都要将各自字符集中的字符先变换为标准字符集的字符后，才进入网络传送，到达目的终端之后，再变换为该终端字符集的字符。<font color = "red">因此，网络通信协议也可以理解为网络上各台计算机之间进行交流的一种语言。</font>

常见的网络通信协议有：TCP/IP协议、IPX/SPX协议、NetBEUI协议等。 

TCP/IP协议是一个协议簇。里面包括很多协议的，UDP只是其中的一个， 之所以命名为TCP/IP协议，因为TCP、IP协议是两个很重要的协议，就用他两命名了。 

* **TCP/IP**（`Transmission Control Protocol/Internet Protocol`,传输控制协议/网际协议） 协议具有很强的灵活性，支持任意规模的网络，几乎可连接所有服务器和工作站。在使用TCP/IP协议时需要进行复杂的设置，每个结点至少需要一个“IP地址”、一个“子网掩码”、一个“默认网关”、一个“主机名”，对于一些初学者来说使用不太方便。 

  ![Transmission Control Protocol/Internet Protocol,传输控制协议/网际协议](D:\Java\笔记\图片\day11【网络编程】\3.png)

 *TCP/IP*（*Transmission Control Protocol/Internet Protocol*）即传输控制协议*/*网间协议，定义了主机如何连入因特网及数据如何再它们之间传输的标准。

从字面意思来看TCP/IP是TCP和IP协议的合称，但实际上TCP/IP协议是指因特网整个TCP/IP协议族。不同于ISO模型的七个分层，TCP/IP协议参考模型把所有的TCP/IP系列协议归类到四个抽象层中

- 应用层：TFTP，HTTP，SNMP，FTP，SMTP，DNS，Telnet 等等
- 传输层：TCP，UDP
- 网络层：IP，ICMP，OSPF，EIGRP，IGMP
- 数据链路层：SLIP，CSLIP，PPP，MTU

每一抽象层建立在低一层提供的服务上，并且为高一层提供服务，看起来大概是这样子的

​     ![img](D:\Java\笔记\图片\day11【网络编程】\Center)

![img](D:\Java\笔记\图片\day11【网络编程】\Center1)

## 1.3 协议分类

<!--P405-->

`java.net`包中包含的类和接口，它们提供低层次的通信细节。我们可以直接使用这些类和接口，来专注于网络程序开发，而不用考虑通信的细节。

`java.net`包中提供了两种常见的网络协议的支持：

* **TCP（Transmission Control Protocol，传输控制协议）**是面向连接的协议，也就是说，在收发数据前，必须和对方建立可靠的连接。 一个TCP连接必须要经过三次“对话”才能建立起来，其中的过程非常复杂， 只简单的描述下这三次对话的简单过程：

  1. 主机A向主机B发出连接请求数据包：“我想给你发数据，可以吗？”，这是第一次对话；
  2. 主机B向主机A发送同意连接和要求同步 （同步就是两台主机一个在发送，一个在接收，协调工作）的数据包 ：“可以，你什么时候发？”，这是第二次对话；
  3. 主机A再发出一个数据包确认主机B的要求同步：“我现在就发，你接着吧！”， 这是第三次对话。

  > <font color = "red">**三次“对话”的目的是使数据包的发送和接收同步， 经过三次“对话”之后，主机A才向主机B正式发送数据。**</font>

* **UDP（User Data Protocol，用户数据报协议）**

  1. UDP是一个非连接的协议，传输数据之前源端和终端不建立连接， 当它想传送时就简单地去抓取来自应用程序的数据，并尽可能快地把它扔到网络上。 在发送端，UDP传送数据的速度仅仅是受应用程序生成数据的速度、 计算机的能力和传输带宽的限制； 在接收端，UDP把每个消息段放在队列中，应用程序每次从队列中读一个消息段。
  2.  由于传输数据不建立连接，因此也就不需要维护连接状态，包括收发状态等， 因此一台服务机可同时向多个客户机传输相同的消息。
  3. UDP信息包的标题很短，只有8个字节，相对于TCP的20个字节信息包的额外开销很小。
  4. 吞吐量不受拥挤控制算法的调节，只受应用软件生成数据的速率、传输带宽、 源端和终端主机性能的限制。
  5. UDP使用尽最大努力交付，即不保证可靠交付， 因此主机不需要维持复杂的链接状态表（这里面有许多参数）。
  6. UDP是面向报文的。发送方的UDP对应用程序交下来的报文， 在添加首部后就向下交付给IP层。既不拆分，也不合并，而是保留这些报文的边界， 因此，应用程序需要选择合适的报文大小。

  我们经常使用“ping”命令来测试两台主机之间TCP/IP通信是否正常。

  > **ping命令**是用来探测主机到主机之间是否可通信，如果不能**ping**到某台主机，表明不能和这台主机建立连接。
  >
  > **ping命令**是使用 IP 和网络控制信息协议 (ICMP)，因而没有涉及到任何传输协议(UDP/TCP) 和应用程序。它发送icmp回送请求消息给目的主机。
  >
  > ICMP协议规定：目的主机必须返回ICMP回送应答消息给源主机。如果源主机在一定时间内收到应答，则认为主机可达。

* 小结TCP与UDP的区别：

  1. <font color = "red">基于连接与无连接；</font>
  2. <font color = "red">对系统资源的要求（TCP较多，UDP少）；</font>
  3. <font color = "red">UDP程序结构较简单；</font>
  4. <font color = "red">流模式与数据报模式 ；</font>
  5. <font color = "red">TCP保证数据正确性，UDP可能丢包；</font>
  6. <font color = "red">TCP保证数据顺序，UDP不保证。</font>

## 1.4 三次握手与四次挥手详解

连接三次握手与断开四次挥手详解

### 名词解释

<font color = "red">序列号seq
确认号ack
确认ACK
同步SYN
终止FIN</font>

![](D:\Java\笔记\图片\day11【网络编程】\三次握手四次挥手1.png)

- 序列号seq：占4个字节，用来标记数据段的顺序，TCP把连接中发送的所有数据字节都编上一个序号，第一个字节的编号由本地随机产生；给字节编上序号后，就给每一个报文段指派一个序号；<font color = "red">序列号seq就是这个报文段中的第一个字节的数据编号。</font>

- 确认号ack：占4个字节，<font color = "red">期待收到对方下一个报文段的第一个数据字节的序号</font>；序列号表示报文段携带数据的第一个字节的编号；而确认号指的是期望接收到下一个字节的编号；因此当前报文段最后一个字节的编号+1即为确认号。

- 确认ACK：占1位，仅当ACK=1时，确认号字段才有效。ACK=0时，确认号无效

  ACK 是TCP报头的控制位之一，对数据进行确认。确认由目的端发出， 用它来告诉发送端这个序列号之前的数据段都收到了。 比如确认号为X，则表示前X-1个数据段都收到了，只有当ACK=1时,确认号才有效，当ACK=0时，确认号无效，这时会要求重传数据，保证数据的完整性。

- 同步SYN：连接建立时用于同步序号。当SYN=1，ACK=0时表示：这是一个连接请求报文段。若同意连接，则在响应报文段中使得SYN=1，ACK=1。因此，SYN=1表示这是一个连接请求，或连接接受报文。SYN这个标志位只有在TCP建产连接时才会被置1，握手完成后SYN标志位被置0。

  SYN 同步序列号，TCP建立连接时将这个位置1。

- 终止FIN：用来释放一个连接。FIN=1表示：此报文段的发送方的数据已经发送完毕，并要求释放运输连接

  FIN 发送端完成发送任务位，当TCP完成数据传输需要断开时,，提出断开连接的一方将这位置1

>  PS：ACK、SYN和FIN这些大写的单词表示标志位，其值要么是1，要么是0；ack、seq小写的单词表示序号。

![](D:\Java\笔记\图片\day11【网络编程】\三次握手四次挥手2.png)

**TCP（Transmission Control Protocol，传输控制协议）**是面向连接的协议，也就是说，在收发数据前，必须和对方建立可靠的连接。

**TCP三次握手过程**

第一次握手：主机A通过向主机B发送一个含有同步序列号的标志位的数据段给主机B，向主机B 请求建立连接，通过这个数据段， 主机A告诉主机B 两件事：我想要和你通信；你可以用哪个序列号作为起始数据段来回应我。

第二次握手：主机B 收到主机A的请求后，用一个带有确认应答（ACK）和同步序列号（SYN）标志位的数据段响应主机A，也告诉主机A两件事：我已经收到你的请求了，你可以传输数据了；你要用那个序列号作为起始数据段来回应我

第三次握手：主机A收到这个数据段后，再发送一个确认应答，确认已收到主机B 的数据段："我已收到回复，我现在要开始传输实际数据了，这样3次握手就完成了，主机A和主机B 就可以传输数据了。

**3次握手的特点**

没有应用层的数据 ,SYN这个标志位只有在TCP建立连接时才会被置1 ,握手完成后SYN标志位被置0。

**TCP建立连接要进行3次握手，而断开连接要进行4次**

第一次： 当主机A完成数据传输后,将控制位FIN置1，提出停止TCP连接的请求 ；

第二次： 主机B收到FIN后对其作出响应，确认这一方向上的TCP连接将关闭,将ACK置1；

第三次： 由B 端再提出反方向的关闭请求,将FIN置1 ；

第四次： 主机A对主机B的请求进行确认，将ACK置1，双方向的关闭结束.。

由TCP的三次握手和四次断开可以看出，<font color = "red">TCP使用面向连接的通信方式， 大大提高了数据通信的可靠性，使发送数据端和接收端在数据正式传输前就有了交互， 为数据正式传输打下了可靠的基础。</font>

### 三次握手过程理解

<font color = "red">序列号seq
确认号ack
确认ACK
同步SYN
终止FIN</font>

- 序列号seq：占4个字节，用来标记数据段的顺序，TCP把连接中发送的所有数据字节都编上一个序号，第一个字节的编号由本地随机产生；给字节编上序号后，就给每一个报文段指派一个序号；<font color = "red">序列号seq就是这个报文段中的第一个字节的数据编号。</font>

- 确认号ack：占4个字节，<font color = "red">期待收到对方下一个报文段的第一个数据字节的序号</font>；序列号表示报文段携带数据的第一个字节的编号；而确认号指的是期望接收到下一个字节的编号；因此当前报文段最后一个字节的编号+1即为确认号。

- 确认ACK：占1位，仅当ACK=1时，确认号字段才有效。ACK=0时，确认号无效

  ACK 是TCP报头的控制位之一，对数据进行确认。确认由目的端发出， 用它来告诉发送端这个序列号之前的数据段都收到了。 比如确认号为X，则表示前X-1个数据段都收到了，只有当ACK=1时,确认号才有效，当ACK=0时，确认号无效，这时会要求重传数据，保证数据的完整性。

- 同步SYN：连接建立时用于同步序号。当SYN=1，ACK=0时表示：这是一个连接请求报文段。若同意连接，则在响应报文段中使得SYN=1，ACK=1。因此，SYN=1表示这是一个连接请求，或连接接受报文。SYN这个标志位只有在TCP建产连接时才会被置1，握手完成后SYN标志位被置0。

  SYN 同步序列号，TCP建立连接时将这个位置1。

- 终止FIN：用来释放一个连接。FIN=1表示：此报文段的发送方的数据已经发送完毕，并要求释放运输连接

  FIN 发送端完成发送任务位，当TCP完成数据传输需要断开时,，提出断开连接的一方将这位置1

![](D:\Java\笔记\图片\day11【网络编程】\三次握手.png)

1. 第一次握手：建立连接时，客户端发送SYN包（SYN=j）到服务器，并进入SYN_SENT状态，等待服务器确认；SYN：同步序列编号（Synchronize Sequence Numbers）。

2. 第二次握手：服务器收到SYN包，必须确认客户的SYN（ack=j+1），同时自己也发送一个SYN包（syn=k），即SYN+ACK包，此时服务器进入SYN_RECV状态；

3. 第三次握手：客户端收到服务器的SYN+ACK包，向服务器发送确认包ACK(ack=k+1），此包发送完毕，客户端和服务器进入ESTABLISHED（TCP连接成功）状态，完成三次握手。


### 四次挥手过程理解 

<font color = "red">序列号seq
确认号ack
确认ACK
同步SYN
终止FIN</font>

![](D:\Java\笔记\图片\day11【网络编程】\四次挥手.png)

1. 客户端进程发出连接释放报文，并且停止发送数据。释放数据报文首部，FIN=1，其序列号为seq=u（等于前面已经传送过来的数据的最后一个字节的序号加1），此时，客户端进入FIN-WAIT-1（终止等待1）状态。

   <font color = "red"> TCP规定，FIN报文段即使不携带数据，也要消耗一个序号。 </font>

2. 服务器收到连接释放报文，发出确认报文，ACK=1，ack=u+1，并且带上自己的序列号seq=v，此时，服务端就进入了CLOSE-WAIT（关闭等待）状态。TCP服务器通知高层的应用进程，客户端向服务器的方向就释放了，这时候处于半关闭状态，即客户端已经没有数据要发送了，但是服务器若发送数据，客户端依然要接受。这个状态还要持续一段时间，也就是整个CLOSE-WAIT状态持续的时间。 

   客户端收到服务器的确认请求后，此时，客户端就进入FIN-WAIT-2（终止等待2）状态，等待服务器发送连接释放报文（在这之前还需要接受服务器发送的最后的数据）。

3. 服务器将最后的数据发送完毕后，就向客户端发送连接释放报文，FIN=1，ack=u+1，由于在半关闭状态，服务器很可能又发送了一些数据，假定此时的序列号为seq=w，此时，服务器就进入了LAST-ACK（最后确认）状态，等待客户端的确认。 

4. 客户端收到服务器的连接释放报文后，必须发出确认，ACK=1，ack=w+1，而自己的序列号是seq=u+1，此时，客户端就进入了TIME-WAIT（时间等待）状态。注意此时TCP连接还没有释放，必须经过2MSL（最长报文段寿命）的时间后，当客户端撤销相应的TCB后，才进入CLOSED状态。 

   服务器只要收到了客户端发出的确认，立即进入CLOSED状态。同样，撤销TCB后，就结束了这次的TCP连接。可以看到，服务器结束TCP连接的时间要比客户端早一些。 


### 常见面试题

<font color = "red">【问题1】为什么连接的时候是三次握手，关闭的时候却是四次握手？</font>

​	**答**：因为当Server端收到Client端的SYN连接请求报文后，可以直接发送SYN+ACK报文。其中ACK报文是用来应答的，SYN报文是用来同步的。但是关闭连接时，当Server端收到FIN报文时，很可能并不会立即关闭SOCKET，所以只能先回复一个ACK报文，告诉Client端，"你发的FIN报文我收到了"。只有等到我Server端所有的报文都发送完了，我才能发送FIN报文，因此不能一起发送。故需要四步握手。

​		    因为当 Server 端收到 Client 端的`SYN`连接请求报文后，可以直接发送`SYN+ACK`报文。**但是在关闭连接时，当 Server 端收到 Client 端发出的连接释放报文时，很可能并不会立即关闭 SOCKET**，所以 Server 端先回复一个`ACK`报文，告诉 Client 端我收到你的连接释放报文了。只有等到 Server 端所有的报文都发送完了，这时 Server 端才能发送连接释放报文，之后两边才会真正地断开连接。故需要四次挥手。

<font color = "red">【问题2】为什么TIME_WAIT状态需要经过2MSL(最大报文段生存时间)才能返回到CLOSE状态？</font>

​	**答**：虽然按道理，四个报文都发送完毕，我们可以直接进入CLOSE状态了，但是我们必须假想网络是不可靠的，有可能最后一个ACK丢失。所以TIME_WAIT状态就是用来重发可能丢失的ACK报文。
​			在Client发送出最后的ACK回复，但该ACK可能丢失。Server如果没有收到ACK，将不断重复发送FIN片段。所以Client不能立即关闭，它必须确认Server接收到了该ACK。Client会在发送出ACK之后进入到TIME_WAIT状态。Client会设置一个计时器，等待2MSL的时间。如果在该时间内再次收到FIN，那么Client会重发ACK并再次等待2MSL。
​			如果直到2MSL，Client都没有再次收到FIN，那么Client推断ACK已经被成功接收，则结束TCP连接。

> 所谓的2MSL是两倍的MSL(`Maximum Segment Lifetime`)。MSL指一个片段在网络中最大的存活时间，2MSL就是一个发送和一个回复所需的最大时间。

- **保证 A 发送的最后一个 ACK 报文段能够到达 B**。这个`ACK`报文段有可能丢失，B 收不到这个确认报文，就会超时重传连接释放报文段，然后 A 可以在`2MSL`时间内收到这个重传的连接释放报文段，接着 A 重传一次确认，重新启动 2MSL 计时器，最后 A 和 B 都进入到`CLOSED`状态，若 A 在`TIME-WAIT`状态不等待一段时间，而是发送完 ACK 报文段后立即释放连接，则无法收到 B 重传的连接释放报文段，所以不会再发送一次确认报文段，B 就无法正常进入到`CLOSED`状态。
- **防止已失效的连接请求报文段出现在本连接中**。A 在发送完最后一个`ACK`报文段后，再经过 2MSL，就可以使这个连接所产生的所有报文段都从网络中消失，使下一个新的连接中不会出现旧的连接请求报文段。

<font color = "red">【问题3】为什么不能用两次握手进行连接？</font>

​	**答**：3次握手完成两个重要的功能，既要双方做好发送数据的准备工作(双方都知道彼此已准备好)，也要允许双方就初始序列号进行协商，这个序列号在握手过程中被发送和确认。

​       现在把三次握手改成仅需要两次握手，死锁是可能发生的。

​		作为例子，考虑计算机S和C之间的通信，假定C给S发送一个连接请求分组，S收到了这个分组，并发送了确认应答分组。按照两次握手的协定，S认为连接已经成功地建立了，可以开始发送数据分组。可是，C在S的应答分组在传输中被丢失的情况下，将不知道S是否已准备好，不知道S建立什么样的序列号，C甚至怀疑S是否收到自己的连接请求分组。在这种情况下，C认为连接还未建立成功，将忽略S发来的任何数据分组，只等待连接确认应答分组。而S在发出的分组超时后，重复发送同样的分组。这样就形成了死锁。

> ​		这主要是为了防止两次握手情况下**已失效的连接请求报文段突然又传送到服务端**，而产生了错误。考虑这样一种情况：client发出的第一个连接请求报文段并没有丢失，而是在某个网络结点长时间的滞留了，以致延误到连接释放以后的某个时间才到达server。本来这是一个早已失效的报文段。但server收到此失效的连接请求报文段后，就误认为是client再次发出的一个新的连接请求。于是就向client发出确认报文段，同意建立连接。假设不采用“三次握手”，那么只要server发出确认，新的连接就建立了。由于现在client并没有发出建立连接的请求，因此不会理睬server的确认，也不会向server发送ack包。但server却以为新的运输连接已经建立，并一直等待client发来数据。这样，server的很多资源就白白浪费掉了。采用“三次握手”的办法可以防止上述现象发生。例如刚才那种情况，client不会向server的确认发出确认。server由于收不到确认，就知道client并没有要求建立连接。
>
> ​		还有一种说法是：三次握手的过程即是通信双方相互告知序列号起始值， 并确认对方已经收到了序列号起始值的必经步骤。**如果只是两次握手， 至多只有连接发起方的起始序列号能被确认**， 另一方选择的序列号则得不到确认

<font color = "red">【问题4】如果已经建立了连接，但是客户端突然出现故障了怎么办？</font>

​	**答**：TCP还设有一个保活计时器，显然，客户端如果出现故障，服务器不能一直等下去，白白浪费资源。服务器每收到一次客户端的请求后都会重新复位这个计时器，时间通常是设置为2小时，若两小时还没有收到客户端的任何数据，服务器就会发送一个探测报文段，以后每隔75秒钟发送一次。若一连发送10个探测报文仍然没反应，服务器就认为客户端出了故障，接着就关闭连接。

## 1.5 网络编程三要素

### 协议

<!--P406-->

* **协议：**计算机网络通信必须遵守的规则。

### IP地址

* **IP地址：指互联网协议地址，俗称IP**。IP地址用来给一个网络中的计算机设备做为一个编号。加入我们把“个人电脑”比作“一台电话”的话，那么“IP地址”就是“电话号码”。

**IP地址分类**

* IPv4：是一个32位的二进制数，通常别分为4个字节，表示成`a.b.c.d`的形式，例如`192.168.65.100`。其中a,b,c,d都是0~255之间的十进制整数，那么最多可以表示42亿个。

* IPv6：由于互联网的蓬勃发展，IP地址的需求量愈来愈大，但网络地址资源有限，是的IP的分配越发紧张。

  为了扩大地址空间，拟通过IPv6重新定义地址空间，采用128位地址长度，每16个字节为一组，分为8组十六进制数，表示成`ABCD:EF01:2345:6789:ABCD:EF01:2345:6789`，号称可以为全世界的每一粒沙子编一个网址，这样就解决了网络地址资源数量不够的问题。

**常用命令**

* 查看本机IP地址，在控制台输入：

  ```ABAP
  ipconfig
  ```

  对于Linux操作系统则不同，有的是：`ip addr`。

* 检查网络是否连通，在控制台输入：

  ```asciiarmor
  ping 空格 IP地址
  ping 220.182.34.23
  ```

**特殊的IP地址**

* 本机IP地址：`127.0.0.1`、`localhost`。

### 端口号

<!--P407-->

网络的通信，本质上是两个进程（应用程序）的通信，每台计算机都有很多的进程，那么在网络通信时，如何区分这些进程呢？

如果说**IP地址**可以唯一标识网络中的设备，那么端口号就可以唯一标识设备中的进程（应用程序）了。

* **端口号：用两个字节表示的整数，它的取值范围是0~65535。**其中，0~1023之间的端口号用于一些知名的网络服务和应用，普通的应用程序需要使用1024以上的端口号。如果端口号被另外一个服务或者应用所占用，会导致当前程序启动失败。

利用`协议`+`IP地址`+`端口号`三元组合，就可以标识网络中的进程了，那么进程间的通信就可以利用这个标识与其他进程进行交互。

![端口号](D:\Java\笔记\图片\day11【网络编程】\4.png)

# 第二章 TCP通信程序

## 2.1 概述

<!--P408-->

<!--P409-->

TCP通信能实现两台计算机之间的数据交互，通信的两端，要严格区分为客户端（Client）与服务端（Server）。

**两端通信时步骤：**

1. 服务端程序，需要事先启动，等待客户端的连接。
2. 客户端主动连接服务器端，连接成功才能通信，服务端不可以主动连接客户端。

**在java中，提供了两个类用于事先TCP通信程序：**

1. 客户端：`java.net.Socket`类表示。创建`Socket`对象，向服务端发送连接请求，服务端响应请求，两者建立连接开始通信。
2. 服务端：`java.net.ServerSocket`类表示。创建`ServerSocket`对象，相当于开启一个服务，并等待服务端的连接。

![TCP通信程序](D:\Java\笔记\图片\day11【网络编程】\5.png)

## 2.2 Socket

我们知道两个进程如果需要进行通讯最基本的一个前提能能够唯一的标示一个进程，在本地进程通讯中我们可以使用PID来唯一标示一个进程，但PID只在本地唯一，网络中的两个进程PID冲突几率很大，这时候我们需要另辟它径了，我们知道IP层的ip地址可以唯一标示主机，而TCP层协议和端口号可以唯一标示主机的一个进程，这样我们可以利用ip地址＋协议＋端口号唯一标示网络中的一个进程。

能够唯一标示网络中的进程后，它们就可以利用socket进行通信了，什么是socket呢？我们经常把socket翻译为套接字，socket是在应用层和传输层之间的一个抽象层，它把TCP/IP层复杂的操作抽象为几个简单的接口供应用层调用已实现进程在网络中通信。

![img](D:\Java\笔记\图片\day11【网络编程】\Center2)

### socket通信流程

socket是"打开—读/写—关闭"模式的实现，以使用TCP协议通讯的socket为例，其交互流程大概是这样子的

![img](D:\Java\笔记\图片\day11【网络编程】\Center3)



- 服务器根据地址类型（ipv4,ipv6）、socket类型、协议创建socket
- 服务器为socket绑定ip地址和端口号
- 服务器socket监听端口号请求，随时准备接收客户端发来的连接，这时候服务器的socket并没有被打开
- 客户端创建socket
- 客户端打开socket，根据服务器ip地址和端口号试图连接服务器socket
- <font color = "red">服务器socket接收到客户端socket请求，被动打开，开始接收客户端请求，直到客户端返回连接信息。这时候socket进入**阻塞**状态，所谓阻塞即accept()方法一直到客户端返回连接信息后才返回，开始接收下一个客户端谅解请求</font>
- 客户端连接成功，向服务器发送连接状态信息
- 服务器accept方法返回，连接成功
- 客户端向socket写入信息
- 服务器读取信息
- 客户端关闭
- 服务器端关闭

**利用Socket建立网络连接的步骤**

建立Socket连接至少需要一对套接字，其中一个运行于客户端，称为ClientSocket ，另一个运行于服务器端，称为ServerSocket 。

套接字之间的连接过程分为三个步骤：服务器监听，客户端请求，连接确认。

1. 服务器监听：服务器端套接字并不定位具体的客户端套接字，而是处于等待连接的状态，实时监控网络状态，等待客户端的连接请求。
2. 客户端请求：指客户端的套接字提出连接请求，要连接的目标是服务器端的套接字。
   为此，客户端的套接字必须首先描述它要连接的服务器的套接字，指出服务器端套接字的地址和端口号，然后就向服务器端套接字提出连接请求。

3. 连接确认：当服务器端套接字监听到或者说接收到客户端套接字的连接请求时，就响应客户端套接字的请求，建立一个新的线程，把服务器端套接字的描述发给客户端，一旦客户端确认了此描述，双方就正式建立连接。
   而服务器端套接字继续处于监听状态，继续接收其他客户端套接字的连接请求。

## 2.2 Socket类

<!--P410-->

<!--socket一般指套接字。 所谓套接字(Socket) -->

`Socket`类：该类实现客户端套接字，套接字指的是两台设备之间通讯的端点。

**构造方法**

* `public Socket(String host, int port)`: 创建套接字对象并将其连接到指定主机上的指定端口号。如果指定的host是null，则相当于指定地址为回送地址。

  * `String host`: 服务器主机的名称/服务器的IP地址
* `int port`：服务器的端口号
  
> 小贴士：回送地址(127.x.x.x)是本机回送地址(Loopback Address)，主要用于网络软件测试以及本地机进程间通信，无论什么程序，一旦使用回送地址发送数据，立即返回，不进行任何网络传输。

构造举例，代码如下：

```java
Socket client = new Socket("127.0.0.1", 6666);
```

**成员方法**

* `OutputStream getOutputStream()`: 返回此套接字的输出流。
* `InputStream getInputStream()`: 返回此套接字的输入流。
* `void close()`: 关闭此套接字。

**实现步骤**

1. 创建一个客户端对象`Socket`，构造方法绑定服务器的IP地址和端口号。

2. 使用`Socket`对象中的方法`getOutputStream()`获取网络字节输出流`OutputStream`对象。

3. 使用网络字节输出流`OutputStream`对象中的方法`write`，给服务器发送数据。

4. 使用`Socket`对象中的方法`InputStream()`获取网络字节输入流`InputStream`对象。

5. 使用网络字节输入流`InputStream`对象中的方法`read`，读取服务器回写的数据。

6. 释放资源(Socket)

   > 注意：
   >
   > * 客户端和服务器进行交互，必须使用Socket中提供的网络流，不能使用自己创建的流对象。
   > * 当我们创建客户端对象Socket的时候，就会去请求服务器和服务器经过3次握手，建立连接通路。
   >   * 这时如果服务器没有启动会抛出异常。`java.net.ConnectException: Connection refused: connect`
   >   * 如果已经启动，可以进行交互。

## 2.3 ServerSocket类

<!--P411-->

**构造方法**

* `ServerSocket(int port)`: 创建绑定到特定端口的服务器套接字。

**成员方法**

* `Socket accept()`：侦听并接受此套接字的连接。

**实现步骤**

1. 创建服务器`ServerSocket`对象和系统要指定的端口号。
2. 使用`ServerSoket`对象中的方法`accept`，获取到请求的客户端对象`Socket`。
3. 使用`Socket`对象中的方法`getInputStream()`获取网络字节输入流`InputStream`对象。
4. 使用网络字节输入流`InputStream`对象的`read`方法，读取客户端发送的数据。
5. 使用Socket对象中的方法`getOutputStream()`获取网络字节输出流`OutputStream`对象。
6. 使用网络字节输出流`OutputStream`对象的`write`方法，回写数据。
7. 释放资源（Socket，ServerSocket）

## 2.4 简单的TCP网络程序

**客户端向服务器发送数据**

```java
public class Demo01TcpClient {
    public static void main(String[] args) throws IOException {
        // 1. 创建一个客户端对象Socket，构造方法绑定服务器的IP地址和端口号。
        Socket socket = new Socket("127.0.0.1", 1000);
        // 2. 使用Socket对象中的方法getOutputStream()获取网络字节输出流OutputStream对象。
        OutputStream os = socket.getOutputStream();
        // 3. 使用网络字节输出流OutputStream对象中的方法write，给服务器发送数据。
        os.write("林炫你好".getBytes());
        // 4. 使用Socket对象中的方法getInputStream()获取网络字节输入流InputStream对象。
        InputStream is = socket.getInputStream();
        // 5. 使用网络字节输入流InputStream对象中的方法read，读取服务器回写的数据。
        byte[] bytes = new byte[1024];
        int len = is.read(bytes);
        System.out.println(new String(bytes, 0, len));
        // 6. 释放资源(Socket)
        socket.close();
    }
}
```

**服务器向客户端回写数据**

```java
public class Demo01TCPServer {
    public static void main(String[] args) throws IOException {
        // 1. 创建服务器ServerSocket对象和系统要指定的端口号。
        ServerSocket serversocket = new ServerSocket(1000);
        // 2. 使用ServerSoket对象中的方法accept，获取到请求的客户端对象Socket。
        Socket socket = serversocket.accept();
        // 3. 使用Socket对象中的方法getInputStream()获取网络字节输入流InputStream对象。
        InputStream is = socket.getInputStream();
        // 4. 使用网络字节输入流InputStream对象的read方法，读取客户端发送的数据。
        byte[] bytes = new byte[1024];
        int len = is.read(bytes);
        System.out.println(new String(bytes, 0, len));
        // 5. 使用Socket对象中的方法getOutputStream()获取网络字节输出流OutputStream对象
        OutputStream os = socket.getOutputStream();
        // 6. 使用网络字节输出流OutputStream对象的write方法，回写数据
        os.write("收到谢谢".getBytes());
        // 7. 释放资源（Socket，ServerSocket）
        socket.close();
        serversocket.close();
    }
}
```

# 第三章 综合案例

## 3.1 文件上传案例

### 文件上传分析图解

<!--P412-->

1. 【客户端】输入流，从硬盘读取文件数据到程序中。
2. 【客户端】输出流，写出文件数据到服务端。
3. 【服务端】输入流，读取文件数据到服务端程序。
4. 【服务端】输出流，写出文件数据到服务器硬盘中。

![6](D:\Java\笔记\图片\day11【网络编程】\6.png)

#### 基本实现

![7](D:\Java\笔记\图片\day11【网络编程】\7.png)

<!--P413-->

**客户端**

1. 创建一个本地字节输入流`FileInputStream`对象，构造方法中绑定要读取的数据源。
2. 创建一个客户端`Socket`对象，构造方法中绑定服务器的IP地址和端口号。
3. 使用`Socket`中的方法`getOutputStream`，获取网络字节输出流`OutputStream`对象。
4. 使用本地字节输入流`FileInputStream`对象的`read`方法，读取本地文件。
5. 使用网络字节输出流`OutputStream`对象中的`write`方法，把读取到的文件上传到服务器。
6. 使用`Socket`中的方法`getInputStream`，获取网络字节输入流`InputStream`对象。
7. 使用网络字节输入流`InputStream`对象中的read方法读取服务端回写的数据。
8. 释放资源（FileInputStream, Socket）。

```java
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

// 客户端
public class TCPClient {
    public static void main(String[] args) throws IOException {
        // 1. 创建一个本地字节输入流FileInputStream对象，构造方法中绑定要读取的数据源。
        FileInputStream fis = new FileInputStream("D:\\图片\\动漫\\清纯诱惑\\12.jpg");
        // 2. 创建一个客户端Socket对象，构造方法中绑定服务器的IP地址和端口号。
        Socket socket = new Socket("127.0.0.1", 8888);
        // 3. 使用Socket中的方法getOutputStream，获取网络字节输出流OutputStream对象。
        OutputStream os = socket.getOutputStream();
        // 4. 使用本地字节输入流FileInputStream对象的read方法，读取本地文件。
        byte[] bytes = new byte[1024];
        int len = 0;
        while((len = fis.read(bytes)) != -1) {
            // 5. 使用网络字节输出流OutputStream对象中的write方法，把读取到的文件上传到服务器。
            os.write(bytes, 0, len);
        }

        // 6. 使用Socket中的方法getInputStream，获取网络字节输入流InputStream对象。
        InputStream is  = socket.getInputStream();
        // 7. 使用网络字节输入流InputStream对象中的read方法读取服务端回写的数据。
        len = 0;
        while((len = is.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, len));
        }
        // 8. 释放资源（FileInputStream, Socket）。
        fis.close();
        socket.close();
    }
}
```

<!--P414-->

**服务器端**

1. 创建一个服务器`ServerSocket`对象，和系统要指定的端口号。
2. 使用`ServerSocket`对象中的`accept`方法，获取到要请求的客户端`Socket`对象。
3. 判断文件夹是否存在，不存在则创建。
4. 使用`Socket`对象中的方法`getInputStream`，获取到网络字节输入流`InputStream`对象。
5. 使用网络字节输入流`InputStream`对象中的read方法，读取客户端上传的文件。
6. 创建一个本地字节输出流`FileOutputStream`对象，构造方法中绑定要输出的目的地。
7. 使用本地字节输出流`FileOutputStream`对象中的`write`方法，把读取到的文件保存到服务器的硬盘上。
8. 使用Socket对象中的方法`getOutputStream`，获取到网络字节输入流`OutputStream`对象。
9. 使用网络字节输出流`OutputStream`对象中的方法`write`，给客户端回写“上传成功”。
10. 释放资源（FileOutputStream, Socket, ServerSocket）。

```java
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) throws IOException {
        // 1. 创建一个服务器ServerSocket对象，和系统要指定的端口号。
        ServerSocket serversocket = new ServerSocket(8888);
        // 2. 使用ServerSocket对象中的accept方法，获取到要请求的客户端Socket对象。
        Socket socket = serversocket.accept();
        // 3. 使用Socket对象中的方法getInputStream，获取到网络字节输入流InputStream对象。
        InputStream is = socket.getInputStream();
        // 4. 判断文件夹是否存在，不存在则创建。
        File file = new File("D:\\Java\\Upload");
        if (!file.exists()) {
            file.mkdirs();
        }
        // 5. 创建一个本地字节输出流FileOutputStream对象，构造方法中绑定要输出的目的地。
        FileOutputStream fos = new FileOutputStream(file + "\\1.jpg");
        // 6.使用网络字节输入流InputStream对象中的read方法，读取客户端上传的文件。
        byte[] bytes = new byte[1024];
        int len = 0;
        while((len = is.read(bytes)) != -1) {
            // 7. 使用本地字节输出流FileOutputStream对象中的write方法，把读取到的文件保存到服务器的硬盘上。
            fos.write(bytes, 0, len);
        }
        // 8. 使用Socket对象中的方法getOutputStream，获取到网络字节输入流OutputStream对象。
        OutputStream os = socket.getOutputStream();
        // 9. 使用网络字节输出流OutputStream对象中的方法write，给客户端回写“上传成功”。
        os.write("上传成功".getBytes());
        // 10. 释放资源（FileOutputStream, Socket, ServerSocket）。
        fos.close();
        socket.close();
        serversocket.close();
    }
}
```

<!--P415-->

**问题**

* 上传完文件后，一直不结束，陷入死循环，需要结束标记
* void shutdownOutput() 禁用此套接字的输出流
* 对于TCP套接字，任何以前写入对的数据将被发送，并且后跟TCP的正常终止序列。

```java
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

// 客户端
public class TCPClient {
    public static void main(String[] args) throws IOException {
        // 1. 创建一个本地字节输入流FileInputStream对象，构造方法中绑定要读取的数据源。
        FileInputStream fis = new FileInputStream("D:\\图片\\动漫\\清纯诱惑\\12.jpg");
        // 2. 创建一个客户端Socket对象，构造方法中绑定服务器的IP地址和端口号。
        Socket socket = new Socket("127.0.0.1", 8888);
        // 3. 使用Socket中的方法getOutputStream，获取网络字节输出流OutputStream对象。
        OutputStream os = socket.getOutputStream();
        // 4. 使用本地字节输入流FileInputStream对象的read方法，读取本地文件。
        int len = 0;
        byte[] bytes = new byte[1024];
        while((len = fis.read(bytes)) != -1) {
            // 5. 使用网络字节输出流OutputStream对象中的write方法，把读取到的文件上传到服务器。
            os.write(bytes, 0, len);
        }

        // 上传完文件后，一直不结束，陷入死循环，需要结束标记
        // void shutdownOutput() 禁用此套接字的输出流
        // 对于TCP套接字，任何以前写入对的数据将被发送，并且后跟TCP的正常终止序列。
        socket.shutdownOutput();

        // 6. 使用Socket中的方法getInputStream，获取网络字节输入流InputStream对象。
        InputStream is  = socket.getInputStream();
        // 7. 使用网络字节输入流InputStream对象中的read方法读取服务端回写的数据。
        while((len = is.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, len));
        }
        // 8. 释放资源（FileInputStream, Socket）。
        fis.close();
        socket.close();
    }
}
```

### 文件上传优化分析

<!--P416 1.14-->

1. 文件名称写死问题

   服务端，保存文件的名称如果写死，那么最终导致服务器硬盘，只会保留一个文件，建议使用系统时间优化，保证文件名称唯一。

   ```java
   FileOutputStream fis = new FileOutputStream(System.currentTimeMillis()."jpg");// 文件名称
   BufferedOutputStream bos = new BufferedOutputStream(fis);
   ```

2. 循环接收的问题

   服务端，指保存一个文件就关闭了，之后的用户无法再上传，这是不符合实际的，使用循环改进，可以不断接收不同用户的文件。

   ```java
   // 每次接收新的连接，创建一个新的Socket
   while (true) {
       Socket socket = new Socket.accept();
       ...
   }
   ```

3. 效率问题

   服务端，在接收大文件时，可能耗费几秒时间，此时不能接收其他用户上传，所以使用多线程技术优化。

   ```java
   while (true) {
       new Thread(new Runnable() {
           @Override
           public void run() {
               try{
                   ...
               } catch(IOException e) {
                   System.out.println(e);
               }
           }
       }).start();
   }
   ```

#### 优化实现

```java
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

// 服务器端
public class TCPServer {
    public static void main(String[] args) throws IOException {
        // 1. 创建一个服务器ServerSocket对象，和系统要指定的端口号。
        ServerSocket serversocket = new ServerSocket(8888);

        // 让服务器一直处于监听状态，一直运行，使用死循环
        while (true) {
            // 2. 使用ServerSocket对象中的accept方法，获取到要请求的客户端Socket对象。
            Socket socket = serversocket.accept();

            // 使用多线程的方式，让多个客户端上传文件
            new Thread(new Runnable() {
                // 重写run方法，完成文件的上传
                @Override
                public void run() {
                    try {
                        // 3. 使用Socket对象中的方法getInputStream，获取到网络字节输入流InputStream对象。
                        InputStream is = socket.getInputStream();
                        // 4. 判断文件夹是否存在，不存在则创建。
                        File file = new File("D:\\Java\\Upload");
                        if (!file.exists()) {
                            file.mkdirs();
                        }

                        // 服务端，保存文件的名称如果写死，那么最终导致服务器硬盘，只会保留一个文件，建议使用系统时间优化，保证文件名称唯一。
                        String filename = "com.cn" + System.currentTimeMillis() + new Random().nextInt(123456) + ".jpg";
                        // 5. 创建一个本地字节输出流FileOutputStream对象，构造方法中绑定要输出的目的地。
                        // FileOutputStream fos = new FileOutputStream(file + "\\12.jpg");
                        FileOutputStream fos = new FileOutputStream(file + "\\" + filename);

                        // 6.使用网络字节输入流InputStream对象中的read方法，读取客户端上传的文件。
                        byte[] bytes = new byte[1024];
                        int len = 0;
                        while((len = is.read(bytes)) != -1) {
                            // 7. 使用本地字节输出流FileOutputStream对象中的write方法，把读取到的文件保存到服务器的硬盘上。
                            fos.write(bytes, 0, len);
                        }
                        // 8. 使用Socket对象中的方法getOutputStream，获取到网络字节输入流OutputStream对象。
                        OutputStream os = socket.getOutputStream();
                        // 9. 使用网络字节输出流OutputStream对象中的方法write，给客户端回写“上传成功”。
                        os.write("上传成功".getBytes());
                        // 10. 释放资源（FileOutputStream, Socket, ServerSocket）。
                        fos.close();
                        socket.close();
                    } catch(IOException e) {
                        System.out.println(e);
                    }
                }
            }).start();
        }

        // 服务器不用关闭了，一直启动着
        // serversocket.close();
    }
}
```

## 3.2 模拟B/S服务器（扩展知识点）

<!--P417-->

模拟网站服务器，使用浏览器访问自己编写的服务端程序，查看网页效果。

### 案例分析

![8](D:\Java\笔记\图片\day11【网络编程】\8.png)

1. 准备页面数据，web文件夹

   复制到我们Moudule中。

2. 我们模拟服务器端，ServerSocket类监听端口，使用浏览器访问。

### 案例实现

<!--P418-->

```java
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

// 创建B/S版本TCP服务器
public class TCPServer {
    public static void main(String[] args) throws IOException {
        // 创建一个服务器ServerSocket，并指定端口号
        ServerSocket serversocket = new ServerSocket(8080);

        // 浏览器解析服务器回写的HTML文件，如果有图片，那么会重新单独的开启一个线程
        // 所以我们需要让服务器一直处于监听状态，然后使用多线程
        while(true) {
            // 使用accept方法获取到请求的客户端对象（浏览器）
            Socket socket = serversocket.accept();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 使用soket对象中的getInputStream方法，获取到网络字节输入流InputStream对象
                        InputStream is = socket.getInputStream();
                        // 使用网络字节输入流InputStream对象中的方法read读取客户端的请求信息
            /*
                    byte[] bytes = new byte[1024];
                    int len = 0;
                    while ((len = is.read(bytes)) != -1) {
                        System.out.println(new String(bytes, 0, len));
                    }
            */


                        // 把is网络字节输入流对象，转换为字符缓冲输入流
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        // 把客户端请求信息的第一行读取出来 GET /11_Net/web/index.html HTTP/1.1
                        String line = br.readLine();
                        // 把读取的信息进行切割，只要中间部分 /11_Net/web/index.html
                        String[] array = line.split(" ");
                        // 把路径前面的/去掉，截取路径 11_Net/web/index.html
                        String htmlpath = array[1].substring(1);

                        // 创建一个本地输入流，构造方法中绑定要读取的HTML路径
                        FileInputStream fis = new FileInputStream(htmlpath);
                        // 使用Socket中的方法getOutputStream获取网络字节输出流OutputStream对象
                        OutputStream os = socket.getOutputStream();

                        // 写入HTTP协议响应开头，固定写法
                        os.write("HTTP/1.1 200 OK\r\n".getBytes());
                        os.write("Content-Type:text/html\r\n".getBytes());
                        // 必须写入空行，否则浏览器不会解析
                        os.write("\r\n".getBytes());

                        // 一读一写复制文件，把服务器读取的HTML文件回写到客户端
                        int len = 0;
                        byte[] bytes = new byte[1024];
                        while((len = fis.read(bytes)) != -1) {
                            os.write(bytes,0, len);
                        }

                        // 释放资源
                        fis.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        
        // 不用关闭服务器
        // serversocket.close();
    }
}
```

# 第四章 OSI七层模型

<font color = "red">应用--表示--会话--传输--网络--数据链路--物理</font>

![](D:\Java\笔记\图片\day11【网络编程】\OSI七层模型.png)



| OSI七层模型 | 功能                                                         | 对应的网络协议                                               |
| ----------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 应用层      | 文件传输，文件管理，电子邮件的信息处理——apdu                 | HTTP、TFTP, FTP, NFS, WAIS、POP3、SMTP                       |
| 表示层      | 确保一个系统的应用层发送的消息可以被另一个系统的应用层读取，编码转换，数据解析，管理数据的解密和加密，最小单位——ppdu | Telnet, Rlogin, SNMP, Gopher                                 |
| 会话层      | 负责在网络中的两节点建立，维持和终止通信，在一层协议中，可以解决节点连接的协调和管理问题。包括通信连接的建立，保持会话过程通信连接的畅通，两节点之间的对话，决定通信是否被终端一斤通信终端是决定从何处重新发送，最小单位——spdu | SMTP, DNS                                                    |
| 传输层      | 定义一些传输数据的协议和端口。传输协议同时进行流量控制，或是根据接收方接收数据的快慢程度，规定适当的发送速率，解决传输效率及能力的问题——tpdu | TCP, UDP                                                     |
| 网络层      | 控制子网的运行，如逻辑编址，分组传输，路由选择最小单位——分组（包）报文 | IP, ICMP, ARP, RARP, AKP, UUCP                               |
| 数据链路层  | 主要是对物理层传输的比特流包装，检测保证数据传输的可靠性，将物理层接收的数据进行MAC（媒体访问控制）地址的封装和解封装，也可以简单的理解为物理寻址。交换机就处在这一层，最小的传输单位——帧 | FDDI, Ethernet, Arpanet, PDN, SLIP, PPP，STP。HDLC,SDLC,帧中继 |
| 物理层      | 定义物理设备的标准，主要对物理连接方式，电气特性，机械特性等制定统一标准，传输比特流，因此最小的传输单位——位（比特流） | IEEE 802.1A, IEEE 802.2到IEEE 802.                           |

<font color = "red">应用--表示--会话--传输--网络--数据链路--物理</font>

## 应用层

​    OSI参考模型中最靠近用户的一层，是为计算机用户提供应用接口，也为用户直接提供各种网络服务。我们常见应用层的网络服务协议有：HTTP，HTTPS，FTP，POP3、SMTP等。

​    <font color = "red">实际公司A的老板就是我们所述的用户，而他要发送的商业报价单，就是应用层提供的一种网络服务，当然，老板也可以选择其他服务，比如说，发一份商业合同，发一份询价单，等等。</font>

## 表示层

​    表示层提供各种用于应用层数据的编码和转换功能,确保一个系统的应用层发送的数据能被另一个系统的应用层识别。如果必要，该层可提供一种标准表示形式，用于将计算机内部的多种数据格式转换成通信中采用的标准表示形式。数据压缩和加密也是表示层可提供的转换功能之一。

​    <font color = "red">由于公司A和公司B是不同国家的公司，他们之间的商定统一用英语作为交流的语言，所以此时表示层（公司的文秘），就是将应用层的传递信息转翻译成英语。同时为了防止别的公司看到，公司A的人也会对这份报价单做一些加密的处理。这就是表示的作用，将应用层的数据转换翻译等。</font>

## 会话层

​    会话层就是负责建立、管理和终止表示层实体之间的通信会话。该层的通信由不同设备中的应用程序之间的服务请求和响应组成。   

​    <font color = "red">会话层的同事拿到表示层的同事转换后资料，（会话层的同事类似公司的外联部），会话层的同事那里可能会掌握本公司与其他好多公司的联系方式，这里公司就是实际传递过程中的实体。他们要管理本公司与外界好多公司的联系会话。当接收到表示层的数据后，会话层将会建立并记录本次会话，他首先要找到公司B的地址信息，然后将整份资料放进信封，并写上地址和联系方式。准备将资料寄出。等到确定公司B接收到此份报价单后，此次会话就算结束了，外联部的同事就会终止此次会话。</font>

## 传输层

​    传输层建立了主机端到端的链接，传输层的作用是为上层协议提供端到端的可靠和透明的数据传输服务，包括处理差错控制和流量控制等问题。该层向高层屏蔽了下层数据通信的细节，使高层用户看到的只是在两个传输实体间的一条主机到主机的、可由用户控制和设定的、可靠的数据通路。我们通常说的，TCP UDP就是在这一层。端口号既是这里的“端”。

​    <font color = "red">传输层就相当于公司中的负责快递邮件收发的人，公司自己的投递员，他们负责将上一层的要寄出的资料投递到快递公司或邮局。</font>

## 网络层

​    本层通过IP寻址来建立两个节点之间的连接，为源端的运输层送来的分组，选择合适的路由和交换节点，正确无误地按照地址传送给目的端的运输层。就是通常说的IP层。这一层就是我们经常说的IP协议层。IP协议是Internet的基础。

​    <font color = "red">网络层就相当于快递公司庞大的快递网络，全国不同的集散中心，比如说，从深圳发往北京的顺丰快递（陆运为例啊，空运好像直接就飞到北京了），首先要到顺丰的深圳集散中心，从深圳集散中心再送到武汉集散中心，从武汉集散中心再寄到北京顺义集散中心。这个每个集散中心，就相当于网络中的一个IP节点。</font>

## 数据链路层 

​    将比特组合成字节,再将字节组合成帧,使用链路层地址 (以太网使用MAC地址)来访问介质,并进行差错检测。

   数据链路层又分为2个子层：逻辑链路控制子层（LLC）和媒体访问控制子层（MAC）。

​    MAC子层处理CSMA/CD算法、数据出错校验、成帧等；LLC子层定义了一些字段使上次协议能共享数据链路层。 在实际使用中，LLC子层并非必需的。

   <font color = "red"> 这个没找到合适的例子</font>

## 物理层   

​    实际最终信号的传输是通过物理层实现的。通过物理介质传输比特流。规定了电平、速度和电缆针脚。常用设备有（各种物理设备）集线器、中继器、调制解调器、网线、双绞线、同轴电缆。这些都是物理层的传输介质。

​     <font color = "red">快递寄送过程中的交通工具，就相当于我们的物理层，例如汽车，火车，飞机，船。</font>

# 第五章 TCP/IP网络协议五层模型

自底向上分别为物理层、链接层、网络层、传输层、应用层。

![](D:\Java\笔记\图片\day11【网络编程】\五层模型.png)

## 物理层

两个网络设备间实现比特流的透明传输，传输010101二进制的电信号。

## 数据链路层

单纯的0和1没有意义，必须规定解读方式：多少个电信号为一组，每个信号应有何意义。这就是“链路层”的功能。

### 以太网协议

以太网规定，一组电信号构成一个数据包，叫做帧，每一帧分成两个部分：标头（Head）和数据（Data）。

“标头”包含数据包的一些说明项，比如发送者、接受者、数据类型等等；“数据”则是数据包的具体内容。

“标头”的长度，固定为18字节。“数据”的长度，最短为46字节，最长为1500字节。因此，整个帧最短为64字节，最长为1518字节。如果数据很长，就必须分割成多个帧进行发送。

### MAC地址

发送者和接受者如何标识呢？

以太网规定，进入网络所有设备，都必须具有“网卡”接口。数据包必须是从一块网卡。传送到另一块网卡。网卡的地址，就是数据包的发送地址和接收地址，这叫做MAC地址。

每块网卡出厂的时候，都有一个全世界独一无二的ＭＡＣ地址，长度是４８个二进制位，通常用１２个十六进制数表示。

### 广播

定义地址只是第一步，后面还有更多步骤。

首先，一块网卡如何知道另一块网卡的ＭＡＣ地址？

ARP协议可以解决这个问题。

有了ＭＡＣ地址，系统怎样才能把数据包准确送到接收方？

以太网采用了一种原始的方式，它不是把数据包准确送到接收方，而是向本网络内所有计算机发送，让每台计算机自己判断，是否为接收方。

![](D:\Java\笔记\图片\day11【网络编程】\广播.png)

上图中，1号计算机向2号计算机发送一个数据包，同一个子网络的3号、4号、5号计算机都会收到这个包。它们读取这个包的"标头"，找到接收方的MAC地址，然后与自身的MAC地址相比较，如果两者相同，就接受这个包，做进一步处理，否则就丢弃这个包。这种发送方式就叫做"广播"（broadcasting）。

## 网络层

他的作用是引进一套新的地址，使得我们能够区分不同的计算机是否属于同一个子网络，这套地址就叫做“网络地址”，简称网址。

网址帮助我们确定计算机所在的子网络，MAC地址是绑定网卡上的，网络地址则是管理员分配的，它们只是随机组合在一起。

网址帮助我们确定计算机所在的子网络，mac地址则将数据包送到该子网络中的目标网卡。因此，从逻辑上可以推断，必定是先处理网络地址，然后再处理MAC地址。

### IP协议

IP地址不能直接用来进行通信，在实际网络的链路上传送数据帧必须使用硬件地址。

规定网络地址的协议，叫做IP协议。它所在的地址，被称为IP地址。目前广泛采用的是IP协议第四版，规定网络地址由32个二进制位组成。

习惯上，我们用分成四段的十进制数表示IP地址，从0.0.0.0到255.255.255.255.每个字段是一个字节，8位，最大值是255.地址分为两个部分，前一部分代表网络，后一部分代表主机。

IP地址的四大类型标识的是网络中的某台主机。IPv4的地址长度为32位，共4个字节，

Ip地址根据网络号和主机号分为A,B,C三类及特殊地址D、E，全0和全1的都保留不用。大多数公司都使用A类网络地址的一大原因，因为它们可使用所有的子网掩码，进行网络设计时的灵活性最大。

![](D:\Java\笔记\图片\day11【网络编程】\IP地址.png)

* A类：(1.0.0.0-126.0.0.0)（默认子网掩码：255.0.0.0或 0xFF000000）第一个字节为网络号，后三个字节为主机号。该类IP地址的最前面为“0”，即最大为01111111127，但是127被用作测试使用，所以最大为126，所以地址的网络号取值于1~126之间。一般用于大型网络。

* B类：(128.0.0.0-191.255.0.0)（默认子网掩码：255.255.0.0或0xFFFF0000）前两个字节为网络号，后两个字节为主机号。该类IP地址的最前面为“10”，1000 0000（128）-1011 1111（191）所以地址的网络号取值于128~191之间。一般用于中等规模网络。

* C类：(192.0.0.0-223.255.255.0)（子网掩码：255.255.255.0或 0xFFFFFF00）前三个字节为网络号，最后一个字节为主机号。该类IP地址的最前面为“110”，1100 0000（192）-1101 1111（223） 所以地址的网络号取值于192~223之间。一般用于小型网络。

* D类：是多播地址。该类IP地址的最前面为“1110”，所以地址的网络号取值于224~239之间。一般用于多路广播用户[1] 。

* E类：是保留地址。该类IP地址的最前面为“1111”，所以地址的网络号取值于240~255之间。

在IP地址3种主要类型里，各保留了3个区域作为私有地址，其地址范围如下：

* A类地址：10.0.0.0～10.255.255.255
* B类地址：172.16.0.0～172.31.255.255
* C类地址：192.168.0.0～192.168.255.255

回送地址：127.0.0.1。 也是本机地址，等效于localhost或本机IP。一般用于测试使用。例如：ping 127.0.0.1来测试本机TCP/IP是否正常。
互联网上的每一台计算机，都会分配到一个IP地址。这个地址分为两个部分，前一部分代表网络，后一部分代表主机。比如，IP地址172.16.254.1，这是一个32位的地址，假定它的网络部分是前24位（172.16.254），那么主机部分就是后8位（最后的那个1）。处于同一个子网络的电脑，它们IP地址的网络部分必定是相同的，也就是说172.16.254.2应该与172.16.254.1处在同一个子网络。

但是，问题在于单单从IP地址，我们无法判断网络部分，还是以172.16.254.1为例，它的网络部分，到底是前24位，还是前16位，甚至是前28位，从IP地址上是看不出来的。

判断两台计算机属于同一个子网络，需要用到“子网掩码”。

所谓“子网掩码”，就是表示子网络特征的一个参数。它在形式上等同于IP地址，也是一个32位二进制数字，它的网络部分全部为1，主机部分全部为0。比如，IP地址172.16.254.1，如果已知网络部分是前24位，主机部分是后8位，那么子网络掩码就是11111111.11111111.11111111.00000000，写成十进制就是255.255.255.0。

知道“子网掩码”，我们就能判断，任意两个IP地址是否处于同一个子网络，方法是将两个IP地址与子网掩码分别进行AND运算，结果相同则说明在同一个子网络中。

比如，已知IP地址172.16.254.1和172.16.254.233的子网掩码都是255.255.255.0，请问它们是否在同一个子网络？两者与子网掩码分别进行AND运算，结果都是172.16.254.0，因此它们在同一个子网络。

总结一下，IP协议的作用主要有两个，一个是为每一台计算机分配IP地址，另一个是确定哪些地址在同一个子网络。

子网掩码:通过子网掩码和ip判断两个ip是否处于同一个网段,通过ip地址和子网掩码做按位与运算

- 172.16.10.1：10101100.00010000.00001010.000000001

  - 255.255.255.0:11111111.11111111.11111111.00000000
  - AND运算得网络地址结果：10101100.00010000.00001010.000000000->172.16.10.0

- 172.16.10.2：10101100.00010000.00001010.000000010

  - 255.255.255.0:11111111.11111111.11111111.00000000

  - AND运算得网络地址结果：10101100.00010000.00001010.000000000->172.16.10.0

    结果都是172.16.10.0，因此它们在同一个子网络。

### ARP协议

APR协议：解决同一个局域网上的主机或路由器的**IP地址和硬件地址**的映射问题；

ARP是在仅知道主机的IP地址时确定其物理地址的协议。ARP协议：广播的方式发送数据包，获取目标主机的mac地址

### RARP协议

RARP协议：解决同一个局域网上的主机或路由器的**硬件地址和IP地址**的映射问题；

RARP是将MAC物理地址转换成IP地址.

## 传输层

传输层功能：建立端口到端口的通信

运输层需要有两种不同的运输协议，即面向连接的 TCP 和无连接的 UDP。

> 端口：我们通过IP和Mac找到了一台特定的主机，如何标识这台主机上的应用程序，答案就是端口，端口即应用程序与网卡关联的编号。

有了MAC地址和IP地址，我们已经可以在互联网上任意两台主机上建立通信。

接下来的问题是，同一台主机上有许多程序都需要用到网络，比如，你一边浏览网页，一边与朋友在线聊天。当一个数据包从互联网上发来的时候，你怎么知道，它是表示网页的内容，还是表示在线聊天的内容？

即我们还需要一个参数，表示这个数据包到底供那个程序（进程）使用。这个参数就叫做“端口”（port）,他其实是每一个使用网卡的程序的编号。每个数据包都发到主机的特定端口，所以不同的程序就能取到自己所需要的数据。

“端口”是0到65535之间的一个整数，正好16个二进制位。0到1023的端口被系统占用，用户只能选用大于1023的端口。不管是浏览网页还是在线聊天，应用程序会随机选用一个端口，然后与服务器的相应端口联系。

“传输层”的功能，就是建立“端口到端口”的通信，相比之下，“网络层”
的功能是建立“主机到主机”的通信，只要确定主机和端口，我们就能实现程序之间的交流。

### UDP协议

在数据包中加入端口信息，需要新的协议，最简单的实现叫做UDP协议，他的格式几乎就是在数据前面，加上端口号。

UDP数据包，也是由“标头”和“数据”两部分组成。

“标头”部分主要定义了发出端口和接受端口，“数据”部分就是具体的内容。然后把整个UDP数据包放入IP数据包的“数据”部分，而前面说过，IP数据包又是放在以太网数据包之中的，所以整个以太网数据包现在变成了下面这样：

![](D:\Java\笔记\图片\day11【网络编程】\UDP协议.jpg)



UDP数据包非常简单，“标头”部分一共有8个字节，总长度不超过65535字节，正好放进一个IP数据包。

### TCP协议

UDP协议的优点是比较简单，容易实现，但是可靠性较差，一旦数据包发出，无法知道对方是否收到。

为了解决这个问题，提高网络可靠性，TCP协议就诞生了。这个协议非常复杂，但可以近似认为，它就是有确认机制的UDP协议，每发出一个数据包都要求确认，如果有一个数据包遗失，就收不到确认，发出方就知道有必要重发这个数据包了。

因此，TCP协议能够确保数据不会遗失。它的缺点是过程复杂、实现困难、消耗较多的资源。
TCP数据包和UDP数据包一样，都是内嵌在IP数据包的"数据"部分。TCP数据包没有长度限制，理论上可以无限长，但是为了保证网络的效率，通常TCP数据包的长度不会超过IP数据包的长度，以确保单个TCP数据包不必再分割。

TCP协议:

- TCP 是面向连接的运输层协议。
- 每一条 TCP 连接只能有两个端点(endpoint)，每一条 TCP 连接只能是点对点的（一对一）。
- TCP 提供可靠交付的服务。
- TCP 提供全双工通信。
- 面向字节流。

UDP协议:

特点:

- UDP 是无连接的，即发送数据之前不需要建立连接。
- UDP 使用尽最大努力交付，即不保证可靠交付，同时也不使用拥塞控制。
- UDP 是面向报文的。UDP 没有拥塞控制，很适合多媒体通信的要求。
- UDP 支持一对一、一对多、多对一和多对多的交互通信。
- UDP 的首部开销小，只有 8 个字节。

## 应用层

应用层收到传输层的数据，接下来就要进行解读，由于互联网是开放架构，数据来源五花八门，必须事先规定好格式，否则根本无法解读。

“应用层”的作用，就是规定应用程序的数据格式。

举例来说，TCP协议可以为各种各样的程序传递数据，比如Email、WWW、FTP等等。那么，必须有不同协议规定电子邮件、网页、FTP数据的格式，这些应用程序协议就构成了"应用层"。
这是最高的一层，直接面对用户。它的数据就放在TCP数据包的"数据"部分。因此，现在的以太网的数据包就变成下面这样。

![](D:\Java\笔记\图片\day11【网络编程】\应用层.jpg)

上网流程分析：
1.在浏览器输入www.baidu.com
2.会取dns服务器通过域名解析成ip地址
3.向ip+端口号这个地址发送请求，就会访问到百度的服务器

socket:在应用层和传输层之间的一个抽象层，它把TCP/IP层复杂的操作抽象为几个简单的接口供应用层调用已实现进程在网络中通信

我们常见应用层的网络服务协议有：HTTP，HTTPS，FTP，POP3、SMTP等。

### HTTP协议

#### HTTP 协议的特点

1. HTTP 允许传输**任意类型**的数据。传输的类型由 Content-Type 加以标记。
2. **无状态**。对于客户端每次发送的请求，服务器都认为是一个新的请求，上一次会话和下一次会话之间没有联系。
3. 支持**客户端/服务器模式**。

#### 3.10 HTTP 报文格式

HTTP 请求由**请求行、请求头部、空行和请求体**四个部分组成。

- **请求行**：包括请求方法，访问的资源 URL，使用的 HTTP 版本。`GET`和`POST`是最常见的 HTTP 方法，除此以外还包括`DELETE、HEAD、OPTIONS、PUT、TRACE`。
- **请求头**：格式为“属性名:属性值”，服务端根据请求头获取客户端的信息，主要有`cookie、host、connection、accept-language、accept-encoding、user-agent`。
- **请求体**：用户的请求数据如用户名、密码等。

**请求报文示例**：

```
POST /xxx HTTP/1.1 请求行
Accept:image/gif.image/jpeg, 请求头部
Accept-Language:zh-cn
Connection:Keep-Alive
Host:localhost
User-Agent:Mozila/4.0(compatible;MSIE5.01;Window NT5.0)
Accept-Encoding:gzip,deflate

username=dabin 请求体
```

HTTP 响应也由四个部分组成，分别是：**状态行、响应头、空行和响应体**。

- **状态行**：协议版本，状态码及状态描述。
- **响应头**：响应头字段主要有`connection、content-type、content-encoding、content-length、set-cookie、Last-Modified、Cache-Control、Expires`。
- **响应体**：服务器返回给客户端的内容。

**响应报文示例**：

```
HTTP/1.1 200 OK
Server:Apache Tomcat/5.0.12
Date:Mon,6Oct2003 13:23:42 GMT
Content-Length:112

<html>
    <body>响应体</body>
</html>
```

#### HTTP 状态码有哪些

![](D:\Java\笔记\图片\day11【网络编程】\HTTP 状态码.png)

#### HTTP1.0 和 HTTP1.1 的区别

- **长连接**：HTTP1.0 默认使用短连接，每次请求都需要建立新的 TCP 连接，连接不能复用。**HTTP1.1 支持长连接，复用 TCP 连接，允许客户端通过同一连接发送多个请求**。不过，这个优化策略也存在问题，当一个队头的请求不能收到响应的资源时，它将会阻塞后面的请求。这就是“**队头阻塞**”问题。
- **断点续传**：HTTP1.0 **不支持断点续传**。HTTP1.1 新增了 **range** 字段，用来指定数据字节位置，**支持断点续传**。
- **错误状态响应码**：在 HTTP1.1 中新增了 24 个错误状态响应码，如`409（Conflict）`表示请求的资源与资源的当前状态发生冲突、`410（Gone）`表示服务器上的某个资源被永久性的地删除。
- **Host 头处理**：在 HTTP1.0 中认为每台服务器都绑定一个唯一的 IP 地址，因此，请求消息中的 URL 并没有传递主机名。到了 HTTP1.1 时代，虚拟主机技术发展迅速，在一台物理服务器上可以存在多个虚拟主机，并且它们共享一个 IP 地址，故 HTTP1.1 增加了 HOST 信息。

#### HTTP1.1 和 HTTP2.0 的区别

HTTP2.0 相比 HTTP1.1 支持的特性：

- **新的二进制格式**：HTTP1.1 基于文本格式传输数据；HTTP2.0 采用二进制格式传输数据，解析更高效。
- **多路复用**：在一个连接里，允许同时发送多个请求或响应，**并且这些请求或响应能够并行地传输而不被阻塞**，避免 HTTP1.1 出现的“队头堵塞”问题。
- **头部压缩**，HTTP1.1 的 header 带有大量信息，而且每次都要重复发送；HTTP2.0 把 header 从数据中分离，并封装成头帧和数据帧，**使用特定算法压缩头帧**，有效减少头信息大小。并且 HTTP2.0 **在客户端和服务器端记录了之前发送的键值对，对于相同的数据，不会重复发送。比如请求 a 发送了所有的头信息字段，请求 b 则只需要发送差异数据**，这样可以减少冗余数据，降低开销。
- **服务端推送**：HTTP2.0 允许服务器向客户端推送资源，无需客户端发送请求到服务器获取。

### HTTPS协议

#### HTTPS 原理

首先是 TCP 三次握手，然后客户端发起一个 HTTPS 连接建立请求，客户端先发一个`Client Hello`的包，然后服务端响应`Server Hello`，接着再给客户端发送它的证书，然后双方经过密钥交换，最后使用交换的密钥加解密数据。

1. **协商加密算法** 。在`Client Hello`里面客户端会告知服务端自己当前的一些信息，包括客户端要使用的 TLS 版本，支持的加密算法，要访问的域名，给服务端生成的一个随机数（Nonce）等。需要提前告知服务器想要访问的域名以便服务器发送相应的域名的证书过来。

   ![图片](https://mmbiz.qpic.cn/mmbiz_png/1Wxib6Z0MOJbCRJHmV85OCNfEPABbFy4j1GfdITJ5LL7uJqrYY46ickjfqx9ykeLibicWRibqUbSyibiaSkVkQCRYSMrQ/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

2. 服务端响应`Server Hello`，告诉客户端服务端**选中的加密算法**。

   ![图片](https://mmbiz.qpic.cn/mmbiz_png/1Wxib6Z0MOJbCRJHmV85OCNfEPABbFy4jSE3XL0Og5a3ys2NK7WtlQw2vuT8NyiaHT8akcudx977yVn1kicaRhPYA/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

3. 接着服务端给客户端发来了 2 个证书。第二个证书是第一个证书的签发机构（CA）的证书。

   ![图片](https://mmbiz.qpic.cn/mmbiz_png/1Wxib6Z0MOJbCRJHmV85OCNfEPABbFy4jjSzYUryavfw6VIohVRFVIB69ibv7biamibQtHgISwHic1Kgwdt2bpMOPQQ/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

4. 客户端使用证书的认证机构 CA 公开发布的 RSA 公钥**对该证书进行验证**，下图表明证书认证成功。

   ![图片](https://mmbiz.qpic.cn/mmbiz_png/1Wxib6Z0MOJbCRJHmV85OCNfEPABbFy4j8dKg8I5EjUl8XPToibw8VWWicVhicRGykdA5jkSib2Zs1QGia5lPpUMiaTrA/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

5. 验证通过之后，浏览器和服务器通过**密钥交换算法**产生共享的**对称密钥**。

   ![图片](https://mmbiz.qpic.cn/mmbiz_png/1Wxib6Z0MOJbCRJHmV85OCNfEPABbFy4jgPhK2V3pIgVicPibvQrnL5Xw7yZ56ZRicv5779o8J0FyBaH45ic6PMFsog/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

   ![图片](data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==)

6. 开始传输数据，使用同一个对称密钥来加解密。

   ![](D:\Java\笔记\图片\day11【网络编程】\HTTPS 原理 6.png)

#### HTTPS 与 HTTP 的区别

1. HTTP 是超文本传输协议，信息是**明文传输**；HTTPS 则是具有**安全性**的 ssl 加密传输协议。
2. HTTP 和 HTTPS 用的端口不一样，HTTP 端口是 80，HTTPS 是 443。
3. HTTPS 协议**需要到 CA 机构申请证书**，一般需要一定的费用。
4. HTTP 运行在 TCP 协议之上；HTTPS 运行在 SSL 协议之上，SSL 运行在 TCP 协议之上。

# 第六章 KeepAlive

首先，我们要明确我们谈的是**TCP**的 **`KeepAlive`** 还是**HTTP**的 **`Keep-Alive`**。TCP的KeepAlive和HTTP的Keep-Alive**是完全不同的概念，不能混为一谈**。

实际上HTTP的KeepAlive写法是`Keep-Alive`，跟TCP的`KeepAlive`写法上也有不同。

- TCP的**keepalive**是侧重在保持客户端和服务端的连接，一方会不定期发送心跳包给另一方，当一方端掉的时候，没有断掉的定时发送几次**心跳包**，如果间隔发送几次，对方都返回的是RST，而不是ACK，那么就释放当前链接。

  设想一下，如果tcp层没有keepalive的机制，一旦一方断开连接却没有发送FIN给另外一方的话，那么另外一方会一直以为这个连接还是存活的，几天，几月。那么这对服务器资源的影响是很大的。

- HTTP的**keep-alive**一般我们都会带上中间的**横杠**，普通的http连接是客户端连接上服务端，然后结束请求后，由客户端或者服务端进行http连接的关闭。下次再发送请求的时候，客户端再发起一个连接，传送数据，关闭连接。这么个流程反复。

  但是一旦客户端发送connection:keep-alive头给服务端，且服务端也接受这个keep-alive的话，两边对上暗号，这个连接就可以复用了，一个http处理完之后，另外一个http数据直接从这个连接走了。减少新建和断开TCP连接的消耗。

二者的作用简单来说：

> HTTP协议的Keep-Alive意图在于短时间内连接复用，希望可以短时间内在同一个连接上进行多次请求/响应。
>
> TCP的KeepAlive机制意图在于保活、心跳，检测连接错误。当一个TCP连接两端长时间没有数据传输时(通常默认配置是2小时)，发送keepalive探针，探测链接是否存活。

**总之，记住HTTP的Keep-Alive和TCP的KeepAlive不是一回事。**

<font color = "red">**tcp的keepalive是在ESTABLISH状态的时候，双方如何检测连接的可用行。**</font>

<font color = "red">**而http的keep-alive说的是如何避免进行重复的TCP三次握手和四次挥手的环节。**</font>

## TCP的KeepAlive

### 为什么要有KeepAlive？

在谈KeepAlive之前，我们先来了解下简单TCP知识(知识很简单，高手直接忽略)。首先要明确的是**在TCP层是没有“请求”一说的**，经常听到在TCP层发送一个请求，这种说法是错误的。

TCP是一种通信的方式，**“请求”一词是事务上的概念**，HTTP协议是一种事务协议，如果说发送一个HTTP请求，这种说法就没有问题。也经常听到面试官反馈有些面试运维的同学，基本的TCP三次握手的概念不清楚，面试官问TCP是如何建立链接，面试者上来就说，假如我是客户端我发送一个请求给服务端，服务端发送一个请求给我。。。

这种一听就知道对TCP基本概念不清楚。下面是我通过wireshark抓取的一个TCP建立握手的过程。（命令行基本上用TCPdump,后面我们还会用这张图说明问题）:

![](D:\Java\笔记\图片\day11【网络编程】\13.jpg)

TCP抓包

现在我看只要看前3行，这就是TCP三次握手的完整建立过程，第一个报文SYN从发起方发出，第二个报文SYN,ACK是从被连接方发出，第三个报文ACK确认对方的SYN，ACK已经收到，如下图：

![](D:\Java\笔记\图片\day11【网络编程】\14.webp)

TCP建立连接过程

**但是数据实际上并没有传输**，请求是有数据的，**第四个报文才是数据传输开始的过程**，细心的读者应该能够发现wireshark把第四个报文解析成HTTP协议，HTTP协议的GET方法和URI也解析出来，所以说TCP层是没有请求的概念，HTTP协议是事务性协议才有请求的概念，TCP报文承载HTTP协议的请求(Request)和响应(Response)。

现在才是开始说明为什么要有KeepAlive。链接建立之后，如果应用程序或者上层协议一直不发送数据，或者隔很长时间才发送一次数据，当链接很久没有数据报文传输时如何去确定对方还在线，到底是掉线了还是确实没有数据传输，链接还需不需要保持，这种情况在TCP协议设计中是需要考虑到的。

TCP协议通过一种巧妙的方式去解决这个问题，当超过一段时间之后，TCP自动发送一个数据为空的报文给对方，如果对方回应了这个报文，说明对方还在线，链接可以继续保持，如果对方没有报文返回，并且重试了多次之后则认为链接丢失，没有必要保持链接。

### 怎么开启KeepAlive？

KeepAlive并不是默认开启的，在Linux系统上没有一个全局的选项去开启TCP的KeepAlive。需要开启KeepAlive的应用必须在TCP的socket中单独开启。Linux Kernel有三个选项影响到KeepAlive的行为：

> - tcp_keepalive_time 7200// 距离上次传送数据多少时间未收到新报文判断为开始检测，单位秒，默认7200s
> - tcp_keepalive_intvl 75// 检测开始每多少时间发送心跳包，单位秒，默认75s
> - tcp_keepalive_probes 9// 发送几次心跳包对方未响应则close连接，默认9次

TCP socket也有三个选项和内核对应，通过setsockopt系统调用针对单独的socket进行设置：

> - TCPKEEPCNT: 覆盖 tcpkeepaliveprobes
> - TCPKEEPIDLE: 覆盖 tcpkeepalivetime
> - TCPKEEPINTVL: 覆盖 tcpkeepalive_intvl

举个例子，以我的系统默认设置为例，kernel默认设置的tcpkeepalivetime是7200s, 如果我在应用程序中针对socket开启了KeepAlive,然后设置的TCP_KEEPIDLE为60，那么TCP协议栈在发现TCP链接空闲了60s没有数据传输的时候就会发送第一个探测报文。

### KeepAlive的不足和局限性

其实，tcp自带的keepalive还是有些不足之处的。

**keepalive只能检测连接是否存活，不能检测连接是否可用。**例如，某一方发生了死锁，无法在连接上进行任何读写操作，但是操作系统仍然可以响应网络层keepalive包。

TCP keepalive 机制依赖于操作系统的实现,灵活性不够，默认关闭，且默认的 keepalive 心跳时间是 两个小时, 时间较长。

代理(如socks proxy)、或者负载均衡器，会让tcp keep-alive失效

基于此，我们旺旺需要加上应用层的心跳。这个需要自己实现，这里就不展开了。

------

## HTTP的Keep-Alive

### HTTP为什么需要Keep-Alive？

通常一个网页可能会有很多组成部分，除了文本内容，还会有诸如：js、css、图片等静态资源，有时还会异步发起AJAX请求。只有所有的资源都加载完毕后，我们看到网页完整的内容。然而，一个网页中，可能引入了几十个js、css文件，上百张图片，如果每请求一个资源，就创建一个连接，然后关闭，代价实在太大了。

基于此背景，我们希望连接能够在**短时间内**得到复用，在加载同一个网页中的内容时，尽量的复用连接，这就是HTTP协议中keep-alive属性的作用。

> - HTTP的Keep-Alive是**HTTP1.1**中**默认开启**的功能。通过headers设置"Connection: close "关闭
> - 在HTTP1.0中是**默认关闭**的。通过headers设置"Connection: Keep-Alive"开启。

对于客户端来说，不论是浏览器，还是手机App，或者我们直接在Java代码中使用HttpUrlConnection，只是负责在请求头中设置Keep-Alive。Keep-Alive属性保持连接的**时间长短是由服务端决定的**，通常配置都是在**几十秒左右。**

TCP连接建立之后，HTTP协议使用TCP传输HTTP协议的请求(Request)和响应(Response)数据，一次完整的HTTP事务如下图：



![](D:\Java\笔记\图片\day11【网络编程】\15.webp)

HTTP请求

这张图我简化了HTTP(Req)和HTTP(Resp)，实际上的请求和响应需要多个TCP报文。
 从图中可以发现一个完整的HTTP事务，有链接的建立，请求的发送，响应接收，断开链接这四个过程，早期通过HTTP协议传输的数据以文本为主，一个请求可能就把所有要返回的数据取到，但是，现在要展现一张完整的页面需要很多个请求才能完成，如图片.JS.CSS等，如果每一个HTTP请求都需要新建并断开一个TCP，这个开销是完全没有必要的。

开启HTTP Keep-Alive之后，能复用已有的TCP链接，当前一个请求已经响应完毕，服务器端没有立即关闭TCP链接，而是等待一段时间接收浏览器端可能发送过来的第二个请求，通常浏览器在第一个请求返回之后会立即发送第二个请求，如果某一时刻只能有一个链接，同一个TCP链接处理的请求越多，开启KeepAlive能节省的TCP建立和关闭的消耗就越多。

当然通常会启用多个链接去从服务器器上请求资源，但是开启了Keep-Alive之后，仍然能加快资源的加载速度。HTTP/1.1之后默认开启Keep-Alive, 在HTTP的头域中增加Connection选项。当设置为`Connection:keep-alive`表示开启，设置为`Connection:close`表示关闭。

# 第七章 粘包

## 一、什么是粘包？

注意：只有TCP有粘包现象，UDP永远不会粘包，为何，且听我娓娓道来。

首先需要掌握一个socket收发消息的原理

发送端可以是一K一K地发送数据，而接收端的应用程序可以两K两K地提走数据，当然也有可能一次提走3K或6K数据，或者一次只提走几个字节的数据，也就是说，应用程序所看到的数据是一个整体，或说是一个流（stream），一条消息有多少字节对应用程序是不可见的，因此TCP协议是面向流的协议，这也是容易出现粘包问题的原因。而UDP是面向消息的协议，每个UDP段都是一条消息，应用程序必须以消息为单位提取数据，不能一次提取任意字节的数据，这一点和TCP是很不同的。怎样定义消息呢？可以认为对方一次性write/send的数据为一个消息，需要明白的是当对方send一条信息的时候，无论底层怎样分段分片，TCP协议层会把构成整条消息的数据段排序完成后才呈现在内核缓冲区。

例如基于TCP的套接字客户端往服务端上传文件，发送时文件内容是按照一段一段的字节流发送的，在接收方看了，根本不知道该文件的字节流从何处开始，在何处结束。

所谓粘包问题主要还是因为接收方不知道消息之间的界限，不知道一次性提取多少字节的数据所造成的。

此外，发送方引起的粘包是由TCP协议本身造成的，TCP为提高传输效率，发送方往往要收集到足够多的数据后才发送一个TCP段。若连续几次需要send的数据都很少，通常TCP会根据优化算法把这些数据合成一个TCP段后一次发送出去，这样接收方就收到了粘包数据。

- TCP（transport control protocol，传输控制协议）是面向连接的，面向流的，提供高可靠性服务。收发两端（客户端和服务器端）都要有一一成对的socket，因此，发送端为了将多个发往接收端的包，更有效的发到对方，使用了优化方法（Nagle算法），将多次间隔较小且数据量小的数据，合并成一个大的数据块，然后进行封包。这样，接收端，就难于分辨出来了，必须提供科学的拆包机制。 即面向流的通信是无消息保护边界的。
- UDP（user datagram protocol，用户数据报协议）是无连接的，面向消息的，提供高效率服务。不会使用块的合并优化算法，, 由于UDP支持的是一对多的模式，所以接收端的skbuff(套接字缓冲区）采用了链式结构来记录每一个到达的UDP包，在每个UDP包中就有了消息头（消息来源地址，端口等信息），这样，对于接收端来说，就容易进行区分处理了。 即面向消息的通信是有消息保护边界的。
- TCP是基于数据流的，于是收发的消息不能为空，这就需要在客户端和服务端都添加空消息的处理机制，防止程序卡住，而udp是基于数据报的，即便是你输入的是空内容（直接回车），那也不是空消息，udp协议会帮你封装上消息头，实验略

udp的recvfrom是阻塞的，一个recvfrom(x)必须对唯一一个sendinto(y),收完了x个字节的数据就算完成,若是y>x数据就丢失，这意味着udp根本不会粘包，但是会丢数据，不可靠

TCP的协议数据不会丢，没有收完包，下次接收，会继续上次继续接收，己端总是在收到ack时才会清除缓冲区内容。数据是可靠的，但是会粘包。

## 二、TCP发送数据的四种情况

假设客户端分别发送了两个数据包D1和D2给服务端，由于服务端一次读取到的字节数是不确定的，故可能存在以下4种情况。

1. 服务端分两次读取到了两个独立的数据包，分别是D1和D2，没有粘包和拆包；
2. 服务端一次接收到了两个数据包，D1和D2粘合在一起，被称为TCP粘包；
3. 服务端分两次读取到了两个数据包，第一次读取到了完整的D1包和D2包的部分内容，第二次读取到了D2包的剩余内容，这被称为TCP拆包；
4. 服务端分两次读取到了两个数据包，第一次读取到了D1包的部分内容D1_1，第二次读取到了D1包的剩余内容D1_2和D2包的整包。

特例：如果此时服务端TCP接收滑窗非常小，而数据包D1和D2比较大，很有可能会发生第五种可能，即服务端分多次才能将D1和D2包接收完全，期间发生多次拆包。



# 第八章 基础知识

## 什么是数字证书

服务端可以向证书颁发机构 CA 申请证书，以避免中间人攻击（防止证书被篡改）。证书包含三部分内容：**证书内容、证书签名算法和签名**，签名是为了验证身份。

![](D:\Java\笔记\图片\day11【网络编程】\数字证书.png)

服务端把证书传输给浏览器，浏览器从证书里取公钥。证书可以证明该公钥对应本网站。

**数字签名的制作过程**：

1. CA 使用证书签名算法对证书内容进行 **hash 运算**。
2. 对 hash 后的值**用 CA 的私钥加密**，得到数字签名。

**浏览器验证过程**：

1. 获取证书，得到证书内容、证书签名算法和数字签名。
2. 用 CA 机构的公钥**对数字签名解密**（由于是浏览器信任的机构，所以浏览器会保存它的公钥）。
3. 用证书里的签名算法**对证书内容进行 hash 运算**。
4. 比较解密后的数字签名和对证书内容做 hash 运算后得到的哈希值，相等则表明证书可信。

## DNS 的解析过程

1. 浏览器搜索**自己的 DNS 缓存**。
2. 若没有，则搜索**操作系统中的 DNS 缓存和 hosts 文件**。
3. 若没有，则操作系统将域名发送至**本地域名服务器**，本地域名服务器查询自己的 DNS 缓存，查找成功则返回结果，否则依次向**根域名服务器、顶级域名服务器、权限域名服务器**发起查询请求，最终返回 IP 地址给本地域名服务器。
4. 本地域名服务器将得到的 IP 址返回给**操作系统**，同时自己也**将 IP 地址缓存起来**。
5. 操作系统将 IP 地址返回给浏览器，同时自己也将 IP 地址缓存起来。
6. 浏览器得到域名对应的 IP 地址。

## 浏览器中输入 URL 返回页面过程

1. **解析域名**，找到主机 IP。
2. 浏览器利用 IP 直接与网站主机通信，**三次握手**，建立 TCP 连接。浏览器会以一个随机端口向服务端的 web 程序 80 端口发起 TCP 的连接。
3. 建立 TCP 连接后，浏览器向主机发起一个 HTTP 请求。
4. 服务器**响应请求**，返回响应数据。
5. 浏览器**解析响应内容，进行渲染**，呈现给用户。

![图片](https://mmbiz.qpic.cn/mmbiz_png/1Wxib6Z0MOJbCRJHmV85OCNfEPABbFy4jhTYt7AIxbntkHfZS87odcabCv6aic56fraib2ageDswRFuK2OqPaee3g/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

## Cookie 和 Session 的区别

- **作用范围不同**，Cookie 保存在客户端，Session 保存在服务器端。
- **有效期不同**，Cookie 可设置为长时间保持，比如我们经常使用的默认登录功能，Session 一般失效时间较短，客户端关闭或者 Session 超时都会失效。
- **隐私策略不同**，Cookie 存储在客户端，容易被窃取；Session 存储在服务端，安全性相对 Cookie 要好一些。
- **存储大小不同**， 单个 Cookie 保存的数据不能超过 4K；对于 Session 来说存储没有上限，但出于对服务器的性能考虑，Session 内不要存放过多的数据，并且需要设置 Session 删除机制。

## 什么是对称加密和非对称加密

**对称加密**：通信双方使用**相同的密钥**进行加密。特点是加密速度快，但是缺点是密钥泄露会导致密文数据被破解。常见的对称加密有`AES`和`DES`算法。

**非对称加密**：它需要生成两个密钥，**公钥和私钥**。公钥是公开的，任何人都可以获得，而私钥是私人保管的。公钥负责加密，私钥负责解密；或者私钥负责加密，公钥负责解密。这种加密算法**安全性更高**，但是**计算量相比对称加密大很多**，加密和解密都很慢。常见的非对称算法有`RSA`和`DSA`。
