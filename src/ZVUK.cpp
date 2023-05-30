#include "ZVUK.h"
#include <iostream>
#include <stdio.h>
#include <windows.h>
#include "audio203api.h"
#include "usb_cmd.h"


JNIEXPORT void JNICALL Java_ZVUK_printHelloWorld(JNIEnv*, jobject) {
	std::cout << "Hello Habr! This is C++ code!!" << std::endl;
}

JNIEXPORT jintArray JNICALL Java_ZVUK_readLastMeasure(JNIEnv *env, jobject) {

    jintArray newArray = env->NewIntArray(5);
    jint* narr = env->GetIntArrayElements(newArray, NULL);

    for (int o = 0 ; o < 5; o++) {
        narr[o] = o;
    }

    env->ReleaseIntArrayElements(newArray, narr, NULL);

    return newArray;

}

JNIEXPORT jint JNICALL Java_ZVUK_open(JNIEnv*, jobject) {
    int rc;
    int num_devices = 0;
    audio203_context_t pcontext = NULL;
    char device_name[256];
    int name_len = sizeof(device_name) - 1;
    rc = audio203_open(&pcontext, 0, device_name, name_len);
    std::cout << pcontext << std::endl;
    if (rc != USB_ERR_OK) {
        printf("Error: no devices connected: %s (%d)\n", audio203_errstr(rc), rc);
    }
    else {
        printf("Device %s opened ok\n", device_name);
    }
    return (jint)pcontext;

}

JNIEXPORT void JNICALL Java_ZVUK_close(JNIEnv*, jobject, jint p) {
    int rc;
    audio203_context_t pcontext = (audio203_context_t)p;
    std::cout << pcontext << std::endl;
    rc = audio203_close(pcontext);
    if (rc == USB_ERR_OK) {
        printf("Device closed\n");
    }

}

JNIEXPORT jint JNICALL Java_ZVUK_getMeasure(JNIEnv*, jobject, jint p, jint r) {
    int rc;
    U8 code;
    audio203_context_t pcontext = (audio203_context_t)p;
    usb_get_measure_t get_measure;
    usb_audio_result_t get_measure2;
    usb_set_measure_t set_measure;
    memset(&get_measure, 0, sizeof(get_measure));
    memset(&get_measure2, 0, sizeof(get_measure2));

    set_measure.freq_range = (U8)r;
    set_measure.measure_time = 50;
    set_measure.delay_before_measure = 0;
    int tries = 0;

    audio203_clear_records(pcontext);

    rc = audio203_set_measure(pcontext, &set_measure, &code);
    if (rc != USB_ERR_OK) {
        return -1;
    }

    rc = audio203_start_measure(pcontext, &code);
    if (rc != USB_ERR_OK) {
        return -2;
    }
    //lbl1:
    Sleep(250);
    rc = audio203_get_measure(pcontext, &get_measure, &get_measure2);

    if (rc != USB_ERR_OK) {
        return -3;
    }
    std::cout << get_measure2.quality << " " << get_measure2.freq << get_measure.status << std::endl;
    /*if (get_measure2.quality == 0) {
        tries++;
        if (tries < 20) {
            goto lbl1;
        }

    }*/

    rc = audio203_stop_measure(pcontext);

    if (rc != USB_ERR_OK) {
        return -4;
    }
    if (get_measure2.quality == 0) {
        return -6;
    }
    else {
        return (jint)get_measure2.freq;
    }

}

