package com.sobremesa.waywt.events;

import com.sobremesa.waywt.models.RemoteThread;

/**
 * Created by omegatai on 14-11-19.
 */
public class ThreadItemClickedEvent {

    private int mPosition;
    private RemoteThread mThread;

    public ThreadItemClickedEvent(int position, RemoteThread thread) {
        this.mPosition = position;
        this.mThread = thread;
    }

    public int getPosition() {
        return mPosition;
    }

    public RemoteThread getThread() {
        return mThread;
    }
}
