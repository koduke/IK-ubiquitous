# encoding:utf-8
'''
Created on 2016/10/25

@author: bantarou-02
'''

import pyaudio
import wave
import time
 
FORMAT = pyaudio.paInt16
CHANNELS = 1
RATE = 12800
CHUNK = 2**11
RECORD_SECONDS = 4
WAVE_OUTPUT_FILENAME = "file2.wav"
 
audio = pyaudio.PyAudio()
frames = []

def callback(in_data, frame_count, time_info, status):
    frames.append(in_data)          #この中で別スレッドの処理
    return(None, pyaudio.paContinue)
 
stream = audio.open(format=FORMAT, channels=CHANNELS,
                rate=RATE, input=True,
                input_device_index=2,
                frames_per_buffer=CHUNK,
                stream_callback=callback)
 
print ("recording...")
stream.start_stream()
time.sleep(RECORD_SECONDS)
print ("finished recording")
 
stream.stop_stream()
stream.close()
audio.terminate()
 
waveFile = wave.open(WAVE_OUTPUT_FILENAME, 'wb')
waveFile.setnchannels(CHANNELS)
waveFile.setsampwidth(audio.get_sample_size(FORMAT))
waveFile.setframerate(RATE)
waveFile.writeframes(b''.join(frames))
waveFile.close()