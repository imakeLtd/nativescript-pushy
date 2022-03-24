import * as pages from "@nativescript/core/ui/page";
import { HelloWorldModel } from "./main-view-model";

// Depending on your app's structure, this may be required in order to do some startup wiring on iOS.
// In this demo it's not needed because the plugin is not lazily loaded (AoT), but just to be safe..
require("@imakeltd/pushy");

export function navigatingTo(args: any) {
  if (args.isBackNavigation) return;

  let page = <pages.Page>args.object;
  page.bindingContext = new HelloWorldModel();
}
