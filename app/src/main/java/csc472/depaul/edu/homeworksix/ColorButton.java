package csc472.depaul.edu.homeworksix;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ColorButton extends android.support.v7.widget.AppCompatButton implements View.OnTouchListener
{
    private int onUP;
    private int onDown;


    public ColorButton(Context context)
    {
        super(context);

        setOnTouchListener(this);
    }

    public ColorButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        setOnTouchListener(this);
    }

    public ColorButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        setOnTouchListener(this);
    }


    public void setBackground(int upDrawable, int downDrawable){
        this.onUP = upDrawable;
        this.onDown = downDrawable;
        setBackgroundResource(upDrawable);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        boolean bOnTouch = false;

        if ((v != null) && (event != null))
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                {
                    setBackgroundResource(onDown);

                    bOnTouch = true;
                }
                break;

                case MotionEvent.ACTION_UP:
                {
                    //TODO: you want to set your button back to the inactive state
                    setBackgroundResource(onUP);
                    bOnTouch = true;
                }
                break;
            }
        }

        return bOnTouch;
    }


}
