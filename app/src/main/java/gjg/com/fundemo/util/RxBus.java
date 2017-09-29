package gjg.com.fundemo.util;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * @author gaojigong
 * @version V1.0
 * @Description: 事件总线操作类
 * @date 16/12/22
 */
public class RxBus {
    private static RxBus instance;

    private RxBus() {

    }

    public static synchronized RxBus getInstance() {
        if (null == instance) {
            instance = new RxBus();
        }
        return instance;
    }

    /**
     * 支持高并发，高吞吐量的线程安全的HashMap实现，存储多个事件的多个观察者
     */
    private ConcurrentHashMap<Object, Map<String,Subject>> subjectMapper = new ConcurrentHashMap<>();

    /**
     * 给一个事件注册一个观察者
     * @param tag 事件名称
     * @param subName 观察者名称
     * @param <T>
     * @return
     */
    public <T> Observable<T> register(@NonNull Object tag,String subName) {
        Map<String,Subject> subjectList = subjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new HashMap<>();
            subjectMapper.put(tag, subjectList);
        }
        Subject<T, T> subject;
        //不会出现重复注册观察者
        subjectList.put(subName,subject = PublishSubject.create());
        return subject;
    }
    /**
     * 取消某个事件的某个观察者
     * @param tag  事件名
     * @param subName 观察者名称
     */
    public void unRegister(@NonNull Object tag, String subName) {
        Map<String,Subject> subjects = subjectMapper.get(tag);
        if (null != subjects) {
            //noinspection SuspiciousMethodCalls
            subjects.remove(subName);
            //如果观察者为空则清空该事件
            if (CollectionUtil.isEmpty(subjects)) {
                subjectMapper.remove(tag);
            }
        }
    }

    /**
     * 取消某个事件的所有观察者
     * @param tag
     */
    public void unRegister(@NonNull Object tag) {
        Map<String,Subject> subjects = subjectMapper.get(tag);
        if (null != subjects) {
            //noinspection SuspiciousMethodCalls
            subjects.clear();
            subjectMapper.remove(tag);
        }
    }
    /**
     * 发出事件，让所有观察者都收到
     */
    public void post(@NonNull Object tag, @NonNull Object content) {
        Map<String,Subject> subjects = subjectMapper.get(tag);
        if (!CollectionUtil.isEmpty(subjects)) {
            for (Subject subject : subjects.values()) {
                subject.onNext(content);
            }
        }
    }

    /**
     * 只给指定的观察者收到
     * @param tag
     * @param content
     * @param subName
     */
    public void post(@NonNull Object tag, @NonNull Object content,String subName) {
        Map<String,Subject> subjects = subjectMapper.get(tag);
        Subject subject = subjects.get(subName);
        if(null != subject){
            subject.onNext(content);
        }
    }

    public ConcurrentHashMap<Object, Map<String, Subject>> getSubjectMapper() {
        return subjectMapper;
    }
}