package gjg.com.fundemo.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author gaojigong
 * @version V1.0
 * @Description:
 * @date 16/12/22
 */
public class CollectionUtil {
    /**
     * 判断集合是否为空
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection collection){
        return null == collection || collection.size() == 0;
    }
    public static boolean isEmpty(Map collection){
        return null == collection || collection.size() == 0;
    }
}
