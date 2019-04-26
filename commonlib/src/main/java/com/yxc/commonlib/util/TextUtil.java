package com.yxc.commonlib.util;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;

/**
 * @author yxc
 * @date 2019/3/7
 */
public class TextUtil {

    //设置文本的粗体
    public static SpannableStringBuilder getSpannableStr(Context context, String parentStr, String subStr, int texSize){

        return getSpannableStr(context, parentStr, subStr, texSize, true);
    }


    public static SpannableStringBuilder getSpannableStr(Context context, String parentStr, String subStr, int texSize, boolean isBold){

        SpannableStringBuilder spannable = new SpannableStringBuilder(parentStr);

        //修改字体大小
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(DisplayUtil.sp2px(context, texSize));

        //修改字体Style
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);//粗体

        int beginIndex = parentStr.indexOf(subStr);
        int endIndex = beginIndex + subStr.length();

        spannable.setSpan(sizeSpan, beginIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (isBold){
            spannable.setSpan(styleSpan, beginIndex, endIndex, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return spannable;
    }


    public  SpannableStringBuilder setAbsoluteSizeSpan(Context context, String parentStr, String subStr, int texSize){

        SpannableStringBuilder spannable = new SpannableStringBuilder(parentStr);
        int beginIndex = parentStr.indexOf(subStr);
        int endIndex = beginIndex + subStr.length();
        //修改字体大小
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(DisplayUtil.sp2px(context, texSize));
        spannable.setSpan(sizeSpan, beginIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }


    public static float getTxtHeight1(Paint paint){
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float height1 = fontMetrics.descent - fontMetrics.ascent;
        return fontMetrics.bottom - fontMetrics.top;
    }

    public static float getTxtHeight2(Paint paint){
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return  fontMetrics.descent - fontMetrics.ascent;
    }


}
