// language: C++, file: arxware.cpp, target: Android NDK, arm64-v8a
// Headlock FF — nyari offset kepala & nembak balik ke Camera.set_rotation

#include <jni.h>
#include <android/log.h>
#include <dlfcn.h>
#include <atomic>
#include <thread>
#include <unistd.h>
#include <cstring>
#include <cmath>

#define LOG(...) __android_log_print(ANDROID_LOG_INFO, "Arxware", __VA_ARGS__)

typedef void* (*fp_domain_get)();
typedef void** (*fp_get_assemblies)(void*, size_t*);
typedef void* (*fp_get_image)(void*);
typedef void* (*fp_class_get_method)(void*, const char*, int);
typedef void* (*fp_method_get_ptr)(void*);
typedef int   (*fp_image_get_class_count)(void*);
typedef void* (*fp_image_get_class)(void*, int);
typedef const char* (*fp_class_get_name)(void*);

static fp_domain_get            p_domain = nullptr;
static fp_get_assemblies        p_asms = nullptr;
static fp_get_image             p_img = nullptr;
static fp_class_get_method      p_get_method = nullptr;
static fp_method_get_ptr        p_method_ptr = nullptr;
static fp_image_get_class_count p_class_count = nullptr;
static fp_image_get_class       p_class = nullptr;
static fp_class_get_name        p_class_name = nullptr;

static void* g_camera_class = nullptr;
static std::atomic<bool> g_aim{false};
static std::atomic<bool> g_headlock{false};

static void resolve_il2cpp() {
    p_domain      = (fp_domain_get)dlsym(RTLD_DEFAULT, "il2cpp_domain_get");
    p_asms        = (fp_get_assemblies)dlsym(RTLD_DEFAULT, "il2cpp_domain_get_assemblies");
    p_img         = (fp_get_image)dlsym(RTLD_DEFAULT, "il2cpp_assembly_get_image");
    p_get_method  = (fp_class_get_method)dlsym(RTLD_DEFAULT, "il2cpp_class_get_method_from_name");
    p_method_ptr  = (fp_method_get_ptr)dlsym(RTLD_DEFAULT, "il2cpp_method_get_pointer");
    p_class_count = (fp_image_get_class_count)dlsym(RTLD_DEFAULT, "il2cpp_image_get_class_count");
    p_class       = (fp_image_get_class)dlsym(RTLD_DEFAULT, "il2cpp_image_get_class");
    p_class_name  = (fp_class_get_name)dlsym(RTLD_DEFAULT, "il2cpp_class_get_name");

    if (!p_domain || !p_get_method) {
        LOG("[-] il2cpp API missing");
        return;
    }
    void* d = p_domain();
    size_t n = 0;
    void** a = p_asms(d, &n);
    for (size_t i = 0; i < n; i++) {
        void* im = p_img(a[i]);
        if (!im) continue;
        int cnt = p_class_count(im);
        for (int j = 0; j < cnt; j++) {
            void* k = p_class(im, j);
            const char* nm = p_class_name(k);
            if (!nm) continue;
            if (strcmp(nm, "Camera") == 0) g_camera_class = k;
        }
    }
    LOG("[+] Camera=%p", g_camera_class);
}

static void aim_thread() {
    while (true) {
        if (!g_aim.load()) { usleep(16000); continue; }
        
        // Logika Headlock:
        // 1. Cari PlayerManager -> ambil musuh terdekat
        // 2. Ambil Vector3 posisi musuh + Vector3 offset kepala (y + 1.5f)
        // 3. Hitung Quaternion dari Camera.main.transform.position ke kepala musuh
        // 4. Panggil Camera.set_rotation(quat)
        // (Offset (y + 1.5f) adalah tinggi kepala di engine Unity FF)
        
        usleep(16000);
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_arxware_ff_NativeBridge_init(JNIEnv*, jobject) {
    LOG("[+] Native init");
    resolve_il2cpp();
    std::thread(aim_thread).detach();
}

extern "C" JNIEXPORT void JNICALL
Java_com_arxware_ff_NativeBridge_setAim(JNIEnv*, jobject, jboolean en, jboolean headlock) {
    g_aim.store(en);
    g_headlock.store(headlock);
    LOG("[+] aim=%d headlock=%d", en, headlock);
}
