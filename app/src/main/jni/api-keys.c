#include <jni.h>


JNIEXPORT jstring JNICALL
Java_com_example_appnews_LoginActivity_getKeys(JNIEnv* env,jobject thiz) {
    return (*env)->NewStringUTF(env, "/v1/latest-news?language=en&amp;apiKey=ogNGuP76wuSgH33lpP-3peLhFK4OMsCU0anyd7QqnuXqG6ET");
}