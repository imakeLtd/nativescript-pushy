import * as pages from "@nativescript/core/ui/page";
import { HelloWorldModel } from "./main-view-model";

export function navigatingTo(args: any) {
  if (args.isBackNavigation) return;

  let page = <pages.Page>args.object;
  page.bindingContext = new HelloWorldModel();
}
