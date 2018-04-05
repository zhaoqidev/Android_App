package cc.upedu.online.domin;

import java.util.List;

/**
 * 广告轮播图实体类
 * Created by Administrator on 2016/3/9 0009.
 */
public class AdRollViewBean {
    private String message;

    private boolean success;

    private Entity entity;

    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setSuccess(boolean success){
        this.success = success;
    }
    public boolean getSuccess(){
        return this.success;
    }
    public void setEntity(Entity entity){
        this.entity = entity;
    }
    public Entity getEntity(){
        return this.entity;
    }


public class ShopCenterBanner {
    private int id;

    private String imagesUrl;

    private int courseId;

    private String title;

    private String keyWord;

    private int seriesNumber;

    private String color;

    private String previewUrl;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setImagesUrl(String imagesUrl){
        this.imagesUrl = imagesUrl;
    }
    public String getImagesUrl(){
        return this.imagesUrl;
    }
    public void setCourseId(int courseId){
        this.courseId = courseId;
    }
    public int getCourseId(){
        return this.courseId;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setKeyWord(String keyWord){
        this.keyWord = keyWord;
    }
    public String getKeyWord(){
        return this.keyWord;
    }
    public void setSeriesNumber(int seriesNumber){
        this.seriesNumber = seriesNumber;
    }
    public int getSeriesNumber(){
        return this.seriesNumber;
    }
    public void setColor(String color){
        this.color = color;
    }
    public String getColor(){
        return this.color;
    }
    public void setPreviewUrl(String previewUrl){
        this.previewUrl = previewUrl;
    }
    public String getPreviewUrl(){
        return this.previewUrl;
    }

}

public class Entity {
    private List<ShopCenterBanner> shopCenterBanner ;

    public void setShopCenterBanner(List<ShopCenterBanner> shopCenterBanner){
        this.shopCenterBanner = shopCenterBanner;
    }
    public List<ShopCenterBanner> getShopCenterBanner(){
        return this.shopCenterBanner;
    }

}

}