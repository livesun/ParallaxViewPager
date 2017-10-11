### ParallaxViewPager

视差视图框架 只需要一句代码即可实现

### 效果图：

![image](http://livesunhexo.oss-cn-shanghai.aliyuncs.com/hexo/viewTouch/ParallaxViewpager.gif)




### 使用：

1、依赖
```
allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}

	dependencies {
	        compile 'com.github.livesun:ParallaxViewPager:v1.0'
	}

```

2、传入布局id数组。
```java
mViewPager.setLayout(getSupportFragmentManager(),
new int[]{R.layout.activity_test,R.layout.activity_kotlin,R.layout.activity_user_info});
```

3、xml文件中，为需要移动的控件配置属性

```java
    <ImageView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:translationXIn="0.12"
        app:translationYIn="0.82"
        app:translationYOut="0.82"
        app:translationXOut="0.12" />
```
 上面有demo 可以看demo的实现
 
博客 
 地址：https://livesun.github.io/2017/10/11/ParallaxViewPager/
