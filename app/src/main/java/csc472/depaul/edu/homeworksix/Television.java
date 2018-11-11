package csc472.depaul.edu.homeworksix;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

public class Television extends android.support.v7.widget.AppCompatImageView implements IRemoteDiscObserver
{

    private int currentResource;
    public Television(Context context)
    {
        super(context);
    }

    public Television(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public Television(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public void setProgram(int nDrawableId)
    {
        this.currentResource = nDrawableId;
        setBackgroundResource(nDrawableId);
    }
    public int getCurrentResource(){
        return currentResource;
    }

    public void channelDown(){
        switch (currentResource){
            case R.drawable.cartoons: {
                setProgram(R.drawable.educational);
                break;
            }
            case R.drawable.educational:{
                setProgram(R.drawable.sports);
                break;
            }
            case R.drawable.sports:{
                setProgram(R.drawable.cartoons);
                break;
            }
            default: setProgram(R.drawable.cartoons);
        }

    }
    public void channelUp(){
        switch (currentResource){
            case R.drawable.cartoons: {
                setProgram(R.drawable.sports);
                break;
            }
            case R.drawable.educational:{
                setProgram(R.drawable.cartoons);
                break;
            }
            case R.drawable.sports:{
                setProgram(R.drawable.educational);
                break;
            }
            default: setProgram(R.drawable.cartoons);
        }
    }
    public void volumeDown(){

    }
    public void volumeUp(){

    }
}


