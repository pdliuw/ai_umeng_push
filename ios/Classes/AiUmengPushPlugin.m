#import "AiUmengPushPlugin.h"
#if __has_include(<ai_umeng_push/ai_umeng_push-Swift.h>)
#import <ai_umeng_push/ai_umeng_push-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "ai_umeng_push-Swift.h"
#endif

@implementation AiUmengPushPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftAiUmengPushPlugin registerWithRegistrar:registrar];
}
@end
