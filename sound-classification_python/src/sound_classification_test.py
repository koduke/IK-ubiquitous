# coding:utf-8
'''
Created on 2016/10/16

@author: bantarou-02
'''

import sys
import numpy as np
from scikits.talkbox.features import mfcc
from scipy.io.wavfile import read
import math
argvs = sys.argv

def calc_mfcc(data, fs):
    mfcc_data, trush, trush = mfcc(data, nwin=256, nfft=512,fs=fs,nceps=13)
    meanceps = np.zeros(mfcc_data[0].size)
    for mc in mfcc_data:
        meanceps += mc
        
    return meanceps/mfcc_data[0].size

def build_model(datas):
    mue=np.zeros(len(datas[0]))
    sigma=np.zeros(len(datas[0]))
    for d in datas:
        mue = mue+d
        mue = mue/len(datas)
        
    for d in datas:
        sigma = sigma+(mue-d)*(mue-d)
        sigma = sigma/len(datas)
        
    return mue, sigma

def gausian(x,mue,sigma):
    delda = 0.001
    return 1/math.sqrt(2*math.pi*sigma+delda)*(math.exp(-1*pow(x-mue,2)/(2*sigma+delda)))

def Likelihood_average(ary):
    ave=0
    for n in ary:
        ave+=n
        
    return n/len(ary)

def ditect(model_mue,model_sigma,target):
    likelihood = []
    for i in range(len(target)):
        likelihood.append(gausian(target[i],model_mue[i],model_sigma[i]))
        
    return Likelihood_average(likelihood)

def judge(val):
    if val > -2:
        print "Open"
    else:
        print "Close"
        
#main 部分
if __name__ == '__main__':
    mfccs = []
    fnames = ['warning1','warning2']
    target = '../wav/fumikiri/warning6.wav'
        
    for fname in fnames:
        fs, data = read('../wav/fumikiri/'+fname+'.wav','rb')
        mfccs.append(calc_mfcc(data, fs))
        
    mue, sigma = build_model(mfccs)
    trush, t_data = read(target,'rb')
    t_mfcc = calc_mfcc(t_data, fs)
    val=math.log10(ditect(mue, sigma, t_mfcc))
    print val
    judge(val)