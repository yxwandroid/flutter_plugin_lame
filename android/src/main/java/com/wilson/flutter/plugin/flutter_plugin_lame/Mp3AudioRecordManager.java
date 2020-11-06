package com.wilson.flutter.plugin.flutter_plugin_lame;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.naman14.androidlame.AndroidLame;
import com.naman14.androidlame.LameBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Mp3AudioRecordManager {

    private static final Mp3AudioRecordManager instance = new Mp3AudioRecordManager();

    private Mp3AudioRecordManager() {
    }

    public static Mp3AudioRecordManager getInstance() {
        return instance;
    }

    int minBuffer;
    int inSamplerate = 16000;

    String filePath = Environment.getExternalStorageDirectory() + "/testrecord.mp3";

    boolean isRecording = false;

    AudioRecord audioRecord;
    AndroidLame androidLame;
    FileOutputStream outputStream;


    public void startRecording() {
        Log.v("wilson", "filePath:  " + filePath);
        minBuffer = AudioRecord.getMinBufferSize(inSamplerate, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        Log.v("wilson", " Initialising audio recorder..");
        audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC, inSamplerate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, minBuffer * 2);

        //5 seconds data
        Log.v("wilson", "creating short buffer array");
        short[] buffer = new short[inSamplerate * 2 * 5];

        // 'mp3buf' should be at least 7200 bytes long
        // to hold all possible emitted data.
        Log.v("wilson", "creating mp3 buffer");
        byte[] mp3buffer = new byte[(int) (7200 + buffer.length * 2 * 1.25)];

        try {
            File tempFile = new File(filePath);
            if(tempFile.exists()){
                tempFile.delete();
            }
            outputStream = new FileOutputStream(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Log.v("wilson", "Initialising Andorid Lame");
        androidLame = new LameBuilder()
                .setInSampleRate(inSamplerate)
                .setOutSampleRate(inSamplerate)
                .setOutChannels(1)
                .setOutBitrate(16)
//                .setQuality(0)
                .setVbrQuality(9)
                .setVbrMode(LameBuilder.VbrMode.VBR_OFF)
                .build();

        Log.v("wilson", "started audio recording");
        audioRecord.startRecording();

        int bytesRead = 0;

        while (isRecording) {

            Log.v("wilson", "reading to short array buffer, buffer sze- " + minBuffer);
            bytesRead = audioRecord.read(buffer, 0, minBuffer);
            Log.v("wilson", "bytes read=" + bytesRead);

            if (bytesRead > 0) {

                Log.v("wilson", "encoding bytes to mp3 buffer..");
                int bytesEncoded = androidLame.encode(buffer, buffer, bytesRead, mp3buffer);
                Log.v("wilson", "bytes encoded=" + bytesEncoded);

                if (bytesEncoded > 0) {
                    try {
                        Log.v("wilson", "writing mp3 buffer to outputstream with " + bytesEncoded + " bytes");
                        outputStream.write(mp3buffer, 0, bytesEncoded);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        Log.v("wilson", "stopped recording");

        Log.v("wilson", "flushing final mp3buffer");
        int outputMp3buf = androidLame.flush(mp3buffer);
        Log.v("wilson", "flushed " + outputMp3buf + " bytes");

        if (outputMp3buf > 0) {
            try {
                Log.v("wilson", "writing final mp3buffer to outputstream");
                outputStream.write(mp3buffer, 0, outputMp3buf);
                Log.v("wilson", "closing output stream");
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.v("wilson", "releasing audio recorder");
        audioRecord.stop();
        audioRecord.release();

        Log.v("wilson", "closing android lame");
        androidLame.close();

        isRecording = false;
    }


}
