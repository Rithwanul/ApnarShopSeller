#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_bikroybaba_seller_util_Utility_getAuthorizationKey(JNIEnv *env, jobject instance) {
    return (*env)->NewStringUTF(env, "Basic R3RlY2g6R3RlY2gkQWFwbmFyc2hvcCQ=");
}

JNIEXPORT jstring JNICALL
Java_com_bikroybaba_seller_util_Utility_getBaseUrl(JNIEnv *env, jobject instance) {
    return (*env)->NewStringUTF(env, "http://120.50.15.44:5050/content/");
}