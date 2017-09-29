package gjg.com.fundemo.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * @author gaojigong
 * @version V1.0
 * @Description: 事件总线的调度中心，用于管理单个处理周期RxBus的事件注册、触发、取消
 * 这个并不是一个单例对象，也就是说它可以单独使用
 * @date 16/12/22
 */
public class RxManager {
    private RxBus mRxBus = RxBus.getInstance();

    /**
     * 管理 Subcription（Observable.subcribe()的返回）
     */
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    /**
     * 存储RxBus的注册(订阅)，清楚时只清楚当前，而不影响RxBus总线
     */
    private ConcurrentHashMap<Object, Map<String,Observable>> subjectMapper = new ConcurrentHashMap<>();
    /**
     * 注入事件监听
     */
    public <T> void on(String eventName, Action1<T> action1,String subName) {
        Observable<T> observable = mRxBus.register(eventName,subName);

        Map<String,Observable> subjectList = subjectMapper.get(eventName);
        if (null == subjectList) {
            subjectList = new HashMap<>();
            subjectMapper.put(eventName, subjectList);
        }
        subjectList.put(subName,observable);

        mCompositeSubscription.add(observable.observeOn(AndroidSchedulers.mainThread()).subscribe(action1, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        }));
    }

    /**
     * 单纯的 Observable和Observer处理的管理
     */
    public void add(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }

    /**
     * 触发事件某个观察者
     */
    public void post(Object tag, Object content,String subName) {
        mRxBus.post(tag, content,subName);
    }
    /**
     * 触发事件的所有观察者
     */
    public void post(Object tag, Object content) {
        mRxBus.post(tag, content);
    }

    /**
     * 清楚当前RxManager的所注册过的事件
     */
    public void clear() {
        mCompositeSubscription.unsubscribe();//取消所有订阅
        for (Map.Entry<Object, Map<String,Observable>> entry : subjectMapper.entrySet()) {
            mRxBus.unRegister(entry.getKey());//移除注册
        }
    }
}