# FrameGroup

### 目前完成

1. data 数据处理层

2. http 网络请求层

3. common 常用工具包，必要包

4. mvp  mvp设计模式框架包，如果需要mvp设计模式，需要集成，依托于common。

### 下一计划

1. 数据加密层

### 目标

分离式android架构

common 必要包-组织总框架结构包

data 数据架构包，非必要

http 网络请求包，非必要

mvp  mvp设计模式框架包，如果需要mvp设计模式，需要集成，依托于common。

<hr/>

rx框架系列-自用系列


rxbus使用

     Disposable subscribe = RxBus.get().subscribe(Integer.class).map(new Function<Object, Object>() {
         @Override
         public Object apply(Object o) {
             return o;
         }
     }).subscribe(new Consumer<Object>() {
         @Override
         public void accept(Object s) throws Exception {
             System.out.println("我在2号界面收到了 " + s);
         }
     });

    // 需要注册bus
     RxBusSubscriptions.bind(this, subscribe);

销毁的时候需要注销bus

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BusSubscriptions.unbind(this);
    }

发送数据

     RxBus.get().post(1234132413);
     RxBus.get().post("asdfsdfad");
     RxBus.get().post(new Event<String>("test1", "afsdg"));

##### 如果使用mvp架构包，则可以省略注册步骤以及销毁步骤

直接进行创建即可

     mPresenter.createBusInstance(Integer.class, new Consumer<Integer>() {
         @Override
         public void accept(Integer integer) throws Exception {
             System.out.println("我在2号界面收到了 " + integer);
         }
     });

<hr/>

rxfragmention

集成写好的实现类
suppoetactivityimp和supportfragmentimp

加载根部fragment

    loadRootFragment(R.id.fl, new Test1Fragment());

跳转某个fragment

    start(new Test2Fragment());

关闭当前

    pop();

关闭到某个fragment

    popTo();


    参数1：某个fragment的class
    参数2：是否包含
    public void popTo(Class<?> clazz, boolean includeTargetFragment)


##### 如果使用mvp架构包，则可以直接集成mvp下的

    BaseActivity
    BaseFragment
    BasePresenterImp

<hr/>

rxpermission

     mPermissionsHelper = new PermissionsHelper(this);
     mPermissionsHelper
             .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
             .subscribe(new Observer<Boolean>() {
                 @Override
                 public void onSubscribe(Disposable d) {

                 }

                 @Override
                 public void onNext(Boolean aBoolean) {
                     // 此方法返回true，为全部获取，否则没有获取到全部权限
                     System.out.println(aBoolean);
                 }

                 @Override
                 public void onError(Throwable e) {

                 }

                 @Override
                 public void onComplete() {
                     System.out.println("完成");
                 }
             });

需要在onRequestPermissionsResult中回传数据

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionsHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

此方法在activity中也是如此使用


##### 如果使用mvp架构包，直接传入数组即可


    requestPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
         new AbstractAction1<Boolean>() {
             @Override
             public void onNext(Boolean aBoolean) {

             }

             @Override
             public void onError(Exception e) {

             }
         });
    }

<hr/>

封装rxretrofit

通过java的泛型，完美解决请求的model问题

    public static void get(ResponseCallback<ResultModel<String>> callback) {
        Map<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("abc", "123");
        Observable<ResultModel<String>> observable = RetrofitManager.getInstance().getRetrofit().create(ApiService.class).get(stringStringHashMap);
        HttpServiceManager.getInstance().enqueue(observable, callback);
    }

封装之后

    HttpHelper.get(new ResponseCallback<ResultModel<String>>() {
        @Override
        public void onResponse(ResultModel<String> response) {
            System.out.println(response);
        }

        @Override
        public void onFailure(String msg) {
            System.out.println(msg);
        }
    });


支持重新设置okhttpclient


     RetrofitManager.getInstance().setBaseUrl("http://192.168.2.154:3001")
                    .setOkHttpClient(OkHttpManager.getInstance().build());

或者直接build也可以用默认的client

     RetrofitManager.getInstance().setBaseUrl("http://192.168.2.154:3001").build();


GlideManager

    /**
     * 展示图片--网络加载
     * 空白或者错误占位图片：picasso提供了两种占位图片，未加载完成或者加载发生错误的时需要一张图片作为提示。
     * 如果加载发生错误会重复三次请求，三次都失败才会显示erro Place holder
     */
    public static void showImage(Context context, ImageView imageView, String url)


    /**
     * 展示图片--网络加载
     * 空白或者错误占位图片：picasso提供了两种占位图片，未加载完成或者加载发生错误的时需要一张图片作为提示。
     * 如果加载发生错误会重复三次请求，三次都失败才会显示erro Place holder
     */
    public static void showImage(Context context, ImageView imageView, String url, int loadingDrawable)


    /**
     * 展示图片--资源文件id R.drawable.landing_screen
     */
    public static void showImage(Context context, ImageView imageView, int drawable)

    /**
     * 展示图片--文件
     */
    public static void showImage(Context context, ImageView imageView, File file)


    /**
     * 展示图片: 转换图片以适应布局大小并减少内存占用
     */
    public static void showGifImage(Context context, ImageView imageView, String url, int width, int height)

    /**
     * 展示图片: 自定义转换图片
     */
    public static void showImage(Context context, ImageView imageView, String url, RequestOptions options)


CameraHelper

    // 获取系统照相机
    public void startSystemCamera(Activity activity, File file)

    // 拍摄完成之后回传将照片放入相册
    public void onActivityResult(Activity activity, File file, int requestCode, Intent data)

    // 获取照片数据
    public List<Photo> getPhotosData(Activity activity)

FileHelper

    // 文件拷贝流
    public boolean copy(InputStream is, File file)



### 参考


[fragmention](https://github.com/YoKeyword/Fragmentation)


[rxpermission](https://github.com/tbruyelle/RxPermissions)
