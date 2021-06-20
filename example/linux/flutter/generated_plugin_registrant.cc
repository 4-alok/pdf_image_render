//
//  Generated file. Do not edit.
//

#include "generated_plugin_registrant.h"

#include <pdf_image_render/pdf_image_render_plugin.h>

void fl_register_plugins(FlPluginRegistry* registry) {
  g_autoptr(FlPluginRegistrar) pdf_image_render_registrar =
      fl_plugin_registry_get_registrar_for_plugin(registry, "PdfImageRenderPlugin");
  pdf_image_render_plugin_register_with_registrar(pdf_image_render_registrar);
}
