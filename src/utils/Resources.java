package utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.ScheduledExecutorService;

import android.app.Activity;
import config.CloudEnv;

/**
 * User: youxueliu
 * Date: 13-7-11
 * Time: 下午6:22
 */
public class Resources
{
    private static final int BUF_SIZE = 1024; // 2K chars(4K bytes)

    /**
     * 根据url地址访问服务器返回字符串，若发生异常，则返回空字符串
     * @param urlStr
     * @return
     */
    public static String toString(String urlStr)
    {
        PreConditions.checkNotNull(urlStr);
        try {
            final URL url = new URL(urlStr);
            final HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.connect();
            final InputStream is = conn.getInputStream();
           return toString(is);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 将流转换成字符串，若发生异常，则返回null
     * @param is
     * @return
     */
    public static String toString(InputStream is)
    {
        PreConditions.checkNotNull(is);
        return toString(Channels.newChannel(is));
    }

    public static String toString(InputStream is,long toBeReadSize)
    {
        PreConditions.checkNotNull(is);
        return toString(Channels.newChannel(is),toBeReadSize);
    }

    public static String toString(InputStream is, long toBeReadSize,int bufSize)
    {
        PreConditions.checkNotNull(is);
        return toString(Channels.newChannel(is),toBeReadSize,bufSize);
    }

    /**
     * 将ReadByteChannel转换为字符串
     * @param inChannel ReadableByteChannel
     * @return 返回通道的内容字符串
     */
    public static String toString(ReadableByteChannel inChannel)
    {
        return toString(inChannel,-1,BUF_SIZE);
    }

    /**
     * 将ReadByteChannel转换为字符串
     * @param inChannel ReadableByteChannel
     * @return 返回通道的内容字符串
     */
    public static byte[] toBytes(ReadableByteChannel inChannel)
    {
        return toBytes(inChannel,-1,BUF_SIZE);
    }

    /**
     * 将ReadByteChannel转换为字符串
     * @param inChannel ReadableByteChannel
     * @return 返回通道的内容字符串
     */
    public static String toString(ReadableByteChannel inChannel,long toBeReadSize)
    {
        return toString(inChannel, toBeReadSize,1);
    }

    /**
     * 将ReadByteChannel转换为字符串
     * @param inChannel ReadableByteChannel
     * @param noCloseChannel 不关闭输入通道
     * @return 返回通道的内容字符串
     */
    public static String toString(ReadableByteChannel inChannel,long toBeReadSize,boolean noCloseChannel)
    {
        return toString(inChannel, toBeReadSize,(int)toBeReadSize,noCloseChannel);
    }

    /**
     * 将ReadByteChannel转换为字符串
     * @param inChannel ReadableByteChannel
     * @return 返回通道的内容字符串
     */
    public static String toString(ReadableByteChannel inChannel,long toBeReadSize,int bufSize)
    {
        return toString(inChannel,toBeReadSize,bufSize,false);
    }

    /**
     * 将ReadByteChannel转换为字符串
     * @param inChannel ReadableByteChannel
     * @return 返回通道的内容字符串
     */
    public static byte[] toBytes(ReadableByteChannel inChannel,long toBeReadSize,int bufSize)
    {
        return toBytes(inChannel,toBeReadSize,bufSize,false);
    }

    public static byte[] toBytes(ReadableByteChannel inChannel,long toBeReadSize,boolean noCloseChannel)
    {
        return toBytes(inChannel, toBeReadSize,(int)toBeReadSize,noCloseChannel);
    }

    /**
     * 将ReadByteChannel转换为字符串
     * @param inChannel ReadableByteChannel
     * @param noCloseChannel 调用方法后不关闭输入通道
     * @return 返回通道的内容字符串
     */
    public static String toString(ReadableByteChannel inChannel,long toBeReadSize,int bufSize,boolean noCloseChannel)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        copy(inChannel,Channels.newChannel(outputStream),bufSize,toBeReadSize,noCloseChannel);
        return new String(outputStream.toByteArray());
    }

    /**
     * 将ReadByteChannel转换为字符串
     * @param inChannel ReadableByteChannel
     * @param noCloseChannel 调用方法后不关闭输入通道
     * @return 返回通道的内容字符串
     */
    public static byte[] toBytes(ReadableByteChannel inChannel,long toBeReadSize,int bufSize,boolean noCloseChannel)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        copy(inChannel,Channels.newChannel(outputStream),bufSize,toBeReadSize,noCloseChannel);
        return outputStream.toByteArray();
    }

