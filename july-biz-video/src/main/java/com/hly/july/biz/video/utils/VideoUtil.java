package com.hly.july.biz.video.utils;

import com.hly.july.common.core.constant.VideoConstants;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;


/**
 * @ClassName VideoUtil
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/7/18 08:42
 * @Version 1.0.0
 **/
@Slf4j
public class VideoUtil {

    public static File fetchFrame(String videoFullPath, String frameSaveSubFilePath, String frameFileName)
            throws Exception {
        log.info("fetchFrame  videoFullPath:{},frameSaveSubFilePath:{},frameFileName:{} ",videoFullPath,frameSaveSubFilePath,frameFileName);
        String endPath = Paths.get(frameSaveSubFilePath,frameFileName).toString();
        File targetFile = new File(endPath);
        File dirFile = new File(frameSaveSubFilePath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoFullPath);
        grabber.start();
        int length = grabber.getLengthInFrames();
        if(length==0){
            return null;
        }
        int targetIndex = VideoConstants.GET_FRAMES_LENGTH;
        if(length<targetIndex){
            targetIndex = length;
        }

        int i = 0;
        Frame frame = null;
        while (i < length) {
            // 过滤前5帧，避免出现全黑的图片，依自己情况而定(每循环一次取一帧)
            frame = grabber.grabFrame();
            if ((i >= targetIndex-1) && (frame.image != null)) {
                break;
            }
            i++;
        }
//        int owidth = frame.imageWidth;
//        int oheight = frame.imageHeight;
//        // 对截取的帧进行等比例缩放
//        int width = 300;
//        int height = (int) (((double) width / owidth) * oheight);
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage fecthedImage = converter.getBufferedImage(frame);
        BufferedImage bi = new BufferedImage(VideoConstants.POSTER_WIDTH, VideoConstants.POSTER_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(fecthedImage.getScaledInstance(VideoConstants.POSTER_WIDTH, VideoConstants.POSTER_HEIGHT, Image.SCALE_SMOOTH),
                0, 0, null);
        ImageIO.write(bi, VideoConstants.POSTER_FORMAT, targetFile);
        grabber.stop();
        return targetFile;
    }

    /**
     * <h5>功能:根据视频旋转度来调整图片</h5>
     *
     * @param src 捕获的图像
     * @param angel 视频旋转度
     * @return BufferedImage
     */
    private static BufferedImage rotate(BufferedImage src, int angel) {
        int src_width = src.getWidth(null);
        int src_height = src.getHeight(null);
        int type = src.getColorModel().getTransparency();
        Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);
        BufferedImage bi = new BufferedImage(rect_des.width, rect_des.height, type);
        Graphics2D g2 = bi.createGraphics();
        g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return bi;
    }

    /**
     * <h5>功能:计算图片旋转大小</h5>
     *
     * @param src 屏幕坐标中捕获的矩形区域
     * @param angel 视频旋转度
     * @return
     */
    private static Rectangle calcRotatedSize(Rectangle src, int angel) {
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }
        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);
        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new java.awt.Rectangle(new Dimension(des_width, des_height));
    }

}
