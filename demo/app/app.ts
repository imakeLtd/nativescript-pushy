import { Application } from "@nativescript/core";

require("nativescript-local-notifications");

// Depending on your app's structure, this may be required in order to do some startup wiring on iOS.
// In this demo it's not needed because the plugin is not lazily loaded (AoT), but just to be safe..
require("nativescript-pushy");

Application.run({moduleName: "app-root"});