    public static void closeChannel(Channel c)
    {
        if(c != null)
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * 将文件转换成字符串，若发生异常，则返回null
     * @param file
     * @return
     */
    public static String toString(File file)
    {
        PreConditions.checkNotNull(file);
        try
        {
            final InputStream is = new FileInputStream(file);
            return toString(is);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将RandomAccessFile转换成字符串，若发生异常，则返回null
     * @param file RandomAccessFile
     * @return 返回文件内容的字符串
     */
    public static String toString(RandomAccessFile file)
    {
        PreConditions.checkNotNull(file);
        final FileChannel channel = file.getChannel();
        return toString(channel);
    }

    public static byte[] toBytes(RandomAccessFile file)
    {
        PreConditions.checkNotNull(file);
        final FileChannel channel = file.getChannel();
        return toBytes(channel);
    }

    /**
     * 将RandomAccessFile指定大小转换成字符串，若发生异常，则返回null
     * @param file File
     * @param toReadSize 将要读取的字节数
     * @return 返回文件内容的字符串
     * @throws IOException if file not found
     */
    public static String toString(File file, long toReadSize)throws IOException
    {
        PreConditions.checkNotNull(file);
        return toString(new FileInputStream(file),toReadSize,(int)toReadSize);
    }

    /**
     * 将RandomAccessFile指定大小转换成字符串，若发生异常，则返回null
     * @param file File
     * @param toBeReadSize 将要读取的字节数
     * @return 返回文件内容的字符串
     * @throws IOException if file not found
     */
    public static String toString(RandomAccessFile file, long toBeReadSize) throws IOException
    {
        PreConditions.checkNotNull(file);
        return toString(file.getChannel(),toBeReadSize,(int)toBeReadSize);
    }

    public static void copy(InputStream is, OutputStream out)
    {
        copy(is,out,BUF_SIZE);
    }

    public static void copy(InputStream is, OutputStream out,CopyStreamListener listener)
    {
        copy(is,out,BUF_SIZE,listener);
    }

    public static void copy(InputStream is, OutputStream out,int bufSize)
    {
        copy(is,out,bufSize,-1,false);
    }

    public static void copy(InputStream is, OutputStream out,int bufSize,CopyStreamListener listener)
    {
        copy(is,out,bufSize,-1,false,listener);
    }

    public static void copy(InputStream is,OutputStream out,long toBeReadSize)
    {
        copy(is,out,BUF_SIZE,toBeReadSize,false);
    }

    public static void copy(InputStream is,OutputStream out,long toBeReadSize,CopyStreamListener listener)
    {
        copy(is,out,BUF_SIZE,toBeReadSize,false,listener);
    }

    public static void copy(InputStream is,OutputStream out,long toBeReadSize,CopyStreamListener listener,Activity activity)
    {
        copy(is,out,BUF_SIZE,toBeReadSize,false,listener,activity);
    }

    public static void copy(InputStream is,OutputStream out,long toBeReadSize,boolean noCloseStream)
    {
        copy(is,out,BUF_SIZE,toBeReadSize,noCloseStream);
    }

    public static void copy(InputStream is,OutputStream out,long toBeReadSize,boolean noCloseStream,CopyStreamListener listener)
    {
        copy(is,out,BUF_SIZE,toBeReadSize,noCloseStream,listener);
    }

    public static void copy(InputStream is,OutputStream out,int bufSize,long toBeRead,boolean noCloseStream)
    {
        copy(is,out,bufSize,toBeRead,noCloseStream,null);
    }

    public static void copy(InputStream is,OutputStream out,long toBeRead,CopyStreamListener listener,Activity activity,String taskComFeature)
    {
        copy(is,out,BUF_SIZE,toBeRead,false,listener,activity,taskComFeature);
    }

    public static void copy(InputStream is,OutputStream out,int bufSize,long toBeRead,boolean noCloseStream,CopyStreamListener listener)
    {
        copy(is,out,bufSize,toBeRead,noCloseStream,listener,null,null);
    }

    public static void copy(InputStream is,OutputStream out,int bufSize,long toBeRead,boolean noCloseStream,CopyStreamListener listener,String taskComFeature)
    {
        copy(is,out,bufSize,toBeRead,noCloseStream,listener,null,taskComFeature);
    }

    public static void copy(InputStream is,OutputStream out,int bufSize,long toBeRead,boolean noCloseStream,CopyStreamListener listener,Activity activity)
    {
        copy(Channels.newChannel(is),Channels.newChannel(out),bufSize,toBeRead,noCloseStream,listener,activity,null);
    }

    public static void copy(InputStream is,OutputStream out,int bufSize,long toBeRead,boolean noCloseStream,CopyStreamListener listener,Activity activity,String taskComFeatures)
    {
        copy(Channels.newChannel(is),Channels.newChannel(out),bufSize,toBeRead,noCloseStream,listener,activity,taskComFeatures);
    }

    private static void copy(ReadableByteChannel inChannel,WritableByteChannel outChannel,int bufSize,long toBeReadSize,boolean noCloseChannel)
    {
        copy(inChannel, outChannel, bufSize, toBeReadSize, noCloseChannel,null,null,null);
    }

    private static void copy(ReadableByteChannel inChannel,WritableByteChannel outChannel,int bufSize,long toBeReadSize,boolean noCloseChannel,final CopyStreamListener listener,final Activity activity,String taskComFeature)
    {
        PreConditions.checkNotNull(inChannel);
        PreConditions.checkNotNull(outChannel);
        PreConditions.checkArgument(bufSize > 0, "bufSize must be positive");
        if(bufSize > 4 * BUF_SIZE) bufSize = 4 * BUF_SIZE;

        ScheduledExecutorService exec = null;
        try{
            final ByteBuffer buff = ByteBuffer.allocate(bufSize);
            int hasReaded = 0;
            int readTimes = (int)toBeReadSize / bufSize;
            if((int)toBeReadSize % bufSize != 0)
                readTimes += 1;

            while(inChannel.read(buff) != -1 && !(isCancelTask(taskComFeature)))
            {
                buff.flip();
                outChannel.write(buff);
                if (listener != null)
                    onByte(activity,listener,hasReaded,bufSize,taskComFeature);
                if(toBeReadSize != -1 && ++hasReaded == readTimes) break;
                buff.clear();
            }

            if (isCancelTask(taskComFeature))
                listener.onCancel();
        }
        catch(Exception e)
        {
            if (e instanceof ClosedByInterruptException)
            {
                listener.onCancel();
            }
            // System.out.println(e.getCause());
            LogUtils.error("read error" + e);
        }
        finally
        {
            if(!noCloseChannel){
                closeChannel(inChannel);
            }
            closeChannel(outChannel);

            if (exec != null)
                exec.shutdown();
        }
    }

    private static boolean isCancelTask(String taskComFeature)
    {
        if (Strings.isNullOrEmpty(taskComFeature)) return false;
        Boolean isCancel = CloudEnv.isCancel(taskComFeature);
        return isCancel == null ? false : isCancel;
    }

    private static void onByte(Activity activity,final CopyStreamListener listener,final int hasReaded,final int bufSize,String comFeature)
    {
        final long alreadyReadSize = hasReaded * bufSize; // 当前已经读了这么多字节
        /*if (listener != null)
            listener.onByte(alreadyReadSize);*/
        if (activity != null && !activity.isFinishing())
        {
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    listener.onByte(alreadyReadSize);
                }
            });
        }

        if(!Strings.isNullOrEmpty(comFeature))
        {
            synchronized (CloudEnv.class)
            {
                CloudEnv.setUploadSize(comFeature, alreadyReadSize);
            }
        }


    }

    /**
     * 读写流接口
     */
    public static interface CopyStreamListener
    {
        /**
         * 已经读了多少字节
         * @param bytes
         */
        void onByte(long bytes);

        void onCancel();
    }

}
