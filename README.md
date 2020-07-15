#### 运行

jar在运行jar---需要jdk1.8文件，运行run.bat文件即可。

![img](https://github.com/Oislop/javaFxCanvas/blob/master/%E5%8F%AF%E4%BB%A5%E8%BF%90%E8%A1%8Cjar---%E9%9C%80%E8%A6%81jdk1.8/run.png)

#### 设计思想

图形编辑器支持通过菜单选择“新建”建立一个新文件，此时可以在空白图形文件上通过选择工具和颜色来绘制二维图形以及对二维图形的一些操作。可以通过菜单选择“打开”或者“保存”实现对图形文件的打开或保存，存储和读取之后图形的信息保持一致。支持创建基本类型，包括直线、矩形、椭圆、圆、多边形、以及自由图形。支持对基本元素的编辑，可对元素进行多选，可对选中的元素画笔的属性进行修改，可对选中的元素进行旋转操作。可对元素进行复制，粘贴以及删除。实现对多个元素的对齐功能。实现图层显示，每个图层可以选择显示或不显示。

本软件采用javaFx编程，jdk为java1.8，以下是主要的开发思想。

​    **设计模式：**

​    ·使用单例模式来创建唯一的图形编辑器实例，相关类在view包。

​	·使用单例模式创建绘制区右键菜单，相关类在view.stage.pane.BoardPane.java。

​    ·使用静态工厂设计模式来创建四个功能区，相关类在view.stage.pane包。

​    ·使用建造者(builder)模式创建工具区，相关类在view.stage.pane.stagetoolbarbuilder包。

​    ·使用建造者(builder)模式创建AlertBuilder对话框类，相关类在view.stage.alert包。

​    ·使用建造者(builder)模式构建ShapeBuilder类，相关类在view.stage.alert包。

​    ·使用策略模式来解决不同图形之间创建，相关类在view.stage.pane.BoardPane.java。

​    ·使用备忘录模式处理图形撤销与重做功能，相关类在view.stage.pane.BoardPane.java。

​    **其他设计思想：**

​    ·使用 try-with-resources 语句替代 try-finally 语句。

​    ·在公共类中使用访问方法而不是公共属性。

​    ·列表优于数组。

​    ·使用枚举类型替代整型常量。

​	·始终使用 Override 注解。

​	·lambda 表达式优于匿名类。

​	·方法引用优于 lambda 表达式。

​	·接口优于抽象类

