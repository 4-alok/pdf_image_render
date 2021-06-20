#import "PdfImageRenderPlugin.h"
#if __has_include(<pdf_image_render/pdf_image_render-Swift.h>)
#import <pdf_image_render/pdf_image_render-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "pdf_image_render-Swift.h"
#endif

@implementation PdfImageRenderPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftPdfImageRenderPlugin registerWithRegistrar:registrar];
}
@end
