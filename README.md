# Inject
# 介绍
//使用方法如下  
public class MainActivity extends AppCompatActivity {
    @Inject()
    public Z z1;
    @Inject()
    private Z z2;
    //多态的注解使用(需要出入需要的类的名称，同时该类需要使用InjectSetClass)看例1
    @InjectSubclass("ChildA")
    public Person persondd;
    @Inject()
    public Person person;
    //单例
    @InjectSingle()
    public DL dl;
    @InjectSingle()
    public DL d2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectBind.bind(this);
        //支持嵌套注入，z类中依然有其他的注入类；如例2  Z中注入B
        System.out.println("先看看首层" + z1.a);
        System.out.println("在看看第二层");
        z2.ge();
        System.out.println("看看多态的注入"+persondd.getName());
        System.out.println("看看正常的"+person.getName());
        //查看单例 两次获取的类的位置相同是单例模式
        System.out.println("看看单例"+d2.toString());
        System.out.println("看看单例"+dl.toString());

    }


}


 I/System.out: 先看看首层鲍佳辉
 I/System.out: 在看看第二层
 I/System.out: BBB
 I/System.out: 看看多态的注入ChildA
 I/System.out: 看看正常的person
 I/System.out: 看看单例com.tiger.dagger2.DL@6e3b790
 I/System.out: 看看单例com.tiger.dagger2.DL@6e3b790
 
 例1
 @InjectSetClass()
public class ChildA extends Person {

    public String getName() {
        return "ChildA";
    }
}

例2
public class Z {

    @Inject
    private B b;
    public String a = "鲍佳辉";
    @Inject
    public static  B c ;

    public Z() {
        InjectBind.bind(this);
    }
    public void ge() {
        System.out.println(b.b);
    }

}
