package gjg.com.fundemo.fix;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;

/**
 * @author : gongdaocai
 * @date : 2017/9/19
 * FileName:
 * @description:
 */


public class FixManager {
    private static final String TAG = "FixManager";
    private Context mContext;
    private File mDexDir;//可访问的内部路径

    public FixManager(Context context) {
        this.mContext = context;
        this.mDexDir = context.getDir("odex",Context.MODE_PRIVATE);
    }

    public void loadFixDex() throws Exception{
        //得到赋值了的
        File[] dexFiles = mDexDir.listFiles();
        List<File> fixdexFiles = new ArrayList<>();
        if(fixdexFiles != null && dexFiles.length > 0){
            for (File dexFile : dexFiles) {
                if(dexFile != null && dexFile.getName().endsWith(".dex"));
                fixdexFiles.add(dexFile);
            }
        }
        fixAndmerge(fixdexFiles);
    }

    public void fieDex(String fixFilePath) throws Exception{
        File srcFile = new File(fixFilePath);
        if(!srcFile.exists()){
            throw new FileNotFoundException("补丁文件不存在 path="+fixFilePath);
        }
        File destFile = new File(mDexDir,srcFile.getName());
        if(destFile.exists()){
            //正式情况下，新补丁包会带版本号，所以不可能重复，也就没必要覆盖
            Log.e(TAG,"fixdex [" + fixFilePath +"] has loaded");
            return;
        }
        //拷贝文件
        copyFile(srcFile,destFile);

        //获取并合并Elements
        List<File> fixDexFiles = new ArrayList<>();
        fixDexFiles.add(destFile);

        fixAndmerge(fixDexFiles);
    }

    private void fixAndmerge(List<File> fixDexFiles) throws Exception {
        //系统原来的ClassLoader
        ClassLoader pathClassLoader = mContext.getClassLoader();
        //通过ClassLoader得到element
        Object applicationDexElement = getDexElementByClassLoader(pathClassLoader);
        //将下载的dex拷贝到系统可访问的路径
        for (File fixDexFile : fixDexFiles) {
            //dex路径
            String fixDexPath = fixDexFile.getAbsolutePath();
            //解压路径
            File optimizedDirectory = new File(mDexDir,"optimizedDirectory");
            if(!optimizedDirectory.exists()){
                optimizedDirectory.mkdirs();
            }
            //lib库路径
            String librarySearchPath = null;
            //父ClassLoader
            ClassLoader parentClassLoader = pathClassLoader;
            ClassLoader fixDexClassLoader = new BaseDexClassLoader(fixDexPath,optimizedDirectory,librarySearchPath,parentClassLoader);
            //通过ClassLoader得到element
            Object dexElement = getDexElementByClassLoader(fixDexClassLoader);
            //合并
            //将补丁添加到原来的Element数组中
            applicationDexElement =  combineArray(dexElement,applicationDexElement);
        }
        //将打好补丁的Element注入到原classLoader中
        injectDexElements(pathClassLoader,applicationDexElement);
    }

    /**
     * 通过反射获取ClassLoader中的pathList，也就是Element数组
     */
    private  Object getDexElementByClassLoader(ClassLoader classLoader) throws Exception{
        //得到BaseDexClassLoader中的pathList字段
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        //得到PathList对象
        Object pathListObj = pathListField.get(classLoader);

        //获取PathList中的Element数组
        //获取Element数组字段
        Field elementsField = pathListObj.getClass().getDeclaredField("dexElements");
        elementsField.setAccessible(true);
        return elementsField.get(pathListObj);
    }

    /**
     * 重组后的element注入到原来的ClassLoader中
     */
    private void injectDexElements(ClassLoader classLoader, Object applicationDexElement) throws Exception{
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathListObj = pathListField.get(classLoader);

        Field elementsField = pathListObj.getClass().getDeclaredField("dexElements");
        elementsField.setAccessible(true);
        //改变值
        elementsField.set(pathListObj,applicationDexElement);
    }

    /**
     * 合并两个Element数组
     * @return
     */
    private Object combineArray(Object arrayLhs, Object arrayRhs) {
        //得到对应数组的对象
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);//补丁包数组长度
        int j = i+Array.getLength(arrayRhs);//合并后的数组长度
        //合并后的数组对象
        Object result = Array.newInstance(localClass,j);
        for(int k=0;k<j;++k){
            if(k < i){
                Array.set(result,k,Array.get(arrayLhs,k));
            }else{
                Array.set(result,k,Array.get(arrayRhs,(k-i)));
            }
        }
        return result;
    }
    private void copyFile(File src,File dest) throws IOException{
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try{
            if(!dest.exists()){
                dest.createNewFile();
            }
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dest).getChannel();
            inChannel.transferTo(0,inChannel.size(),outChannel);
        }finally {
            if(inChannel != null){
                inChannel.close();
            }
            if(outChannel != null){
                outChannel.close();
            }
        }
    }
}
