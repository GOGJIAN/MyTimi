# MyTimi
记账demo  

<img src="https://github.com/GOGJIAN/MyTimi/blob/master/untitled.gif" width="200"/> 

添加依赖：
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
	
  ```
  dependencies {
	        compile 'com.github.GOGJIAN:MyTimi:v1.0'
	}
  ```
  
使用：
```
<com.jianjian.typecycleview.View.MainCycleView
        android:id="@+id/add_order_image_button"
        android:layout_width="90dp"
        android:layout_height="90dp"
        app:startPoint="0"
        app:cycleWidth="10"
        app:animationLength="2000"
        app:endPoint="360"
       />
```
       
通过四个xml属性设置起点，终点，圈的宽度，动画时长

内容使用该类型的ArrayList，在这个类型中有颜色和大小两个属性
```
import com.jianjian.typecycleview.View.CycleItem;
```
使用这个方法添加元素
```
CycleItem.setColor(int);
CycleItem.setNum(float);

mMainCycleView.setData(ArrayList<CycleItem>）
```
