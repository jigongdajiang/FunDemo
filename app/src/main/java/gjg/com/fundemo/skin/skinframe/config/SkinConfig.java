package gjg.com.fundemo.skin.skinframe.config;

/**
 * @author : gongdaocai
 * @date : 2017/9/26
 * FileName:
 * @description:
 */


public class SkinConfig {
    /**
     * Skin缓存的sp文件名
     */
    public static final String SP_SKIN = "sp_skin";
    /**
     * Skin缓存的当前皮肤路径，加载成功后会设置
     */
    public static final String SP_SKIN_CURRENT_PATH = "sp_skin_current_path";

    /**
     * 皮肤文件不存在
     */
    public static final int SKIN_FILE_NOT_EXIST = -1;
    /**
     * 不是一个合理的皮肤apk文件，通过检测包名获得
     */
    public static final int SKIN_FILE_ERROR = -2;
    /**
     * 皮肤文件签名校验失败
     */
    public static final int SKIN_FILE_SIGNATURE_VERIFY_FAILURE = -3;
    /**
     * 与当前皮肤文件相同，无需换肤
     */
    public static final int SKIN_FILE_SAME_CURRENT = -4;
    /**
     * 当前皮肤文件合格
     */
    public static final int SKIN_FILE_QUALIFIED = 0;

    /**
     * 换肤成功
     */
    public static final int SKIN_CHANGE_SUCCESS = 1;
    public static final int SKIN_CHANGE_UN_NEED_RESTORE = -1;

}
