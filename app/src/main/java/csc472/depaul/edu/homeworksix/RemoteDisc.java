package csc472.depaul.edu.homeworksix;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class RemoteDisc extends android.support.v7.widget.AppCompatButton implements View.OnTouchListener
{
    //single observer - for multiple covert to arraylist
    //the observer can be set - so set last operation is current observer
    private IRemoteDiscObserver iRemoteDiscObserver = null;

    private enum REMOTE_DISC_BUTTON
    {
        NONE,
        UP,
        LEFT,
        RIGHT,
        DOWN
    }

    public RemoteDisc(Context context)
    {
        super(context);

        setOnTouchListener(this);
    }

    public RemoteDisc(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        setOnTouchListener(this);
    }

    public RemoteDisc(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        setOnTouchListener(this);
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
                    REMOTE_DISC_BUTTON rdb = getRemoteButton(event.getX(), event.getY());
                    Log.v("Touch_", rdb.name());

                    int imageId = getImageId(rdb);
                    setBackgroundResource(imageId);
                    notifyRemoteDiscObservers(rdb);
                    bOnTouch = true;
                }
                break;

                case MotionEvent.ACTION_UP:
                {
                    setBackgroundResource(R.drawable.remote_disc_inactive);
                    bOnTouch = true;
                }
                break;
            }
        }

        return bOnTouch;
    }

    //this method will return to you the proper button based on your x/y value
    private REMOTE_DISC_BUTTON getRemoteButton(float x, float y)
    {
        REMOTE_DISC_BUTTON remoteDiscButton = REMOTE_DISC_BUTTON.NONE;

        float centerX = getWidth()  / 2;
        float centerY = getHeight() / 2;

        float normalizedX = x - centerX;
        float normalizedY = y - centerY;

        if (Math.abs(normalizedX) >= Math.abs(normalizedY))//left/right
        {
            remoteDiscButton = (x >= centerX ? REMOTE_DISC_BUTTON.RIGHT : REMOTE_DISC_BUTTON.LEFT);
        }
        else //top/bottom
        {
            remoteDiscButton = (y >= centerY ? REMOTE_DISC_BUTTON.DOWN : REMOTE_DISC_BUTTON.UP);
        }

        return remoteDiscButton;
    }

    private int getImageId(REMOTE_DISC_BUTTON remoteDiscButton)
    {
        int nImageId = R.drawable.remote_disc_inactive;

        switch (remoteDiscButton)
        {
            case LEFT:
                nImageId = R.drawable.remote_disc_left_pressed;
            break;

            case UP:
                nImageId = R.drawable.remote_disc_up_pressed;
            break;

            case RIGHT:
                nImageId = R.drawable.remote_disc_right_pressed;
            break;

            case DOWN:
                nImageId = R.drawable.remote_disc_down_pressed;
            break;
        }

        return nImageId;
    }

    private void notifyRemoteDiscObservers(REMOTE_DISC_BUTTON remoteDiscButton)
    {
        switch (remoteDiscButton)
        {
            case LEFT:
            {
                iRemoteDiscObserver.channelDown();
            }
            break;

            case RIGHT:
            {
                iRemoteDiscObserver.channelUp();
            }
            break;

            case UP:
            {
                iRemoteDiscObserver.volumeUp();
            }
            break;

            case DOWN:
            {
                iRemoteDiscObserver.volumeDown();
            }
            break;
        }
    }

    public void setRemoteDiscObserver(final IRemoteDiscObserver iiRemoteDiscObserver)
    {
        iRemoteDiscObserver = iiRemoteDiscObserver;
    }
}
