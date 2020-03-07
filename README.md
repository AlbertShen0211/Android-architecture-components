# Android Jetpack 最佳开发姿势
在Android架构组件基础上，融入Kotlin 协程+retrofit，模拟网络，全面快速开发。
## Navigation
NavController在 NavHost 中管理应用导航的对象，沿导航图中的特定路径导航至特定目标，或直接导航至特定目标。

 首先，定义 layout/activity_main.xml
```
 <fragment
        android:id="@+id/nav_host_fragment"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:defaultNavHost="true"
        app:layout_constraintBottom_toTopOf="@id/nav_view"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:navGraph="@navigation/mobile_navigation" />
```
 其次，定义 navigation/mobile_navigation.xml
    Activity的添加，需要先在Project创建对应的Activity，即可在布局设计器处理。

   ```
 <navigation  
 <fragment
        android:id="@+id/navigation_home"
        android:name="com.android.myapplication.ui.home.HomeFragment"
        android:label="@string/title_home"
        tools:layout="@layout/fragment_home">

<action
            android:id="@+id/action_navigation_home_to_detail_activity"
            app:destination="@id/detail_activity" />
    </fragment>
      ...
    <activity
        android:id="@+id/detail_activity"
        android:name="com.android.myapplication.ui.detail.DetailActivity"
        android:label="DetailActivity">
        <argument
            android:name="detailId"
            app:argType="string" />
    </activity>
</navigation>

```
最后，页面跳转，参数传递
```
    val direction =  HomeFragmentDirections.actionNavigationHomeToDetailActivity(plantId)
    view.findNavController().navigate(direction)
```
 参数接收：
 ```
     private val args: DetailActivityArgs by navArgs()
```


## Databinding
在onCreateView()中直接使用控件，会报空指针异常，这个姿势 binding.tvNavigation 是可以的。

```
val binding = FragmentHomeBinding.inflate(inflater, container, false)
binding.tvNavigation.setOnClickListener {
            navigateToDetailPage("1", it)
        }

```
布局文件中，字符串拼接，如跟ViewModel一起使用:

```
   android:text='@{"Data From Network-> "+viewModel.response}'
```




## ViewModel
以生命周期的方式存储和管理界面相关的数据。
Kotlin协程 viewModelScope, 如果 ViewModel 已清除，则在此范围内启动的协程都会自动取消。

```
private val homeViewModel: HomeViewModel by viewModels {
        InjectorUtils.provideHomeViewModelFactory(requireContext())
    }

viewModelScope.launch {
           ...
        }
```


## LiveData
一种可观察的数据存储器类,具有生命周期感知能力，意指它遵循其他应用组件（如 Activity、Fragment 或 Service）的生命周期。

```
   var plantName = gardenPlantings.map {
        ...
    }
 ```
map 实现LiveData的转换


## Room
创建应用数据的缓存, SQLite 的基础上提供了一个抽象层，充分利用 SQLite 的强大功能,更强健的数据库访问机制。

使用 Room 引用复杂数据，Room 提供了在基本类型和包装类型之间进行转换的功能，但不允许实体之间进行对象引用。

要为自定义类型添加此类支持，您需要提供一个 TypeConverter，它可以在自定义类与 Room 可以保留的已知类型之间来回转换。

```
class Converters {//TypeConverters
    ...
}
```
将 @TypeConverters 注释添加到 AppDatabase 类中，以便 Room 可以使用您为该 AppDatabase 中的每个实体和 DAO 定义的转换器：

```
@Database(entities = table, version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
   ...
}

```

```
@Insert
suspend fun insertPlant(plant: Plant): Long
```
认为是协程suspend



## WorkManager

使用 WorkManager API 可以轻松地调度即使在应用退出或设备重启时仍应运行的可延迟异步任务。

```
val workManagerConfiguration = Configuration.Builder()
            .setWorkerFactory(RefreshDataWork.Factory())
            .build()

 WorkManager.initialize(appContext, workManagerConfiguration)
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

val work = PeriodicWorkRequestBuilder<RefreshDataWork>(2, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

WorkManager.getInstance(appContext)
            .enqueueUniquePeriodicWork(RefreshDataWork::class.java.name, KEEP, work)

```

PeriodicWorkRequest 用于重复或重复工作，最小间隔应为15分钟。

OneTimeWorkRequest 一次性申请，不重复工作。

WorkManager按顺序执行，单例模式，app启动时执行一次。








