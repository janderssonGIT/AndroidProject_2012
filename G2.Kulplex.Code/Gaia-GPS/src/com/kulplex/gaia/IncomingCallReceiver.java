/**
 *	IncomingCallReceiver
 *
 *	This class is based on the SipDemo, provided by Google. It has been modified for our project.
 *
 * 
 */

package com.kulplex.gaia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.sip.*;

/**
 * Listens for incoming SIP calls, intercepts and hands them off to CallActivity.
 */
public class IncomingCallReceiver extends BroadcastReceiver {
    /**
     * Processes the incoming call, answers it, and hands it over to the
     *CallActivity.
     * @param context The context under which the receiver is running.
     * @param intent The intent being received.
     */
    @SuppressWarnings("static-access")
	@Override
    public void onReceive(Context context, Intent intent) {
        SipAudioCall incomingCall = null;
        try {

            SipAudioCall.Listener listener = new SipAudioCall.Listener() {
                @Override
                public void onRinging(SipAudioCall call, SipProfile caller) {
                    try {
                        call.answerCall(30);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            CallActivity cActivity = (CallActivity) context;

            incomingCall = cActivity.manager.takeAudioCall(intent, listener);
            incomingCall.answerCall(30);
            incomingCall.startAudio();
            incomingCall.setSpeakerMode(true);
            if(incomingCall.isMuted()) {
                incomingCall.toggleMute();
            }

            cActivity.call = incomingCall;

            cActivity.updateStatus(incomingCall);

        } catch (Exception e) {
            if (incomingCall != null) {
                incomingCall.close();
            }
        }
    }

}