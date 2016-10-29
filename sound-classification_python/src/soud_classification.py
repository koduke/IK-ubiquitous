# encoding:utf-8
'''
Created on 2016/10/17

@author: bantarou-02
'''

# coding:utf-8
'''
Created on 2016/10/16

@author: bantarou-02
'''
import time
import sys
import numpy as np

from scikits.talkbox.features import mfcc
from scipy.io.wavfile import read
from sklearn.svm import LinearSVC
from sklearn.svm import SVC
from sklearn.multiclass import OneVsRestClassifier
import math
argvs = sys.argv

MAX_FEATURE_SIZE = 13

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
        
#main 関数部分
if __name__ == '__main__':
    mfccs = []
    labels = []
    
    #ファイルの名前位置
    fnames = ['warning','siren','bell']
    target = '../wav/fumikiri/warning14.wav'
    
    fcnt = 1
    for fname in fnames:
        for cnt in range(1,MAX_FEATURE_SIZE+1):
            if fcnt == 1:
                fs, data = read('../wav/fumikiri/'+fname+str(cnt)+'.wav','rb')
                mfcc_tmp = calc_mfcc(data, fs)
                if mfcc_tmp[0] != float('inf') and mfcc_tmp[0] != float('-inf'):
                    if mfcc_tmp[0] == mfcc_tmp[0]:
                        mfccs.append(mfcc_tmp)
                        labels.append(1)
            elif fcnt == 2:
                fs, data = read('../wav/kyuukyuu/'+fname+str(cnt)+'.wav','rb')
                mfcc_tmp = calc_mfcc(data, fs)
                if mfcc_tmp[0] != float('inf') and mfcc_tmp[0] != float('-inf'):
                    if mfcc_tmp[0] == mfcc_tmp[0]:
                        mfccs.append(mfcc_tmp)
                        labels.append(2)
            elif fcnt == 3:
                fs, data = read('../wav/bell/'+fname+str(cnt)+'.wav','rb')
                mfcc_tmp = calc_mfcc(data, fs)
                if mfcc_tmp[0] != float('inf') and mfcc_tmp[0] != float('-inf'):
                    if mfcc_tmp[0] == mfcc_tmp[0]:
                        mfccs.append(mfcc_tmp)
                        labels.append(3)
            
        fcnt+=1
        
    
    #targetの読み込み
    trush, t_data = read(target,'rb')
    t_mfcc = calc_mfcc(t_data, fs)
    #targetの値が異常な値でないか検査
    if t_mfcc[0] == float('inf') or t_mfcc[0] == float('-inf') or t_mfcc[0] != t_mfcc[0]:
        print 'value_error'
        print t_mfcc
        t_mfcc = {0,0,0,0,0,0,0,0,0,0,0,0,0}
    
    list_mfcc = []
    list_mfcc.append(t_mfcc)
    
    #SVMによる機械学習
    estimator = SVC(C=1.0,kernel='rbf')
    multi_svm = OneVsRestClassifier(estimator)
    t1 = time.time()
    multi_svm.fit(mfccs, labels)
    t2 = time.time()
    results = multi_svm.predict(list_mfcc)
    print results
    print("SVC    time:{:3.6f}".format(t2-t1))