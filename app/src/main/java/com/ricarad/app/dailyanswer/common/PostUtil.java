package com.ricarad.app.dailyanswer.common;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2019-1-12.
 */

public class PostUtil {
    private Context context;
    private CosXmlSimpleService cosXmlService;
    private String postId;
    private static String URL = "https://postimgs-1258506940.cos-website.ap-shanghai.myqcloud.com/";
    private int isFinished;

    public PostUtil(Context context, String postId) {
        this.context = context;
        this.postId = "gg";
        String appid = "1258506940";
        String region = "ap-shanghai";
        //创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion(appid, region)
                .setDebuggable(true)
                .builder();
        String secretId = "AKIDoQoHWWCsBE2by8orPkpfFdmiT0IKoh5G";
        String secretKey ="60gBRRDn8E7lmZ6OULyPaVBbmlUxIOEo";

        /**
         * 初始化 {@link QCloudCredentialProvider} 对象，来给 SDK 提供临时密钥。
         */
        QCloudCredentialProvider credentialProvider = new ShortTimeCredentialProvider(secretId,
                secretKey, 600);
        this.cosXmlService = new CosXmlSimpleService(context, serviceConfig, credentialProvider);
    }

    public String getHtml(String rawHtml){
        List<String> netPaths = convertImgPaths(rawHtml);
        if (netPaths == null)
            return null;
        Document doc = Jsoup.parse(rawHtml);
        if (doc.select("img").first() == null){
            return null;
        }
        Elements es = doc.getElementsByTag("img");
        int i = 0;
        for (Element e: es){
            e.attr("src", netPaths.get(i++));
        }
        return doc.toString();
    }

    //把图片的本地路径转化为公网路径
    public List<String> convertImgPaths(String rawHtml){
        int i = 0;
        List<String> localPaths = getPathsFromHtml(rawHtml);

        List<String> netPaths = new ArrayList<>();
        for(String s: localPaths){
            String r = uploadImg(s, postId + i);
            if (r == null)
                return null;
            else
                netPaths.add(r);
        }
        return netPaths;
    }
    public List<String> getPathsFromHtml(String rawHtml) {
        Document doc = Jsoup.parse(rawHtml);
        List<String> pathList = new ArrayList<>();
        if (doc.select("img").first() == null){
            return null;
        }
        Elements es = doc.getElementsByTag("img");
        for (Element e : es) {
            pathList.add(e.attr("src"));
        }
        return pathList;
    }

    public String uploadImg(String localPath, String ImgId){
        isFinished = 0;
        // 初始化 TransferConfig
        TransferConfig transferConfig = new TransferConfig.Builder().build();
        //初始化 TransferManager
        TransferManager transferManager = new TransferManager(cosXmlService, transferConfig);
        String bucket = "postimgs-1258506940";
        String cosPath = ImgId;
        String srcPath = localPath.substring(6, localPath.length());
        String uploadId = null; //若存在初始化分片上传的 UploadId，则赋值对应uploadId值用于续传，否则，赋值null。
        //上传文件
        COSXMLUploadTask cosxmlUploadTask = transferManager.upload(bucket, cosPath, srcPath, uploadId);
        //设置返回结果回调
        cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Log.d("upload img======>",  "Success: " + result.printResult());
                isFinished = 1;
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.d("upload img======>",  "Failed: " + (exception == null ? serviceException.getMessage() : exception.toString()));
                isFinished = -1;
            }
        });
        while (true){
            if (isFinished == 1)
                //返回类似 http://xxxx.png的路径
                return URL + cosPath;
            else if (isFinished == -1)
                return null;
        }
    }
}
