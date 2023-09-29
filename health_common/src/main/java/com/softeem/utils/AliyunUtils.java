package com.softeem.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;

import java.io.*;

//其实是啊里云,不是七牛云但是为了调用方便
public class AliyunUtils {
    //上传是文件名，下载是文件名
    public static String objectName = null;
    //下载这里就是下载到本地文件位置；上传就是上传的源文件位置
    public static String filePath = null;
    // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。关于其他Region对应的Endpoint信息，请参见访问域名和数据中心。
    private static String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
    // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
    private static String accessKeyId = "LTAI5tM76miJ5uMhDdoRxwvZ";

    // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
    private static String accessKeySecret = "1Ox8Ql6xU33LEvZJJ23S9hmG88qhpx";

    // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
    // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
    // 填写Bucket名称，例如examplebucket。
    private static String bucketName = "hrj";

    /**
     * 专门负责上传文件到 OSS 服务器的工具方法
     *
     * @param fileName 上传的文件名——      test/djx.jpg ：有目录的写法，\\不行
     * @param filePath 上传的文件位置——    d:\\test\\1.jpg
     * @return 包含上传结果以及上传的文件在 OSS 上的访问路径
     */
    public static void upload2aliyun(String filePath, String fileName) {
        AliyunUtils.objectName = fileName;
        AliyunUtils.filePath = filePath;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            InputStream inputStream = new FileInputStream(filePath);
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, inputStream);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }


    /**
     * 专门负责上传文件到 OSS 服务器的工具方法
     *
     * @param fileName 上传的文件名——      test/djx.jpg ：有目录的写法，\\不行
     * @param bytes    上传的文件名——      Byte数组上传到目标存储空间
     * @return 包含上传结果以及上传的文件在 OSS 上的访问路径
     */
    public static void upload2aliyun(byte[] bytes, String fileName) {
        objectName = fileName;
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 专门负责上传文件到 OSS 服务器的工具方法
     *
     * @param objectName_ 下载的文件名——      test/djx.jpg ：有目录的写法，\\不行
     * @param filePath_   下载的文件位置——    d:\\test\\1.jpg
     * @return 包含上传结果以及上传的文件在 OSS 上的访问路径
     */
    public static void downloadFile(String objectName_, String filePath_) {
        objectName = objectName_;
        filePath = filePath_;
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            // 下载Object到本地文件，并保存到指定的本地路径中。如果指定的本地文件存在会覆盖，不存在则新建。
            File file = new File(filePath);

            file.mkdirs();//创建文件

            // 如果未指定本地路径，则下载后的文件默认保存到示例程序所属项目对应本地路径中。
            ossClient.getObject(new GetObjectRequest(bucketName, objectName), file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally { // 关闭OSSClient。
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

    }

    public static void deleteFileFromaliyun(String fileName) {
        objectName = fileName;
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        System.out.println(objectName);
        // 删除文件或目录。如果要删除目录，目录必须为空。
        ossClient.deleteObject(bucketName, objectName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}

