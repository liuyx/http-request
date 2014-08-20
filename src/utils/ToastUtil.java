package utils;

import android.content.Context;
import android.widget.Toast;

/**
 * User: youxueliu
 * Date: 13-7-17
 * Time: 下午5:20
 */
public class ToastUtil {

    /**
     * 显示Toast.LENGTH_SHORT 类型的msg
     * @param context
     * @param msg
     */
     public static void showShortMsg(Context context,String msg)
     {
         PreConditions.checkNotNull(context);
         PreConditions.checkNotNull(msg);
         Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
     }

     public static void showShortMsg(Context context,int strResId)
     {
         PreConditions.checkNotNull(context);
         final String msg = context.getString(strResId);
         if(msg != null)
         {
             Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
         }
     }

    /**
     * 显示Toast.LENGTH_LONG 类型的msg
     * @param context
     * @param msg
     */
     public static void showLongMsg(Context context,String msg)
     {
         PreConditions.checkNotNull(context);
         PreConditions.checkNotNull(msg);
         Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
     }
}
