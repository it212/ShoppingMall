# ShoppingMall

ShoppingMall 是一款电商商城的小项目，主要是对 Android 一些原生控件以及第三方框架的综合应用。

## 简介
它所用到的关键技术如下所示：

控件方面：
- TabHost（底部标签，用于 Fragment 的选择）
- Toolbar（标题栏，采用了自定义的标题栏）
- RecyclerView（用于列表视图的展示）
- CardView（用于列表视图子项）
- WebView（用于 HTML5 部分的访问）

第三方框架：
- [Picasso](https://github.com/square/picasso)（用于列表图片的展示）
- [OkHttp3](https://github.com/square/okhttp)（用于网络请求，从服务器获取相应数据）
- [AndroidImageSlider](https://github.com/daimajia/AndroidImageSlider)（轮播器）
- [MaterialRefeshLayout](https://github.com/android-cjj/Android-MaterialRefreshLayout)（SwipeRefreshLayout的加强版，支持下拉刷新）

## 权限申明

~~~xml
<!-- if you want to load images from the internet -->
<uses-permission android:name="android.permission.INTERNET" />

<!-- if you want to load images from a file OR from the internet -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
~~~

## 截图

<img src="https://img-blog.csdnimg.cn/20190318203535482.gif" width="216" height="384"/>  <img src="https://img-blog.csdnimg.cn/20190318203006661.gif" width="216" height="384"/>  <img src="https://img-blog.csdnimg.cn/20190318202919183.gif" width="216" height="384"/>  <img src="https://img-blog.csdnimg.cn/20190318203103608.gif" width="216" height="384"/>

## 注意事项

- 本应用为初代版本，可能会出现一些尚未发觉的 bug。
- 主页以及分类页面采用了Charles进行了服务器的模拟，它们的模拟数据位于serverdata 包下。
- 我的页面尚未开发，这部分功能将在 version2.0 进行开发。
- 代码结构部分对网络进行了封装，但是尚未对 Picasso 进行一个比较好的封装。
- 由于本应用使用了非常多的 RecyclerView 控件，所以有必要对 Adapter 进行一个比较好的封装，但是由于作者的疏忽，本应用内没有对其进行封装，在下个版本中会完成这部分工作。

## 关于作者

Marck
- [个人博客](https://blog.csdn.net/qq_38182125)

## License
~~~
Copyright 2019 Marck Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
~~~
